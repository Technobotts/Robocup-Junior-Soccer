package technobotts.rescue;

import lejos.nxt.I2CPort;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.addon.Lamp;
import lejos.nxt.addon.LineLeader;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;
import lejos.util.Delay;

/**
 * @author Eric
 */
public class RescueRobot
{
	public enum State {
		IDLE, FOLLOW, LINESEARCH, DEBRISAVOID, TWITCHAVOID, VICTIMSIGNAL, REDROOM
	};

	private State            state = State.IDLE;

	public final Motors      motors;
	public final Lamp        lamp;
	public final LineLeader  lineSensor;
	public final ColorSensor colorSensor;
	public final TachoPilot  pilot;

	public RescueColors      colors;

	public RescueRobot(Motor leftMotor, Motor rightMotor,
	                   LineLeader lineSensor, ColorSensor colorSensor, Lamp lamp)
	{
		motors = new Motors(leftMotor, rightMotor);
		this.lineSensor = lineSensor;
		this.colorSensor = colorSensor;
		this.lamp = lamp;
		this.pilot = new TachoPilot(3.6f, 14f, motors.leftMotor, motors.rightMotor);
		pilot.setMoveSpeed(30);
		pilot.setTurnSpeed(200);
	}

	public RescueRobot(Motor leftMotor, Motor rightMotor, I2CPort linePort,
	                   SensorPort colorPort, MotorPort lampPort)
	{
		this(leftMotor,
		     rightMotor,
		     new LineLeader(linePort),
		     new ColorSensor(colorPort),
		     new Lamp(lampPort));
	}

	public synchronized void showVictimFound()
	{
		final int duration = 2000;
		final int pulseRate = 4;
		long startTime = System.currentTimeMillis();
		long i;
		do
		{
			i = System.currentTimeMillis() - startTime;
			double p = (Math.sin(i / 500.0 * pulseRate * Math.PI) + 1) / 2;
			int b = (int) (p * 100);
			int f = (int) (440 * Math.pow(2, p));
			lamp.on(b);
			Sound.playTone(f, 10);
		} while(i <= duration);
		lamp.off();
	}

	public synchronized State getState()
	{
		return state;
	}

	public synchronized void setState(State state)
	{
		this.state = state;
	}

	public static class Motors
	{
		private final Motor leftMotor;
		private final Motor rightMotor;

		public enum Controller {
			FOLLOW, LINESEARCH, DEBRIS, REDROOM, MAIN, NONE
		}

		private Controller owner = Controller.NONE;

		public void getMotors(Controller requester)
		{
			if(requester == Controller.NONE)
				throw new IllegalArgumentException();
			synchronized(owner)
			{
				if(requester == owner)
				{
					return;
				}
				else
				{
					System.out.println("requester:" + requester);
					while(owner != Controller.NONE)
					{
						Sound.playTone(880, 25);
						try
						{
							owner.wait();
						}
						catch(InterruptedException e)
						{}
					}
					System.out.println("new:" + requester);
					System.out.println("old:" + owner);
					System.out.println();
					owner = requester;
				}
			}
		}

		public void releaseMotors(Controller requester)
		{
			synchronized(owner)
			{
				if(requester == owner)
				{
					System.out.println("releaser:" + requester);
					owner = Controller.NONE;
					owner.notify();
				}
			}
		}

		public Motors(Motor leftMotor, Motor rightMotor)
		{
			this.leftMotor = leftMotor;
			this.rightMotor = rightMotor;
		}

		private synchronized void setMotorSpeed(Motor motor, int speed)
		{
			if(Math.signum(speed) == 1 && !motor.isForward())
				motor.forward();
			else if(Math.signum(speed) == -1 && !motor.isBackward())
				motor.backward();

			motor.setSpeed(Math.abs(speed));
		}

		public synchronized void spin(int angle, int speed)
		{
			stop();
			leftMotor.setSpeed(speed);
			rightMotor.setSpeed(speed);
			leftMotor.rotate(angle, true);
			rightMotor.rotate(-angle, true);
		}

		public synchronized void setLeftSpeed(int speed)
		{
			setMotorSpeed(leftMotor, speed);
		}

		public synchronized void resetLeftTacho()
		{
			leftMotor.resetTachoCount();

		}

		public synchronized int getLeftTacho()
		{
			return leftMotor.getTachoCount();
		}

		public synchronized void setRightSpeed(int speed)
		{
			setMotorSpeed(rightMotor, speed);
		}

		public synchronized void resetRightTacho()
		{
			rightMotor.resetTachoCount();
		}

		public synchronized int getRightTacho()
		{
			return rightMotor.getTachoCount();
		}

		public synchronized void stop()
		{
			leftMotor.stop();
			rightMotor.stop();
		}
	}

	public static void main(String args[]) throws Exception
	{
		RescueRobot robot = new RescueRobot(Motor.B, Motor.C, null,
		                                    (ColorSensor) null, (Lamp) null);
		for(double theta = -4 * Math.PI; theta < 4 * Math.PI; theta += Math.PI / 128)
		{
			robot.motors.setRightSpeed((int) (Math.sin(theta) * 900));
			Thread.sleep(10);
		}
	}

	public boolean hasLine()
	{
		if(colors.getSensorColor(colorSensor) == colors.black)
		{
			return true;
		}
		else
		{

			int[] sensors = lineSensor.getSensors();
			if(sensors != null)
				return sensors[0] < 50 && sensors[1] < 50 || sensors[6] < 50 && sensors[7] < 50;
			else
				return false;
		}
	}

	public void doLineSearch()
	{
		if(hasLine())
		{
			return;
		}
		else
		{
			int[] angles = {40, -40, 80, -80, 120, -120, 0};
			int lastangle = 0;
			for(int angle : angles)
			{
				pilot.rotate((lastangle - angle), true);
				while(pilot.isMoving())
				{
					if(hasLine())
					{
						pilot.stop();
						Sound.beepSequence();
						return;
					}
					Thread.yield();
				}
				pilot.stop();
				lastangle = angle;
				Delay.msDelay(250);
			}
			return;
		}
	}
}

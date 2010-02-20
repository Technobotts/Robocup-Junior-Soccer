package technobotts.rescue;

import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.addon.Lamp;
import lejos.nxt.addon.LineLeader;
import lejos.robotics.TachoMotor;
import lejos.robotics.navigation.TachoPilot;
import lejos.util.Delay;

/**
 * @author Eric
 *         A class that represents the robot, and stores shared objects.
 */
public class RescueRobot
{
	public final Lamp             lamp;
	public final LineLeader       lineSensor;
	public final ColorSensor      colorSensor;
	public final BetterTachoPilot pilot;
	
	public long timeSinceLastLine;

	public RescueColors           colors;

	public class BetterTachoPilot extends TachoPilot
	{
		public BetterTachoPilot(float leftWheelDiameter, float rightWheelDiameter, float trackWidth,
		                        TachoMotor leftMotor,
		                        TachoMotor rightMotor, boolean reverse)
		{
			super(leftWheelDiameter, rightWheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
		}

		public BetterTachoPilot(float wheelDiameter, float trackWidth, TachoMotor leftMotor, TachoMotor rightMotor,
		                        boolean reverse)
		{
			super(wheelDiameter, trackWidth, leftMotor, rightMotor, reverse);
		}

		public BetterTachoPilot(float wheelDiameter, float trackWidth, TachoMotor leftMotor, TachoMotor rightMotor)
		{
			super(wheelDiameter, trackWidth, leftMotor, rightMotor);
		}

		public float getLeftDegPerDistance()
		{
			return _leftDegPerDistance;
		}

		public float getRightDegPerDistance()
		{
			return _rightDegPerDistance;
		}

		public synchronized void setLeftSpeed(int speed)
		{
			if(Math.signum(speed) == Math.signum(_leftDegPerDistance))
				_left.forward();
			else if(Math.signum(speed) != 0)
				_left.backward();

			_left.setSpeed(Math.abs(speed));
		}

		public synchronized void setRightSpeed(int speed)
		{
			if(Math.signum(speed) == Math.signum(_rightDegPerDistance))
				_right.forward();
			else if(Math.signum(speed) != 0)
				_left.backward();

			_right.setSpeed(Math.abs(speed));
		}
	}

	public RescueRobot(RescueColors colors)
	{
		this.colors = colors;
		this.colorSensor = new ColorSensor(SensorPort.S1);
		this.lineSensor = new LineLeader(SensorPort.S2);
		this.lamp = new Lamp(MotorPort.A);
		this.pilot = new BetterTachoPilot(3.6f, 14f, Motor.C, Motor.B);
		pilot.setMoveSpeed(30);
		pilot.setTurnSpeed(200);
	}

	/**
	 * Flashes the lamp for 2 seconds while playing oscillating tones
	 */
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

	/**
	 * Determines if the robot is over a line
	 * @return whether a line is spotted
	 */
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
				return sensors[0] < 25 && sensors[1] < 25 || sensors[6] < 25 && sensors[7] < 25;
			else
				return false;
		}
	}

	/**
	 * Does a search for a line around the robot's current position.
	 * @return whether a line was found
	 */
	public boolean doLineSearch()
	{
		//If the line is already visible, return
		if(hasLine())
			return true;

		//List of angles to turn to for line searching
		int[] angles = {40, -40, 80, -80, 120, -120, 0};
		
		int lastangle = 0;
		for(int angle : angles)
		{
			pilot.rotate((lastangle - angle), true);
			while(pilot.isMoving())
			{
				if(hasLine())
				{
					//If the line is visible, stop the motors, and return
					pilot.stop();
					Sound.beepSequence();
					return true;
				}
				Thread.yield();
			}
			//If he line still hasn't been found, return false
			pilot.stop();
			lastangle = angle;
			Delay.msDelay(250);
		}
		return false;
	}

}

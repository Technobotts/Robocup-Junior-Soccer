package technobotts.rescue;

import technobotts.rescue.tasks.AntiTwitch;
import technobotts.rescue.tasks.DebrisAvoid;
import technobotts.rescue.tasks.Follower;
import technobotts.rescue.tasks.LineLogger;
import technobotts.rescue.tasks.VictimFinder;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
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

	public final TouchSensor      leftBumper;
	public final TouchSensor      rightBumper;

	public Follower               followerT;
	public VictimFinder           victimT;
	public AntiTwitch             twitchT;
	public LineLogger             loggerT;
	public DebrisAvoid            debrisT;

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
				_right.backward();

			_right.setSpeed(Math.abs(speed));
		}
	}

	public RescueRobot(RescueColors colors)
	{
		this.colors = colors;
		this.colorSensor = new ColorSensor(SensorPort.S1);
		this.lineSensor = new LineLeader(SensorPort.S2);
		this.lamp = new Lamp(MotorPort.A);
		this.pilot = new BetterTachoPilot(3.6f, 20f, Motor.C, Motor.B);
		pilot.setMoveSpeed(30);
		pilot.setTurnSpeed(200);

		leftBumper = new TouchSensor(SensorPort.S3);
		rightBumper = new TouchSensor(SensorPort.S4);

		followerT = new Follower(this);
		victimT = new VictimFinder(this);
		twitchT = new AntiTwitch(this);
		loggerT = new LineLogger(this);
		debrisT = new DebrisAvoid(this);
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
	public synchronized boolean hasLine()
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

	private boolean _isSearching = false;

	public boolean isSearching()
	{
		return _isSearching;
	}

	/**
	 * Does a search for a line around the robot's current position.
	 * @return whether a line was found
	 */
	public boolean doLineSearch()
	{
		// If the line is already visible, return
		if(hasLine())
			return true;

		_isSearching = true;
		try
		{
			// List of angles to turn to for line searching
			int[] angles = {40, -40, 80, -80, 120, -120, 0};

			int lastangle = 0;
			for(int angle : angles)
			{
				pilot.rotate((lastangle - angle), true);
				while(pilot.isMoving())
				{
					if(hasLine())
					{
						// If the line is visible, stop the motors, and return
						pilot.stop();
						Sound.beepSequence();
						return true;
					}
					Thread.yield();
				}
				// If he line still hasn't been found, return false
				pilot.stop();
				lastangle = angle;
				Delay.msDelay(250);
			}
			return false;
		}
		finally
		{
			_isSearching = false;
		}
	}

}

package technobotts.soccer.robot.onenxt;

import technobotts.nxt.addon.IRSeekerV2;
import technobotts.nxt.addon.InvertedCompassSensor;
import technobotts.robotics.navigation.SimpleOmniPilot;
import technobotts.soccer.robot.SoccerRobot;
import technobotts.soccer.util.DualLSFinder;
import technobotts.util.BooleanSmoother;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class ForwardRobot extends SoccerRobot
{

	public static final SensorPort COMPASS_PORT  = SensorPort.S4;
	public static final SensorPort BACK_IR_PORT  = SensorPort.S2;
	public static final SensorPort FRONT_IR_PORT = SensorPort.S3;
	public static final SensorPort US_PORT       = SensorPort.S1;

	private UltrasonicSensor us;

	public ForwardRobot()
	{
		super(new InvertedCompassSensor(COMPASS_PORT),
		      new DualLSFinder(new IRSeekerV2(FRONT_IR_PORT, IR_MODE), 0f,
		    		  		new IRSeekerV2(BACK_IR_PORT, IR_MODE), 180f),
		      new SimpleOmniPilot.OmniMotor(Motor.C, 53.1301f, 6.4f, 1, 9.6f, true),
		      new SimpleOmniPilot.OmniMotor(Motor.B, 180.0000f, 6.4f, 1, 8.8f),
		      new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f));
		
		us = new UltrasonicSensor(US_PORT);
	}

	public boolean connectToSlave()
	{
		return true;
	}

	public boolean kick() {
		return true;
	}

	public double getGoalAngle()
	{
		return Double.NaN;
	}

	private boolean thinksHasBall() {
		us.ping();

		int[] dists = new int[8];
		if (us.getDistances(dists) == 0)
			for (int dist : dists)
				if (dist < 10)
					return true;

		return false;
	}

	private BooleanSmoother usSmoother = new BooleanSmoother(0.45);
	public boolean hasBall() {
		return usSmoother.getOutput(thinksHasBall());
	}

	public float distanceToGoal()
	{
		return Float.NaN;
	}

	public boolean disconnect()
	{
		return true;
	}

	@Override
	public float getRearWallDist()
	{
		return Float.NaN;
	}
}

package technobotts.soccer.robot;

import java.io.PrintStream;

import technobotts.robotics.LightSourceFinder;
import technobotts.robotics.navigation.OmniCompassPilot;
import technobotts.util.AngleSmoother;
import technobotts.util.DataProcessor;
import lejos.nxt.LCD;
import lejos.nxt.LCDOutputStream;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.DirectionFinder;

public abstract class SoccerRobot extends OmniCompassPilot
{
	private LightSourceFinder ballDetector;
	private DirectionFinder   compass;
	protected NXTConnection   slave;

	protected DataProcessor   ballSmoother = new AngleSmoother(0.15);

	public SoccerRobot(DirectionFinder compass, LightSourceFinder ballDetector, OmniMotor... motors)
	{
		super(compass, motors);
		this.compass = compass;

		// this.setMoveSpeed(Float.POSITIVE_INFINITY);
		this.ballDetector = ballDetector;
	}

	public final float getHeading()
	{
		return compass.getDegreesCartesian();
	}

	public final LightSourceFinder getBallDetector()
	{
		return ballDetector;
	}

	public abstract boolean hasBall();

	public final float getBallAngle()
	{
		if(hasBall())
			return 0;
		float angle = (float) ballSmoother.getOutput(ballDetector.getAngle());
		while(angle > 180)
			angle -= 360;
		while(angle <= -180)
			angle += 360;
		return angle;
	}

	public abstract double getGoalAngle();

	public abstract boolean connectToSlave();

	public abstract boolean disconnect();

	public abstract boolean kick();

	public abstract boolean bumperIsPressed();

}

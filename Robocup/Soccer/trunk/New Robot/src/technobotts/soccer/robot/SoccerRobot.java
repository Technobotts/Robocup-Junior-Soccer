package technobotts.soccer.robot;

import lejos.nxt.comm.NXTConnection;
import lejos.robotics.DirectionFinder;
import lejos.robotics.LightSourceFinder;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.util.AngleSmoother;
import lejos.util.DataProcessor;

public abstract class SoccerRobot extends OmniCompassPilot
{
	private LightSourceFinder ballDetector;
	private DirectionFinder  compass;
	protected NXTConnection  slave;

	private DataProcessor    ballSmoother = new AngleSmoother(0.15);

	public SoccerRobot(DirectionFinder compass,
	                           LightSourceFinder ballDetector,
	                           OmniMotor... motors)
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

}

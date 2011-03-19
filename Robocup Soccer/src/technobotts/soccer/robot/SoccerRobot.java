package technobotts.soccer.robot;

import lejos.nxt.comm.NXTConnection;
import lejos.robotics.DirectionFinder;
import technobotts.nxt.addon.IRSeekerV2.Mode;
import technobotts.robotics.LightSourceFinder;
import technobotts.robotics.navigation.OmniCompassPilot;
import technobotts.util.AngleSmoother;
import technobotts.util.DataProcessor;

public abstract class SoccerRobot extends OmniCompassPilot
{
	private LightSourceFinder ballDetector;
	private DirectionFinder   compass;
	protected NXTConnection   slave;

	public static final Mode  IR_MODE      = Mode.AC_600Hz;

	protected DataProcessor   ballSmoother = new AngleSmoother(0.15);

	public SoccerRobot(DirectionFinder compass, LightSourceFinder ballDetector, OmniMotor... motors)
	{
		super(compass, motors);
		setMoveSpeed(200);
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

	public float getBallAngle()
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
	
	public abstract float getRearWallDist();

}

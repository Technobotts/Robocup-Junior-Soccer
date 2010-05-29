package technobotts.soccer.robot;

import lejos.nxt.comm.NXTConnection;
import lejos.robotics.DirectionFinder;
import lejos.robotics.LightSourceFinder;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.robotics.navigation.SimpleOmniPilot.OmniMotor;
import lejos.util.AngleSmoother;
import lejos.util.DataProcessor;

public abstract class AbstractSoccerRobot implements SoccerRobot
{
	public LightSourceFinder ballDetector;
	private DirectionFinder  compass;
	public OmniCompassPilot  pilot;
	protected NXTConnection    slave;
	

	
	private DataProcessor ballSmoother = new AngleSmoother(0.15);

	public AbstractSoccerRobot(DirectionFinder compass,
	                   LightSourceFinder ballDetector,
	                   OmniMotor... motors)
	{
		this.compass = compass;
		this.pilot = new OmniCompassPilot(compass, motors);

		//this.pilot.setMoveSpeed(Float.POSITIVE_INFINITY);
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
	
	public final float getBallAngle()
	{
		float angle = (float) ballSmoother.getOutput(ballDetector.getAngle());
		while(angle > 180)
			angle -= 360;
		while(angle <= -180)
			angle += 360;
		return angle;
	}
	
	public final OmniCompassPilot getPilot()
	{
		return pilot;
	}

}

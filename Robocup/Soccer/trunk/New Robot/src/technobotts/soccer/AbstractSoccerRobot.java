package technobotts.soccer;

import lejos.nxt.comm.NXTConnection;
import lejos.robotics.DirectionFinder;
import lejos.robotics.LightSourceFinder;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.robotics.navigation.SimpleOmniPilot.OmniMotor;

public abstract class AbstractSoccerRobot implements SoccerRobot
{
	public LightSourceFinder ballDetector;
	private DirectionFinder  compass;
	public OmniCompassPilot  pilot;
	protected NXTConnection    slave;

	public AbstractSoccerRobot(DirectionFinder compass,
	                   LightSourceFinder ballDetector,
	                   OmniMotor... motors)
	{
		this.compass = compass;
		this.pilot = new OmniCompassPilot(compass, motors);
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

}

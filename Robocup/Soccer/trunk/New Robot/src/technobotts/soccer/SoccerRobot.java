package technobotts.soccer;

import lejos.nxt.comm.NXTConnection;
import lejos.robotics.DirectionFinder;
import lejos.robotics.LightSourceFinder;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.robotics.navigation.SimpleOmniPilot.OmniMotor;

public abstract class SoccerRobot
{
	public LightSourceFinder ballDetector;
	private DirectionFinder  compass;
	public OmniCompassPilot  pilot;
	protected NXTConnection    slave;

	public SoccerRobot(DirectionFinder compass,
	                   LightSourceFinder ballDetector,
	                   OmniMotor... motors)
	{
		this.compass = compass;
		this.pilot = new OmniCompassPilot(compass, motors);
		this.ballDetector = ballDetector;
	}
	
	public abstract boolean connectTo(String slaveName);
	
	public abstract boolean disconnect();

	public abstract boolean hasBall();

	public abstract boolean kick();

	public final float getHeading()
	{
		return compass.getDegreesCartesian();
	}

}

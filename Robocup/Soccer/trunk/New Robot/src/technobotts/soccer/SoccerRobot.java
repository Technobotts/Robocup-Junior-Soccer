package technobotts.soccer;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.InvertedCompassSensor;
import lejos.nxt.addon.LightSourceFinder;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.DirectionFinder;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.robotics.navigation.SimpleOmniPilot;
import lejos.robotics.navigation.SimpleOmniPilot.OmniMotor;

public abstract class SoccerRobot
{
	public LightSourceFinder ballDetector;
	private DirectionFinder  compass;
	public OmniCompassPilot  pilot;
	//private NXTConnection    slave;

	public SoccerRobot(DirectionFinder compass,
	                   LightSourceFinder ballDetector,
	                   OmniMotor... motors)
	{
		this.compass = compass;
		this.pilot = new OmniCompassPilot(compass, motors);
		this.ballDetector = ballDetector;
	}

	public abstract boolean hasBall();

	public abstract void kick();

	public float getHeading()
	{
		return compass.getDegreesCartesian();
	}

}

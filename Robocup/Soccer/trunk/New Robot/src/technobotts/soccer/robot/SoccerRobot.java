package technobotts.soccer.robot;

import lejos.robotics.LightSourceFinder;
import lejos.robotics.navigation.OmniCompassPilot;

public interface SoccerRobot
{
	public boolean connectToSlave();
	public boolean disconnect();

	public LightSourceFinder getBallDetector();
	public boolean hasBall();
	public float getBallAngle();

	public OmniCompassPilot getPilot();

	public boolean kick();

	public float getHeading();
}

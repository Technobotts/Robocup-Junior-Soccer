package technobotts.soccer;

import lejos.robotics.LightSourceFinder;

public interface SoccerRobot
{
	public boolean connectTo(String slaveName);

	public boolean disconnect();

	public boolean hasBall();

	public boolean kick();

	public LightSourceFinder getBallDetector();

	public float getHeading();

}

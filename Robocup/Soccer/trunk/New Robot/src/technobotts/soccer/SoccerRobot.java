package technobotts.soccer;

import lejos.robotics.LightSourceFinder;

public interface SoccerRobot
{
	public boolean connectToSlave();

	public boolean disconnect();

	public boolean hasBall();

	public boolean kick();

	public LightSourceFinder getBallDetector();

	public float getHeading();

}

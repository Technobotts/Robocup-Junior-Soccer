package technobotts.soccer;

import technobotts.soccer.robot.SoccerRobot;

public abstract class Strategy
{
	public abstract void executeWith(SoccerRobot robot);
}

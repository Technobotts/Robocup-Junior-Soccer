package technobotts.soccer;

import technobotts.soccer.robot.SoccerRobot;

public abstract class Strategy<RobotType extends SoccerRobot> implements Runnable
{
	protected RobotType robot;
	public Strategy(RobotType robot)
    {
    	this.robot = robot;
    }
}

package technobotts.soccer;

import lejos.nxt.Sound;
import technobotts.soccer.robot.SoccerRobot;

public abstract class Strategy
{
	protected abstract void executeWithConnected(SoccerRobot robot) throws InterruptedException;
	public void executeWith(SoccerRobot robot)
	{
		if(!robot.connectToSlave())
			Sound.buzz();
		
		try
        {
	        executeWithConnected(robot);
        }
        catch(InterruptedException e)
        {}
		
		if(!robot.disconnect())
			Sound.buzz();
	}
}

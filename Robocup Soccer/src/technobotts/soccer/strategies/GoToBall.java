package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.SoccerRobot;

public class GoToBall extends Strategy
{
	@Override
    public void executeWith(SoccerRobot robot)
    {
		robot.setDirectionFinder(robot.getBallDetector());
		robot.rotateTo(0,true);
		robot.travel(0);

		boolean success = robot.connectToSlave();
		if(!success)
			Sound.beepSequenceUp();
		
		Button.ENTER.waitForPressAndRelease();
    }

	@Override
    protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
    {	    
    }
}

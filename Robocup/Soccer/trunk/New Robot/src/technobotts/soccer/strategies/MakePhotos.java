package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.NewSoccerRobot;
import technobotts.soccer.robot.SoccerRobot;

public class MakePhotos extends Strategy
{

	public static void main(String[] args)
    {
		Strategy s = new MakePhotos();
		s.executeWith(new NewSoccerRobot());
    }
	@Override
    protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
    {
	    for(float angle = 0; angle<360; angle+=10)
	    {
	    	Button.ENTER.waitForPressAndRelease();
	    	Thread.sleep(500);
	    	robot.rotateTo(angle);
	    	Sound.beep();
	    }
	    
    }

}

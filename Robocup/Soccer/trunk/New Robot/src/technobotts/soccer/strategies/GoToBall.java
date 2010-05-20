package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.NewSoccerRobot;
import technobotts.soccer.Strategy;

public class GoToBall extends Strategy<NewSoccerRobot>
{
	public GoToBall(NewSoccerRobot robot)
    {
	    super(robot);
    }

	@Override
    public void run()
    {
		robot.pilot.setDirectionFinder(robot.ballDetector);
		robot.pilot.rotateTo(0,true);
		robot.pilot.travel(0);

		boolean success = robot.connectTo("NXT");
		if(!success)
			Sound.beepSequenceUp();
		
		Button.ENTER.waitForPressAndRelease();
    }
	
    public static void main(String[] args)
    {
		Strategy<NewSoccerRobot> s = new GoToBall(new NewSoccerRobot());
		s.run();
    }

}

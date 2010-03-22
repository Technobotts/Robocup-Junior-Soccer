import lejos.nxt.*;
import technobotts.soccer.SoccerRobot;


public class CrappySoccer
{
	static SoccerRobot robot = new SoccerRobot();
	
	public static void main(String ...args)
	{
	robot.pilot.setRegulation(true);
	robot.pilot.setDirectionFinder(robot.IR);
	robot.pilot.rotate(0,true);
	
	robot.pilot.travel(0);

	Button.ENTER.waitForPressAndRelease();
	}
}

import lejos.nxt.Button;
import technobotts.soccer.SoccerRobot;

public class SimpleSoccer
{
	static SoccerRobot robot = new SoccerRobot();
	
	public static void main(String[] args)
	{
		while(true)
		{
			while(robot.IR.getAngle() != 0)
				Thread.yield();
			
			robot.pilot.travel(0);
			
			while(!robot.hasBall())
				Thread.yield();
			
			robot.pilot.setDirectionFinder(robot.compass);
			robot.pilot.rotate(0);
			robot.pilot.travel(0, 10);
			robot.kick();
			Button.ENTER.waitForPressAndRelease();
		}
	}

}

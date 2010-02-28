import lejos.nxt.Button;
import lejos.robotics.navigation.SimpleOmniPilot;
import technobotts.soccer.SoccerRobot;

public class SimpleSoccer
{
	static SoccerRobot robot = new SoccerRobot();
	
	public static void main(String[] args)
	{
		while(true)
		{
			robot.pilot.setRegulation(false);
			float angle;
			do
			{
				angle = robot.IR.getAngle();
				robot.pilot.setTurnSpeed(Math.abs(angle)*90);
				robot.pilot.rotate();
			} while(angle != 0);
			robot.pilot.stop();
			((SimpleOmniPilot) robot.pilot).travel(0);
			
			while(!robot.hasBall())
				Thread.yield();
			
			robot.pilot.setDirectionFinder(robot.compass);
			robot.pilot.rotate(0);
			
			robot.kick();
			Button.ENTER.waitForPressAndRelease();
		}
	}

}

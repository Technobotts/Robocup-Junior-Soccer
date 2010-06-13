import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.robotics.navigation.SimpleOmniPilot;
import technobotts.soccer.SoccerDisplay;
import technobotts.soccer.SoccerRobot;


public class BetterSoccer
{
	static SoccerRobot robot = new SoccerRobot();
	
	public static void main(String[] args) throws InterruptedException
	{
		Thread display = new SoccerDisplay(robot);
		display.start();
		while(true)
		{
			if(!robot.IR.hasDirection())
			{
				robot.pilot.setRegulation(false);
				
				((SimpleOmniPilot) robot.pilot).rotate();
				
				while(!robot.IR.hasDirection())
					Thread.yield();
				
				robot.pilot.stop();
			}
			
			while(robot.IR.hasDirection())
			{
				while(robot.hasBall())
				{
					robot.pilot.setRegulation(true);
					robot.pilot.setDirectionFinder(robot.compass);
					
					robot.pilot.travel(0);
					Sound.beepSequenceUp();
					Thread.sleep(500);
					robot.kick();
				}
				
				float angle = robot.IR.getAngle();
				System.out.println("a: "+angle);
				 
				if(Math.abs(angle) > 60)
				{
					robot.pilot.setRegulation(true);
					robot.pilot.setDirectionFinder(robot.IR);
					
					robot.pilot.stop();
					robot.pilot.rotate(0);
					
					Thread.sleep(500);
				}
				else if(!Float.isNaN(angle))
				{
					robot.pilot.setRegulation(true);
					robot.pilot.setDirectionFinder(robot.IR);
					
					robot.pilot.travel(0);
				}
				Thread.sleep(100);
			}
		}
	}
}

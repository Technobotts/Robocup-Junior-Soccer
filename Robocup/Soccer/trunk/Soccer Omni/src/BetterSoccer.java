import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.robotics.navigation.SimpleOmniPilot;
import technobotts.soccer.SoccerRobot;


public class BetterSoccer
{
	static SoccerRobot robot = new SoccerRobot();
	
	public static void main(String[] args)
	{
		while(true)
		{
			if(!robot.IR.hasDirection())
			{
				//Sound.playTone(440, 100);
				((SimpleOmniPilot) robot.pilot).rotate();
				while(!robot.IR.hasDirection())
					Thread.yield();
				robot.pilot.stop();
				//Sound.playTone(880, 100);
				
			}
			
			while(robot.IR.hasDirection())
			{
				while(robot.hasBall())
				{
					//Sound.beepSequenceUp();
					Button.ENTER.waitForPressAndRelease();
					robot.kick();
				}
				
				float angle = robot.IR.getAngle();
				System.out.println("a: "+angle);
				
				if(Math.abs(angle) > 45)
				{
					//Sound.playTone(261, 100);
					robot.pilot.setDirectionFinder(robot.IR);
					//robot.pilot.setRegulation(true);
					robot.pilot.rotate(0);
					//robot.pilot.setRegulation(false);
					robot.pilot.setDirectionFinder(robot.compass);
					//Sound.playTone(Sound.C2, 100);
				}
				else if(!Float.isNaN(angle))
				{
					((SimpleOmniPilot) robot.pilot).travel(angle);
				}
				Thread.yield();
			}
		}
	}
}

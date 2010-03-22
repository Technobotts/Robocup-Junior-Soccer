import java.util.Random;

import lejos.nxt.Sound;
import technobotts.soccer.SoccerDisplay;
import technobotts.soccer.SoccerRobot;

public class NFaceSoccer
{
	static SoccerRobot robot = new SoccerRobot();

	public static void main(String... args) throws InterruptedException
	{
		Thread display = new SoccerDisplay(robot);
		display.start();
		robot.pilot.setRegulation(true);

		Random r = new Random();
		int randHeading = 0;
		while(true)
		{
			robot.pilot.setMoveSpeed(50);
			while(!robot.IR.hasDirection())
			{
				robot.status = "Searching";
				robot.pilot.travel(180);
				Thread.sleep(100);
			}
			robot.pilot.setMoveSpeed(200);
			robot.pilot.stop();
			while(robot.IR.hasDirection())
			{
				while(robot.hasBall())
				{
					robot.status = "Dribbling";
					robot.pilot.travel(0);
					Sound.beepSequenceUp();
					Thread.sleep(250);
					robot.kick();
					robot.pilot.stop();
					Thread.sleep(500);
				}

				float angle = robot.IR.getAngle();

				if(Math.abs(angle) > 60)
				{
					robot.status = "Behind";
					robot.pilot.travel(180 * 0.25f + angle * 0.75f);

					Thread.sleep(500);
				}
				else if(!Float.isNaN(angle))
				{
					robot.pilot.travel(angle);
				}
				Thread.sleep(100);
			}
		}
	}
}

import java.io.IOException;
import java.util.Random;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import technobotts.soccer.SoccerDisplay;
import technobotts.soccer.SoccerRobot;

public class NFaceSoccer
{
	static SoccerRobot robot = new SoccerRobot();

	public static void main(String... args) throws InterruptedException, IOException
	{
		Thread display = new SoccerDisplay(robot);
		display.start();
		robot.pilot.setRegulation(true);

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
					//robot.status = "Behind";
					//float angle2 = 180 - Math.signum(angle) * (180 - Math.abs(angle)) * 0.75f;
					robot.status = ""+angle;// + ", " + angle2 + "        ";
					robot.pilot.travel(angle);//2);

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

import java.io.IOException;
import java.util.Random;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import technobotts.soccer.SoccerDisplay;
import technobotts.soccer.SoccerRobot;

public class SoccerNew
{
	static SoccerRobot robot = new SoccerRobot();

	public static void showIRAngle(float angle)
	{
		int tone;
		if(angle == -120)
			tone = 220;
		else if(angle == -90)
			tone = 277;
		else if(angle == -60)
			tone = 330;
		else if(angle == -30)
			tone = 392;
		else if(angle == 0)
			tone = 440;
		else if(angle == 30)
			tone = 554;
		else if(angle == 60)
			tone = 659;
		else if(angle == 90)
			tone = 784;
		else if(angle == 120)
			tone = 880;
		else
			return;
		
		Sound.playTone(tone, 500);
	}

	public static void main(String... args) throws InterruptedException
	{
		try
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
					Thread.yield();
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
					//showIRAngle(angle);

					if(Math.abs(angle) > 45)
					{
						robot.status = "Behind";
						final float p = 0.8f;

						float angle2 = (180 * (1 - p) * Math.signum(angle) + angle * p);

						robot.pilot.travel(angle2);
					}
					else if(!Float.isNaN(angle))
					{
						robot.pilot.travel(angle);
					}
					Thread.sleep(100);
				}
			}
		}
		catch(Exception e)
		{
			robot.pilot.stop();
			while(Button.readButtons() == 0)
				Sound.buzz();
		}
	}
}

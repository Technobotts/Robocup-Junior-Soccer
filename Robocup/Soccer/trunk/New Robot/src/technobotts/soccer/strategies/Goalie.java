package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.DataProcessor;
import lejos.util.Delay;
import lejos.util.SimplePID;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.NewSoccerRobot;
import technobotts.soccer.robot.OldSoccerRobot;
import technobotts.soccer.robot.SoccerRobot;

public class Goalie extends Strategy
{

	DataProcessor pid = new SimplePID(3, 0, 0.5);

	public static void main(String[] args) throws InterruptedException
	{
		Strategy s = new Goalie();
		s.executeWith(new OldSoccerRobot());
	}

	long        lastResetTime    = System.currentTimeMillis();

	final float degreesPerSecond = 15;
	final float initialHeading   = 80;

	public float calculateHeading()
	{
		float heading = (System.currentTimeMillis() - lastResetTime) * degreesPerSecond / 1000f;
		if(heading > 180)
			heading = 180;

		return heading;
	}

	public void resetHeading()
	{
		lastResetTime = System.currentTimeMillis();
	}

	public void executeWithConnected(SoccerRobot robot) throws InterruptedException
	{

		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
			{
				robot.kick();
			}
			if(robot.bumperIsPressed())
			{
				resetHeading();
			}

			float ballAngle = robot.getBallAngle();
			float robotSpeed = (float) pid.getOutput(ballAngle);

			LCD.clear();
			LCD.drawString(ballAngle + ":" + robotSpeed, 0, 0);
			LCD.refresh();

			if(!Float.isNaN(robotSpeed))
			{
				float heading = calculateHeading();
				if(Math.abs(robotSpeed) < 15 && heading < 135)
				{
					robot.setMoveSpeed(300);
					robot.travel(0);
				}
				else
				{
					robot.setMoveSpeed(Math.abs(robotSpeed));
					if(robotSpeed > 0)
						robot.travel(heading);
					else
						robot.travel(-heading);
				}
			}
			Delay.msDelay(50);
		}
		
		if(!robot.disconnect())
			Sound.buzz();
	}

}

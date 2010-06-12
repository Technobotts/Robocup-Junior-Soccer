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
	public static void main(String[] args) throws InterruptedException
	{
		Strategy s = new Goalie();
		s.executeWith(new OldSoccerRobot());
	}

	DataProcessor pid = new SimplePID(3, 0, 0.5);

	long        lastResetTime    = System.currentTimeMillis();

	final float degreesPerSecond = 15;
	final float initialHeading   = 80;

	public float calculateHeading()
	{
		float heading = initialHeading + (System.currentTimeMillis() - lastResetTime) * degreesPerSecond / 1000f;
		if(heading > 180)
			heading = 180;

		return heading;
	}

	public void resetHeading()
	{
		lastResetTime = System.currentTimeMillis();
	}

	protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
	{
		while(!Button.ESCAPE.isPressed())
		{
			float ballAngle = robot.getBallAngle();
			float robotSpeed = (float) pid.getOutput(ballAngle);
			
			if(robot.hasBall())
			{
				robot.setMoveSpeed(300);
				robot.travel(0);
				Thread.sleep(250);
				robot.kick();
				robot.stop();
			}
			else if(robot.bumperIsPressed())
			{
				resetHeading();
			}
			else if(!Float.isNaN(robotSpeed))
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
	}

}

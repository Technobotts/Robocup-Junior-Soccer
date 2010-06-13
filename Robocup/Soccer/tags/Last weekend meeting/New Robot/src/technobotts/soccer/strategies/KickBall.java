package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.SoccerRobot;

public class KickBall extends Strategy
{

	@Override
	protected void executeWithConnected(SoccerRobot robot)
	{
		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
				robot.kick();

			try
			{
				Thread.sleep(250);
			}
			catch(InterruptedException e)
			{}
		}
	}
}

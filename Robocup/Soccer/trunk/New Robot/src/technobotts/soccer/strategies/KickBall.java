package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.SoccerRobot;

public class KickBall extends Strategy
{

	@Override
	public void executeWith(SoccerRobot robot)
	{
		if(!robot.connectToSlave())
			Sound.buzz();

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
		robot.disconnect();
	}
}

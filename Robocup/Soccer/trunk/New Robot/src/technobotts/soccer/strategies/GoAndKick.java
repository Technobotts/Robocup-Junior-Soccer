package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.SoccerRobot;

public class GoAndKick extends Strategy
{

	@Override
	public void executeWith(SoccerRobot robot)
	{
		if(!robot.connectToSlave())
			Sound.buzz();
		
		robot.setDirectionFinder(robot.getBallDetector());
		robot.rotateTo(0,true);
		robot.travel(0);

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

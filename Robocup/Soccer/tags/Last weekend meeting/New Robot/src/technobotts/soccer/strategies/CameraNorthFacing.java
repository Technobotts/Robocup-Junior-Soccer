package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.DataProcessor;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.*;
import technobotts.soccer.util.RobotDirectionModifier;

public class CameraNorthFacing extends Strategy
{
	DataProcessor robotHeadingModifier = new RobotDirectionModifier(3);

	@Override
	protected void executeWithConnected(SoccerRobot robot)
	{
		robot.rotateTo(0, true);

		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
			{
				robot.rotate((float) robot.getGoalAngle(), true);
				robot.travel(0);
				Delay.msDelay(500);
				robot.kick();
				robot.rotateTo(0);
			}

			float ballAngle = robot.getBallAngle();
			LCD.clear();
			LCD.drawString("Angle: " + Math.rint(ballAngle), 0, 0);

			float heading = robot.getHeading() + ballAngle;

			if(Float.isNaN(heading))
			{
				robot.travel(180);
			}
			else
			{
				robot.travel((float) robotHeadingModifier.getOutput(heading));
			}
			Delay.msDelay(50);
		}
	}
}

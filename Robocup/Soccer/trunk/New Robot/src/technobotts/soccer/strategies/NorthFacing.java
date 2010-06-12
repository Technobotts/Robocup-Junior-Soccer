package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.DataProcessor;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.*;
import technobotts.soccer.util.RobotDirectionModifier;

public class NorthFacing extends Strategy
{
	DataProcessor robotHeadingModifier = new RobotDirectionModifier(1.5);

	@Override
	protected void executeWithConnected(SoccerRobot robot)
	{
		robot.rotateTo(0, true);
		int i = 0;
//		float lastHeading = Float.NaN;
		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
			{
				robot.travel(0);
				robot.kick();
			}
			float ballAngle = robot.getBallAngle();
			LCD.clear();
			LCD.drawString("Angle: "+Math.rint(ballAngle),0,0);
			
			float heading = robot.getHeading() + ballAngle;
			if(Float.isNaN(heading))
			{
				robot.travel(180);
			}
			else
			{
				robot.travel((float) robotHeadingModifier.getOutput(heading));
			}
			LCD.drawInt(i++, 0, 3);
			Delay.msDelay(50);
			
		}
	}
}

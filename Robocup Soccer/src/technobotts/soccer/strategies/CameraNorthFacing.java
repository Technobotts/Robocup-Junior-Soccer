package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.*;
import technobotts.soccer.util.RobotDirectionModifier;
import technobotts.util.DataProcessor;

public class CameraNorthFacing extends Strategy
{
	DataProcessor robotHeadingModifier = new RobotDirectionModifier(0.75);
	
	boolean isRunning = false;
	
	float NaNHeading = Float.NaN;
	
	
	@Override
	protected void executeWithConnected(SoccerRobot robot)
	{
		robot.rotateTo(0, true);

		while(!Button.ESCAPE.isPressed())
		{
			if(isRunning)
			{
    			if(robot.hasBall())
    			{
    				robot.setMoveSpeed(200);
    				//float goalAngle = (float) robot.getGoalAngle();
        			//LCD.drawString("goalAngle: " + Math.rint(goalAngle), 0, 0);
    				//goalAngle = Float.isNaN(goalAngle) ? 0 : goalAngle;
    				//robot.rotate((float) robot.getGoalAngle(), true);
    				robot.travel(0);
    				Delay.msDelay(500);
    				robot.kick();
    				robot.rotateTo(0,false);
    			}
    
    			float ballAngle = robot.getBallAngle();
    			LCD.clear();
    			//LCD.drawString("ballAngle: " + Math.rint(ballAngle), 0, 0);
    
    			float heading = robot.getHeading() + ballAngle;
    
    			if(Float.isNaN(heading))
    			{
    				//if(robot.isStalled())
    				//	Sound.beepSequence();
    				//robot.isStalled()? -25: 200);
    				//robot.travel(180);
//    				if(Float.isNaN(NaNHeading))
//    					NaNHeading = robot.getTargetHeading();
//    				NaNHeading+
//    				robot.setMoveSpeed(150);
//    				robot.travel()NaNHeading
    				robot.stop();
    			}
    			else
    			{
    				NaNHeading = Float.NaN;
    				robot.setMoveSpeed(200);
    				robot.travel((float) robotHeadingModifier.getOutput(heading));
    			}
			}
			else
			{
				robot.rotateTo(robot.getHeading(), true);
			}
			if(Button.ENTER.isPressed())
			{
				isRunning = !isRunning;
				while(Button.readButtons() != 0)
					Thread.yield();
				if(isRunning)
				{
    				robot.setMoveSpeed(200);
					robot.travel(robot.getHeading());
    				Delay.msDelay(500);
    				robot.kick();
    				robot.rotateTo(0);
				}
				else
				{
					robot.stop();
					robot.resetPID();
				}
			}
			Thread.yield();
			//Delay.msDelay(50);
		}
	}
}

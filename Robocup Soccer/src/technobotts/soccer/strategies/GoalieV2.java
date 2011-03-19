package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.OldSoccerRobot;
import technobotts.soccer.robot.SoccerRobot;
import technobotts.soccer.util.GoalieTrajectoryFinder;

public class GoalieV2 extends Strategy
{
	public static void main(String[] args) throws InterruptedException
	{
		Strategy s = new GoalieV2();
		s.executeWith(new OldSoccerRobot());
	}
	boolean isRunning = false;

	public float           targetDist        = 17.5f; // Distance
	// to
	// maintain
	// from
	// goal

	GoalieTrajectoryFinder headingCalculator = new GoalieTrajectoryFinder(200, targetDist, 5, 0.01, 0);
	private boolean        ballLost          = false;
	private boolean        backHome          = true;

	protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
	{
		int loopCount = 0;
		while(!Button.ESCAPE.isPressed())
		{
			if(isRunning)
			{
    			float ballAngle = robot.getBallAngle();
    			float wallDist = robot.getRearWallDist();
    			headingCalculator.setInput(ballAngle, wallDist);
    
    			// Robot is near to wall again
    			if(backHome)
    			{
    				robot.setMoveSpeed(headingCalculator.getSpeed());
    				robot.travel(headingCalculator.getHeading());
    				
    			}
    
    			// Robot is too near to wall
    			if(wallDist < targetDist / 2)
    				headingCalculator.reset();
    
    			// Robot can kick the ball
    			if(robot.hasBall())
    			{
    				robot.setMoveSpeed(300);
    				robot.travel(0);
    				Thread.sleep(250);
    
    				if(headingCalculator.atMaximum())
    					robot.stop();
    
    				robot.kick();
    				robot.travel(180);
    			}
    
    			// Robot has just got near the wall
    			if(!backHome && wallDist < targetDist)
    			{
    				backHome = true;
    				robot.stop();
    			}
    
    			// Keep track of whether the ball has been lost
    			if(!Float.isNaN(ballAngle))
    			{
    				ballLost = false;
    			}
    			else if(!ballLost)
    			{
    				robot.setMoveSpeed(300);
    				robot.travel(Math.random() > 0.5 ? -150 : 150);
    				ballLost = true;
    				backHome = false;
    			}
    			
    			// Profiling code
    			loopCount++;
    			if(loopCount % 10 == 0)
    				LCD.drawInt(loopCount, 0, 7);
    			if(loopCount % 100 == 0)
    				Sound.beep();
    
    			LCD.drawString(wallDist + ":" + headingCalculator.getOutput() + "            ", 0, 0);
			}
			
			if(Button.ENTER.isPressed())
			{
				isRunning = !isRunning;
				while(Button.readButtons() != 0)
					Thread.yield();
				if(isRunning)
				{
					ballLost = false;
					backHome = true;
					headingCalculator.reset();
				}
				else
				{
					robot.stop();
				}
			}
			Thread.yield();
		}
	}
}

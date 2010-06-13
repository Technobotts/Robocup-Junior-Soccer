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
import technobotts.soccer.util.GoalieHeadingCalculator;

public class Goalie extends Strategy
{
	public static void main(String[] args) throws InterruptedException
	{
		Strategy s = new Goalie();
		s.executeWith(new OldSoccerRobot());
	}

	DataProcessor pid               = new SimplePID(3, 0, 0.5);

	GoalieHeadingCalculator  headingCalculator = new GoalieHeadingCalculator(80, 15);

	boolean ballLost = false;
	
	protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
	{
		while(!Button.ESCAPE.isPressed())
		{
			float ballAngle = robot.getBallAngle();
			float robotSpeed = (float) pid.getOutput(ballAngle);

			if(robot.bumperIsPressed())
			{
				headingCalculator.reset();
				robot.travel(0);
			}
			else if(!Float.isNaN(ballAngle))
			{
				
				float heading = (float) headingCalculator.getOutput();
				
				if(heading == 180)
				{
					//Robot REALLY should start retreating now
					robot.setMoveSpeed(300);
					robot.travel(heading);
				}
				else if(Math.abs(ballAngle) < 15)
				{
					//Ball ahead
					robot.setMoveSpeed(300);
					robot.travel(0);
					if(robot.hasBall())
					{
						//Ball in reach!
						Thread.sleep(250);
						robot.kick();
						Thread.sleep(250);
						robot.travel(180);
					}
				}
				else if(!Float.isNaN(robotSpeed))
				{
					//Ball to side
					robot.setMoveSpeed(Math.abs(robotSpeed));
					if(robotSpeed > 0)
						robot.travel(heading);
					else
						robot.travel(-heading);
				}
			}
			else
			{
				//No ball
				headingCalculator.pause();
				robot.stop();
			}

			Delay.msDelay(50);
		}
	}
}

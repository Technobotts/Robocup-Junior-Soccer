package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.OldSoccerRobot;
import technobotts.soccer.robot.SoccerRobot;
import technobotts.soccer.util.GoalieV2HeadingCalculator;

public class GoalieV2 extends Strategy
{
	public static void main(String[] args) throws InterruptedException
	{
		Strategy s = new GoalieV2();
		s.executeWith(new OldSoccerRobot());
	}


	GoalieV2HeadingCalculator headerCalculator = new GoalieV2HeadingCalculator(1);

	float         yBias = 0;

	private boolean ballLost = false;
	

	protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
	{
		while(!Button.ESCAPE.isPressed())
		{
			float ballAngle = robot.getBallAngle();
			
			boolean noball = Float.isNaN(ballAngle);
			
			if(robot.bumperIsPressed())
			{
				headerCalculator.reset();
				if(noball)
					robot.stop();
				else
					robot.travel(0);
			}
			else if(!noball)
			{
				double ballAngleRad = Math.toRadians(ballAngle);

				double xComp = Math.sin(ballAngleRad);
				double yComp = Math.cos(ballAngleRad) - headerCalculator.getOutput();
				float heading = (float) Math.toDegrees(Math.atan2(xComp, yComp));
				float speed = (float) (300 * Math.sqrt(xComp * xComp + yComp * yComp));

				robot.setMoveSpeed(speed);
				robot.travel(heading);

				if(robot.hasBall())
				{
					Thread.sleep(250);
					robot.setMoveSpeed(300);
					robot.travel(0);
					robot.kick();
					Thread.sleep(250);
					robot.travel(180);
				}
				ballLost = false;
			}
			else
			{
				if(!ballLost)
				{
					robot.setMoveSpeed(300);
					robot.travel(Math.random() > 0.5 ? -135 : 135);
					ballLost = true;
				}
			}

			Delay.msDelay(50);
		}
	}
}

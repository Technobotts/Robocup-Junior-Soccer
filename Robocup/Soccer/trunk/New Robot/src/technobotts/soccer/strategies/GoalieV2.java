package technobotts.soccer.strategies;

import java.awt.geom.Point2D;

import lejos.geom.Point;
import lejos.nxt.Button;
import lejos.util.Delay;
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

	GoalieTrajectoryFinder headingCalculator = new GoalieTrajectoryFinder(1, 300);
	private boolean           ballLost          = false;

	protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
	{
		headingCalculator.start();
		while(!Button.ESCAPE.isPressed())
		{
			float ballAngle = robot.getBallAngle();

			boolean noball = Float.isNaN(ballAngle);

			if(robot.bumperIsPressed())
			{
				headingCalculator.restart();
				if(noball)
					robot.stop();
				else
					robot.travel(0);
			}
			else if(!noball)
			{
				headingCalculator.resume();
				headingCalculator.setInput(ballAngle);

				robot.setMoveSpeed(headingCalculator.getSpeed());
				robot.travel(headingCalculator.getHeading());

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
				headingCalculator.pause();
			}
			Delay.msDelay(50);
		}
	}
}

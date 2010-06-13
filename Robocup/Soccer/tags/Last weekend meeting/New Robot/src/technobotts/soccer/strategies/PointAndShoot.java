package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.NewSoccerRobot;
import technobotts.soccer.robot.SoccerRobot;

public class PointAndShoot extends Strategy
{

	public static void main(String[] args) throws InterruptedException
	{
		Strategy s = new PointAndShoot();
		s.executeWith(new NewSoccerRobot());
	}

	@Override
	protected void executeWithConnected(SoccerRobot robot)
	{
		while(!Button.ESCAPE.isPressed())
		{
			double goalAngle = robot.getGoalAngle();

			LCD.clear();
			LCD.drawString("GoalAngle: " + Math.floor(goalAngle), 0, 0);
			LCD.refresh();

			if(robot.hasBall())
			{
				robot.rotate((float) goalAngle, true);
				//robot.travel(0);
				Delay.msDelay(500);
				robot.kick();
				robot.rotateTo(0);
			}
		}
	}

}

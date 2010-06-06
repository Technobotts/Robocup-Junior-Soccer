package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.NewSoccerRobot;

public class PointAndShoot extends Strategy<NewSoccerRobot>
{
	public PointAndShoot(NewSoccerRobot robot)
	{
		super(robot);
	}

	public static void main(String[] args)
	{
		Strategy<NewSoccerRobot> s = new PointAndShoot(new NewSoccerRobot());
		s.run();
	}

	@Override
	public void run()
	{
		if(!robot.connectToSlave())
			Sound.buzz();

		while(!Button.ESCAPE.isPressed())
		{

			double goalAngle = robot.getGoalAngle();

			LCD.clear();
			LCD.drawString("GoalAngle: " + Math.floor(goalAngle), 0, 0);
			LCD.refresh();

			if(robot.hasBall())
			{
				robot.pilot.rotate((float) goalAngle, true);
				robot.pilot.travel(0);
				Delay.msDelay(500);
				robot.kick();
				robot.pilot.rotateTo(0);
			}

		}
		if(!robot.disconnect())
			Sound.buzz();
	}
}

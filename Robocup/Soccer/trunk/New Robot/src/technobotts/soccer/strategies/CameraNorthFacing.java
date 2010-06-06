package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.util.DataProcessor;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.*;
import technobotts.soccer.util.RobotDirectionModifier;

public class CameraNorthFacing extends Strategy<NewSoccerRobot>
{
	DataProcessor robotHeadingModifier = new RobotDirectionModifier(3);

	public CameraNorthFacing(NewSoccerRobot robot)
	{
		super(robot);
	}

	public static void main(String[] args)
	{
		Strategy<NewSoccerRobot> s = new CameraNorthFacing(new NewSoccerRobot());
		s.run();
	}

	@Override
	public void run()
	{
		OmniCompassPilot pilot = robot.getPilot();
		if(!robot.connectToSlave())
			Sound.buzz();

		pilot.rotateTo(0, true);
		
		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
			{
				robot.pilot.rotate((float) robot.getGoalAngle(), true);
				robot.pilot.travel(0);
				Delay.msDelay(500);
				robot.kick();
				robot.pilot.rotateTo(0);
			}

			float ballAngle = robot.getBallAngle();
			LCD.clear();
			LCD.drawString("Angle: " + Math.rint(ballAngle), 0, 0);

			float heading = robot.getHeading() + ballAngle;
			
			if(Float.isNaN(heading))
			{
				pilot.travel(180);
			}
			else
			{
				pilot.travel((float) robotHeadingModifier.getOutput(heading));
			}
			Delay.msDelay(50);
		}
		robot.disconnect();
	}
}

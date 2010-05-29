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

public class NorthFacing extends Strategy<SoccerRobot>
{
	DataProcessor robotHeadingModifier = new RobotDirectionModifier(1.5);
	
	public NorthFacing(SoccerRobot robot)
	{
		super(robot);
	}

	public static void main(String[] args)
	{
		Strategy<SoccerRobot> s = new NorthFacing(new NewSoccerRobot());
		s.run();
	}

	@Override
	public void run()
	{
		OmniCompassPilot pilot = robot.getPilot();
		if(!robot.connectToSlave())
			Sound.buzz();

		pilot.rotateTo(0, true);
		int i = 0;
//		float lastHeading = Float.NaN;
		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
			{
				pilot.travel(0);
				robot.kick();
			}
			float ballAngle = robot.getBallAngle();
			LCD.clear();
			LCD.drawString("Angle: "+Math.rint(ballAngle),0,0);
			
			float heading = robot.getHeading() + ballAngle;
			if(Float.isNaN(heading))
			{
				pilot.travel(180);
			}
			else
			{
				pilot.travel((float) robotHeadingModifier.getOutput(heading));
			}
			LCD.drawInt(i++, 0, 3);
			Delay.msDelay(50);
			
		}
		robot.disconnect();
	}
}

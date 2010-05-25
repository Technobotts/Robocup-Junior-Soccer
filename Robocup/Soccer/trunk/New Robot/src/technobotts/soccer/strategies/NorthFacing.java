package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.robotics.navigation.OmniCompassPilot;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.AbstractSoccerRobot;
import technobotts.soccer.robot.NewSoccerRobot;
import technobotts.soccer.robot.SoccerRobot;

public class NorthFacing extends Strategy<SoccerRobot>
{
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

		float lastHeading = Float.NaN;
		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
			{
				pilot.travel(0);
				robot.kick();
			}
			float ballAngle = robot.getBallAngle();
			LCD.drawString("Angle: "+Math.rint(ballAngle*10)/10+"     ",0,0);
			
			float heading = robot.getHeading() + ballAngle;
			if(Float.isNaN(heading))
				heading = lastHeading;
			else
				lastHeading = heading;
			pilot.travel(heading);
			Sound.beep();
			try
			{
				Thread.sleep(250);
			}
			catch(InterruptedException e)
			{}
		}
		robot.disconnect();
	}
}

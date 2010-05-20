package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.AbstractSoccerRobot;
import technobotts.soccer.NewSoccerRobot;
import technobotts.soccer.Strategy;

public class NorthFacing extends Strategy<AbstractSoccerRobot>
{
	public NorthFacing(AbstractSoccerRobot robot)
	{
		super(robot);
	}

	public static void main(String[] args)
	{
		Strategy<AbstractSoccerRobot> s = new NorthFacing(new NewSoccerRobot());
		s.run();
	}

	@Override
	public void run()
	{
		if(!robot.connectTo("NXT"))
			Sound.buzz();

		robot.pilot.rotateTo(0, true);

		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
			{
				robot.pilot.travel(0);
				robot.kick();
			}

			robot.pilot.travel(robot.getHeading() + robot.ballDetector.getAngle());
			try
			{
				Thread.sleep(50);
			}
			catch(InterruptedException e)
			{}
		}
		robot.disconnect();
	}
}

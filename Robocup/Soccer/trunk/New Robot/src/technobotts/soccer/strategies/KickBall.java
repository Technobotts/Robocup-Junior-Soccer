package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.NewSoccerRobot;
import technobotts.soccer.SoccerRobot;
import technobotts.soccer.Strategy;

public class KickBall extends Strategy<SoccerRobot>
{

	public KickBall(SoccerRobot robot)
	{
		super(robot);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args)
	{
		Strategy<SoccerRobot> s = new KickBall(new NewSoccerRobot());
		s.run();
	}

	@Override
	public void run()
	{
		if(!robot.connectToSlave())
			Sound.buzz();

		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
				robot.kick();

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

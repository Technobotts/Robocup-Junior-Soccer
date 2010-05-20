package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.AbstractSoccerRobot;
import technobotts.soccer.NewSoccerRobot;
import technobotts.soccer.SoccerRobot;
import technobotts.soccer.Strategy;

public class GoAndKick extends Strategy<AbstractSoccerRobot>
{

	public GoAndKick(AbstractSoccerRobot robot)
	{
		super(robot);
	}

	public static void main(String[] args)
	{
		Strategy<AbstractSoccerRobot> s = new GoAndKick(new NewSoccerRobot());
		s.run();
	}

	@Override
	public void run()
	{
		if(!robot.connectTo("NXT"))
			Sound.buzz();
		
		robot.pilot.setDirectionFinder(robot.ballDetector);
		robot.pilot.rotateTo(0,true);
		robot.pilot.travel(0);

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

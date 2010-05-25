package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.AbstractSoccerRobot;
import technobotts.soccer.robot.NewSoccerRobot;

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
		if(!robot.connectToSlave())
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

package technobotts.soccer.strategies;


import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.util.DataProcessor;
import lejos.util.SimplePID;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.NewSoccerRobot;
import technobotts.soccer.robot.SoccerRobot;

public class Goalie extends Strategy
{

	SimplePID pid = new SimplePID(2,0,0);
	
	public static void main(String[] args)
	{
		Strategy s = new Goalie();
		s.executeWith(new NewSoccerRobot());
	}

	@Override
	public void executeWith(SoccerRobot robot)
	{
		if(!robot.connectToSlave())
			Sound.buzz();

		robot.travel(90);
		
		while(!Button.ESCAPE.isPressed())
		{
			if(robot.hasBall())
			{
				robot.kick();
			}
			float ballAngle = robot.getBallAngle();
//			if(ballAngle > 0) robot.travel(90);
//			else if(ballAngle == 0) robot.stop();
//			else if(ballAngle < 0) robot.travel(-90);
			robot.setMoveSpeed((float) pid.getOutput(ballAngle));
		}
		if(!robot.disconnect())
			Sound.buzz();
	}

}

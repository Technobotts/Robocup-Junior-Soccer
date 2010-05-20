package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.NewSoccerRobot;
import technobotts.soccer.AbstractSoccerRobot;
import technobotts.soccer.slave.SoccerSlave;

public class CommTest
{

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{
		AbstractSoccerRobot robot = new NewSoccerRobot();
		boolean success = robot.connectTo("NXT");
		if(!success)
			Sound.beepSequenceUp();

		while(!Button.ESCAPE.isPressed())
		{
    		if(robot.hasBall())
    		{
    			robot.kick();
    		}
    		Thread.sleep(250);
		}

		robot.disconnect();
	}

}

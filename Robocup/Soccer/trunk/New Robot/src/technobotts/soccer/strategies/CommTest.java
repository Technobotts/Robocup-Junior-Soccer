package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.SoccerRobot;
import technobotts.soccer.master.NewSoccerRobot;
import technobotts.soccer.slave.SoccerSlave;

public class CommTest
{

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{
		SoccerRobot robot = new NewSoccerRobot();
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

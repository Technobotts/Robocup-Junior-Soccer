package technobotts.soccer.strategies;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import technobotts.soccer.master.NewSoccerRobot;
import technobotts.soccer.util.DualLightSourceFinder;

public class IRTest
{

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{
		NewSoccerRobot robot = new NewSoccerRobot();

		boolean success = robot.connectTo("NXT");
		if(!success)
			Sound.beepSequenceUp();

		while(true)
		{
			Sound.beep();
			float angle = ((DualLightSourceFinder) robot.ballDetector).getLeft().getAngle();
			LCD.drawString("Angle = "+(int)angle+"    ", 0, 0);
			robot.pilot.rotate(angle,true);
			Thread.sleep(250);
		}

	}

}

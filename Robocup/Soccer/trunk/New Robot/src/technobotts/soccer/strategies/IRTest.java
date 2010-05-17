package technobotts.soccer.strategies;

import lejos.nxt.Button;
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
		
		robot.pilot.setDirectionFinder(robot.ballDetector);
		robot.pilot.rotateTo(0,true);
		robot.pilot.travel(0);

		boolean success = robot.connectTo("NXT");
		if(!success)
			Sound.beepSequenceUp();
		
		Button.ENTER.waitForPressAndRelease();
/*
		while(true)
		{
			float angle = robot.ballDetector.getAngle();
			LCD.drawString("Angle = "+Math.floor(angle)+"    ", 0, 0);
			robot.pilot.rotate(-angle,true);
			Thread.sleep(100);
		}*/

	}

}

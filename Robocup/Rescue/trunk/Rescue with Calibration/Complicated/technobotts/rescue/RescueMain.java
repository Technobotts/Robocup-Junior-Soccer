package technobotts.rescue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.addon.Lamp;
import lejos.nxt.addon.LineLeader;

public class RescueMain
{
	static RescueRobot robot;

	static void init()
	{
		RescueColors cols = null;
		try
		{
			File f = new File("CalibrationData.dat");

			FileInputStream is = new FileInputStream(f);
			cols = RescueColors.readObject(is);
			is.close();
			LCD.drawString("File read", 0, 0);
			LCD.drawString("sucessfully", 0, 0);
		}
		catch(IOException e)
		{
			LCD.drawString("Error reading", 0, 0);
			LCD.drawString("file", 0, 1);
			Button.waitForPress();
			System.exit(-1);
		}
		cols.printToLCD();
		robot = new RescueRobot(cols);
		Button.ENTER.waitForPressAndRelease();
		LCD.clear();
	}

	public static void main(String... args)
	{
		init();
//
//		Follower     f = new Follower(robot);
//		VictimFinder v = new VictimFinder(robot);
//		AntiTwitch   t = new AntiTwitch(robot);

		robot.followerT.start();
		robot.victimT.start();
		robot.twitchT.start();
		robot.loggerT.start();
		robot.debrisT.start();

		Button.ESCAPE.waitForPressAndRelease();

		robot.followerT.stop();
		robot.victimT.stop();
		robot.twitchT.stop();
	}
}

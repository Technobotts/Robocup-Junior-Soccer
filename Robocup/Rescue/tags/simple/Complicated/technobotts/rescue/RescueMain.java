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

	static void initRobot()
	{
		LineLeader ll = new LineLeader(SensorPort.S2);
		ColorSensor cs = new ColorSensor(SensorPort.S1);
		Lamp l = new Lamp(MotorPort.A);
		robot = new RescueRobot(Motor.C, Motor.B, ll, cs, l);
	}

	static void loadColors()
	{
		try
		{
			File f = new File("CalibrationData.dat");

			FileInputStream is = new FileInputStream(f);
			robot.colors = RescueColors.readObject(is);
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
		robot.colors.printToLCD();
		Button.ENTER.waitForPressAndRelease();
		LCD.clear();
	}

	public static void main(String... args)
	{
		initRobot();
		loadColors();

		Follower     f = new Follower(robot);
		VictimFinder v = new VictimFinder(robot);
		AntiTwitch   t = new AntiTwitch(robot);

		f.start();
		v.start();
		t.start();

		Button.ESCAPE.waitForPressAndRelease();

		f.stop();
		v.stop();
		t.stop();
	}
}

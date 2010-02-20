import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import lejos.nxt.*;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.addon.Lamp;
import lejos.nxt.addon.LineLeader;
import lejos.robotics.navigation.Pilot;
import lejos.robotics.navigation.TachoPilot;
import lejos.util.Delay;
import technobotts.rescue.*;

public class SimpleFollower
{
	static int	    maxspeed	   = 400;
	static int	    minspeed	   = -200;

	static double[]	sensorsFactors	= {216, 36, 6, 1, 0, 0, 0, 0};

	static int scalePower(double input, double maxinput)
	{
		return (int) (input * (maxspeed - minspeed) / maxinput + minspeed);
	}

	static int calcLeftSpeed(int[] sensors)
	{
		double rawpower = 0;
		double maxrawpower = 0;
		for(int i = 0; i < 8; i++)
		{
			rawpower += sensors[7 - i] * sensorsFactors[i];
			maxrawpower += 100 * sensorsFactors[i];
		}
		return scalePower(rawpower, maxrawpower);
	}

	static int calcRightSpeed(int[] sensors)
	{
		double rawpower = 0;
		double maxrawpower = 0;
		for(int i = 0; i < 8; i++)
		{
			rawpower += sensors[i] * sensorsFactors[i];
			maxrawpower += 100 * sensorsFactors[i];
		}
		return scalePower(rawpower, maxrawpower);
	}

	static RescueRobot	  robot;

	static RescueColors	  colors;
	static MovementLogger	logger;

	static void init() throws InterruptedException
	{
		Sound.beep();
		try
		{
			File f = new File("CalibrationData.dat");

			FileInputStream is = new FileInputStream(f);
			colors = RescueColors.readObject(is);
			is.close();
			LCD.drawString("File read", 0, 0);
			LCD.drawString("sucessfully", 0, 0);
		}
		catch(IOException e)
		{
			LCD.drawString("Error reading", 0, 0);
			LCD.drawString("file", 0, 1);
			Thread.sleep(1000);
			System.exit(-1);
		}
		colors.printToLCD();
		robot = new RescueRobot(colors);
		Button.ENTER.waitForPressAndRelease();

		logger = new MovementLogger(robot);
		Thread t = new Thread(logger);
		t.setDaemon(true);
		t.start();
	}

	public static boolean linefound()
	{
		if(colors.getSensorColor(robot.colorSensor) != colors.white)
		{
			return true;
		}
		else
		{
			/*
			 * byte[] sensors = robot.lineSensor.getSensors();
			 * if(sensors != null)
			 * {
			 * return sensors[0] < 50 || sensors[3] < 50 || sensors[4] < 50 || sensors[7] < 50;
			 * }
			 * else
			 */
			return false;
		}
	}

	public static boolean findline()
	{
		TachoPilot p = robot.pilot;
		int[] angles = new int[] {30, -30, 60, -60, 90, -90, 0};
		int lastangle = 0;
		for(int angle : angles)
		{
			p.rotate((lastangle - angle) * 4, true);
			while(p.isMoving())
			{
				if(linefound())
				{
					Sound.beepSequence();
					p.stop();
					return true;
				}
				Thread.yield();
			}
			p.stop();
			lastangle = angle;
			Delay.msDelay(250);
		}
		return false;
	}

	public static void main(String... args) throws InterruptedException
	{
		init();
		long lastVictimTime = 0;
		while(true)
		{
			RawColor color = colors.getSensorColor(robot.colorSensor);
			if((color == colors.silver || color == colors.green)
			   && System.currentTimeMillis() - lastVictimTime > 2500)
			{
				synchronized(robot.pilot)
				{
					robot.pilot.stop();
					robot.showVictimFound();
					lastVictimTime = System.currentTimeMillis();
					robot.pilot.travel(5);
					findline();
				}
			}
			else if(color == colors.white || color == colors.black)
			{
				synchronized(robot.pilot)
				{
					int[] sensors = robot.lineSensor.getSensors();
					if(sensors != null)
					{

						int leftspeed = calcLeftSpeed(sensors);
						int rightspeed = calcRightSpeed(sensors);
						robot.pilot.setLeftSpeed(leftspeed);
						robot.pilot.setRightSpeed(rightspeed);
					}
				}
			}
			Thread.sleep(50);
		}
	}
}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Graphics;

import technobotts.rescue.RawColor;
import technobotts.rescue.RescueColors;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.addon.LineLeader;
import lejos.util.TextMenu;

public class CalibrateV2
{

	/**
	 * @param args
	 * @throws InterruptedException
	 */

	static LineLeader  ll = new LineLeader(SensorPort.S2);
	static ColorSensor cs = new ColorSensor(SensorPort.S1);
	static final String	fileName	= "CalibrationData.dat";

	public static void CalibLL() throws InterruptedException
	{
		LCD.drawString("Hold the LL", 0, 1);
		LCD.drawString("over White", 0, 2);
		Button.ENTER.waitForPressAndRelease();
		ll.sendCommand('W');
		Thread.sleep(50);

		LCD.drawString("Hold the LL", 0, 1);
		LCD.drawString("over Black", 0, 2);
		Button.ENTER.waitForPressAndRelease();
		ll.sendCommand('B');
		Thread.sleep(50);

		Graphics g = new Graphics();
		g.clear();
		g.fillRect(19, 0, 19, 100);
		g.drawRect(59, 0, 19, 100);

		int[] white = ll.getWhiteLimits();
		for(int i = 0; i < 8; i++)
		{
			g.drawString("" + white[i], 78 - (white[i] + "").length() * 6, i * 8);
		}

		int[] black = ll.getBlackLimits();
		for(int i = 0; i < 8; i++)
		{
			g.drawString("" + black[i], 38 - (black[i] + "").length() * 6, i * 8, true);
		}
		Button.ESCAPE.waitForPressAndRelease();
		g.clear();
	}

	public static void TestLL() throws InterruptedException
	{
		Graphics g = new Graphics();
		g.clear();
		g.autoRefresh(false);
		while(!Button.ESCAPE.isPressed())
		{
			int[] sensors = ll.getSensors();
			g.setColor(1);
			g.fillRect(0, 0, 100, 64);
			g.setColor(0);
			if(sensors != null)
			{
				int len = sensors.length;
				for(int i = 0; i < len; i++)
				{
					int s = 64 * sensors[i] / 100;
					g.fillRect(i * 12 + 3, 64 - s, 10, s);
				}
			}
			g.refresh();
			Thread.sleep(50);
			g.clear();
		}
		Button.ESCAPE.waitForPressAndRelease();
	}
	
	public static void TestCS() throws InterruptedException
	{
		RescueColors colors = null;
		try
		{
			File f = new File(fileName);

			FileInputStream is = new FileInputStream(f);
			colors = RescueColors.readObject(is);
			is.close();
			LCD.drawString(" File read   ", 0, 1);
			LCD.drawString(" sucessfully ", 0, 2);
		}
		catch(IOException e)
		{
			LCD.drawString("Error reading file", 0, 1);
			LCD.drawString(e.getMessage(), 0, 2);
			return;
		}
		finally
		{
			Button.waitForPress();
		}
		LCD.clear();
		colors.printToLCD();
		Button.waitForPress();
		LCD.clear();
		while(!Button.ESCAPE.isPressed())
		{
			RawColor color = colors.getSensorColor(cs);
			String current = "N/A";
			if(color == colors.silver)
			{
				current = "Victim (S)";
			}
			else if(color == colors.white)
			{
				current = "Nothing";
			}
			else if(color == colors.green)
			{
				current = "Victim (G)";
			}
			else if(color == colors.black)
			{
				current = "Line";
			}
			else
			{
				current = "N/A";
			}
			LCD.drawString(RawColor.fromHTSensor(cs)+"               ", 0, 7);
			LCD.drawString(current + " seen       ", 0, 6);
			LCD.refresh();
			Thread.sleep(50);
		}
	}
	public static RawColor askForColor(String name) throws InterruptedException
	{
		LCD.clear();
		LCD.drawString("Hold over " + name, 0, 0);
		RawColor color = null;
		do
		{
			color = RawColor.fromHTSensor(cs);
			LCD.drawString(color + "     ", 0, 1);
			LCD.refresh();
			Thread.sleep(10);
		} while(!Button.ENTER.isPressed());

		LCD.drawString("Got:", 0, 1);
		LCD.drawString(color.toString(), 0, 2);
		Thread.sleep(250);

		return color;
	}
	public static void CalibCS() throws InterruptedException
	{
		RawColor silver, white, green, black;
		silver = askForColor("silver");
		white = askForColor("white");
		green = askForColor("green");
		black = askForColor("black");

		RescueColors colors = new RescueColors(silver, white, green, black);
		colors.printToLCD();
		int bID = Button.waitForPress();
		LCD.clear();
		if(bID == Button.ID_ENTER)
		{
			try
			{
				File f = new File(fileName);
				if(!f.exists())
				{
					f.createNewFile();
				}
				else
				{
					f.delete();
					f.createNewFile();
				}

				FileOutputStream os = new FileOutputStream(f);
				colors.writeObject(os);
				os.close();
				LCD.drawString("File written", 0, 0);
				LCD.drawString("sucessfully", 0, 0);
			}
			catch(IOException e)
			{
				LCD.drawString("Error writing to file", 0, 0);
				LCD.drawString(e.getMessage(), 0, 1);
			}
		}
		else
		{
			LCD.drawString("Operation Aborted",0,0);
		}
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		TextMenu sensorMenu = new TextMenu(new String[] {"LineLeader", "Color sensor"}, 0, "Calibrate");
		int sensor;
		do
		{
			sensor = sensorMenu.select();
			if(sensor == 0)
			{
				int mode;
				TextMenu modeMenu = new TextMenu(new String[] {"Calibrate", "Check"}, 0, "LineLeader");
				do
				{
					mode = modeMenu.select();
					while(Button.ENTER.isPressed());

					if(mode == 0)
					{
						CalibLL();
					}
					else if(mode == 1)
					{
						TestLL();
					}
				} while(mode >= 0);
			}
			else if(sensor == 1)
			{
				int mode;
				TextMenu modeMenu = new TextMenu(new String[] {"Calibrate", "Check"}, 0, "ColorSensor");
				do
				{
					mode = modeMenu.select();
					while(Button.ENTER.isPressed());

					if(mode == 0)
					{
						CalibCS();
					}
					else if(mode == 1)
					{
						TestCS();
					}
				} while(mode >= 0);
			}
		} while(sensor >= 0);
	}

}

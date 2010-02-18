import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import technobotts.rescue.*;

import lejos.nxt.*;
import lejos.nxt.addon.ColorSensor;

public class Calibrate
{
	static final String	fileName	= "CalibrationData.dat";
	static ColorSensor	cs	     = new ColorSensor(SensorPort.S1);

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

	public static void main(String[] args) throws Exception
	{
		while(Button.ENTER.isPressed())
			Thread.yield();
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
}
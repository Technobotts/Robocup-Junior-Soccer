import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import technobotts.rescue.RawColor;
import technobotts.rescue.RescueColors;

import lejos.nxt.*;
import lejos.nxt.addon.ColorSensor;


public class CalibView
{
	static final String	fileName	= "CalibrationData.dat";
	static ColorSensor	cs	     = new ColorSensor(SensorPort.S1);

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{
		RescueColors colors = null;
		try
		{
			File f = new File(fileName);

			FileInputStream is = new FileInputStream(f);
			colors = RescueColors.readObject(is);
			is.close();
			LCD.drawString("File read", 0, 0);
			LCD.drawString("sucessfully", 0, 0);
		}
		catch(IOException e)
		{
			LCD.drawString("Error reading file", 0, 0);
			LCD.drawString(e.getMessage(), 0, 1);
			System.exit(-1);
		}
		colors.printToLCD();
		Button.ENTER.waitForPressAndRelease();
		LCD.clear();
		while(true)
		{
			RawColor color = colors.getSensorColor(cs);
			if(color == colors.silver)
			{
				LCD.drawString("Victim (S) ",0,0);
			}
			else if(color == colors.white)
			{
				LCD.drawString("Nothing    ",0,0);
			}
			else if(color == colors.green)
			{
				LCD.drawString("Victim (G) ",0,0);
			}
			else if(color == colors.black)
			{
				LCD.drawString("Line      ",0,0);
			}
			LCD.drawString(RawColor.fromHTSensor(cs)+"               ", 0, 7);
			Thread.sleep(50);
		}
	}

}

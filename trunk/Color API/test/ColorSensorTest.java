import lejos.nxt.Button;
import lejos.nxt.ColorLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;

public class ColorSensorTest
{
	public static void main(String[] args) throws Exception
	{		
		ColorLightSensor cs = new ColorLightSensor(SensorPort.S1,
				SensorPort.TYPE_COLORFULL);

		LCD.clear();
		LCD.drawString("Hold over black", 1, 1);
		Button.ENTER.waitForPressAndRelease();
		cs.calibrateLow();

		LCD.clear();
		LCD.drawString("Hold over white", 1, 1);
		Button.ENTER.waitForPressAndRelease();
		cs.calibrateHigh();

		while(!Button.ESCAPE.isPressed())
		{
			/*int[] vals = new int[4];
			cs.readValues(vals);

			String output = "(" + vals[Colors.RGB_RED] + ","
					+ vals[Colors.RGB_GREEN] + "," + vals[Colors.RGB_BLUE]
					+ ")";*/
			int[] colors = cs.getColor();
			String output = "(" + colors[0] + ","
								+ colors[1] + ","
								+ colors[2] + ")";
			LCD.clear();
			LCD.drawString(output, 1, 1);
			LCD.refresh();
		}
	}
}

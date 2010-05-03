import lejos.nxt.Button;
import lejos.nxt.I2CPort;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.robotics.ImprovedRangeReadings;
import lejos.robotics.RangeReading;


public class IRTest
{
	public static void main(String... args) throws InterruptedException
	{
		IRSeekerV2 ir = new IRSeekerV2(SensorPort.S1, Mode.DC);
		while(!Button.ESCAPE.isPressed())
		{
			LCD.clear();
			ImprovedRangeReadings readings = ir.getRangeValues();
			int y = 0;
			for(RangeReading reading:readings)
			{
				LCD.drawString(reading.getAngle()+": "+reading.getRange(), 0, y++);
			}
			RangeReading totalReading = readings.getRangeReading();
			LCD.drawString(Math.round(totalReading.getAngle())+": "+Math.round(totalReading.getRange()*10)/10,0,7);
			LCD.refresh();
			Thread.sleep(100);
		}
	}
}
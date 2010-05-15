import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.robotics.ImprovedRangeReadings;
import lejos.robotics.RangeReading;
import lejos.util.TextMenu;

public class IRTest
{
	public static void main(String... args) throws InterruptedException
	{
		Mode[] modes = {Mode.DC, Mode.AC_600Hz, Mode.AC_1200Hz};
		TextMenu textmenu = new TextMenu(new String[] {"DC", "AC 600Hz", "AC 1200Hz"});

		int choice = textmenu.select();
		if(choice < 0)
			return;
		
		IRSeekerV2 ir = new IRSeekerV2(SensorPort.S1, modes[choice]);
		while(!Button.ESCAPE.isPressed())
		{
			LCD.clear();
			ImprovedRangeReadings readings = ir.getRangeValues();
			int y = 0;
			for(RangeReading reading : readings)
			{
				LCD.drawString(reading.getAngle() + ": " + reading.getRange(), 0, y++);
			}
			RangeReading totalReading = readings.getRangeReading();
			LCD.drawString(Math.round(totalReading.getAngle()) + ": " + Math.round(totalReading.getRange() * 10) / 10,
			               0, 7);
			//LCD.drawString("A: "+ir.getAngle(), 0, 7);
			LCD.refresh();
			Thread.sleep(100);
		}
	}
}
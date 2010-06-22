import technobotts.robotics.ImprovedRangeReadings;
import technobotts.sensors.IRSeekerV2;
import technobotts.sensors.IRSeekerV2.Mode;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.RangeReading;
import lejos.util.TextMenu;

public class IRTest
{
	public static void main(String... args) throws InterruptedException
	{
		Mode[] modes = {Mode.DC, Mode.AC_600Hz, Mode.AC_1200Hz};
		TextMenu modeMenu = new TextMenu(new String[] {"DC", "AC 600Hz", "AC 1200Hz"},0,"Mode");
		
		TextMenu portMenu = new TextMenu(new String[] {"S1","S2","S3","S4"},0,"Port");

		int mode = modeMenu.select();
		if(mode < 0)
			return;
		
		int port = portMenu.select();
		if(mode < 0)
			return;
		
		IRSeekerV2 ir = new IRSeekerV2(SensorPort.PORTS[port], modes[mode]);
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
			LCD.drawString("A: "+ir.getAngle(), 0, 7);
			LCD.refresh();
			Thread.sleep(100);
		}
	}
}
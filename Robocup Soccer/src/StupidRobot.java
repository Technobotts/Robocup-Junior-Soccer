import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.LightDetector;
import lejos.util.Delay;

public class StupidRobot
{

	static LightDetector[] sensors = new LightDetector[] {
	    new LightSensor(SensorPort.S1, true), new LightSensor(SensorPort.S2, true),
	    new LightSensor(SensorPort.S3, true), new LightSensor(SensorPort.S4, true),
	                        };

	public static void main(String[] args)
	{
		while(true)
		{
			LCD.clear();
			int i = 0;
			for(LightDetector sensor : sensors)
			{
				LCD.drawString(i+": "+sensor.getLightValue(), 0, i++);
			}
			LCD.refresh();
			Delay.msDelay(100);
		}
	}

}

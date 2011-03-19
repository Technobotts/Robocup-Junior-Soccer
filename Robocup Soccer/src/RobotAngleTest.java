import technobotts.nxt.addon.InvertedCompassSensor;
import technobotts.soccer.util.RobotDirectionModifier;
import technobotts.util.DataProcessor;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassSensor;


public class RobotAngleTest
{

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{
		CompassSensor sensor = new InvertedCompassSensor(SensorPort.S1);
		DataProcessor dp = new RobotDirectionModifier(1.5);
		
		while(!Button.ESCAPE.isPressed())
		{
			double angle = sensor.getDegreesCartesian();
			double calc = dp.getOutput(angle);
			
			LCD.clear();
			LCD.drawString("Raw:  "+angle, 0, 0);
			LCD.drawString("Calc: "+calc, 0, 1);
			LCD.refresh();
			
			Thread.sleep(100);
		}
	}

}

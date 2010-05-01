import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class KickerTester
{
	public static void main(String... args) throws InterruptedException
	{
		Motor kicker = Motor.A;
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S2);

		kicker.setSpeed(1000);
		kicker.smoothAcceleration(false);

		while(true)
		{
			int dist = us.getDistance();
			if(dist <= 10)
			{
				Sound.beepSequenceUp();

				kicker.setPower(100);
				kicker.forward();
				long startTime = System.currentTimeMillis();
				while(kicker.getTachoCount() < 90
				      && startTime + 1500 > System.currentTimeMillis())
					;
				kicker.flt();
				Thread.sleep(100);

				kicker.setPower(50);
				kicker.rotateTo(0);
			}
			LCD.clear();
			LCD.drawString("US: " + us.getDistance(), 0, 0);
			LCD.refresh();
			Thread.sleep(100);
		}
	}
}

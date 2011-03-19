import lejos.nxt.Button;
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
		kicker.backward();
		Thread.sleep(500);
		kicker.resetTachoCount();
		kicker.lock(100);

		while(true)
		{
			int dist = us.getDistance();
			
			LCD.clear();
			LCD.drawString("US: " + dist, 0, 0);
			LCD.refresh();
			
			if(dist <= 10 || Button.ENTER.isPressed())
			{
				Sound.beepSequenceUp();

				kicker.forward();
				long startTime = System.currentTimeMillis();
				while(kicker.getTachoCount() < 120
				      && startTime + 1000 > System.currentTimeMillis())
					;
				kicker.flt();
				Thread.sleep(250);

				kicker.backward();
				Thread.sleep(500);
				kicker.lock(100);
			}

			Thread.sleep(100);
		}
	}
}

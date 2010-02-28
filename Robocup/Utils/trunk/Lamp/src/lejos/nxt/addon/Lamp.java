package lejos.nxt.addon;

import lejos.nxt.BasicMotorPort;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.Sound;

public class Lamp extends RCXMotor
{
	public Lamp(BasicMotorPort port)
	{
		super(port);
		// TODO Auto-generated constructor stub
	}

	public void on()
	{
		on(100);
	}

	public void on(int brightness)
	{
		setPower(Math.abs(brightness));
		if(brightness > 0)
			forward();
		else
			backward();
	}

	public void off()
	{
		stop();
	}

	public void pulse(long duration, float pulseRate)
	{
		long startTime = System.currentTimeMillis();
		long i;
		do
		{
			i = System.currentTimeMillis() - startTime;
			int b = (int) (Math.sin(i / 500.0 * pulseRate * Math.PI) * 50 + 50);
			on(b);
		} while(i <= duration);
		off();
	}

	public void pulse(long duration)
	{
		pulse(duration, 2);
	}
	
	public static void main(String... args)
	{
		Lamp lamp = new Lamp(MotorPort.A);
		lamp.on();
		Button.waitForPress();
		final int duration = 20000;
		final int pulseRate = 4;
		long startTime = System.currentTimeMillis();
		long i;
		do
		{
			i = System.currentTimeMillis() - startTime;
			double p = (Math.sin(i / 500.0 * pulseRate * Math.PI) + 1) / 2;
			int b = (int) ((p-0.5) * 50);
			int f = (int) (440 * Math.pow(2, p));
			lamp.on(b);
			Sound.playTone(f, 10);
		} while(i <= duration);
		lamp.off();
	}
}

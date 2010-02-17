package lejos.nxt.addon;

import lejos.nxt.BasicMotorPort;

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
		setPower(brightness);
		forward();
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
			int b = (int) (Math.sin(i/500.0*pulseRate*Math.PI)*50+50);
			on(b);
		} while(i <= duration);
		off();
	}
	
	public void pulse(long duration)
	{
		pulse(duration,2);
	}
}

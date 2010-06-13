import lejos.nxt.Button;
import lejos.nxt.Motor;

public class MotorComp
{

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		Motor[] tests = {Motor.A, Motor.B, Motor.C};

		for(Motor m : tests)
		{
			m.setSpeed(1000);
			m.smoothAcceleration(false);
			m.lock(100);
		}

		while(true)
		{
			while(!Button.ENTER.isPressed())
			{}
			for(Motor m : tests)
			{
				m.forward();
			}
			while(!Button.ENTER.isPressed())
			{}
			for(Motor m : tests)
			{
				m.stop();
			}
		}
	}

}

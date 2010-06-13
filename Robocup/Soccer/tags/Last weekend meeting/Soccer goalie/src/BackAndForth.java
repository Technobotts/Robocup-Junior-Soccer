import lejos.nxt.*;
import lejos.nxt.CompassSensor;
import technobotts.util.CompassPilot;

public class BackAndForth
{
	public static void main(String... strings) throws InterruptedException
	{
		CompassSensor c = new CompassSensor(SensorPort.S1);
		CompassPilot p = new CompassPilot(c,
		                                  8.16f, 14.5f,
		                                  Motor.B, Motor.C);
		p.setMoveSpeed(75);
		
		c.resetCartesianZero();
		
		while(!Button.ENTER.isPressed())
		{
			LCD.drawString("Compass:"+c.getDegreesCartesian()+"            ", 0, 0);
		}
		
		
		while(true)
		{
			p.rotateTo(90);
			Thread.sleep(250);
			p.travel(50);
			Thread.sleep(250);
			
			p.rotateTo(270);
			Thread.sleep(250);
			p.travel(50);
			Thread.sleep(250);
		}
	}
}

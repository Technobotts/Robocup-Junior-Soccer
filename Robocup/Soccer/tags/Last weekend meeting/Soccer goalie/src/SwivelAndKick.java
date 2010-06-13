import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.CompassSensor;
import lejos.nxt.Sound;
import lejos.nxt.addon.IRSeekerV2;
import technobotts.util.CompassPilot;


public class SwivelAndKick
{
	public static void main(String ...args)
	{
		CompassSensor c = new CompassSensor(SensorPort.S1);
		CompassPilot p = new CompassPilot(c,
		                                  8.16f, 14.5f,
		                                  Motor.B, Motor.C);
		IRSeekerV2 ir = new IRSeekerV2(SensorPort.S3,IRSeekerV2.Mode.AC);
		p.setMoveSpeed(100);
		p.setTurnSpeed(360);
		
		c.resetCartesianZero();
		
		Motor.A.smoothAcceleration(false);
		
		while(true)
		{
			boolean left = false;
			
			float angle = ir.getAngle();
			if(!Float.isNaN(angle))
			{
				p.stop();
				if(angle == 0 && ir.getSensorValue(3)>100)
				{
					p.travel(40);
					
					Motor.A.setPower(100);
					Motor.A.forward();
					while(Motor.A.getTachoCount()<180);
					Motor.A.flt();

					Motor.A.setPower(50);
					Motor.A.rotateTo(0);
					
					p.rotate(180);
					p.travel(40);
				}
				else
				{
					p.rotate(angle);
				}
			}
			else if(!p.isMoving())
			{
				if(left)
					p.rotateTo(60, true);
				else
					p.rotateTo(-60, true);
				
				left = !left;
			}
		}
	}
}

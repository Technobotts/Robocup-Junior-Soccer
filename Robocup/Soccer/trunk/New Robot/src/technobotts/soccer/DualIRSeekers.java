package technobotts.soccer;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.robotics.DirectionFinder;

public class DualIRSeekers extends DualDirectionFinders
{
	protected IRSeekerV2 left;
	protected IRSeekerV2 right;
	
	public IRSeekerV2 getLeft()
    {
    	return left;
    }
	public IRSeekerV2 getRight()
    {
    	return right;
    }

	DualIRSeekers(IRSeekerV2 left, float f,
	              IRSeekerV2 right, float g)
	{
		super(left, f,
		      right, g);
		
		this.left = left;
		this.right = right;
	}

	@Override
	public float getDegreesCartesian()
	{
		float leftAngle = left.getDegreesCartesian() - leftOffset;
		float rightAngle = right.getDegreesCartesian() + rightOffset;

		if(Float.isNaN(leftAngle))
			return rightAngle;
		if(Float.isNaN(rightAngle))
			return leftAngle;
		else
		{
			float leftStrength = left.getStrength(leftAngle);
			float rightStrength = right.getStrength(rightAngle);

			return (leftAngle * leftStrength + rightAngle * rightStrength) / (leftStrength + rightStrength);
		}
	}

	public static void main(String... args) throws InterruptedException
	{
		DualIRSeekers ballFinder = new DualIRSeekers(new IRSeekerV2(SensorPort.S2, Mode.AC_600Hz),
		                                                    53.13f,
		                                                    new IRSeekerV2(SensorPort.S1, Mode.AC_600Hz),
		                                                    53.13f);

		while(true)
		{
			LCD.clear();
			
			LCD.drawString("Angle: " + ballFinder.getDegreesCartesian(), 0, 0);
			
			LCD.drawString("Right: " + ballFinder.getRight().getAngle(), 0, 2);
			LCD.drawString("     : " + ballFinder.getRight().getStrength(), 0, 3);
			
			LCD.drawString("Left : " + ballFinder.getLeft().getAngle(), 0, 5);
			LCD.drawString("     : " + ballFinder.getLeft().getStrength(), 0, 6);
			
			LCD.refresh();
			
			Thread.sleep(250);
		}
	}
}

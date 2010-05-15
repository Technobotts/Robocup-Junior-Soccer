package technobotts.soccer.util;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.LightSourceFinder;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.robotics.RangeReadings;

public class DualLightSourceFinder extends DualDirectionFinders implements LightSourceFinder
{
	protected LightSourceFinder left;
	protected LightSourceFinder right;

	public LightSourceFinder getLeft()
	{
		return left;
	}

	public LightSourceFinder getRight()
	{
		return right;
	}

	public DualLightSourceFinder(LightSourceFinder left, float leftOffset,
	              LightSourceFinder right, float rightOffset)
	{
		super(left, leftOffset,
		      right, rightOffset);

		this.left = left;
		this.right = right;
	}

	@Override
	public float getAngle()
	{
		float leftAngle = left.getAngle() - leftOffset;
		float rightAngle = right.getAngle() + rightOffset;

		if(Float.isNaN(leftAngle))
			return rightAngle;
		if(Float.isNaN(rightAngle))
			return leftAngle;
		else
		{
			float leftStrength = left.getRange();
			float rightStrength = right.getRange();

			return (leftAngle * leftStrength + rightAngle * rightStrength) / (leftStrength + rightStrength);
		}
	}

	@Override
	public float getRange()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public static void main(String... args) throws InterruptedException
	{
		DualLightSourceFinder ballFinder = new DualLightSourceFinder(new IRSeekerV2(SensorPort.S2, Mode.AC_600Hz),
		                                             53.13f,
		                                             new IRSeekerV2(SensorPort.S1, Mode.AC_600Hz),
		                                             53.13f);

		while(true)
		{
			LCD.clear();

			LCD.drawString("Angle: " + ballFinder.getDegreesCartesian(), 0, 0);

			LCD.drawString("Right: " + ballFinder.getRight().getAngle(), 0, 2);
			LCD.drawString("     : " + ballFinder.getRight().getRange(), 0, 3);

			LCD.drawString("Left : " + ballFinder.getLeft().getAngle(), 0, 5);
			LCD.drawString("     : " + ballFinder.getLeft().getRange(), 0, 6);

			LCD.refresh();

			Thread.sleep(250);
		}
	}

	@Override
	public RangeReadings getRangeValues()
	{
		RangeReadings leftRanges = left.getRangeValues();
		RangeReadings rightRanges = right.getRangeValues();
		
		RangeReadings ranges = new RangeReadings(leftRanges.size() + rightRanges.size());
		
		ranges.addAll(leftRanges);
		ranges.addAll(rightRanges);
		
		return ranges;
	}
}
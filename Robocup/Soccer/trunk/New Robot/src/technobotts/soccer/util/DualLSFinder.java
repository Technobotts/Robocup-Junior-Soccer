package technobotts.soccer.util;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.IRSeekerV2;
import lejos.robotics.LightSourceFinder;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.robotics.RangeReadings;
import lejos.util.TextMenu;

public class DualLSFinder extends DualSensor<LightSourceFinder> implements LightSourceFinder
{
	public DualLSFinder(LightSourceFinder left, float leftOffset,
	                    LightSourceFinder right, float rightOffset)
	{
		super(left, leftOffset,
		      right, rightOffset);
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
			while(leftAngle - rightAngle > 180)
				leftAngle -= 360;
			while(leftAngle - rightAngle <= -180)
				leftAngle += 360;
			
			float leftStrength = left.getRange();
			float rightStrength = right.getRange();

			float angle = (leftAngle * leftStrength + rightAngle * rightStrength) / (leftStrength + rightStrength);
			
			while(angle > 180)
				angle-=360;

			while(angle <= -180)
				angle+=360;
			
			return angle;
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
		Mode[] modes = {Mode.DC, Mode.AC_600Hz, Mode.AC_1200Hz};
		TextMenu modeMenu = new TextMenu(new String[] {"DC", "AC 600Hz", "AC 1200Hz"},0,"Mode");
		
		int mode = modeMenu.select();
		if(mode < 0)
			return;
		
		DualLSFinder ballFinder = new DualLSFinder(new IRSeekerV2(SensorPort.S3, modes[mode]),
		                                           53.13f,
		                                           new IRSeekerV2(SensorPort.S2, modes[mode]),
		                                           53.13f);

		while(true)
		{
			LCD.clear();

			LCD.drawString("Angle: " + Math.round(ballFinder.getDegreesCartesian() * 10) / 10, 0, 0);

			LCD.drawString("Right: " + ballFinder.getRight().getAngle(), 0, 2);
			LCD.drawString("     : " + ballFinder.getRight().getRange(), 0, 3);

			LCD.drawString("Left : " + ballFinder.getLeft().getAngle(), 0, 5);
			LCD.drawString("     : " + ballFinder.getLeft().getRange(), 0, 6);

			LCD.refresh();

			Thread.sleep(50);
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

	@Override
	public float getDegreesCartesian()
	{
		return getAngle();
	}

	@Override
	public void resetCartesianZero()
	{}

	@Override
	public void startCalibration()
	{}

	@Override
	public void stopCalibration()
	{}
}

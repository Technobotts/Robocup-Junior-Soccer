package lejos.robotics;

import java.awt.Point;


public class ImprovedRangeReadings extends RangeReadings
{

	public ImprovedRangeReadings(int numReadings)
	{
		super(numReadings);
	}

	public float getRange()
	{
		return getRangeReading().getRange();
	}
	public float getAngle()
	{
		return getRangeReading().getAngle();
	}
	
	public RangeReading getRangeReading()
	{
		float x = 0;
		float y = 0;
		
		for(RangeReading reading:this)
		{
			if(!reading.invalidReading())
			{
				x += reading.getRange()*Math.sin(Math.toRadians(reading.getAngle()));
				y += reading.getRange()*Math.cos(Math.toRadians(reading.getAngle()));
			}
		}
		
		float range = (float) Math.sqrt(x*x+y*y);
		float angle = (float) Math.toDegrees(Math.atan2(x, y));
		return new RangeReading(angle, range);
	}
}

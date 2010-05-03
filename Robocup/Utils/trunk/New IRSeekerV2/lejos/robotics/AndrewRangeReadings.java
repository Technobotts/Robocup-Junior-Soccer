package lejos.robotics;

public class AndrewRangeReadings extends ImprovedRangeReadings
{

	public AndrewRangeReadings(int numReadings)
	{
		super(numReadings);
		// TODO Auto-generated constructor stub
	}

	public RangeReading getRangeReading()
	{
		RangeReading r1 = null;
		RangeReading r2 = null;
		for(RangeReading r : this)
		{
			if(r1 == null || r.getRange() >= r1.getRange())
			{
				r2 = r1;
				r1 = r;
			}
			else if(r2 == null || r.getRange() >= r2.getRange())
			{
				r2 = r;
			}
		}
		float angle = (r1.getAngle() * r1.getRange() + r2.getAngle() * r2.getRange())
		              / (r1.getRange() + r2.getRange());
		float range = r2.getRange() + (r1.getRange() - r2.getRange()) *
			(angle - r2.getAngle()) / (r1.getAngle() - r2.getAngle());
		
		return new RangeReading(angle, range);
	}
}

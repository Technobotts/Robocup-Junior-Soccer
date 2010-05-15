package technobotts.soccer.util;

import lejos.robotics.DirectionFinder;

public class DualDirectionFinder extends DualSensor<DirectionFinder> implements DirectionFinder
{


	DualDirectionFinder(DirectionFinder left, float leftOffset, DirectionFinder right, float rightOffset)
    {
	    super(left, leftOffset, right, rightOffset);
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
    		return (leftAngle+rightAngle)/2;
	}

	@Override
    public void resetCartesianZero()
    {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void startCalibration()
    {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void stopCalibration()
    {
	    // TODO Auto-generated method stub
	    
    }
}

package technobotts.soccer.util;

import lejos.robotics.DirectionFinder;

public class DualDirectionFinders implements DirectionFinder
{

	protected DirectionFinder left;
	protected float leftOffset;
	protected DirectionFinder right;
	protected float rightOffset;

	
	public DirectionFinder getLeft()
    {
    	return left;
    }
	public DirectionFinder getRight()
    {
    	return right;
    }


	DualDirectionFinders(DirectionFinder left, float leftOffset,
	                     DirectionFinder right, float rightOffset)
    {
	    this.left = left;
	    this.leftOffset = leftOffset;
	    this.right = right;
	    this.rightOffset = rightOffset;
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
	}

	@Override
	public void startCalibration()
	{
	}

	@Override
	public void stopCalibration()
	{
	}

}

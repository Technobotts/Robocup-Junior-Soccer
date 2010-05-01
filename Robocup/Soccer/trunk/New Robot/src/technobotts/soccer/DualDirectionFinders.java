package technobotts.soccer;

import lejos.robotics.DirectionFinder;

public class DualDirectionFinders implements DirectionFinder
{

	protected DirectionFinder left;
	protected float leftOffset;
	
	public DirectionFinder getLeft()
    {
    	return left;
    }
	public DirectionFinder getRight()
    {
    	return right;
    }

	protected DirectionFinder right;
	protected float rightOffset;
	

	DualDirectionFinders(DirectionFinder left, float f, DirectionFinder right, float g)
    {
	    this.left = left;
	    this.leftOffset = f;
	    this.right = right;
	    this.rightOffset = g;
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

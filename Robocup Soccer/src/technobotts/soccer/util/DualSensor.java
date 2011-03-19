package technobotts.soccer.util;

public class DualSensor<T>
{

	protected T left;
	protected float leftOffset;
	protected T right;
	protected float rightOffset;

	
	public T getLeft()
    {
    	return left;
    }
	public T getRight()
    {
    	return right;
    }


	DualSensor(T left, float leftOffset,
	           T right, float rightOffset)
    {
	    this.left = left;
	    this.leftOffset = leftOffset;
	    this.right = right;
	    this.rightOffset = rightOffset;
    }

}

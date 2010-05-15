package lejos.robotics;

public interface LightSourceFinder extends RangeScanner, DirectionFinder
{
	public float getAngle();
	public float getRange();
}

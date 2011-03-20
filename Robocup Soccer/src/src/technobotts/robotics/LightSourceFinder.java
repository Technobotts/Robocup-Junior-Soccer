package technobotts.robotics;

import lejos.robotics.DirectionFinder;
import lejos.robotics.RangeScanner;

public interface LightSourceFinder extends RangeScanner, DirectionFinder
{
	public float getAngle();
	public float getRange();
}

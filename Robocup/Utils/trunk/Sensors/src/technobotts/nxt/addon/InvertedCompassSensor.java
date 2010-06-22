package technobotts.nxt.addon;


import lejos.nxt.I2CPort;
import lejos.robotics.DirectionFinder;

public class InvertedCompassSensor extends FixedCompassSensor implements DirectionFinder
{

	public InvertedCompassSensor(I2CPort port)
    {
	    super(port);
	    // TODO Auto-generated constructor stub
    }

	@Override
    public float getDegrees()
    {
	    // TODO Auto-generated method stub
	    return 360-super.getDegrees();
    }

}

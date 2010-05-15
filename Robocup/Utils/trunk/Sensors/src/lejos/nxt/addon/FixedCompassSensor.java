package lejos.nxt.addon;


import lejos.nxt.I2CPort;

public class FixedCompassSensor extends CompassSensor
{

	public FixedCompassSensor(I2CPort port)
    {
	    super(port);
	    // TODO Auto-generated constructor stub
    }

	@Override
	public float getDegrees()
	{
	    float deg = super.getDegrees();
	    if(deg == -1) return Float.NaN;
	    else return deg;
	}
}

package lejos.nxt;

public class CompassSensor extends lejos.nxt.addon.CompassSensor
{

	public CompassSensor(I2CPort port)
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

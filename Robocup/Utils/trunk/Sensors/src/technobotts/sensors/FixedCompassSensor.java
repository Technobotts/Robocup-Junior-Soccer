package technobotts.sensors;

import lejos.nxt.I2CPort;
import lejos.nxt.addon.CompassSensor;

public class FixedCompassSensor extends CompassSensor
{
	public FixedCompassSensor(I2CPort port)
	{
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized float getDegrees()
	{
		float deg = super.getDegrees();
		if(deg == -1)
			return Float.NaN;
		else
			return deg;
	}
}

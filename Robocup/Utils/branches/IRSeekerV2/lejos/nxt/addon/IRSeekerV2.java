package lejos.nxt.addon;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.robotics.DirectionFinder;

/**
 * HiTechnic IRSeekerV2 sensor - untested. www.hitechnic.com
 */
public class IRSeekerV2 extends I2CSensor implements DirectionFinder
{
	public static enum Mode {
		AC_600Hz	(true, 600),
		AC_1200Hz	(true, 1200),
		DC			(true, 0);

		public final boolean pulsed;
		public final int     frequency;

		Mode(boolean pulsed, int frequency)
		{
			this.pulsed = pulsed;
			this.frequency = frequency;
		}
	};

	public static final byte  address = 0x08;
	byte[]                    buf     = new byte[1];
	public static final float noAngle = Float.NaN;

	private Mode              mode;

	/**
	 * Set the mode of the sensor
	 */
	public void setMode(Mode mode)
	{
		this.mode = mode;
		if(mode.pulsed)
		{
			byte modeId = (byte) (mode.frequency <= 900 ? 1 : 0);
			this.sendData(0x41, modeId);
		}
	}

	public IRSeekerV2(I2CPort port, Mode mode)
	{
		super(port, I2CPort.STANDARD_MODE);
		setMode(mode);
		setAddress(address);
	}

	/**
	 * Returns the direction of the target (1 to 9) or 0 if no target.
	 * @return direction
	 */
	public int getDirection()
	{
		int register = 0;
		if(mode.pulsed)
		{
			register = 0x49;
		}
		else
		{
			register = 0x42;
		}
		int ret = getData(register, buf, 1);
		if(ret != 0)
			return -1;
		return (0xFF & buf[0]);
	}

	/**
	 * Returns the angle of the target (-180 to 180) or NaN.
	 * @return direction
	 */
	public float getAngle(boolean blocking)
	{
		while(true)
		{
			int dir = getDirection();
			if(dir <= 0 && !blocking)
				return Float.NaN;
			else
				return (dir - 5) * 30;
		}
	}

	public float getRange()
	{
		return getRange(getAngle());
	}

	public float getRange(float angle)
	{
		if(Float.isNaN(angle) || angle > 120 || angle < -120)
			return Float.NaN;
		int lowerBound = (int) (angle - (angle + 360) % 60);
		int lowerStrength = getSensorValue(lowerBound / 60 + 3);
		if(lowerBound == angle)
		{
			return lowerStrength;
		}
		else
		{
			int upperBound = lowerBound + 60;
			int upperStrength = getSensorValue(upperBound / 60 + 3);
			return (angle - lowerBound) / (upperBound - lowerBound) * (upperStrength - lowerStrength) + lowerStrength;
		}
	}

	public float getAngle()
	{
		return getAngle(false);
	}

	public boolean hasDirection()
	{
		return getDirection() != 0;
	}

	/**
	 * Returns value of sensor 1 - 5.
	 * @param id
	 *            The id of the sensor to read
	 * @return sensor value (0 to 255).
	 */
	public int getSensorValue(int id)
	{
		int register = 0;
		if(mode.pulsed)
		{
			register = 0x4A;
		}
		else
		{
			register = 0x43;
		}
		if(id < 1 || id > 5)
			throw new IllegalArgumentException(
			                                   "The argument 'id' must be between 1 and 5");
		int ret = getData(register + (id - 1), buf, 1);
		if(ret != 0)
			return -1;
		return (0xFF & buf[0]);
	}

	/**
	 * Gets the values of each sensor, returning them in an array.
	 * @return Array of sensor values (0 to 255).
	 */
	public int[] getSensorValues()
	{
		int[] values = new int[5];
		for(int i = 0; i < 5; i++)
		{
			values[i] = getSensorValue(i + 1);
		}
		return values;
	}

	/**
	 * Returns the average sensor reading (DC Only)
	 * @return sensor value (0 to 255).
	 */
	public int getAverage(int id)
	{
		if(!mode.pulsed)
		{
			if(id <= 0 || id > 5)
				return -1;
			int ret = getData(0x48, buf, 1);
			if(ret != 0)
				return -1;
			return (0xFF & buf[0]);
		}
		else
		{
			return -1;
		}
	}

	/**
	 * Returns a string representation of the strengths
	 */
	public String toString()
	{
		return "(" + getSensorValue(1) + "," + getSensorValue(2) + ","
		       + getSensorValue(3) + "," + getSensorValue(4) + ","
		       + getSensorValue(5) + ")";
	}

	@Override
	public float getDegreesCartesian()
	{
		return getAngle();
	}

	@Override
	public void resetCartesianZero()
	{}

	@Override
	public void startCalibration()
	{}

	@Override
	public void stopCalibration()
	{}
}

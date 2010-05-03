package lejos.nxt.addon;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.robotics.AndrewRangeReadings;
import lejos.robotics.DirectionFinder;
import lejos.robotics.ImprovedRangeReadings;
import lejos.robotics.RangeReading;
import lejos.robotics.RangeReadingFinder;
import lejos.robotics.RangeScanner;

public class IRSeekerV2 extends I2CSensor implements RangeScanner, RangeReadingFinder
{
	public static enum Mode {
		AC_600Hz(true, 600),
		AC_1200Hz(true, 1200),
		DC(false, 0);

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

	public float getAngle()
	{
		return getAngle(false);
	}

	public boolean hasDirection()
	{
		return getDirection() != 0;
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

	@Override
	public ImprovedRangeReadings getRangeValues()
	{
		int register;

		if(mode.pulsed)
			register = 0x4A;
		else
			register = 0x43;

		byte ranges[] = new byte[5];
		int result = getData(register, ranges, 5);
		if(result != 0)
			return null;

		AndrewRangeReadings readings = new AndrewRangeReadings(5);

		int angle = -120;
		for(int i = 0; i < 5; i++)
		{
			readings.set(i, new RangeReading(angle, 0xFF & ranges[i]));
			angle += 60;
		}
		return readings;
	}

	public RangeReading getRangeReading()
	{
		return getRangeValues().getRangeReading();
	}
}

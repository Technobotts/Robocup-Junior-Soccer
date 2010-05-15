package lejos.nxt.addon;

import java.util.Fraction;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;

/**
 * @author Eric
 */
public class LineLeader extends I2CSensor
{
	private byte[]           buf      = new byte[1];

	public static final char WHITE    = 'W';

	/**
	 * Used by calibrateSensor() to calibrate the black level
	 */
	public static final char BLACK    = 'B';

	/**
	 * Used by setSleep() to put sensor to sleep
	 */
	public static final char SLEEP    = 'D';

	/**
	 * Used by setSleep() to wake the sensor up
	 */
	public static final char WAKE     = 'P';

	/**
	 * Used by setLineColor() to reset the color inversion (black line on a
	 * white background, this is also the default).
	 */
	public static final char NORMAL   = 'R';

	/**
	 * Used by setLineColor() to invert the colors (White line on a black
	 * background)
	 */
	public static final char INVERTED = 'I';

	/**
	 * Used by setElectricalFrequency() to configure the sensor for US region
	 * (and regions with 60 Hz electrical frequency).
	 */
	public static final char US       = 'A';

	/**
	 * Used by setElectricalFrequency() to configure the sensor for European
	 * region (and regions with 50 Hz electrical frequency)
	 */
	public static final char EU       = 'E';

	/**
	 * Used by setElectricalFrequency() to configure the sensor for universal
	 * frequency (in this mode the sensor adjusts for any frequency, this is
	 * also the default mode).
	 */
	public static final char U        = 'U';

	/**
	 * Creates a new instance of the class LineLeader
	 * @param port
	 *            The sensor port the LineLeader is connected to
	 */
	public LineLeader(I2CPort port)
	{
		this(port, 0x01);
	};

	/**
	 * Creates a new instance of the class LineLeader
	 * @param port
	 *            The sensor port the LineLeader is connected to
	 * @param addr
	 *            The I2C address of the LineLeader (default 0x01)
	 */
	public LineLeader(I2CPort port, int addr)
	{
		super(port);
		setAddress(addr);
	}

	/**
	 * Changes the I2C address of the LineLeader
	 * @param addr
	 *            The new I2C address
	 * @return
	 */
	public int changeSensorAddress(int addr)
	{
		byte[] buffer = {(byte) 0xA0, (byte) 0xAA, (byte) 0xA5, (byte) addr};
		return sendData(0x41, buffer, 4);
	}

	/**
	 * Gets a single byte of data from the specified register
	 * @param register
	 *            The register to read
	 * @return
	 */
	public int getData(int register)
	{
		buf[0] = 0;

		if(getData(register, buf, 1) == 0)
			return buf[0] & 0xff;
		else
			return -1;
	}

	/**
	 * Sends a command to the LineLeader
	 * @param command
	 *            <table>
	 *            <tr>
	 *            <th>W</th>
	 *            <td>Calibrate White.</td>
	 *            </tr>
	 *            <tr>
	 *            <th>B</th>
	 *            <td>Calibrate Black.</td>
	 *            </tr>
	 *            <tr>
	 *            <th>D</th>
	 *            <td>Put Sensor to sleep.</td>
	 *            </tr>
	 *            <tr>
	 *            <th>P</th>
	 *            <td>Wake up the sensor.</td>
	 *            </tr>
	 *            <tr>
	 *            <th>I</th>
	 *            <td>Color inversion (White line on a black background).</td>
	 *            </tr>
	 *            <tr>
	 *            <th>R</th>
	 *            <td>Reset Color inversion (black line on a white background, this is also the
	 *            default).</td>
	 *            </tr>
	 *            <tr>
	 *            <th>S</th>
	 *            <td>Take a snapshot, this command looks at the line under the sensor and stores
	 *            the width and position of the line in sensor’s memory. Subsequently, sensor will
	 *            use these characteristics of line to track it. This command inverts the colors if
	 *            it sees a white line on a black background. (PID parameters are not affected).</td>
	 *            </tr>
	 *            <tr>
	 *            <th>A</th>
	 *            <td>Configure Sensor for US region (and regions with 60Hz electrical frequency).</td>
	 *            </tr>
	 *            <tr>
	 *            <th>E</th>
	 *            <td>Configure sensor for European regions (and regions with 50Hz electrical
	 *            frequency).</td>
	 *            </tr>
	 *            <tr>
	 *            <th>U</th>
	 *            <td>Configure sensor for universal frequency (in this mode the sensor adjusts for
	 *            any frequency). This is the default mode.</td>
	 *            </tr>
	 *            </table>
	 * @return status: == 0 success, != 0 failure
	 */
	public int sendCommand(char command)
	{
		return sendData(0x41, (byte) command);
	}

	/**
	 * Retrieves an array of the calibrated readings of the eight sensors
	 * @return Array of readings
	 */
	public int[] getSensors()
	{
		byte[] buf = new byte[8];

		if(getData(0x49, buf, 8) == 0)
		{
			int[] vals = new int[buf.length];
			for(int i = 0; i < buf.length; i++)
				vals[i] = buf[i] & 0xff;
			return vals;
		}
		else
			return null;
	}

	/**
	 * Retrieves an array of the calibrated white limits for the eight sensors
	 * @return Array of white limits
	 */
	public int[] getWhiteLimits()
	{
		byte[] buf = new byte[8];

		if(getData(0x51, buf, 8) == 0)
		{
			int[] vals = new int[buf.length];
			for(int i = 0; i < buf.length; i++)
				vals[i] = buf[i] & 0xff;
			return vals;
		}
		else
			return null;
	}

	/**
	 * Retrieves an array of the calibrated black limits for the eight sensors
	 * @return Array of black limits
	 */
	public int[] getBlackLimits()
	{
		byte[] buf = new byte[8];

		if(getData(0x59, buf, 8) == 0)
		{
			int[] vals = new int[buf.length];
			for(int i = 0; i < buf.length; i++)
				vals[i] = buf[i] & 0xff;
			return vals;
		}
		else
			return null;
	}

	/**
	 * @param index
	 * @return
	 */
	public int getSensorRaw(int index)
	{
		byte[] buf = new byte[2];
		if(index < 0 || index >= 8)
			throw new IllegalArgumentException();
		if(getData(0x74 + index * 2, buf, 2) == 0)
			return buf[1] << 8 + buf[0];
		else
			return -1;

	}

	/**
	 * @return
	 */
	public float getSteering()
	{
		int steering = getData(0x42);
		if(steering == -1)
			return Float.NaN;
		else
			return (byte) steering;
	}

	/**
	 * @return
	 */
	public int getWeightedAverage()
	{
		return getData(0x43);
	}

	/**
	 * @return
	 */
	public int getResult()
	{
		return getData(0x44);
	}

	/**
	 * @return
	 */
	public int getSetpoint()
	{
		return getData(0x45);
	}

	/**
	 * @param value
	 * @return
	 */
	public int setSetpoint(byte value)
	{
		return sendData(0x43, value);
	}

	public void setKp(double d)
	{
		Fraction frac = new Fraction(d);
		setKp(frac.getNumerator(), frac.getDenominator());
	}

	public double getKp()
	{
		int numerator = getData(0x46);
		int denominator = getData(0x61);
		if(numerator >= 0 && denominator > 0)
			return numerator / (double) denominator;
		else
			return -1;
	}

	public void setKi(double ki)
	{
		Fraction frac = new Fraction(ki);
		setKp(frac.getNumerator(), frac.getDenominator());
	}

	public double getKi()
	{
		int numerator = getData(0x47);
		int denominator = getData(0x62);
		if(numerator >= 0 && denominator > 0)
			return numerator / (double) denominator;
		else
			return -1;
	}

	public void setKd(double kd)
	{
		Fraction frac = new Fraction(kd);
		setKp(frac.getNumerator(), frac.getDenominator());
	}

	public double getKd()
	{
		int numerator = getData(0x47);
		int denominator = getData(0x62);
		if(numerator >= 0 && denominator > 0)
			return numerator / (double) denominator;
		else
			return -1;
	}

	/**
	 * @param numerator
	 * @param divisor
	 */
	public void setKp(int numerator, int denominator)
	{
		if(denominator == 0)
			throw new IllegalArgumentException("Argument 'denominator' is 0");
		sendData(0x46, (byte) numerator);
		sendData(0x61, (byte) denominator);
	}

	/**
	 * @param numerator
	 * @param denominator
	 */
	public void setKi(int numerator, int denominator)
	{
		if(denominator == 0)
			throw new IllegalArgumentException("Argument 'denominator' is 0");
		sendData(0x47, (byte) numerator);
		sendData(0x62, (byte) denominator);
	}

	/**
	 * @param numerator
	 * @param denominator
	 */
	public void setKd(int numerator, int denominator)
	{
		if(denominator == 0)
			throw new IllegalArgumentException("Argument 'denominator' is 0");
		sendData(0x48, (byte) numerator);
		sendData(0x63, (byte) denominator);
	}
}

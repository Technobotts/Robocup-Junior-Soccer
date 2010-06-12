package lejos.util;

/**
 * @author Eric
 */
public class SimplePID extends DataProcessor
{
	/** The P I and D factors to multiply by */
	private double  Kp, Ki, Kd;
	/** The P I and D terms of the PID algorithm */
	private double  pTerm, iTerm, dTerm;

	/** The current system time */
	private long    _tick;
	/** The system time at the last data input */
	private long    _lastTick;

	/** The deviation from the set point of last data input */
	private double  _error;

	/** The previous value of _error */
	private double  _lastError;
	/** The integrated error values */
	private long    intError;
	private long    _maxInt    = 20000;

	/** The calculated PID controller output */
	private double  _output;

	private boolean hasData;

	/**
	 * Constructs the SimplePID object
	 * @param Kp The Proportional factor
	 * @param Ki The Integral factor
	 * @param Kd The Derivative factor
	 */
	public SimplePID(double Kp, double Ki, double Kd)
	{
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
		reset();
	}

	/**
	 * Resets important member variables
	 */

	public void reset()
	{
		intError = 0;
		pTerm = 0;
		iTerm = 0;
		dTerm = 0;
		hasData = false;
	}

	public double getPTerm()
	{
		return pTerm;
	}

	public double getITerm()
	{
		return iTerm;
	}

	public double getDTerm()
	{
		return dTerm;
	}

	public double getOutput()
	{
		return _output;
	}

	public void setInput(double input)
	{
		_tick = System.currentTimeMillis();
		_error = input;

		pTerm = Kp * _error;

		long dt = _tick - _lastTick;
		if(!hasData)
		{
			// First time around: no I or D part;
			hasData = true;
			_output = pTerm;
		}
		else if(dt != 0 && !Double.isNaN(input))
		{
			intError += _error * dt;
			if(intError > _maxInt)
				intError = _maxInt;
			else if(intError < -_maxInt)
				intError = -_maxInt;

			iTerm = Ki * intError;
			dTerm = Kd * (_error - _lastError) / dt;

			_output = pTerm + iTerm + dTerm;
		}
		_lastTick = _tick;
		_lastError = _error;
	}

	public void setMaxInt(long maxInt)
	{
		_maxInt = maxInt;
	}
}

package lejos.util;

/**
 * @author Eric
 */
public class SimplePID extends DataProcessor
{
	/** The P I and D factors to multiply by */
	private double  _Kp, _Ki, _Kd;
	/** The P I and D terms of the PID algorithm */
	private double  _pTerm, _iTerm, _dTerm;

	/** The current system time */
	private long    _tick;
	/** The system time at the last data input */
	private long    _lastTick;

	/** The deviation from the set point of last data input */
	private double  _error;

	/** The previous value of _error */
	private double  _lastError;
	/** The integrated error values */
	private long    _intError;
	private long    _maxInt = 20000;

	/** The calculated PID controller output */
	private double  _output;

	/** Whether the controller is running */
	private boolean _isRunning = false;
	private boolean _hasData;

	/**
	 * Constructs the SimplePID object
	 * @param Kp The Proportional factor
	 * @param Ki The Integral factor
	 * @param Kd The Derivative factor
	 */
	public SimplePID(double Kp, double Ki, double Kd)
	{
		this._Kp = Kp;
		this._Ki = Ki;
		this._Kd = Kd;
		reset();
	}

	/**
	 * Resets important member variables
	 */

	public void reset()
	{
		_intError = 0;
		_pTerm = 0;
		_iTerm = 0;
		_dTerm = 0;
		_hasData = false;
	}

	public double getPTerm()
	{
		return _pTerm;
	}

	public double getITerm()
	{
		return _iTerm;
	}

	public double getDTerm()
	{
		return _dTerm;
	}

	public double getOutput()
	{
		return _output;
	}
	
	public void setInput(double input)
	{
		if(_isRunning)
		{
			_tick = System.currentTimeMillis();
			_error = input;

			_pTerm = _Kp * _error;

			long dt = _tick - _lastTick;
			if(!_hasData)
			{
				// First time around: no I or D part;
				_hasData = true;
				_output = _pTerm;
			}
			else if(dt != 0)
			{
				_intError += _error * dt;
				if(_intError > _maxInt)
					_intError = _maxInt;
				else if(_intError < -_maxInt)
					_intError = -_maxInt;

				_iTerm = _Ki * _intError;
				_dTerm = _Kd * (_error - _lastError) / dt;

				_output = _pTerm + _iTerm + _dTerm;
			}
			_lastTick = _tick;
			_lastError = _error;
		}
	}

	public void setMaxInt(long maxInt)
	{
		_maxInt = maxInt;
	}

	public void start()
	{
		if(!_isRunning)
		{
			reset();
			_isRunning = true;
		}
	}

	public void stop()
	{
		_isRunning = false;
	}
}

package technobotts.soccer.util;

import lejos.util.DataProcessor;

public class RobotDirectionModifier extends DataProcessor
{
	public double  exponent;
	private double angle = Float.NaN;

	public RobotDirectionModifier(double exponent)
	{
		this.exponent = exponent;
	}

	@Override
	public double getOutput()
	{
		return angle;
	}

	@Override
	public void setInput(double input)
	{
		while(input > 180)
			input -= 360;
		while(input <= -180)
			input += 360;
		double abs = Math.abs(input) / 180;
		angle = (exponent * abs - Math.pow(abs, exponent)) / (exponent - 1) * Math.signum(input) * 180;
	}

	@Override
    public void reset()
    {
    }
}

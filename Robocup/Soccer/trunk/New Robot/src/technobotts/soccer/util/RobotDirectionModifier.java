package technobotts.soccer.util;

import lejos.util.DataProcessor;

public class RobotDirectionModifier extends DataProcessor
{
	public double  degree;
	private double angle = Float.NaN;

	public RobotDirectionModifier(double degree)
	{
		this.degree = degree;
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
		angle = (degree * abs - Math.pow(abs, degree)) / (degree - 1) * Math.signum(input) * 180;
	}
}

public class CopyOfSmoother
{
	BasicSmoother	NaNSmoother;
	BasicSmoother	valueSmoother;

	public CopyOfSmoother(double t)
	{
		valueSmoother = new BasicSmoother(t);
		NaNSmoother = new BasicSmoother(t);
	}

	public double getOutput(double input)
	{
		boolean isNaN = NaNSmoother.getOutput(Double.isNaN(input) ? 1 : 0) > 0.5;
		return isNaN ? Double.NaN : valueSmoother.getOutput(input);
		
	}
}

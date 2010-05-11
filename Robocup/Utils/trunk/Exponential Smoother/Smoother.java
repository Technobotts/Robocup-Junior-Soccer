public class Smoother extends BasicSmoother
{
	BasicSmoother	NaNSmoother;

	public Smoother(double t)
	{
		super(t);
		NaNSmoother = new BasicSmoother(t);
	}

	public double getOutput(double input)
	{
		boolean isNaN = Double.isNaN(input);
		double NaNProb = NaNSmoother.getOutput(isNaN ? 1 : 0);
		if(NaNProb > 0.5)
			return Double.NaN;
		else
			return super.getOutput(input);
		
	}
}

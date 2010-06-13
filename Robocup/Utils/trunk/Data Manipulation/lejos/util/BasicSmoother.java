package lejos.util;

public class BasicSmoother extends DataProcessor
{

	private double   t;
	private long     lastPollTime;

	protected double average;

	public BasicSmoother(double t)
	{
		this.t = t;
	}

	public double getOutput()
	{
		return average;
	}

	public void setInput(double x)
	{
		if(!Double.isNaN(x))
		{
			long pollTime = System.currentTimeMillis();
			if(Double.isNaN(average))
			{
				average = x;
			}
			else
			{
				double deltaT = (pollTime - lastPollTime) / 1000.0;
				double alpha = 1 - Math.exp(-deltaT / t);
				average += alpha * (x - average);
			}

			lastPollTime = pollTime;
		}
	}

	public void reset()
	{
		average = Double.NaN;
	}
}

package lejos.util;
public class BasicSmoother implements DataProcessor 
{

	private double t;
	private long   lastPollTime;

	private double average   = 0;

	private long   datacount = 0;

	public BasicSmoother(double t)
	{
		this.t = t;
	}

	public double getOutput()
	{
		return average;
	}

	public double getOutput(double x)
	{
		if(!Double.isNaN(x))
		{
			long pollTime = System.currentTimeMillis();
			if(datacount == 0)
			{
				average = x;
			}
			else
			{
				double deltaT = (pollTime - lastPollTime) / 1000.0;
				double alpha = 1 - Math.exp(-deltaT / t);
				average += alpha * (x - average);
			}

			datacount++;

			lastPollTime = pollTime;
		}
		return average;
	}
}

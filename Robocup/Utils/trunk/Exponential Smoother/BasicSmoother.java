public class BasicSmoother
{

	private double	t;
	private long	lastPollTime;

	private double	average;

	private long	datacount	= 0;

	public BasicSmoother(double d)
	{
		this.t = d;
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
				double deltaT = (pollTime-lastPollTime)/1000.0;
				double alpha = 1 - Math.exp(-deltaT / t);
				average += alpha * (x - average);
			}
	
			datacount++;
	
			lastPollTime = pollTime;
		}
		return average;
	}
}

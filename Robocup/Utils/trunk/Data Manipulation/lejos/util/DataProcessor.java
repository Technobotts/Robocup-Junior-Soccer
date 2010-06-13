package lejos.util;

public abstract class DataProcessor
{
	protected DataProcessor()
	{
		reset();
	}

	public abstract double getOutput();

	public abstract void setInput(double input);

	public final double getOutput(double input)
	{
		setInput(input);
		return getOutput();
	}

	public abstract void reset();
}

package lejos.util;

public abstract class DataProcessor {
	public abstract double getOutput();
	public abstract void setInput(double input);
	
	public final double getOutput(double input)
	{
		setInput(input);
		return getOutput();
	}
}

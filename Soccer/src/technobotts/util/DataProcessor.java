package technobotts.util;

public abstract class DataProcessor
{
	public static final DataProcessor IDENTITY = new DataProcessor() {
		private double output = Double.NaN;
		
		@Override public void setInput(double input) { output = input; }
		@Override public void reset() { }
		@Override public double getOutput() {return output;}
	};
	public abstract double getOutput();

	public abstract void setInput(double input);

	public final double getOutput(double input)
	{
		setInput(input);
		return getOutput();
	}

	public abstract void reset();
}

package technobotts.util;

public class AngleSmoother extends Smoother
{

	public AngleSmoother(double t)
	{
		super(t);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setInput(double input)
	{
		double rawOutput = ((BasicSmoother) this).getOutput();
		while(input - rawOutput > 180)
			input -= 360;
		while(input - rawOutput <= -180)
			input += 360;

		super.setInput(input);
		((BasicSmoother) this).average = ((BasicSmoother) this).average%360;
		super.average = super.average%360;
	}

}

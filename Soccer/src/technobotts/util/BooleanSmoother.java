package technobotts.util;

public class BooleanSmoother extends BasicSmoother {

	public BooleanSmoother(double t) {
		super(t);
	}
	
	public void setInput(boolean value) {
		super.setInput(value ? 1 : 0);
	}
	
	public boolean getBooleanOutput() {
		return super.getOutput() > 0.5;
	}
	public boolean getOutput(boolean input) {
		setInput(input); return getBooleanOutput();
	}

}

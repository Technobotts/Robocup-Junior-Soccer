import technobotts.nxt.addon.FixedCompassSensor;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;


public class CompassTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FixedCompassSensor compass = new FixedCompassSensor(SensorPort.S1);
		LCD.drawString("name: "+compass.getSensorType(), 0, 1);
		LCD.drawString("pID : "+compass.getProductID(), 0, 2);
		LCD.drawString("vers: "+compass.getVersion(), 0, 3);
		while(!Button.ESCAPE.isPressed())
			LCD.drawString("angle: "+compass.getDegrees(), 0, 0);

	}

}

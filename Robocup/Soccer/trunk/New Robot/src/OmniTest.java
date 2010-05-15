import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.InvertedCompassSensor;
import lejos.robotics.DirectionFinder;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.robotics.navigation.SimpleOmniPilot;

public class OmniTest
{

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String... args) throws InterruptedException
	{
		Thread.sleep(1000);
		DirectionFinder compass = new InvertedCompassSensor(SensorPort.S1);
		compass.resetCartesianZero();
		OmniCompassPilot pilot = new OmniCompassPilot(compass,
		                                              new SimpleOmniPilot.OmniMotor(Motor.C, 53.1301f, 6.4f, 1, 9.6f,
		                                                                            true),
		                                              new SimpleOmniPilot.OmniMotor(Motor.B, 180, 6.4f, 1, 8.8f),
		                                              new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f)
		                         );
		pilot.setMoveSpeed(1000);
		pilot.rotate(0, false);
		Button.ENTER.waitForPressAndRelease();

		pilot.travel(0);
		Button.ENTER.waitForPressAndRelease();

	}

}

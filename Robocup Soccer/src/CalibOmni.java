import technobotts.robotics.navigation.SimpleOmniPilot;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.DirectionFinder;

public class CalibOmni
{
	public static void main(String... args)
	{
		DirectionFinder compass = new CompassSensor(SensorPort.S1);
		SimpleOmniPilot pilot = new SimpleOmniPilot(new SimpleOmniPilot.OmniMotor(Motor.C, 53.1301f, 6.4f, 1, 9.6f,true),
		                                            new SimpleOmniPilot.OmniMotor(Motor.B, 180, 6.4f, 1, 8.8f),
		                                            new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f)
		                        );
		pilot.setTurnSpeed(45);
		compass.startCalibration();
		pilot.rotate(720);
		compass.stopCalibration();
	}
}

import lejos.nxt.Button;
import technobotts.soccer.robot.SoccerRobot;
import technobotts.soccer.robot.NewSoccerRobot;

public class OmniTest
{

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String... args) throws InterruptedException
	{
		Thread.sleep(1000);
		SoccerRobot robot = new NewSoccerRobot();
		/*DirectionFinder compass = new InvertedCompassSensor(SensorPort.S1);
		compass.resetCartesianZero();
		OmniCompassPilot pilot = new OmniCompassPilot(compass,
		                                              new SimpleOmniPilot.OmniMotor(Motor.C, 53.1301f, 6.4f, 1, 9.6f,
		                                                                            true),
		                                              new SimpleOmniPilot.OmniMotor(Motor.B, 180, 6.4f, 1, 8.8f),
		                                              new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f)
		                         );*/
		
		robot.setMoveSpeed(200);
		robot.setTurnSpeed(180);
		robot.rotateTo(0, false);
		Button.ENTER.waitForPressAndRelease();

		robot.travel(0);
		Button.ENTER.waitForPressAndRelease();

	}

}

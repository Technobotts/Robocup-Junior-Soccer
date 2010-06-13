import technobotts.soccer.SoccerRobot;
import lejos.nxt.Button;
import lejos.nxt.Motor;

public class BallSpin
{
	static SoccerRobot robot;
	public static void main(String... args)
	{
		Motor.B.setSpeed(900);
		Motor.A.setSpeed(125);
		Motor.C.setSpeed(125);
		while(!Button.ESCAPE.isPressed())
		{
			Motor.B.forward();
			Motor.A.forward();
			Motor.C.backward();
		}
	}
}
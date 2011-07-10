package technobotts.soccer.robot.onenxt;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import technobotts.nxt.addon.IRSeekerV2;
import technobotts.nxt.addon.InvertedCompassSensor;
import technobotts.robotics.navigation.SimpleOmniPilot;
import technobotts.soccer.robot.SoccerRobot;
import technobotts.util.BooleanSmoother;

//import technobotts.util.AngleSmoother;

public class GoalieRobot extends SoccerRobot {
	public static final SensorPort COMPASS_PORT = SensorPort.S1;
	public static final SensorPort IR_PORT = SensorPort.S4;
	public static final SensorPort US_PORT = SensorPort.S3;
	public static final SensorPort US2_PORT = SensorPort.S2;

	private UltrasonicSensor frontUS;
	private UltrasonicSensor rearUS;

	private boolean isKicking = false;

	public GoalieRobot() {
		super(new InvertedCompassSensor(COMPASS_PORT), new IRSeekerV2(IR_PORT,
				IR_MODE), new SimpleOmniPilot.OmniMotor(Motor.C, 53.1301f,
				6.4f, 1, 9.6f, true), new SimpleOmniPilot.OmniMotor(Motor.B,
				180.0000f, 6.4f, 1, 8.8f), new SimpleOmniPilot.OmniMotor(
				Motor.A, 306.8699f, 6.4f, 1, 9.6f));

		frontUS = new UltrasonicSensor(US_PORT);
		rearUS = new UltrasonicSensor(US2_PORT);
		// ballSmoother = new AngleSmoother(0);
	}
	public float getBallAngle() {
		return getBallDetector().getAngle();

	}

	@Override
	public boolean hasBall() {
		if (isKicking)
			return false;

		frontUS.ping();

		int[] dists = new int[8];
		if (frontUS.getDistances(dists) == 0)
			for (int dist : dists)
				if (dist < 10)
					return true;

		return false;
	}

	@Override
	public boolean connectToSlave() {
		return true;
	}

	private BooleanSmoother kickSmoother = new BooleanSmoother(0.25);
	public boolean kick() {
		Sound.beepSequenceUp();
		kickSmoother.reset();
		kickSmoother.setInput(true);
		while(kickSmoother.getBooleanOutput())
			kickSmoother.setInput(hasBall());
		Sound.beepSequence();
		return true;
	}

	@Override
	public float getRearWallDist() {
		rearUS.ping();
	    float distance = rearUS.getDistance();
	    return distance == 255? Float.NaN : distance;
	}

	public boolean disconnect() {
		return true;
	}

	@Override
	public double getGoalAngle() {
		return 0;
	}
}

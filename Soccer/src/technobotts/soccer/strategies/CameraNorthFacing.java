package technobotts.soccer.strategies;

import lejos.nxt.LCD;
import lejos.robotics.RangeReading;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.*;
import technobotts.soccer.util.RobotDirectionModifier;
import technobotts.util.DataProcessor;

public class CameraNorthFacing extends Strategy {
	public CameraNorthFacing(SoccerRobot robot) {
		super(robot);
	}

	DataProcessor robotHeadingModifier = new RobotDirectionModifier(2);

	boolean isRunning = false;
	boolean kicking = false;
	
	long lastBallTime = System.currentTimeMillis();
	
	@Override
	protected void activated() {
		robot.setMoveSpeed(300);
		robot.travel(robot.getHeading());
		Delay.msDelay(1000);
		while(robot.hasBall()) ;
		robot.rotateTo(0);
	}
	
	@Override
	protected void disabledPeriodic() {
		robot.rotateTo(robot.getHeading(), true);
	}

	@Override
	protected void activePeriodic() {
		long time = System.currentTimeMillis();
		if (robot.hasBall()) {
			if (!kicking) {
				// Sound.beepSequenceUp();
				kicking = true;
				robot.setMoveSpeed(robot.MAX_SPEED);
				robot.travel(0);
			}
			lastBallTime = time;
		} else {
			// Sound.beepSequence();
			kicking = false;

			float range = robot.getBallDetector().getRange();
			float ballAngle = robot.getBallAngle();

			LCD.drawString(ballAngle + "," + range, 0, 0);
			float heading = robot.getHeading() + ballAngle;
			heading %= 360;
			
			if (range > 3 && !Float.isNaN(heading)) {
				lastBallTime = time;
				robot.setMoveSpeed(robot.MAX_SPEED);
				float target = (float) robotHeadingModifier.getOutput(heading);

				// LCD.drawString(heading +", "+target+"     ", 0, 0);

				robot.travel(target);
			}
			else if(time > lastBallTime + 500) {
				robot.stop();
			}
		}
	}

	@Override
	protected void executeWithConnected(SoccerRobot robot) { };
}

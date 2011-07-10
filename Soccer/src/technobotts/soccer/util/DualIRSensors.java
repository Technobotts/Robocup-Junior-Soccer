package technobotts.soccer.util;

import lejos.robotics.DirectionFinder;
import technobotts.nxt.addon.IRSeekerV2;
import technobotts.robotics.LightSourceFinder;

public class DualIRSensors extends DualSensor<DirectionFinder> implements
		DirectionFinder {
	IRSeekerV2 first;
	int firstAngle;

	public DualIRSensors(
			IRSeekerV2 left, float leftOffset,
			IRSeekerV2 right, float rightOffset) {
		super(left, leftOffset, right, rightOffset);
	}

	@Override
	public float getDegreesCartesian() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void startCalibration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopCalibration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetCartesianZero() {
		// TODO Auto-generated method stub
		
	}
}

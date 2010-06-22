package technobotts.soccer.slave;

import lejos.nxt.Motor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class GoalieSlaveRobot extends SoccerSlaveRobot
{
	UltrasonicSensor rearWallDetector;
	
	public GoalieSlaveRobot(UltrasonicSensor ballDetector, Motor kickerMotor, UltrasonicSensor rearWallDetector)
    {
	    super(ballDetector, kickerMotor);
	    this.rearWallDetector = rearWallDetector;
	    this.rearWallDetector.off();
    }

	public float getRearWallDist()
    {
		rearWallDetector.ping();
	    float distance = rearWallDetector.getDistance();
	    return distance == 255? Float.NaN : distance;
    }

}

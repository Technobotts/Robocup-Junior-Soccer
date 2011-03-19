package technobotts.soccer.slave;

import technobotts.soccer.util.CameraPoller;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.NXTCam;

public class CameraSlaveRobot extends SoccerSlaveRobot
{
	protected CameraPoller camPoller;

	public CameraSlaveRobot(UltrasonicSensor US, Motor kickerMotor, NXTCam cam, int goalColor)
	{
		super(US,kickerMotor);
		this.camPoller = new CameraPoller(cam, goalColor);
		this.camPoller.start();
	}

	public double getGoalAngle()
    {
		Sound.beep();
    	return camPoller.getAngle();
    }

}
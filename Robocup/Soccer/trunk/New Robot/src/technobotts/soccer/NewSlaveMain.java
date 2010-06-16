package technobotts.soccer;

import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.NXTCam;
import technobotts.soccer.slave.CameraSlaveRobot;
import technobotts.soccer.slave.SlaveCommunicator;
import technobotts.soccer.slave.SoccerSlaveRobot;
import technobotts.soccer.util.GoalFinder;

public class NewSlaveMain
{

	/**
	 * @param args
	 */


	public static void main(String[] args) throws InterruptedException, IOException
	{
		SoccerSlaveRobot robot = new CameraSlaveRobot(new UltrasonicSensor(SensorPort.S2),
		                                              Motor.A,
		                                              new TouchSensor(SensorPort.S3),
		                                              new NXTCam(SensorPort.S1),
		                                              GoalFinder.BLUE);
		SlaveCommunicator s = new SlaveCommunicator(robot);
		s.run();
		System.exit(0);
	}
}

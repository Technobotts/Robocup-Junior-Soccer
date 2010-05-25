package technobotts.soccer;

import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.NXTCam;
import technobotts.soccer.slave.CameraSlaveRobot;
import technobotts.soccer.slave.SoccerSlave;
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
		                                              new NXTCam(SensorPort.S1),
		                                              GoalFinder.BLUE);
		SoccerSlave s = new SoccerSlave(robot);
		s.start();
	}

}

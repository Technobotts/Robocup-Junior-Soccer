package technobotts.soccer;

import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.NXTCam;
import lejos.util.TextMenu;
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
		TextMenu goalSelection = new TextMenu(new String[] {"Blue", "Yellow"}, 0, "Goal Color");
		int goalColor = goalSelection.select();
		
		SoccerSlaveRobot robot = new CameraSlaveRobot(new UltrasonicSensor(SensorPort.S2),
		                                              Motor.A,
		                                              new NXTCam(SensorPort.S1),
		                                              goalColor);
		SlaveCommunicator s = new SlaveCommunicator(robot);
		s.run();
		System.exit(0);
	}
}

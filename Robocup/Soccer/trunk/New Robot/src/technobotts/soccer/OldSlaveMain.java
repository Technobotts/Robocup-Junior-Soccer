package technobotts.soccer;

import technobotts.soccer.slave.CameraSlaveRobot;
import technobotts.soccer.slave.SlaveCommunicator;
import technobotts.soccer.slave.SoccerSlaveRobot;
import technobotts.soccer.util.GoalFinder;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.NXTCam;
import lejos.nxt.comm.*;


public class OldSlaveMain
{
	public static void main(String[] args) throws Exception
    {
		SoccerSlaveRobot robot = new SoccerSlaveRobot(null,
		                                              Motor.A,
		                                              new TouchSensor(SensorPort.S1));
		SlaveCommunicator s = new SlaveCommunicator(robot);
		s.run();
		System.exit(0);
    }
}

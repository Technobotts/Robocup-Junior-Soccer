package technobotts.soccer;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import technobotts.soccer.slave.GoalieSlaveRobot;
import technobotts.soccer.slave.SlaveCommunicator;
import technobotts.soccer.slave.SoccerSlaveRobot;


public class OldSlaveMain
{
	public static void main(String[] args) throws Exception
    {
		SoccerSlaveRobot robot = new GoalieSlaveRobot(null,  Motor.A, new UltrasonicSensor(SensorPort.S1));
		SlaveCommunicator s = new SlaveCommunicator(robot);
		s.run();
		System.exit(0);
    }
}

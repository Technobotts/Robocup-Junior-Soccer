package technobotts.soccer.slave;

import java.io.*;

import lejos.nxt.*;
import lejos.nxt.addon.NXTCam;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import technobotts.comm.MessageType;
import technobotts.soccer.util.GoalFinder;

public class SoccerSlave extends Thread
{
	private NXTConnection    master;

	private DataOutputStream dos;
	private DataInputStream  dis;

	private SlaveRobot       robot;

	public SoccerSlave(SlaveRobot robot) throws IOException
	{
		master = RS485.getConnector().waitForConnection(0, NXTConnection.PACKET);
		if(master == null)
			throw new IOException("Could not connect to another NXT");

		this.robot = robot;
		dos = master.openDataOutputStream();
		dis = master.openDataInputStream();
	}

	public void run()
	{
		while(true)
		{
			try
			{
				byte message = dis.readByte();
				if(message == MessageType.KICK.getValue())
				{
					Sound.beepSequenceUp();
					robot.kick();
					dos.writeBoolean(true); // TODO
				}
				else if(message == MessageType.GOAL_POS.getValue())
				{
					Sound.beep();
					dos.writeDouble(robot.getGoalAngle());
				}
				else if(message == MessageType.US_PING.getValue())
				{
					Sound.twoBeeps();
					dos.writeBoolean(robot.hasBall());
				}
				else if(message == MessageType.SHUTDOWN.getValue())
				{
					Sound.buzz();
					dos.writeBoolean(true);
					break;
				}
				dos.flush();
			}
			catch(IOException e)
			{
				continue;
			}
		}
		
	}

	public static void main(String[] args) throws InterruptedException, IOException
    {
		SlaveRobot robot = new SlaveRobot(new NXTCam(SensorPort.S1),
		                                  new UltrasonicSensor(SensorPort.S2),
		                                  Motor.A,
		                                  GoalFinder.BLUE);
		SoccerSlave s = new SoccerSlave(robot);
		s.start();
    }
}
package technobotts.soccer.slave;

import java.io.*;

import lejos.nxt.*;
import lejos.nxt.addon.NXTCam;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import technobotts.soccer.util.GoalFinder;
import technobotts.soccer.util.MessageType;

public class SoccerSlave extends Thread
{
	private NXTConnection    master;

	private DataOutputStream dos;
	private DataInputStream  dis;

	private SoccerSlaveRobot robot;

	public SoccerSlave(SoccerSlaveRobot robot) throws IOException
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
					robot.kick();
					dos.writeBoolean(true);
				}
				else if(message == MessageType.GOAL_POS.getValue())
				{
					dos.writeDouble(robot.getGoalAngle());
				}
				else if(message == MessageType.US_PING.getValue())
				{
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
}
package technobotts.soccer.slave;

import java.io.*;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.*;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import technobotts.soccer.util.MessageType;

public class SoccerSlave extends Thread
{
	private NXTConnection    master;

	private DataOutputStream dos;
	private DataInputStream  dis;

	private SoccerSlaveRobot robot;

	private static void fillRoundRect(Graphics g, int x, int y, int width, int height, int arcWidth, int arcHeight)
	{
		g.fillRect(x, y + arcHeight, width, height - arcHeight*2);
		g.fillRect(x + arcWidth, y, width - arcWidth*2, height);
		g.fillArc(x, y, arcWidth * 2, arcHeight * 2, 90, 90);
		g.fillArc(x + width - arcWidth * 2 -1, y, arcWidth * 2, arcHeight * 2, 0, 90);
		g.fillArc(x + width - arcWidth * 2 - 1, y + height - arcHeight * 2 - 1, arcWidth * 2, arcHeight * 2, 270, 90);
		g.fillArc(x, y + height - arcHeight * 2 - 1, arcWidth * 2, arcHeight * 2, 180, 90);
	}
	
	public SoccerSlave(SoccerSlaveRobot robot) throws IOException
	{
		Graphics g = new Graphics();
		g.clear();
		fillRoundRect(g, 5, 5, 90, 54, 5, 5);
		g.drawString("Connecting...", g.getCenteredX("Connecting..."), 28, true);
		
		master = RS485.getConnector().waitForConnection(0, NXTConnection.PACKET);
		if(master == null)
			throw new IOException("Could not connect to another NXT");

		this.robot = robot;
		dos = master.openDataOutputStream();
		dis = master.openDataInputStream();
	}

	public void run()
	{
		Graphics g = new Graphics();
		g.clear();
		fillRoundRect(g, 5, 5, 90, 54, 5, 5);
		g.drawString("Running!", g.getCenteredX("Running!"), 28, true);
		
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
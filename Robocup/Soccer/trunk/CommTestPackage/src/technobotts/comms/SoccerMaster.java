package technobotts.comms;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import technobotts.comms.SoccerPackage.MessageType;

import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;


public class SoccerMaster 
{
	private String name = "Soccer";
	
	private NXTConnection con;
	
	private DataOutputStream reciever;
	private DataInputStream sender;
	
	boolean hasBall;
	float goalAngle;
	
	public SoccerMaster()
	{
		con = RS485.getConnector().connect(name, NXTConnection.PACKET);
		reciever = con.openDataOutputStream();
		sender =  con.openDataInputStream();
	}
	
	public void requestKick()
	{
		byte[] buf = SoccerPackage.MakeRequest(MessageType.KICK);
		con.sendPacket(buf,buf.length);
	}
	
	public void requestUsPing()
	{
		byte[] buf = SoccerPackage.MakeRequest(MessageType.US_PING);
		con.sendPacket(buf,buf.length);
	}
	
	class Reciever extends Thread
	{
		public Reciever()
        {
			setDaemon(true);
        }
		public void run()
		{
			while(true)
			{
				byte[] data = {};
				int result = con.read(data, 32, true);
				if(result>0 && data[0] == SoccerPackage.REQUEST_HEADER)
				{
					MessageType type = MessageType.values()[data[1] & 0x7F];
					switch(type)
					{
					case US_PING:
						hasBall = (data[3] != 0);
					case GOAL_ANGLE:
						Float.intBitsToFloat()
							
					}
				}
			}
		}
	}
}

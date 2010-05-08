package technobotts.comms;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;

public class SoccerSlave implements MessageType
{
	private NXTConnection    con;

	private DataOutputStream dos;
	private DataInputStream  dis;
	
	private Responder        responder;

	public SoccerSlave() throws IOException
	{
		con = RS485.getConnector().waitForConnection(0, NXTConnection.PACKET);
		if(con == null)
			throw new IOException("Could not connect to another NXT");

		dos = con.openDataOutputStream();
		dis = con.openDataInputStream();
		
		responder = new Responder();
		responder.start();
	}

	class Responder extends Thread
	{
		public void run()
		{
			while(true)
			{
				try
				{
					byte message = dis.readByte();

					if(message == KICK)
					{
						Sound.beepSequenceUp();
						dos.writeBoolean(true); //TODO
					}
					else if(message == GOAL_POS)
					{
						Sound.beep();
						dos.writeFloat(3.14159f); //TODO
					}
					else if(message == US_PING)
					{
						Sound.twoBeeps();
						dos.writeBoolean(false); //TODO
					}
					else if(message == SHUTDOWN)
					{
						Sound.buzz();
						dos.writeBoolean(true); //TODO
						break;
					}
				}
				catch(IOException e)
				{
					continue;
				}
			}
		}
	}

	public static void main(String... args) throws IOException, InterruptedException
    {
		@SuppressWarnings("unused")
        SoccerSlave s;
		try
        {
			s = new SoccerSlave();
        }
        catch(IOException e)
        {

            LCD.drawString(e.getMessage(), 0, 5);
            Thread.sleep(2000);
            System.exit(1);
        }
    }
}

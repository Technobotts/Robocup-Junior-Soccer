package technobotts.comms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.util.TextMenu;

public class SoccerMaster
{
	@SuppressWarnings("unused")
    private final String     recipientName;
	private NXTConnection    con;

	private DataOutputStream dos;
	private DataInputStream  dis;

	public SoccerMaster(String name) throws IOException
	{
		recipientName = name;
		con = RS485.getConnector().connect(name, NXTConnection.PACKET);
		if(con == null)
			throw new IOException("Could not connect to another NXT");

		dos = con.openDataOutputStream();
		dis = con.openDataInputStream();
	}

	public boolean kick()
	{
		try
		{
			dos.writeByte(MessageType.KICK.getValue());
			dos.flush();
			return dis.readBoolean();
		}
		catch(IOException e)
		{
			return false;
		}
	}

	public float getGoalAngle()
	{
		try
		{
			dos.writeByte(MessageType.GOAL_POS.getValue());
			dos.flush();
			return dis.readFloat();
		}
		catch(IOException e)
		{
			return Float.NaN;
		}
	}

	public boolean hasBall()
	{
		try
		{
			dos.writeByte(MessageType.US_PING.getValue());
			dos.flush();
			return dis.readBoolean();
		}
		catch(IOException e)
		{
			return false;
		}
	}

	public boolean shutdown()
	{
		try
		{
			dos.writeByte(MessageType.SHUTDOWN.getValue());
			dos.flush();
			return dis.readBoolean();
		}
		catch(IOException e)
		{
			return false;
		}
	}

	public static void main(String[] args) throws InterruptedException
    {
		
		SoccerMaster m = null;
        try
        {
	        m = new SoccerMaster("Soccer");
        }
        catch(IOException e)
        {

            LCD.drawString(e.getMessage(), 0, 5);
            Thread.sleep(2000);
            System.exit(1);
        }
		
		TextMenu menu = new TextMenu(new String[]{"Kick",
		                                          "US Ping",
		                                          "Goal Direction",
		                                          "Shutdown"},
		                                          0,
		                                          "Send:");
		int code = 0;
		while(true)
		{
			LCD.clear();
			code = menu.select(code);
			LCD.clear();
			MessageType msg = MessageType.values()[code];
			if(msg == MessageType.KICK)
			{
				boolean success = m.kick();
				LCD.drawString("Success: "+success, 0, 0);
			}
			else if(msg == MessageType.GOAL_POS)
			{
				float angle = m.getGoalAngle();
				LCD.drawString("Angle: "+angle, 0, 0);
				
			}
			else if(msg == MessageType.US_PING)
			{
				boolean hasBall = m.hasBall();
				LCD.drawString("Ball: "+hasBall, 0, 0);
			}
			else if(msg == MessageType.SHUTDOWN)
			{
				if(m.shutdown())
					break;
			}
			else
			{
				Sound.buzz();
			}
			Thread.sleep(1000);
			while(Button.readButtons() != 0);
		}
    }
}

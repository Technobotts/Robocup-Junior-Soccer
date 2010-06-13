import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.InvertedCompassSensor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.robotics.DirectionFinder;
import lejos.util.TextMenu;

public class CommTest
{
	final static String name = "Soccer";

	public static void main(String... args) throws InterruptedException
	{

		String[] modes = {"Send", "Receive"};
		TextMenu modeMenu = new TextMenu(modes, 0, "Mode");
		int choice = modeMenu.select();
		
		if(choice == 0)
		{
			// SEND
			NXTConnection con = RS485.getConnector().connect(name, NXTConnection.PACKET);
			DataOutputStream dos = con.openDataOutputStream();
			DirectionFinder c = new InvertedCompassSensor(SensorPort.S1);
			while(!Button.ESCAPE.isPressed())
			{
				try
				{
					dos.writeFloat(c.getDegreesCartesian());
					dos
	                //dos.flush();
				}
				catch(IOException e)
				{
					Sound.buzz();
				}
				Thread.sleep(100);
			}
		}
		else if(choice == 1)
		{
			// RECEIVE
			NXTConnection con = RS485.getConnector().waitForConnection(0, NXTConnection.PACKET);
			if (con == null)
	        {
	            LCD.drawString("Connect fail", 0, 5);
	            Thread.sleep(2000);
	            System.exit(1);
	        }
			
			DataInputStream dis = con.openDataInputStream();
			while(!Button.ESCAPE.isPressed())
			{
				try
				{
					float angle = dis.readFloat();
					LCD.clear();
					LCD.drawString("Angle: "+angle+"     ", 0, 0);
					LCD.refresh();
				}
				catch(IOException e)
				{
					Sound.buzz();
				}
				Thread.sleep(100);
			}
		}
	}
}

package technobotts.soccer;

import lejos.nxt.*;
import lejos.nxt.comm.*;


public class OldSlaveMain
{

	static class Responder extends LCPResponder
	{
		Responder(NXTCommConnector con)
		{
			super(con);
		}

		protected void disconnect()
		{
			super.disconnect();
			super.shutdown();
		}
	}

	public static void main(String[] args) throws Exception
    {
        LCD.drawString("Running...", 0, 1);
        final Responder resp = new Responder(RS485.getConnector());
    	Button.ESCAPE.addButtonListener(new ButtonListener() {
			public void buttonReleased(Button b)
			{
				resp.disconnect();
				System.exit(0);
			}
			
			public void buttonPressed(Button b)
			{			
			}
		});
    	Sound.beep();
        resp.start();
        resp.join();
        LCD.drawString("Closing...  ", 0, 1);
    }
}

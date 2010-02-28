
import lejos.nxt.*;
import lejos.nxt.comm.*;
import lejos.util.TextMenu;

/**
 * Create an LCP responder to handle LCP requests. Allow the
 * User to choose between Bluetooth, USB and RS485 protocols.
 * 
 * @author Andy Shaw
 *
 */
public class KickReciever
{
    /**
     * Our local Responder class so that we can over-ride the standard
     * behaviour. We modify the disconnect action so that the thread will
     * exit.
     */
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
        Responder resp = new Responder(RS485.getConnector());
        resp.start();
        resp.join();
        LCD.drawString("Closing...  ", 0, 1);
    }
}


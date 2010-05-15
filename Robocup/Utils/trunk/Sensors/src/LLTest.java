import lejos.nxt.*;
import lejos.nxt.addon.LineLeader;

public class LLTest
{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		LineLeader ll = new LineLeader(SensorPort.S1);
		ll.sendCommand('E');
		ll.setKp(10,32);

		if(10.0/32.0 != ll.getKp())
		{
			Sound.twoBeeps();
			LCD.drawString("kp = "+ll.getKp(), 0, 0);
			Thread.sleep(1000);
		}
		LCD.drawString("version: "+ll.getVersion(),0,0);
		Button.ENTER.waitForPressAndRelease();
		while(true)
		{
			LCD.clear();
			int[] sensors = ll.getSensors();
			int[] black = ll.getBlackLimits();
			int[] white = ll.getWhiteLimits();
			for(int i = 0; i < 8; i++)
			{
				LCD.drawString(black[i] + " " + sensors[i] + " " + white[i], 0, i);
			}
			LCD.refresh();
			Thread.sleep(50);
		}
	}
}

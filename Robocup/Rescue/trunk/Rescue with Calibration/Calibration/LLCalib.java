import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.SystemSettings;
import lejos.nxt.addon.LineLeader;

/**
 * 
 */

/**
 * @author Administrator
 */
public class LLCalib
{

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		Thread.sleep(200);

		LineLeader LL = new LineLeader(SensorPort.S2);
		LL.sendCommand(LineLeader.EU);

		LCD.drawString("Calibrate White.", 3, 0);
		Button.ENTER.waitForPressAndRelease();
		LL.sendCommand('W');

		int[] white = LL.getWhiteLimits();
		for(int i = 0; i < 8; i++)
		{
			LCD.drawString("" + white[i], 0, i);
		}

		LCD.drawString("Calibrate Black.", 3, 0);
		Button.ENTER.waitForPressAndRelease();
		LL.sendCommand('B');

		int[] black = LL.getBlackLimits();
		for(int i = 0; i < 8; i++)
		{
			LCD.drawString("" + black[i], 8, i);
		}
		Button.ENTER.waitForPressAndRelease();
	}

}

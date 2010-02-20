import javax.microedition.lcdui.Graphics;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.LineLeader;


public class LLTest
{
	public static void main(String... args) throws InterruptedException
	{
		LineLeader ll = new LineLeader(SensorPort.S2);
		Graphics g = new Graphics();
		g.autoRefresh(false);
		while(true)
		{
			g.clear();
			int[] sensors = ll.getSensors();
			g.setColor(1);
			g.fillRect(0, 0, 100, 64);
			g.setColor(0);
			if(sensors != null)
			{
				int len = sensors.length;
				for(int i = 0; i<len; i++)
				{
					int s = 64*sensors[i]/100;
					g.fillRect(i*12+3, 64-s, 10, s);
				}
			}
			g.refresh();
			Thread.sleep(50);
		}
	}
}

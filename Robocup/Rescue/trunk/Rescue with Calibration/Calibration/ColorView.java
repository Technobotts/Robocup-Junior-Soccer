import javax.microedition.lcdui.Graphics;

import technobotts.rescue.RawColor;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.addon.LineLeader;


public class ColorView
{
	public static void main(String... args) throws InterruptedException
	{
		//ColorSensor cs = new ColorSensor(SensorPort.S2);
		LightSensor ls = new LightSensor(SensorPort.S1);
		Graphics graphics = new Graphics();
		graphics.autoRefresh(false);
		while(true)
		{
			graphics.clear();
			graphics.fillRect(0, 0, (int) (/11.0), 63);
			//RawColor color = RawColor.fromHTSensor(cs);
			//int r = (int) (Math.log(color.getR()/1008) / Math.log(2) * 80);
			//int g = (int) (Math.log(color.getR()/1008) / Math.log(2) * 80);
			//int b = (int) (Math.log(color.getR()/1008) / Math.log(2) * 80);
			//graphics.fillRect(0, 60, r, 2);
			//graphics.fillRect(0, 57, g, 2);
			//graphics.fillRect(0, 54, b, 2);
			//graphics.refresh();
			Thread.sleep(50);
		}
	}
}

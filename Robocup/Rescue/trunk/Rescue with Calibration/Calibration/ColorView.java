import java.awt.Rectangle;

import javax.microedition.lcdui.Graphics;

import technobotts.rescue.RawColor;

import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensor;


public class ColorView
{
	public static void drawColor(Rectangle rect,RawColor color)
	{
		final double power = 5/12.0;
		
		Graphics graphics = new Graphics();
		final double scale = rect.width/Math.pow(1024,power);
		int r = (int) (Math.pow(color.getR()-1,power)*scale);
		int g = (int) (Math.pow(color.getG()-1,power)*scale);
		int b = (int) (Math.pow(color.getB()-1,power)*scale);
		int height = (rect.height-1)/3;
		graphics.fillRect(rect.x, rect.y, r, height-1);
		graphics.fillRect(rect.x, rect.y+height, g, height-1);
		graphics.fillRect(rect.x, rect.y+height*2, b, height-1);
		graphics.refresh();
	}
	
	public static void main(String... args) throws InterruptedException
	{
		ColorSensor cs = new ColorSensor(SensorPort.S1);
		Graphics graphics = new Graphics();
		graphics.autoRefresh(false);
		
		while(true)
		{ 
			graphics.clear();
			RawColor color = RawColor.fromHTSensor(cs);
			drawColor(new Rectangle(10, 10, 80, 32),color);
			Thread.sleep(50);
		}
	}
}

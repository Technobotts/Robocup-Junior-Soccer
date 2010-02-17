import technobotts.ColorList;
import lejos.nxt.LCD;
import lejos.util.Color;
import lejos.util.ColorHSL;
import lejos.util.ColorRGB;

public class ColorTest
{
	public static void main(String args[]) throws Exception
	{		
//		Color red = new ColorHSL(354, 1, 0.64f);
//		Color red2 = new ColorRGB(0xFF, 0x47, 0x5A);
//		red2.getRGB().getB();
//		LCD.drawString("Red:" + red, 0, 0);
//		LCD.drawString("Red2:" + red2, 0, 1);
//		LCD.drawString("Red==Red2:" + red.equals(red2), 0, 2);
//		LCD.refresh();
//		Thread.sleep(1000);
		
		ColorList cl = new ColorList();
		cl.add(Color.RED);
		cl.add(Color.BLACK);
		cl.add(Color.BLUE);
		
		LCD.drawString("Near: " + cl.getNearestColor(Color.GREEN), 0, 0);
		Thread.sleep(10000);
		
		
	}
}

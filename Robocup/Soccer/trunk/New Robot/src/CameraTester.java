import java.awt.Rectangle;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.NXTCam;
import lejos.util.Smoother;
import lejos.util.TextMenu;
import technobotts.soccer.util.CameraPoller;
import technobotts.soccer.util.GoalFinder;

public class CameraTester
{
	public static void main(String[] args) throws InterruptedException
	{
		TextMenu goalSelection = new TextMenu(new String[] {"Blue", "Yellow"}, 0, "Goal Color");
		int goalColor = goalSelection.select();

		NXTCam cam = new NXTCam(SensorPort.S1);

		CameraPoller gFinder = new CameraPoller(cam, goalColor);
		Graphics g = new Graphics();

		LCD.setAutoRefresh(0);
		for(int i=0;i<1000;i++)
		{
			LCD.clear();
			LCD.drawInt(cam.getNumberOfObjects(), 0, 0, 2);
			Rectangle largest = gFinder.getGoalRectangle();
			if(largest != null)
			{
				int x = largest.x * 64 / 144;
				int y = largest.y * 64 / 144;
				int width = largest.width * 64 / 144;
				int height = largest.height * 64 / 144;
				g.fillRect(x, y, width, height);
			}
			
			double angle = gFinder.getAngle();
			LCD.drawString("" + angle, 0, 1);
			
			LCD.refresh();
			Thread.sleep(50);
		}
	}
}

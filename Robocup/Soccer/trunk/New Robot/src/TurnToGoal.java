import java.awt.Rectangle;

import javax.microedition.lcdui.Graphics;

import technobotts.soccer.GoalFinder;

import lejos.nxt.SensorPort;
import lejos.nxt.addon.NXTCam;
import lejos.util.TextMenu;

public class TurnToGoal
{
	public static void main(String[] args) throws InterruptedException
	{
		TextMenu goalSelection = new TextMenu(new String[] {"Blue", "Yellow"}, 0, "Goal Color");
		int goalColor = goalSelection.select();

		NXTCam cam = new NXTCam(SensorPort.S1);
		
		GoalFinder gFinder = new GoalFinder(cam,goalColor);
		
		Graphics g = new Graphics();
	
		while(true)
		{
			g.clear();
			Rectangle largest = gFinder.getGoalRectangle();
			if(largest != null)
			{
				int x = largest.x * 64 / 144;
				int y = largest.y * 64 / 144;
				int width = largest.width * 64 / 144;
				int height = largest.height * 64 / 144;
				float angle = gFinder.getDegreesCartesian();

				g.fillRect(x, y, width, height);
				g.drawString("" + angle, 0, 0);
			}
			g.refresh();
			Thread.sleep(50);
		}
	}
}

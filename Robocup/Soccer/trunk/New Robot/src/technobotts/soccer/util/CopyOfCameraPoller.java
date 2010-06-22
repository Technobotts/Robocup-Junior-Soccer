package technobotts.soccer.util;

import java.awt.Rectangle;

import technobotts.util.Smoother;

import lejos.nxt.Sound;
import lejos.nxt.addon.NXTCam;

public class CopyOfCameraPoller extends Thread
{
	private NXTCam cam;
	private int goalColor;

	private Smoother s;

	public CopyOfCameraPoller(NXTCam cam, int goalColor)
	{
		this.cam = cam;
		this.s = new Smoother(1);
		
		setDaemon(true);
	}
	
	public double getAngle()
	{
		return s.getOutput();
	}
	
	private Rectangle goalRectangle;
	public Rectangle getGoalRectangle()
	{
		return goalRectangle;
	}

	public void run()
	{
		GoalFinder gFinder = new GoalFinder(cam, goalColor);
		while(true)
		{
			goalRectangle = gFinder.getGoalRectangle();
			if(goalRectangle == null)
				Sound.buzz();
			s.setInput(gFinder.getAngle(goalRectangle));
			try
            {
	            Thread.sleep(500);
            }
            catch(InterruptedException e)
            {
            }
		}
	}
}

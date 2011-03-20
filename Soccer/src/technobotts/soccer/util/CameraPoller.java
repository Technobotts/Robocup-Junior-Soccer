package technobotts.soccer.util;

import java.awt.Rectangle;

import technobotts.util.Smoother;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.addon.NXTCam;
import lejos.util.Delay;

public class CameraPoller extends Thread
{
	private NXTCam cam;
	private int goalColor;

	private volatile Smoother s;

	public CameraPoller(NXTCam cam, int goalColor)
	{
		this.cam = cam;
		this.s = new Smoother(1);
		this.goalColor = goalColor;
		
		setDaemon(true);
	}
	
	public double getAngle()
	{ 
		return s.getOutput();
	}
	/*
	private Rectangle goalRectangle;
	public Rectangle getGoalRectangle()
	{
		return goalRectangle;
	}
	*/

	public void run()
	{
		GoalFinder gFinder = new GoalFinder(cam, goalColor);
		while(true)
		{
			synchronized(cam)
			{
				s.setInput(gFinder.getDegreesCartesian());
			}
			Delay.msDelay(50);
		}
	}
}

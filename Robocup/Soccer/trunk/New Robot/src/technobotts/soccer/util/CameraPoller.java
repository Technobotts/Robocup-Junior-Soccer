package technobotts.soccer.util;

import lejos.nxt.addon.NXTCam;
import lejos.util.Smoother;

public class CameraPoller extends Thread
{
	private NXTCam cam;
	private int goalColor;

	private Smoother s;

	public CameraPoller(NXTCam cam, int goalColor)
	{
		this.cam = cam;
		this.s = new Smoother(1);
		
		setDaemon(true);
	}
	
	public double getAngle()
	{
		return s.getOutput();
	}

	public void run()
	{
		GoalFinder gFinder = new GoalFinder(cam, goalColor);
		while(true)
		{
			s.setInput(gFinder.getDegreesCartesian());
		}
	}
}

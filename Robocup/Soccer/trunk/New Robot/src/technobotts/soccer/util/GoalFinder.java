package technobotts.soccer.util;

import java.awt.Rectangle;
import java.util.List;
import java.util.RectGroup;

import lejos.nxt.addon.CamRect;
import lejos.nxt.addon.NXTCam;
import lejos.robotics.DirectionFinder;

public class GoalFinder implements DirectionFinder
{
	final static int BLUE   = 0;
	final static int YELLOW = 1;

	private NXTCam   camera;
	private int      goalColorId;
	
	private CamPoller poller = null;

	public GoalFinder(NXTCam camera, int goalColorId)
	{
		this.camera = camera;
		camera.enableTracking(true);
		this.goalColorId = goalColorId;
	}

	private float getAngle(float xCoord)
	{
		final float offset = 102.19f;
		final float scale = 0.0057f;
		return (float) (Math.toDegrees(Math.atan(scale * (xCoord - offset))));
	}
	private float getAngle(Rectangle rect)
	{
		if(rect == null)
			return Float.NaN;
		return getAngle(rect.x + rect.width / 2);
	}
	
	private Rectangle calcGoalRectangle()
	{
		RectGroup blocks = new RectGroup();
		CamRect[] rects = CamRect.fromNXTCam(camera);

		for(CamRect r : rects)
		{
			if(r.getArea() >= 100)
			{
				if(r.colorId == goalColorId)
					blocks.add(r);
			}
		}
		
		List<RectGroup> groupedBlocks = blocks.getDistinctGroups();
		RectGroup mergedBlocks = new RectGroup(groupedBlocks.size());

		for(RectGroup group : groupedBlocks)
		{
			mergedBlocks.add(group.getBoundingBox());
		}

		return mergedBlocks.getLargest(500);
	}
	
	public Rectangle getGoalRectangle()
	{
		if(poller != null)
			return poller.largestBlock;
		else
			return calcGoalRectangle();
	}
	
	public float getDegreesCartesian()
	{
		return getAngle(getGoalRectangle());
	}

	public void startPolling()
	{
		startPolling(50);
	}
	public void startPolling(int pollTime)
	{
		poller = new CamPoller(pollTime);
		poller.start();
	}
	public void stopPolling()
	{
		poller = null;
	}
	
	
	private class CamPoller extends Thread
	{
		Rectangle largestBlock = null;
		int pollTime = 0;
		
		public CamPoller(int pollTime)
		{
			this.pollTime = pollTime;
			setDaemon(true);
		}

		public void run()
		{
			while(poller == this)
			{
				largestBlock = calcGoalRectangle();
				try
                {
	                sleep(pollTime);
                }
                catch(InterruptedException e)
                {
                }
			}
		}
	}


	@Override
    public void resetCartesianZero()
    {  
    }

	@Override
    public void startCalibration()
    {
    }

	@Override
    public void stopCalibration()
    {
    }
}

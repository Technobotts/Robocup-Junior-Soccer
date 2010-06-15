package technobotts.robotics;

import java.awt.Rectangle;

import lejos.nxt.addon.NXTCam;

public class CamRect extends Rectangle
{
	public int colorId;

	public CamRect(Rectangle rectangle, int objectColor)
	{
		super();
		setRect(rectangle);
		colorId = objectColor;
	}

	public int getArea()
	{
		return width * height;
	}

	public boolean intersects(Rectangle r)
	{
		final int tolerance = 3;
		return super.intersects(new Rectangle(r.x - tolerance,
		                                      r.y - tolerance,
		                                      r.width + 2 * tolerance,
		                                      r.height + 2 * tolerance));
	}
	
	public static CamRect[] fromNXTCam(NXTCam camera)
	{
		int num = camera.getNumberOfObjects();
		if(num <= 0)
			return new CamRect[] {};
		CamRect[] rects = new CamRect[num];
		for(int i = 0; i < rects.length; i++)
		{
			rects[i] = new CamRect(camera.getRectangle(i), camera.getObjectColor(i));
		}
		return rects;
	}
}
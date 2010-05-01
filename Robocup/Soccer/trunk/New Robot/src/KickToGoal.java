import java.awt.Rectangle;

import javax.microedition.lcdui.Graphics;


import lejos.nxt.*;
import lejos.nxt.addon.CamRect;
import lejos.nxt.addon.NXTCam;
import lejos.nxt.addon.CompassSensor;
import lejos.nxt.addon.InvertedCompassSensor;
import lejos.robotics.DirectionFinder;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.robotics.navigation.SimpleOmniPilot;

public class KickToGoal
{
	
	final static int BLUE       = 0;
	final static int YELLOW     = 1;
	final static int NUM_COLORS = 2;
	

	public static void main(String... args) throws InterruptedException
	{
		NXTCam cam = new NXTCam(SensorPort.S1);
		DirectionFinder compass = new InvertedCompassSensor(SensorPort.S3);
		OmniCompassPilot pilot = new OmniCompassPilot(compass,
		                                              new SimpleOmniPilot.OmniMotor(Motor.C, 53.1301f, 6.4f, 1, 9.6f,
		                                                                            true),
		                                              new SimpleOmniPilot.OmniMotor(Motor.B, 180, 6.4f, 1, 8.8f),
		                                              new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f)
		                         );
		
		cam.enableTracking(true);
		Graphics g = new Graphics();
		compass.resetCartesianZero();
		pilot.setMoveSpeed(1000);
		Sound.beep();
		Thread.sleep(1000);
		
		while(true)
		{
			CamRect[] rects = CamRect.fromNXTCam(cam);
			g.clear();
			for(CamRect r : rects)
			{
				int x = r.x;
				int y = r.y;
				int width = r.width;
				int height = r.height;
				int centerCoord = x+width/2;
				final int centerHeading = (int) (57.2957795*(Math.atan(0.0057*(centerCoord-102.19))));
				
				if(r.colorId == YELLOW) g.drawRect(x, y, width, height); 
				else g.fillRect(x, y, width, height);
				pilot.rotate(centerHeading, false);
			}

		}
	}

}

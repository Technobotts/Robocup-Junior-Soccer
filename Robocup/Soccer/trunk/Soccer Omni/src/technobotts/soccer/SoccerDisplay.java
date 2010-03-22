package technobotts.soccer;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.Button;

public class SoccerDisplay extends Thread
{
	private SoccerRobot _robot;

	public SoccerDisplay(SoccerRobot robot)
	{
		_robot = robot;
		setDaemon(true);
	}

	public void run()
	{
		Graphics g = new Graphics();
		while(true)
		{
			g.clear();

			// g.drawString("IR=" + _robot.IR.getAngle(), 0, 0);
			// g.drawString(_robot.IR.toString(), 8, 8);
			g.drawString("Compass=" + _robot.compass.getDegreesCartesian(), 0, 16);
			g.drawString("Ball=" + _robot.hasBall(), 0, 24);

			g.drawString("Status:", 0, 36);
			if(_robot.status != null)
				g.drawString(_robot.status, 8, 44);

			g.refresh();

			try
			{
				sleep(15);
			}
			catch(InterruptedException e)
			{}
		}
	}

	public static void main(String... args)
	{
		Thread t = new SoccerDisplay(new SoccerRobot());
		t.start();
		Button.ENTER.waitForPressAndRelease();
	}
}

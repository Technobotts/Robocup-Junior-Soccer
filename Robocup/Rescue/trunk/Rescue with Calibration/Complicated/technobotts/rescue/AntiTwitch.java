package technobotts.rescue;

import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class AntiTwitch extends RescueTask
{
	protected final float tolerance = 180;
	protected final int   time      = 2000;

	public AntiTwitch(RescueRobot robot)
	{
		super(robot);
		setName("AntiTwitch");
		setDaemon(true);
	}

	public void run()
	{
		int left = 0, right = 0;
		int lastLeft = 0, lastRight = 0;

		isRunning = true;
		while(isRunning)
		{
			if(!isPaused)
			{
				try
				{
					sleep(time);
				}
				catch(InterruptedException e)
				{
					continue;
				}
				
				left = _robot.pilot.getLeft().getTachoCount();
				right = _robot.pilot.getRight().getTachoCount();
				
				if(Math.abs(right - lastRight) <= tolerance && Math.abs(left - lastLeft) <= tolerance && !_robot.loggerT.lineIsLost())
				{
					synchronized(_robot.pilot)
					{
						System.out.println("AntiTwitch has Motors");
						Sound.beepSequenceUp();
						LCD.drawString("Lack of progress", 0, 0);
						_robot.pilot.travel(10);
						_robot.doLineSearch();
						LCD.clear();
					}
				}
				lastLeft = left;
				lastRight = right;
			}
			yield();
		}
	}
}

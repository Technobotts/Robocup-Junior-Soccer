package technobotts.rescue.tasks;

import technobotts.rescue.RescueRobot;
import technobotts.rescue.RescueTask;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class AntiTwitch extends RescueTask
{
	protected final float tolerance   = 180;
	protected final int   time        = 2000;
	private int           cancelCount = 0;

	public void cancel(boolean add)
	{
		if(add || cancelCount == 0)
			cancelCount++;
		interrupt();
	}
	public void cancel()
	{
		cancel(false);
	}

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
			try
			{
				sleep(time);
			}
			catch(InterruptedException e)
			{
				continue;
			}

			synchronized(_robot.pilot)
			{
				left = _robot.pilot.getLeft().getTachoCount();
				right = _robot.pilot.getRight().getTachoCount();
				
				if(cancelCount > 0)
				{
					cancelCount--;
					continue;
				}
				else if(Math.abs(right - lastRight) <= tolerance && Math.abs(left - lastLeft) <= tolerance
				   && !_robot.loggerT.lineIsLost())
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

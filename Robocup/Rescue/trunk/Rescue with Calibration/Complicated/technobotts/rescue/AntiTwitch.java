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
		int left, right;
		int lastLeft = 0, lastRight = 0;
		isRunning = true;
		while(isRunning)
		{
			left = _robot.motors.getLeftTacho();
			right = _robot.motors.getRightTacho();

			try
			{
				sleep(time);
			}
			catch(InterruptedException e)
			{
				continue;
			}

			if(Math.abs(right - lastRight) <= tolerance && Math.abs(left - lastLeft) <= tolerance)
			{
				synchronized(_robot.motors)
				{
					System.out.println("AntiTwitch has Motors");
					Sound.beepSequenceUp();
					LCD.drawString("Lack of progress", 0, 0);
					_robot.pilot.travel(10);
					_robot.doLineSearch();
					LCD.clear();
				}
			}
			yield();
			lastLeft = left;
			lastRight = right;
		}
	}
}

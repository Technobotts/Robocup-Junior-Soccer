package technobotts.rescue;

public abstract class RescueTask extends Thread
{
	protected RescueRobot _robot;
	protected boolean isRunning = true;
	public RescueTask(RescueRobot robot)
	{
		_robot = robot;
	}
	public abstract void run();
	
	public void stop()
	{
		isRunning = false;
	}
}

package technobotts.util;

public class Timer
{
	private long startTime;
	private long stopTime;
	RunState     runState;
	
	enum RunState {
		RUNNING,
		PAUSED,
		STOPPED,
		UNSTARTED
	}
	
	public Timer()
	{
		reset();
	}

	public void start()
	{
		if(runState == RunState.UNSTARTED)
		{
			startTime = System.currentTimeMillis();
			runState = RunState.RUNNING;
		}
	}
	public void restart()
	{
		reset();
		start();
	}

	public void pause()
	{
		if(runState == RunState.RUNNING)
		{
			stopTime = System.currentTimeMillis();
			runState = RunState.PAUSED;
		}
	}

	public void resume()
	{
		if(runState == RunState.PAUSED)
		{
			startTime += System.currentTimeMillis() - stopTime;
			stopTime = -1;
			runState = RunState.RUNNING;
		}
	}

	public void stop()
	{
		if(runState != RunState.RUNNING && runState != RunState.PAUSED)
		{
			stopTime = System.currentTimeMillis();
			runState = RunState.STOPPED;
		}
	}

	public long getTime()
	{
		if(runState == RunState.RUNNING)
			return System.currentTimeMillis() - startTime;
		else
			return stopTime;
	}

	public void reset()
	{
		startTime = -1;
		stopTime = -1;
		runState = RunState.UNSTARTED;
	}

	public double getTimeSeconds()
	{
		double time = getTime();
		if(time < 0)
			return Double.NaN;
		else
			return time/1000;
	}
}
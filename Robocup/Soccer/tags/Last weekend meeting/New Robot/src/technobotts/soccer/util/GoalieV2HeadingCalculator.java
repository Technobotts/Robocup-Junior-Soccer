package technobotts.soccer.util;

import lejos.util.PausableDataProcessor;
import lejos.util.Timer;

public class GoalieV2HeadingCalculator extends PausableDataProcessor
{
	private double  xComp, yComp;

	private boolean isPaused    = false;
	private long    pauseOffset = 0;
	private long    lastResetTime;
	
	private double factor;
	
	public GoalieV2HeadingCalculator(double factor)
	{
		this.factor = factor/1000;
	}

	public void pause()
	{
		if(!isPaused)
		{
			pauseOffset = System.currentTimeMillis() - lastResetTime;
			isPaused = true;
		}
	}

	public void resume()
	{
		if(isPaused)
		{
			lastResetTime = System.currentTimeMillis() - pauseOffset;
			isPaused = false;
		}
	}

	@Override
	public void setInput(double ballAngle)
	{
	}

	@Override
	public double getOutput()
	{
		resume();
		float deltaT = System.currentTimeMillis() - lastResetTime;
		return deltaT*factor;
	}

	@Override
	public void reset()
	{
		lastResetTime = System.currentTimeMillis();
	}
	
}

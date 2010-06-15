package technobotts.soccer.util;

import technobotts.util.DataProcessor;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class GoalieHeadingCalculator extends DataProcessor
{
	private float initialHeading;
	private long  lastResetTime;
	private float degreesPerMillisecond;

	public GoalieHeadingCalculator(float initialHeading, float degreesPerSecond)
	{
		this.initialHeading = initialHeading;
		this.degreesPerMillisecond = degreesPerSecond / 1000;
		reset();
	}
	@Override
	public double getOutput()
	{
		resume();
		float deltaT = System.currentTimeMillis() - lastResetTime;
		float calculatedHeading = initialHeading + deltaT * degreesPerMillisecond;
		
		if(calculatedHeading > 180)
			calculatedHeading = 180;
		return calculatedHeading;
	}

	@Override
	public void reset()
	{
		lastResetTime = System.currentTimeMillis();
	}
	
	@Override
	public void setInput(double input)
	{
		
	}
	
	private boolean isPaused = false;
	private long pauseOffset = 0;

	public void pause()
	{
		if(!isPaused)
		{
			pauseOffset = System.currentTimeMillis()-lastResetTime;
			isPaused = true;
		}
	}
	
	public void resume()
	{
		if(isPaused)
		{
			lastResetTime = System.currentTimeMillis()-pauseOffset;
			isPaused = false;
		}
	}
	
	public static void main(String... args) throws InterruptedException
    {
		GoalieHeadingCalculator g = new GoalieHeadingCalculator(80, 15);
		while(!Button.ESCAPE.isPressed())
		{
			LCD.clear();
			LCD.drawString("output: "+g.getOutput(), 0, 0);
			LCD.refresh();
			Thread.sleep(250);
			if(Button.ENTER.isPressed())
				g.reset();
			if(Button.LEFT.isPressed())
			{
				g.pause();
				while(Button.LEFT.isPressed());
			}
		}
		
    }
}

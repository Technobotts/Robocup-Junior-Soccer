package technobotts.soccer.util;

import technobotts.util.Timer;

public class GoalieTrajectoryFinder extends Timer
{
	private double xComp, yComp;
	private double yBias;
	private double factor;
	private double speed;
	
	private double maxBias = 1.5;

	public GoalieTrajectoryFinder(double degreesPerSecond, double speed)
	{
		factor = degreesPerSecond / 1000;
		this.speed = speed;
	}

	public float getHeading()
	{
		return (float) Math.toDegrees(Math.atan2(xComp, yComp));
	}

	public float getSpeed()
	{
		return (float) (Math.sqrt(xComp * xComp + yComp * yComp) * speed);
	}

	public void setInput(float ballAngle)
	{
		double ballAngleRad = Math.toRadians(ballAngle);
		xComp = Math.sin(ballAngleRad);
		yBias = getTime() * factor;
		if(atMaximum())
			yBias = maxBias;
		yComp = Math.cos(ballAngleRad) - yBias;
	}

	public boolean atMaximum()
	{
		return yBias >= maxBias;
	}
}

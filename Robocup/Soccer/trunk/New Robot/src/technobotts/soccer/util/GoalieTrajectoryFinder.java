package technobotts.soccer.util;

import technobotts.util.SimplePID;

public class GoalieTrajectoryFinder extends SimplePID
{
	private double xComp, yComp;
	private double yBias;
	private double targetDist;
	private double speed;

	private double maxBias = 500;

	public GoalieTrajectoryFinder(double speed, double targetDist, double p, double i, double d)
	{
		super(p, i, d);
		this.speed = speed;
		this.targetDist = targetDist;
	}

	public float getHeading()
	{
		return (float) Math.toDegrees(Math.atan2(xComp, yComp));
	}

	public float getSpeed()
	{
		return (float) Math.sqrt(xComp * xComp + yComp * yComp);
	}

	public void setInput(float ballAngle, float goalDist)
	{
		if(Float.isNaN(ballAngle))
		{
			xComp = 0;
			yComp = 0;
		}
		else
		{
			double ballAngleRad = Math.toRadians(ballAngle);
			xComp = Math.sin(ballAngleRad) * speed*2;
			yComp = Math.cos(ballAngleRad) * speed;
		}
		yBias = getOutput(targetDist - goalDist);
		if(atMaximum())
			yBias = maxBias;
		yComp += yBias;
	}

	public boolean atMaximum()
	{
		return yBias >= maxBias;
	}
}

package technobotts.soccer.util;

import technobotts.util.Timer;
import technobotts.util.SimplePID;

public class GoalieTrajectoryFinder extends SimplePID
{
	private double xComp, yComp;
	private double xBias, yBias;
	private double targetDist;
	private double speed;

	private double maxBias = 500;
	private Timer t = new Timer();

	public GoalieTrajectoryFinder(double speed, double targetDist, double p, double i, double d)
	{
		super(p, i, d);
		this.speed = speed;
		this.targetDist = targetDist;
		t.start();
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
		
		xBias = 0;//Math.sin(t.getTimeSeconds()*Math.toRadians(20))*20;
		xComp += xBias;
	}
	
	public void reset()
	{
		if(t!=null)
			t.restart();
		super.reset();
	}

	public boolean atMaximum()
	{
		return yBias >= maxBias;
	}
}

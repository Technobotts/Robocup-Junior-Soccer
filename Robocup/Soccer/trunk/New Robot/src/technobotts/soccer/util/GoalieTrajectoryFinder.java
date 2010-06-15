package technobotts.soccer.util;

public class GoalieTrajectoryFinder extends Timer
{
	private double xComp, yComp;
	private double factor;
	private double speed;

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
		yComp = Math.cos(ballAngleRad) - getTime() * factor;

	}
}

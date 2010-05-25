package technobotts.soccer.slave;


import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;

public class SoccerSlaveRobot
{
	private UltrasonicSensor US;
	private Motor            kickerMotor;

	private boolean          isKicking = false;

	public SoccerSlaveRobot(UltrasonicSensor US, Motor kickerMotor)
	{
		this.US = US;
		this.kickerMotor = kickerMotor;
		kickerMotor.smoothAcceleration(false);
		kickerMotor.setSpeed(1000);
		kickerMotor.regulateSpeed(false);

		this.US.off();
	}

	public boolean hasBall()
	{
		if(isKicking)
			return true;

		US.ping();

		int[] dists = new int[8];
		if(US.getDistances(dists) == 0)
			for(int dist : dists)
				if(dist < 10)
					return true;

		return false;
	}

	public void kick()
	{
		final int kickAngle = 110;

		kickerMotor.setPower(100);
		kickerMotor.forward();

		isKicking = true;

		long startTime = System.currentTimeMillis();
		while(kickerMotor.getTachoCount() < kickAngle && startTime + 1000 > System.currentTimeMillis())
			Thread.yield();

		kickerMotor.flt();
		try
		{
			Thread.sleep(100);
		}
		catch(InterruptedException e)
		{}
		kickerMotor.setPower(50);
		kickerMotor.rotateTo(0);

		isKicking = false;
	}
	
	public double getGoalAngle()
	{
		return Double.NaN;
	}

}

package technobotts.soccer.slave;

import technobotts.util.Timer;
import lejos.nxt.Motor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class SoccerSlaveRobot
{
	private UltrasonicSensor US;
	private Motor            kickerMotor;

	private boolean          isKicking = false;
	private TouchSensor      bumper;

	public SoccerSlaveRobot(UltrasonicSensor US, Motor kickerMotor, TouchSensor bumper)
	{
		this.US = US;
		this.kickerMotor = kickerMotor;
		this.bumper = bumper;
		kickerMotor.smoothAcceleration(false);
		kickerMotor.setSpeed(1000);
		kickerMotor.regulateSpeed(false);

		if(US!=null)
			this.US.off();
	}

	public boolean hasBall()
	{
		if(US == null)
			return false;
		
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
	
	KickerThread kThread = new KickerThread();

	public boolean kick()
	{
		if(isKicking || kickerMotor == null)
			return false;

		synchronized(kickerMotor)
		{
			isKicking = true;
			kickerMotor.notifyAll();
			// Tell it to start kicking
			while(!kThread.hasKicked())
			{
				try
				{
					kickerMotor.wait();
				}
				catch(InterruptedException e)
				{
					return false;
				}
			}
		}
		return true;

	}

	private class KickerThread extends Thread
	{
		final int kickAngle = 75;
		final int timeOut   = 5000;

		Timer     t         = new Timer();

		public KickerThread()
		{
			setDaemon(true);
			start();
		}

		private boolean hasKicked = false;

		public boolean hasKicked()
		{
			return hasKicked;
		}

		@Override
		public void run()
		{
			while(true)
			{
				hasKicked = false;

				synchronized(kickerMotor)
				{
					while(!isKicking)
					{
						try
						{
							kickerMotor.wait();
						}
						catch(InterruptedException e1)
						{
							continue;
						}
					}
					
					kickerMotor.setPower(100);
					kickerMotor.forward();
					// Set the kicker going

					t.restart();

					while(kickerMotor.getTachoCount() <= kickAngle / 2)
						Thread.yield();
					// Go half way

					hasKicked = true;
					kickerMotor.notifyAll();
					// Wake the main thread
				}

				while(kickerMotor.getTachoCount() <= kickAngle && t.getTime() < timeOut)
					Thread.yield();
				// Wait for it to reach full extension, or time out

				kickerMotor.flt();

				Delay.msDelay(100);
				kickerMotor.setPower(50);
				kickerMotor.rotateTo(0);
				// Move the kicker back
				isKicking = false;
			}
		}
	};

	/*public void kick()
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
	}*/

	public double getGoalAngle()
	{
		return Double.NaN;
	}

	public boolean bumperIsPressed()
	{
		if(bumper == null)
			return false;
		
		return bumper.isPressed();
	}

}

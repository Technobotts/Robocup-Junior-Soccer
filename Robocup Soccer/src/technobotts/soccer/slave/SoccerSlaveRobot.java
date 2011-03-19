package technobotts.soccer.slave;

import technobotts.util.Timer;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public abstract class SoccerSlaveRobot
{
	private UltrasonicSensor ballDetector;

	private Motor            kickerMotor;

	private boolean          isKicking = false;

	public SoccerSlaveRobot(UltrasonicSensor ballDetector, Motor kickerMotor)
	{
		this.ballDetector = ballDetector;
		this.kickerMotor = kickerMotor;
		kickerMotor.smoothAcceleration(false);
		kickerMotor.setSpeed(1000);
		kickerMotor.regulateSpeed(false);
		kickerMotor.setBrakePower(100);

		
		resetKicker();
		
		kickerMotor.resetTachoCount();
		if(ballDetector != null)
			this.ballDetector.off();
	}
	
	public void beepSequenceUp()
	{
		new Thread() {
			public void run()
			{
				Sound.beepSequenceUp();
			}
		}.start();
	}
	

	
	public void beepSequenceDown()
	{
		new Thread() {
			public void run()
			{
				Sound.beepSequence();
			}
		}.start();
	}

	public boolean hasBall()
	{
		if(ballDetector == null || isKicking)
			return false;

		ballDetector.ping();

		int[] dists = new int[8];
		if(ballDetector.getDistances(dists) == 0)
			for(int dist : dists)
				if(dist < 10)
					return true;

		return false;
	}
	
	KickerThread kThread = new KickerThread();

	private void resetKicker()
	{
		kickerMotor.backward();
		Delay.msDelay(500);
		kickerMotor.lock(100);
	}
	
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
					beepSequenceUp();
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
				resetKicker();
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

}

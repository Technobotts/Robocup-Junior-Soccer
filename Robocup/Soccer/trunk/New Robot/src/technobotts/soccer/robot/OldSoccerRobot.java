package technobotts.soccer.robot;

import java.io.IOException;

import technobotts.nxt.addon.IRSeekerV2;
import technobotts.nxt.addon.InvertedCompassSensor;
import technobotts.nxt.addon.IRSeekerV2.Mode;
import technobotts.robotics.navigation.SimpleOmniPilot;
import technobotts.util.AngleSmoother;
import technobotts.util.Timer;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RS485;
import lejos.nxt.remote.FixedRemoteMotor;
import lejos.nxt.remote.FixedRemoteNXT;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteNXT;
import lejos.util.Delay;

public class OldSoccerRobot extends SoccerRobot
{
	public static final SensorPort COMPASS_PORT = SensorPort.S1;
	public static final SensorPort IR_PORT      = SensorPort.S2;
	public static final SensorPort US_PORT      = SensorPort.S3;
	public static final Mode       IR_MODE      = Mode.AC_1200Hz;

	public static final String     SLAVE_NAME   = "Lewis B";

	private UltrasonicSensor       US;
	private RemoteMotor            kickerMotor;
	private TouchSensor            bumper;

	private RemoteNXT         slave;
	private volatile boolean       isKicking    = false;

	public OldSoccerRobot()
	{
		super(new InvertedCompassSensor(COMPASS_PORT), new IRSeekerV2(IR_PORT, IR_MODE),
		      new SimpleOmniPilot.OmniMotor(Motor.C, 53.1301f, 6.4f, 1, 9.6f, true),
		      new SimpleOmniPilot.OmniMotor(Motor.B, 180.0000f, 6.4f, 1, 8.8f),
		      new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f));

		US = new UltrasonicSensor(US_PORT);
		ballSmoother = new AngleSmoother(0);
	}

	@Override
	public boolean connectToSlave()
	{
		try
		{
			slave = new RemoteNXT(SLAVE_NAME, RS485.getConnector());
			kickerMotor = slave.A;
			bumper = new TouchSensor(slave.S1);
			return true;
		}
		catch(IOException ioe)
		{
			return false;
		}
	}

	@Override
	public boolean disconnect()
	{
		slave.stopProgram();
		slave.close();
		return true;
	}

	@Override
	public boolean hasBall()
	{
		if(isKicking)
			return false;

		US.ping();

		int[] dists = new int[8];
		if(US.getDistances(dists) == 0)
			for(int dist : dists)
				if(dist < 10)
					return true;

		return false;
	}

	KickerThread kThread = new KickerThread();

	@Override
	public boolean kick()
	{
		if(isKicking)
			return false;

		synchronized(kThread)
		{
			isKicking = true;
			kThread.notifyAll();
			// Tell it to start kicking
			while(!kThread.hasKicked())
			{
				try
				{
					kThread.wait();
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

		private boolean hasKicked;

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

				synchronized(this)
				{
					while(!isKicking)
					{
						try
						{
							this.wait();
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
					this.notifyAll();
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
			}
		}
	};

	@Override
	public double getGoalAngle()
	{
		return 0;
	}

	@Override
	public boolean bumperIsPressed()
	{
		return bumper.isPressed();
	}
}

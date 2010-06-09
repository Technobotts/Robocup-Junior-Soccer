package technobotts.soccer.robot;

import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.InvertedCompassSensor;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.nxt.comm.RS485;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteNXT;
import lejos.robotics.navigation.SimpleOmniPilot;

public class OldSoccerRobot extends SoccerRobot
{
	public static final SensorPort COMPASS_PORT = SensorPort.S1;
	public static final SensorPort IR_PORT      = SensorPort.S2;
	public static final SensorPort US_PORT      = SensorPort.S3;
	public static final Mode       IR_MODE      = Mode.AC_1200Hz;

	public static final String     SLAVE_NAME   = "Lewis B";

	private UltrasonicSensor       US;
	private RemoteMotor            kickerMotor;

	private RemoteNXT              slave;
	private boolean                isKicking    = false;

	public OldSoccerRobot()
	{
		super(new InvertedCompassSensor(COMPASS_PORT),
		      new IRSeekerV2(IR_PORT, IR_MODE),
		      new SimpleOmniPilot.OmniMotor(Motor.C,  53.1301f, 6.4f, 1, 9.6f, true),
		      new SimpleOmniPilot.OmniMotor(Motor.B, 180.0000f, 6.4f, 1, 8.8f),
		      new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f));

		US = new UltrasonicSensor(US_PORT);
	}

	@Override
	public boolean connectToSlave()
	{
		try
		{
			slave = new RemoteNXT(SLAVE_NAME, RS485.getConnector());
			kickerMotor = slave.A;
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
			return true;

		US.ping();

		int[] dists = new int[8];
		if(US.getDistances(dists) == 0)
			for(int dist : dists)
				if(dist < 10)
					return true;

		return false;
	}

	@Override
	public boolean kick()
	{
		try
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
			return true;
		}
		catch(NullPointerException e)
		{
			// FIXME Horrible hack to fix PrintStream Issue
			return false;
		}
	}

	@Override
	public double getGoalAngle()
	{
		return 0;
	}
}

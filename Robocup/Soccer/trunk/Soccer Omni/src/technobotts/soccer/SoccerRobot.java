package technobotts.soccer;

import java.io.IOException;

import lejos.nxt.*;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassSensor;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.comm.RS485;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteNXT;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.robotics.navigation.SimpleOmniPilot;

public class SoccerRobot
{
	public final OmniCompassPilot pilot;
	public final CompassSensor    compass;
	public final IRSeekerV2       IR;
	public final UltrasonicSensor US;
	protected RemoteMotor         kickerMotor;

	private static RemoteNXT connectToKickerNXT()
	{
		try
		{
			LCD.clear();
			LCD.drawString("Connecting...", 0, 0);
			RemoteNXT nxt = new RemoteNXT("NXT", RS485.getConnector());
			LCD.clear();
			LCD.drawString("Success!", 0, 1);
			return nxt;
		}
		catch(IOException ioe)
		{
			LCD.clear();
			LCD.drawString("Failure", 0, 0);
			return null;
		}
		finally
		{
			try
			{
				Thread.sleep(2000);
			}
			catch(InterruptedException e)
			{}
		}
	}

	public SoccerRobot()
	{
		RemoteNXT nxt = connectToKickerNXT();
		if(nxt == null)
			throw new IllegalStateException();
		kickerMotor = nxt.A;
		kickerMotor.smoothAcceleration(false);
		kickerMotor.regulateSpeed(false);
		
		US = new UltrasonicSensor(SensorPort.S3);
		compass = new CompassSensor(SensorPort.S1);
		IR = new IRSeekerV2(SensorPort.S2, IRSeekerV2.Mode.DC);

		pilot = new OmniCompassPilot(compass,
		                             new SimpleOmniPilot.OmniMotor(Motor.A, 60, 6.4f, 1, 11, true),
		                             new SimpleOmniPilot.OmniMotor(Motor.B, 180, 6.4f, 1, 11),
		                             new SimpleOmniPilot.OmniMotor(Motor.C, 300, 6.4f, 1, 11)
		        );
	}

	private boolean isKicking = false;

	public boolean hasBall()
	{
		if(isKicking)
			return true;

		int[] dists = new int[8];
		US.ping();
		
		if(US.getDistances(dists) == 0)
			for(int dist : dists)
				if(dist < 10)
					return true;
		
		return false;

	}

	public void kick()
	{
		final int kickAngle = 60;

		kickerMotor.setPower(100);
		kickerMotor.forward();
		isKicking = true;
		while(kickerMotor.getTachoCount() < kickAngle)
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

}

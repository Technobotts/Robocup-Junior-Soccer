package technobotts.soccer.robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import technobotts.nxt.addon.IRSeekerV2;
import technobotts.nxt.addon.InvertedCompassSensor;
import technobotts.robotics.navigation.SimpleOmniPilot;
import technobotts.soccer.util.MessageType;
//import technobotts.util.AngleSmoother;

public class OldSoccerRobot extends SoccerRobot
{
	public static final SensorPort COMPASS_PORT = SensorPort.S1;
	public static final SensorPort IR_PORT      = SensorPort.S2;
	public static final SensorPort US_PORT      = SensorPort.S3;
	public static final  IRSeekerV2.Mode       IR_MODE      =  technobotts.nxt.addon.IRSeekerV2.Mode.DC;

	public static final String     SLAVE_NAME   = "Lewis B";

	private UltrasonicSensor       US;

	private boolean                isKicking    = false;

	public OldSoccerRobot()
	{
		super(new InvertedCompassSensor(COMPASS_PORT), new IRSeekerV2(IR_PORT, IR_MODE),
		      new SimpleOmniPilot.OmniMotor(Motor.C, 53.1301f, 6.4f, 1, 9.6f, true),
		      new SimpleOmniPilot.OmniMotor(Motor.B, 180.0000f, 6.4f, 1, 8.8f),
		      new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f));

		US = new UltrasonicSensor(US_PORT);
		//ballSmoother = new AngleSmoother(0);
	}

	private DataOutputStream dos;
	private DataInputStream  dis;
	
	public float getBallAngle()
	{
		return getBallDetector().getAngle();
		
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

	@Override
	public boolean connectToSlave()
	{
		slave = RS485.getConnector().connect(SLAVE_NAME, NXTConnection.PACKET);
		if(slave != null)
		{
			dis = slave.openDataInputStream();
			dos = slave.openDataOutputStream();
			return true;
		}
		else
			return false;
	}

	public boolean kick()
	{
		isKicking = true;
		try
		{
			dos.writeByte(MessageType.KICK.getValue());
			dos.flush();
			return dis.readBoolean();
		}
		catch(IOException e)
		{
			return false;
		}
		catch(NullPointerException e)
		{
			return false;
		}
		finally
		{
			isKicking = false;
		}
	}

	@Override
	public float getRearWallDist()
	{
		try
		{
			dos.writeByte(MessageType.GOAL_DIST.getValue());
			dos.flush();
			return dis.readFloat();
		}
		catch(IOException e)
		{
			return Float.NaN;
		}
		catch(NullPointerException e)
		{
			return Float.NaN;
		}
	}

	public boolean disconnect()
	{
		try
		{
			dos.writeByte(MessageType.SHUTDOWN.getValue());
			dos.flush();
			return true;
		}
		catch(IOException e)
		{
			return false;
		}
		catch(NullPointerException e)
		{
			return false;
		}
		finally
		{
			slave = null;
			dis = null;
			dos = null;
		}
	}

	@Override
	public double getGoalAngle()
	{
		return 0;
	}
}

package technobotts.soccer.robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;

import technobotts.nxt.addon.IRSeekerV2;
import technobotts.nxt.addon.InvertedCompassSensor;
import technobotts.nxt.addon.IRSeekerV2.Mode;
import technobotts.robotics.navigation.SimpleOmniPilot;
import technobotts.soccer.util.DualLSFinder;
import technobotts.soccer.util.MessageType;

public class CopyOfNewSoccerRobot extends SoccerRobot
{
	public static final SensorPort COMPASS_PORT  = SensorPort.S1;
	public static final SensorPort RIGHT_IR_PORT = SensorPort.S2;
	public static final SensorPort LEFT_IR_PORT  = SensorPort.S3;
	public static final Mode       IR_MODE       = Mode.DC;

	public static final String     SLAVE_NAME    = "John B";

	private DataOutputStream       dos;
	private DataInputStream        dis;

	public CopyOfNewSoccerRobot()
	{
		super(new InvertedCompassSensor(COMPASS_PORT),
		      new DualLSFinder(new IRSeekerV2(LEFT_IR_PORT, IR_MODE),
		                                53.1301f,
		                                new IRSeekerV2(RIGHT_IR_PORT, IR_MODE),
		                                53.1301f),
		      new SimpleOmniPilot.OmniMotor(Motor.C,  53.1301f, 6.4f, 1, 9.6f, true),
		      new SimpleOmniPilot.OmniMotor(Motor.B, 180.0000f, 6.4f, 1, 8.8f),
		      new SimpleOmniPilot.OmniMotor(Motor.A, 306.8699f, 6.4f, 1, 9.6f));
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
	}

	public double getGoalAngle()
	{
		try
		{
			dos.writeByte(MessageType.GOAL_POS.getValue());
			dos.flush();
			return dis.readDouble();
		}
		catch(IOException e)
		{
			return Double.NaN;
		}
		catch(NullPointerException e)
		{
			return Double.NaN;
		}
	}

	public boolean hasBall()
	{
		try
		{
			dos.writeByte(MessageType.US_PING.getValue());
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
	}

	/*
	@Override
    public boolean bumperIsPressed()
    {
		try
		{
			dos.writeByte(MessageType.BUMPER_CHECK.getValue());
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
    }*/
	
	public float distanceToGoal()
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
    public float getRearWallDist()
    {
	    return Float.NaN;
    }
}

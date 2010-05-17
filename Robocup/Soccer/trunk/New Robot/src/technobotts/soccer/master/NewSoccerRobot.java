package technobotts.soccer.master;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.*;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.InvertedCompassSensor;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RS485;
import lejos.robotics.navigation.SimpleOmniPilot;
import technobotts.comm.MessageType;

import technobotts.soccer.SoccerRobot;
import technobotts.soccer.util.DualLightSourceFinder;

public class NewSoccerRobot extends SoccerRobot
{
	public static final SensorPort COMPASS_PORT  = SensorPort.S1;
	public static final SensorPort RIGHT_IR_PORT  = SensorPort.S2;
	public static final SensorPort LEFT_IR_PORT = SensorPort.S3;
	public static final Mode       IR_MODE       = Mode.DC;

	private DataOutputStream       dos;
	private DataInputStream        dis;
	public NewSoccerRobot()
	{
		super(new InvertedCompassSensor(SensorPort.S1),
		      new DualLightSourceFinder(new IRSeekerV2(LEFT_IR_PORT, IR_MODE),
		                                53.1301f,
		                                new IRSeekerV2(RIGHT_IR_PORT, IR_MODE),
		                                53.1301f),
		      new SimpleOmniPilot.OmniMotor(Motor.A,
		                                    53.1301f,
		                                    6.4f,
		                                    1,
		                                    9.6f,
		                                    true),
  		      new SimpleOmniPilot.OmniMotor(Motor.B,
		                                    180,
		                                    6.4f,
		                                    1,
		                                    8.8f),
		      new SimpleOmniPilot.OmniMotor(Motor.C,
		                                    306.8699f,
		                                    6.4f,
		                                    1,
		                                    9.6f));
	}

	@Override
	public boolean connectTo(String slaveName)
	{
		slave = RS485.getConnector().connect(slaveName, NXTConnection.PACKET);
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

	public boolean disconnect()
	{
		try
		{
			dos.writeByte(MessageType.SHUTDOWN.getValue());
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
			slave = null;
			dis = null;
			dos = null;
		}
	}
}

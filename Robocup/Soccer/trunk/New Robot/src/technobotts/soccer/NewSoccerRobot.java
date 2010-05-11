package technobotts.soccer;

import technobotts.soccer.util.DualLightSourceFinder;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassSensor;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.addon.InvertedCompassSensor;
import lejos.nxt.addon.LightSourceFinder;
import lejos.nxt.addon.IRSeekerV2.Mode;
import lejos.robotics.DirectionFinder;
import lejos.robotics.navigation.OmniCompassPilot;
import lejos.robotics.navigation.SimpleOmniPilot;
import lejos.robotics.navigation.SimpleOmniPilot.OmniMotor;

public class NewSoccerRobot extends SoccerRobot
{
	public static final SensorPort COMPASS_PORT  = SensorPort.S1;
	public static final SensorPort LEFT_IR_PORT  = SensorPort.S2;
	public static final SensorPort RIGHT_IR_PORT = SensorPort.S3;
	public static final Mode       IR_MODE       = Mode.DC;

	public NewSoccerRobot()
	{
		super(new InvertedCompassSensor(SensorPort.S1),
		      
		      new DualLightSourceFinder(new IRSeekerV2(LEFT_IR_PORT,IR_MODE),
		                                53.1301f,
		                                new IRSeekerV2(RIGHT_IR_PORT,IR_MODE),
		                                53.1301f),
		                                
		      new SimpleOmniPilot.OmniMotor(Motor.C,
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
		                                    
		      new SimpleOmniPilot.OmniMotor(Motor.A,
		                                    306.8699f,
		                                    6.4f,
		                                    1,
		                                    9.6f)
		);
	}

	@Override
    public boolean hasBall()
    {
	    // TODO Auto-generated method stub
	    return false;
    }

	@Override
    public void kick()
    {
	    // TODO Auto-generated method stub
	    
    }
}

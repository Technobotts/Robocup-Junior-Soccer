package technobotts.rescue.tasks;

import technobotts.rescue.*;
import technobotts.rescue.RescueRobot.Motors.Controller;

public class Follower extends Task
{
	public final int	                   maxspeed	      = 600;
	public final int	                   minspeed	      = -400;

	private double[]	                   sensorsFactors	= {8, 4, 2, 1, 0, 0, 0, 0};

	private RescueRobot	robot;

	public Follower(RescueRobot robot)
	{
		this.robot = robot;
	}

	private int scalePower(double input, double maxinput)
	{
		return (int) (input * (maxspeed - minspeed) / maxinput + minspeed);
	}

	private int calcLeftSpeed(byte[] sensors)
	{
		double rawpower = 0;
		double maxrawpower = 0;
		for(int i = 0; i < 8; i++)
		{
			rawpower += sensors[7 - i] * sensorsFactors[i];
			maxrawpower += 100 * sensorsFactors[i];
		}
		return scalePower(rawpower, maxrawpower);
	}

	private int calcRightSpeed(byte[] sensors)
	{
		double rawpower = 0;
		double maxrawpower = 0;
		for(int i = 0; i < 8; i++)
		{
			rawpower += sensors[i] * sensorsFactors[i];
			maxrawpower += 100 * sensorsFactors[i];
		}
		return scalePower(rawpower, maxrawpower);
	}

	public void execute()
	{
		byte[] LLSensorVals = robot.lineSensor.getSensors();
		// int[] LLSensorVals = {100,50,10,0,0,0,0,0};

		int leftSpeed = calcLeftSpeed(LLSensorVals);
		int rightSpeed = calcRightSpeed(LLSensorVals);

		robot.motors.getMotors(Controller.FOLLOW);
		robot.motors.setLeftSpeed(leftSpeed);
		robot.motors.setRightSpeed(rightSpeed);
		robot.motors.releaseMotors(Controller.FOLLOW);
	}
}

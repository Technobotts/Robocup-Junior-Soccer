package technobotts.rescue.tasks;

import technobotts.rescue.RescueRobot;
import technobotts.rescue.RescueTask;

public class Follower extends RescueTask
{
	private int     maxspeed = 400;
	private int     minspeed = -400;
	static double[] sensorFactors = {200, 50, 2, 1, 0, 0, 0, 0};

	int scalePower(double input, double maxinput)
	{
		return (int) Math.round(input * (maxspeed - minspeed) / maxinput + minspeed);
	}

	int calcLeftSpeed(int[] sensors)
	{
		double rawpower = 0;
		double maxrawpower = 0;
		for(int i = 0; i < 8; i++)
		{
			rawpower += sensors[7 - i] * sensorFactors[i];
			maxrawpower += 100 * sensorFactors[i];
		}
		return scalePower(rawpower, maxrawpower);
	}

	int calcRightSpeed(int[] sensors)
	{
		double rawpower = 0;
		double maxrawpower = 0;
		for(int i = 0; i < 8; i++)
		{
			rawpower += sensors[i] * sensorFactors[i];
			maxrawpower += 100 * sensorFactors[i];
		}
		return scalePower(rawpower, maxrawpower);
	}

	public Follower(RescueRobot robot)
	{
		super(robot);
		setName("Follower");
	}

	@Override
	public void run()
	{
		isRunning = true;
		while(isRunning)
		{
			synchronized(_robot.pilot)
			{
				if(_robot.loggerT.lineIsLost())
				{
					_robot.pilot.setMoveSpeed(100);
					_robot.pilot.forward();
				}
				else
				{
    				int[] sensors = _robot.lineSensor.getSensors();
    				if(sensors != null)
    				{
    					int leftspeed = calcLeftSpeed(sensors);
    					int rightspeed = calcRightSpeed(sensors);
    					_robot.pilot.setLeftSpeed(leftspeed);
    					_robot.pilot.setRightSpeed(rightspeed);
    				}
				}
			}
			yield();
		}
	}
}

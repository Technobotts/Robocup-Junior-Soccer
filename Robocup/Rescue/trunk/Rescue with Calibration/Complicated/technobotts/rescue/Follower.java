package technobotts.rescue;

public class Follower extends RescueTask
{
	private int     maxspeed = 400;
	private int     minspeed = -200;
	static double[] sensorsFactors = {216, 36, 6, 1, 0, 0, 0, 0};

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
			rawpower += sensors[7 - i] * sensorsFactors[i];
			maxrawpower += 100 * sensorsFactors[i];
		}
		return scalePower(rawpower, maxrawpower);
	}

	int calcRightSpeed(int[] sensors)
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

	public Follower(RescueRobot robot)
	{
		super(robot);
	}

	@Override
	public void run()
	{
		isRunning = true;
		while(isRunning)
		{
			synchronized(_robot.motors)
			{
				System.out.println("Follower has Motors");
				int[] sensors = _robot.lineSensor.getSensors();
				if(sensors != null)
				{
					int leftspeed = calcLeftSpeed(sensors);
					int rightspeed = calcRightSpeed(sensors);
					System.out.println(leftspeed +","+rightspeed );
					_robot.motors.setLeftSpeed(leftspeed);
					_robot.motors.setRightSpeed(rightspeed);
				}
			}
			yield();
		}
	}
}

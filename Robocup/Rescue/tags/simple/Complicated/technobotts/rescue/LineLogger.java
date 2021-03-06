package technobotts.rescue;

import lejos.nxt.SensorPortListener;
import lejos.nxt.Sound;

public class LineLogger extends RescueTask
{
	public final int dist = 35;
	private int      lastLeftMotor;
	private int      lastRightMotor;

	public LineLogger(RescueRobot robot)
	{
		super(robot);
		// TODO Auto-generated constructor stub
	}

	private boolean distGone()
	{
		float leftDist = (_robot.motors.getLeftTacho() - lastLeftMotor) / _robot.pilot.getLeftDegPerDistance();
		float rightDist = (_robot.motors.getRightTacho() - lastRightMotor) / _robot.pilot.getRightDegPerDistance();
		return leftDist > 35 && rightDist > 35;
	}

	public void lineLost()
	{
		synchronized(_robot.motors)
		{
			_robot.motors.stop();
			
			if(_robot.doLineSearch()) return;
			
			for(int i = 0; i<5; i++)
				Sound.buzz();
		}
	}

	@Override
	public void run()
	{
		isRunning = true;
		while(isRunning)
		{
			while(_robot.hasLine())
				yield();
			
			lastLeftMotor = _robot.motors.getLeftTacho();
			lastRightMotor = _robot.motors.getRightTacho();
			
			while(!_robot.hasLine())
			{
				if(distGone())
				{
					lineLost();
					break;
				}
				yield();
			}

		}
	}

}

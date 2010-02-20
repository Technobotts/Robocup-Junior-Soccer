package technobotts.rescue;

import lejos.nxt.Button;
import lejos.nxt.Sound;

public class LineLogger extends RescueTask
{
	public final int dist = 35;
	private int      lastLeftMotor = 0;
	private int      lastRightMotor = 0;

	public LineLogger(RescueRobot robot)
	{
		super(robot);
		setDaemon(true);
	}

	private boolean _lineIsLost = false;
	
	
	public boolean lineIsLost()
	{
		return _lineIsLost;
	}
	
	public float distSinceLastLine()
	{
		float leftDist = (_robot.pilot.getLeft().getTachoCount() - lastLeftMotor) / _robot.pilot.getLeftDegPerDistance();
		float rightDist = (_robot.pilot.getRight().getTachoCount() - lastRightMotor) / _robot.pilot.getRightDegPerDistance();
		return Math.min(leftDist, rightDist);
	}
	
	private boolean _lineIsLost()
	{
		return distSinceLastLine() > dist;
	}

	public void LineLost()
	{
		synchronized(_robot.pilot)
		{
			_robot.pilot.stop();
			
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
			_lineIsLost = false;
			while(_robot.hasLine())
			{
    			lastLeftMotor = _robot.pilot.getLeft().getTachoCount();
    			lastRightMotor = _robot.pilot.getRight().getTachoCount();
				yield();
			}
			while(!_robot.hasLine())
			{
				if(!_lineIsLost && _lineIsLost())
				{
					_lineIsLost = true;
					Sound.beep();
				}
				yield();
			}
			yield();

		}
	}

}

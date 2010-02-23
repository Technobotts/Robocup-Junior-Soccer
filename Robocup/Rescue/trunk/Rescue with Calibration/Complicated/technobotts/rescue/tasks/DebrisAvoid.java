package technobotts.rescue.tasks;

import technobotts.rescue.RescueRobot;
import technobotts.rescue.RescueTask;
import lejos.nxt.SensorPort;
import lejos.nxt.SensorPortListener;
import lejos.nxt.Sound;

public class DebrisAvoid extends RescueTask
{
	private long lastRight = 0;
	private long lastLeft = 0;
	private long tolerance=  200;

	public DebrisAvoid(RescueRobot robot)
    {
	    super(robot);
	    setName("Debris");
	    SensorPort.S3.addSensorPortListener(left);
	    SensorPort.S4.addSensorPortListener(right);
    }
	SensorPortListener left = new SensorPortListener() {
		public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue)
		{
			if(_robot.leftBumper.isPressed())
				lastLeft = System.currentTimeMillis();
		}
	};
	SensorPortListener right = new SensorPortListener() {
		public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue)
		{
			if(_robot.rightBumper.isPressed())
				lastRight = System.currentTimeMillis();
		}
	};

	@Override
	public void run()
	{
		isRunning = true;
		while(isRunning)
		{
			try
            {
	            Thread.sleep(tolerance);
            }
            catch(InterruptedException e)
            {
            	continue;
            }
			synchronized(_robot.pilot)
			{
    			boolean left = false, right = false;
    			if(lastRight > System.currentTimeMillis()-tolerance)
    			{
    				right = true;
    			}
    			if(lastLeft > System.currentTimeMillis()-tolerance)
    			{
    				left = true;
    			}
    			
    			if(right && left)
    			{
    				if(_robot.loggerT.distSinceLastLine() > 15)
    				{
    					_robot.pilot.travel(-10);
        				_robot.pilot.rotate(90);
    				}
    				else
    				{
    					_robot.pilot.arc(15);
        				_robot.pilot.rotate(90);
    				}
					_robot.twitchT.cancel();
    			}
    			else if(right)
    			{
    				_robot.pilot.rotate(-90);
					_robot.twitchT.cancel();
    			}
    			else if(left)
    			{
    				_robot.pilot.rotate(90);
					_robot.twitchT.cancel();
    			}
			}
		}
	}

}

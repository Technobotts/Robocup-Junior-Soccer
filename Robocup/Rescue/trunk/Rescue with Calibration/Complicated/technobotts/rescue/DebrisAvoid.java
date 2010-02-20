package technobotts.rescue;

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
    				if(true /*WALL*/)
    				{
    					_robot.pilot.travel(-10);
        				_robot.pilot.rotate(-90);
    				}
    				else if(false /*Debris*/)
    				{
    					
    				}
    			}
    			else if(right)
    			{
    				_robot.pilot.rotate(-90);
    			}
    			else if(left)
    			{
    				_robot.pilot.rotate(90);
    			}
			}
		}
	}

}

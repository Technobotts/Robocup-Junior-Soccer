package technobotts.rescue;

import technobotts.rescue.RescueTask;

public class VictimFinder extends RescueTask
{
	private final int victimPause = 2500;
	private long      lastVictimTime;

	public VictimFinder(RescueRobot robot)
	{
		super(robot);
		setName("Victim");
		lastVictimTime = System.currentTimeMillis() - victimPause;
	}

	private long timeSinceLastVictim()
	{
		return System.currentTimeMillis() - lastVictimTime;
	}

	@Override
	public void run()
	{
		isRunning = true;
		while(isRunning)
		{
			RawColor color = _robot.colors.getSensorColor(_robot.colorSensor);
			if((color == _robot.colors.silver || color == _robot.colors.green) && timeSinceLastVictim() > victimPause)
			{
				synchronized(_robot.pilot)
				{
					_robot.twitchT.pause();
					
					System.out.println("Victim has Motors");
					_robot.pilot.stop();
					_robot.showVictimFound();
					lastVictimTime = System.currentTimeMillis();
					if(!_robot.loggerT.lineIsLost())
    				{
    					_robot.pilot.travel(10);
    
    					_robot.doLineSearch();
					}
					_robot.twitchT.resume();
				}
			}
			yield();
		}
	}
}

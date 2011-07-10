package technobotts.soccer;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.util.Delay;
import technobotts.soccer.robot.SoccerRobot;

public abstract class Strategy
{
	protected SoccerRobot robot;
	private boolean isRunning = false;
	
	protected Strategy(SoccerRobot robot) {
		this.robot = robot;
	}

	@Deprecated
	protected Strategy() {
		
	}
	
	@Deprecated
	protected void executeWithConnected(SoccerRobot robot) throws InterruptedException { };
	public void executeWith(SoccerRobot robot)
	{
		if(!robot.connectToSlave())
			Sound.buzz();
		
		try {
			executeWithConnected(robot);
		} catch (InterruptedException e) {
		}
     
		if(!robot.disconnect())
			Sound.buzz();
	}
	protected void activated() {}
	protected void activePeriodic() { };
	protected void disabled() { robot.stop(); };
	protected void disabledPeriodic() { };
	
	public void execute() {
		if(!robot.connectToSlave()) Sound.buzz();
		
		while(Button.ENTER.isPressed()) ;
		
		while(!Button.ESCAPE.isPressed())
		{
			if(Button.ENTER.isPressed())
			{
				isRunning = !isRunning;
				while(Button.readButtons() != 0) Thread.yield();

				if(isRunning) activated();
				else disabled();
			}
			if(isRunning)
				activePeriodic();
			else
				disabledPeriodic();
			
			Thread.yield();
		}
     
		if(!robot.disconnect()) Sound.buzz();		
	}
}

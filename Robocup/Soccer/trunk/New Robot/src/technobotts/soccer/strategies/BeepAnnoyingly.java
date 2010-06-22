package technobotts.soccer.strategies;

import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.util.Delay;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.OldSoccerRobot;
import technobotts.soccer.robot.SoccerRobot;

public class BeepAnnoyingly extends Strategy
{
	public static void main(String[] args)
	{
		Strategy s = new BeepAnnoyingly();
		s.executeWith(new OldSoccerRobot());
	}
	
	public static float generateTone(float ratio)
	{
		ratio = (float) (Math.floor(ratio*24)/12);
		return (float) (Math.pow(2,ratio)*220);
	}

	@Override
	protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
	{
		while(true)
		{
			float distance = robot.getRearWallDist();
			LCD.clear();
			LCD.drawString("["+distance + "]        ", 0, 0);
			LCD.refresh();
			
			Sound.playTone((int) generateTone(distance/200f), 100);
			Delay.msDelay(100);
		}
	}
}

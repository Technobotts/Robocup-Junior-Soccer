package technobotts.soccer;

import technobotts.soccer.robot.*;
import technobotts.soccer.strategies.*;

public class OldMasterMain
{
	public static void main(String[] args) throws InterrupteddException
	{
		Strategy s = new GoalieV2();
		s.executeWith(new OldSoccerRobot());
	}
}
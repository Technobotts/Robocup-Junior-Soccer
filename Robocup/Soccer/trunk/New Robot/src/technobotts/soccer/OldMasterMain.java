package technobotts.soccer;

import technobotts.soccer.robot.*;
import technobotts.soccer.strategies.*;

public class OldMasterMain
{
	public static void main(String[] args)
	{
		Strategy s = new Goalie();
		s.executeWith(new OldSoccerRobot());
	}
}
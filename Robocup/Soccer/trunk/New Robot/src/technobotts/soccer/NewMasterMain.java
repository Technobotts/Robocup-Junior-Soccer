package technobotts.soccer;

import technobotts.soccer.robot.*;
import technobotts.soccer.strategies.*;

public class NewMasterMain
{
	public static void main(String[] args)
	{
		Strategy s = new CameraNorthFacing();
		s.executeWith(new NewSoccerRobot());
	}
}

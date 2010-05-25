package technobotts.soccer;

import technobotts.soccer.robot.*;
import technobotts.soccer.strategies.*;

public class NewMasterMain
{
	public static void main(String[] args)
	{
		Strategy<SoccerRobot> s = new NorthFacing(new NewSoccerRobot());
		s.run();
	}
}

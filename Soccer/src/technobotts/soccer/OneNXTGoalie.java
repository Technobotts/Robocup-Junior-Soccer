package technobotts.soccer;

import technobotts.soccer.robot.onenxt.GoalieRobot;
import technobotts.soccer.strategies.GoalieV2;

public class OneNXTGoalie {
	public static void main(String[] args) throws InterruptedException
	{
		Strategy s = new GoalieV2(new GoalieRobot());
		s.execute();
	}
}

package technobotts.soccer;

import technobotts.soccer.robot.onenxt.ForwardRobot;
import technobotts.soccer.strategies.CameraNorthFacing;

public class OneNXTAttacker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Strategy s = new CameraNorthFacing();
		//s.executeWith(new ForwardRobot());
		
		new CameraNorthFacing(new ForwardRobot()).execute();
	}

}

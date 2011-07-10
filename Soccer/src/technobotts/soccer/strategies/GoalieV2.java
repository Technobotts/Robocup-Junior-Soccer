package technobotts.soccer.strategies;

import lejos.nxt.LCD;
import technobotts.soccer.Strategy;
import technobotts.soccer.robot.SoccerRobot;
import technobotts.soccer.robot.onenxt.GoalieRobot;
import technobotts.soccer.util.GoalieTrajectoryFinder;

public class GoalieV2 extends Strategy
{
	public GoalieV2(SoccerRobot robot) {
		super(robot);
	}

	public static void main(String[] args) throws InterruptedException
	{
		Strategy s = new GoalieV2(new GoalieRobot());
		s.execute();
	}
	boolean isRunning = false;

	public float           targetDist        = 17.5f;
	// Distance to maintain from goal

	GoalieTrajectoryFinder headingCalculator = new GoalieTrajectoryFinder(robot.MAX_SPEED, targetDist, 5, 0.01, 0);
	private boolean        ballLost          = false;
	private boolean        backHome          = true;
	private long lastBallTime = System.currentTimeMillis();
	
	protected boolean randomBool() {
		return Math.random() > 0.5;
	}
	
	protected void activated() {
		ballLost = false;
		backHome = true;
		headingCalculator.reset();
	}
	
	protected void activePeriodic() {
		float ballAngle = robot.getBallAngle();
		float wallDist = robot.getRearWallDist();
		headingCalculator.setInput(ballAngle, wallDist);

		// Robot is near to wall again
		if(backHome)
		{
			robot.setMoveSpeed(headingCalculator.getSpeed());
			robot.travel(headingCalculator.getHeading());
		}

		// Robot is too near to wall
		if(wallDist < targetDist / 2)
			headingCalculator.reset();

		if(robot.hasBall())
		{
			robot.setMoveSpeed(robot.MAX_SPEED);
			robot.travel(0);

			robot.kick();
		}

		// Robot has just got near the wall
		if(!backHome && wallDist < targetDist)
		{
			backHome = true;
			robot.stop();
		}

		// Keep track of whether the ball has been lost
		long time = System.currentTimeMillis();
		
		//Ball is visible
		if(!Float.isNaN(ballAngle))
		{
			lastBallTime = time;
			ballLost = false;
		}
		//ball just lost, or 2 seconds since last seen
		else if(!ballLost || lastBallTime + 2000 < time)
		{
			robot.setMoveSpeed(robot.MAX_SPEED);
			robot.travel(randomBool() ? -150 : 150);
			ballLost = true;
			backHome = false;
			lastBallTime = time;
		}

		LCD.drawString(wallDist + "f:" + headingCalculator.getOutput() + "            ", 0, 0);
	}
}

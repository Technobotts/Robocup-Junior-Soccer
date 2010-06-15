package technobotts.soccer.strategies;

import technobotts.soccer.Strategy;
import technobotts.soccer.robot.SoccerRobot;
import technobotts.soccer.util.GoalieV2HeadingCalculator;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class GoalieBehaviour extends Strategy
{
	GoalieV2HeadingCalculator headingCalculator = new GoalieV2HeadingCalculator(1, 300);
	boolean                   ballIsLost        = false;

	private class GotBallBehavior implements Behavior
	{
		SoccerRobot robot;

		public GotBallBehavior(SoccerRobot robot)
		{
			this.robot = robot;
		}

		public boolean takeControl()
		{
			return robot.hasBall();
		}

		public void suppress()
		{}

		public void action()
		{
			Sound.beepSequence();
			robot.setMoveSpeed(300);
			robot.travel(0);
			Delay.msDelay(250);
			robot.kick();
			robot.travel(180);
		}
	}

	private class NearGoalBehavior implements Behavior
	{
		SoccerRobot robot;

		public NearGoalBehavior(SoccerRobot robot)
		{
			this.robot = robot;
		}

		public boolean takeControl()
		{
			return robot.bumperIsPressed();
		}

		public void suppress()
		{}

		public void action()
		{
			Sound.beepSequenceUp();
			headingCalculator.restart();
			robot.travel(0);
		}
	}

	private class DefendGoalBehavior implements Behavior
	{
		SoccerRobot robot;
		boolean     isRunning;

		public DefendGoalBehavior(SoccerRobot robot)
		{
			this.robot = robot;
		}

		public boolean takeControl()
		{
			return !Float.isNaN(robot.getBallAngle());
		}

		public void suppress()
		{
			isRunning = false;
		}

		public void action()
		{
			Sound.beep();
			headingCalculator.resume();
			isRunning = true;

			while(isRunning)
			{
				headingCalculator.setInput(robot.getBallAngle());

				robot.setMoveSpeed(headingCalculator.getSpeed());
				robot.travel(headingCalculator.getHeading());
			}
		}
	}

	private class NoBallBehavior implements Behavior
	{
		SoccerRobot robot;

		public NoBallBehavior(SoccerRobot robot)
		{
			this.robot = robot;
		}

		public boolean takeControl()
		{
			return Float.isNaN(robot.getBallAngle());
		}

		public void suppress()
		{

		}

		public void action()
		{
			Sound.twoBeeps();
			robot.setMoveSpeed(300);
			robot.travel(Math.random() > 0.5 ? -135 : 135);
			headingCalculator.pause();
		}
	}

	@Override
	protected void executeWithConnected(SoccerRobot robot) throws InterruptedException
	{

		Behavior[] behaviors = new Behavior[] {
		    new GotBallBehavior(robot), new NearGoalBehavior(robot), new DefendGoalBehavior(robot),
		    new NoBallBehavior(robot),
		};

		Arbitrator a = new Arbitrator(behaviors, true);

		a.start();
	}
}

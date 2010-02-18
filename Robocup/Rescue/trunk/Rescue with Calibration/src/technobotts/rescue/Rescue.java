package technobotts.rescue;

import lejos.nxt.BasicMotor;
import lejos.nxt.BasicMotorPort;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.ColorSensor;
import lejos.nxt.addon.Lamp;
import lejos.nxt.addon.LineLeader;
import lejos.nxt.addon.RCXMotor;
import technobotts.rescue.*;
import technobotts.rescue.RescueRobot.Motors.Controller;
import technobotts.rescue.tasks.*;

public class Rescue
{
	public static void main(String[] args) throws Exception
	{
		LineLeader ll = new LineLeader(SensorPort.S2);
		ColorSensor cs = new ColorSensor(SensorPort.S1);
		Lamp l = new Lamp(MotorPort.A);
		RescueRobot robot = new RescueRobot(Motor.C, Motor.B, ll, cs, l);

		Follower follower = new Follower(robot);

		robot.lamp.pulse(2000,4);
		follower.start();

		while(true)
		{
			Sound.beep();

			Thread.sleep(5000);

			// follower.stop();
			robot.motors.getMotors(Controller.MAIN);
				robot.motors.stop();
				Thread.sleep(1000);
			robot.motors.releaseMotors(Controller.MAIN);
		}
		/*
		 * //Start the basic tasks (follow, victimsearch, debrisCheck)
		 * //wait for
		 * while(true) { if(debrisSearch.signal) { follow.stop();
		 * debrisSearch.stop();
		 * debrisAvoid.start(); lineSearch.start();
		 * robot.setState(State.DEBRISAVOID);
		 * } else if(victimSearch.signal) { follow.stop(); victimSearch.stop();
		 * debrisSearch.stop();
		 * signalVictim.start();
		 * robot.setState(State.VICTIMSIGNAL);
		 * //Upon completion: // follow.start(); // debrisSearch.start(); //
		 * //wait 1 second // victimSearch.start(); }
		 * }
		 */

	}

}

package technobotts.soccer.strategies;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import technobotts.soccer.NewSoccerRobot;
import technobotts.soccer.AbstractSoccerRobot;
import technobotts.soccer.SoccerRobot;
import technobotts.soccer.Strategy;
import technobotts.soccer.slave.SoccerSlave;

public class KickBall extends Strategy<SoccerRobot>
{

	public KickBall(SoccerRobot robot)
    {
	    super(robot);
	    // TODO Auto-generated constructor stub
    }

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException
	{
		Strategy<SoccerRobot> s = new KickBall(new NewSoccerRobot());
		s.run();
	}

	@Override
    public void run()
    {

		boolean success = robot.connectTo("NXT");
		if(!success)
			Sound.beepSequenceUp();

		while(!Button.ESCAPE.isPressed())
		{
    		if(robot.hasBall())
    		{
    			robot.kick();
    		}
    		try
            {
	            Thread.sleep(250);
            }
            catch(InterruptedException e)
            {}
		}
		robot.disconnect();
    }

}

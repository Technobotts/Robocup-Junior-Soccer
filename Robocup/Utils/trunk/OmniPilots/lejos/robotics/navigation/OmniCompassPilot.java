package lejos.robotics.navigation;

import lejos.nxt.LCD;
import lejos.robotics.DirectionFinder;
import lejos.util.Delay;
import java.util.PID.SimplePID;

public class OmniCompassPilot extends SimpleOmniPilot
{
	protected DirectionFinder    _compass;

	protected float              _targetFacing    = 0;
	protected float              _targetDirection = 0;
	protected boolean            _travel          = false;
	protected boolean            _regulate        = true;
	protected DirectionRegulator _regulator       = new DirectionRegulator();

	public OmniCompassPilot(DirectionFinder compass, OmniMotor... motors)
	{
		super(motors);
		_compass = compass;
		_compass.resetCartesianZero();
		_regulator.setDaemon(true);
		_regulator.start();
	}

	@Override
	public void rotate(float angle, boolean immediateReturn)
	{
		synchronized(_regulator)
		{
			_travel = false;
			_targetFacing = angle;
			_regulator.reset();
		}
		while(!(Math.abs(_regulator.getError()) < 3) && !immediateReturn)
			Thread.yield();
	}

	@Override
	public void stop()
	{
		synchronized(_regulator)
		{
			_travel = false;
			_regulator.reset();
		}
	}

	@Override
	public void travel(float heading)
	{
		if(Float.isNaN(heading))
			stop();
		else
			synchronized(_regulator)
			{
				_travel = true;
				_targetDirection = heading;
				_regulator.reset();
			}

	}

	protected float[] getMotorTravelVelocities(float heading)
	{
		return super.getMotorTravelVelocities(heading);
	}

	public void setDirectionFinder(DirectionFinder df)
	{
		_compass = df;
		_regulator.reset();
	}

	public void setRegulation(boolean on)
	{
		synchronized(_regulator)
		{
			_regulate = on;
			_regulator.notifyAll();
		}
	}

	private class DirectionRegulator extends Thread
	{
		private float[] getTravelSpeedsWithBias(float rotationBias, float error)
		{
			float[] travelSpeeds = getMotorTravelVelocities(_targetDirection + error);
			float[] rotationSpeeds = getMotorRotationVelocities(rotationBias);
			float[] speeds = new float[_motors.length];
			for(int i = 0; i < _motors.length; i++)
			{
				speeds[i] = travelSpeeds[i] + rotationSpeeds[i];
			}
			return speeds;
		}

		private SimplePID pid = new SimplePID(4, 0.0001, 0);

		private float getError()
		{
			float err = _compass.getDegreesCartesian() - _targetFacing;
			// Handles the wrap-around problem:
			while(err <= -180)
				err += 360;
			while(err > 180)
				err -= 360;
			return err;
		}

		public synchronized void reset()
		{
			pid.reset();
		}

		@Override
		public void run()
		{
			pid.start();
			while(true)
			{
				while(_regulate)
				{
					synchronized(this)
					{
						float error = getError();
						if(Float.isNaN(error))
							continue;
						float rotationBias = (float) pid.getOutput(error);
						LCD.drawString("Bias=" + rotationBias + "    ", 0, 0);
						LCD.drawString("Heading=" + _compass.getDegreesCartesian() + "    ", 0, 1);
						LCD.drawString("Target=" + _targetFacing + "    ", 0, 3);
						float[] speeds;

						if(_travel)
							speeds = getTravelSpeedsWithBias(rotationBias, error);
						else
							speeds = getMotorRotationVelocities(rotationBias);
						setMotorSpeeds(speeds);
					}
					Delay.msDelay(5);
				}
				while(!_regulate)
				{
    				try
                    {
    	                this.wait();
                    }
                    catch(InterruptedException e)
                    {}
				}
			}
		}

	}
}

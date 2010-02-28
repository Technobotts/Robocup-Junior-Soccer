package lejos.robotics.navigation;


import lejos.nxt.Battery;
import lejos.nxt.Motor;

public class SimpleOmniPilot
{
	public static class OmniMotor
	{
		protected final Motor _motor;
		protected final float _angle;
		protected final float _degPerRDist;
		protected final float _degPerRDeg;

		public OmniMotor(Motor motor, float angle, float wheelDiameter, float gearRatio,
		                 float distanceToCenter)
		{
			this(motor, angle, wheelDiameter, gearRatio, distanceToCenter, false);
		}

		/**
		 * @param motor The physical motor to be used
		 * @param angle The orientation of the motor in degrees relative to the robot's intended
		 *            forward direction, counting clockwise.
		 * @param wheelDiameter The large diameter of the omni-directional wheel
		 * @param gearRatio The number of rotations the wheel makes per rotation of the motor shaft
		 * @param distanceToCenter The distance from the center of the omni-directional wheel to the
		 *            intersection of the extension of the omni-directional robot's wheel axles
		 * @param counterClockwise Specifies whether turning the motor forwards results in a
		 *            counter clockwise rotation
		 */
		public OmniMotor(Motor motor, float angle, float wheelDiameter, float gearRatio,
		                 float distanceToCenter, boolean counterClockwise)
		{
			this._motor = motor;
			this._angle = angle;

			int polarity = counterClockwise ? -1 : 1;
			_degPerRDist = wheelDiameter * (float) Math.PI * gearRatio * polarity;
			_degPerRDeg = (2 * distanceToCenter) / (wheelDiameter * gearRatio) * polarity;
		}

		protected void setSpeed(float speed)
		{
			_motor.setSpeed(Math.round(Math.abs(speed)));
		}

		public void rotateSpeed(float rawSpeed)
		{
			if(rawSpeed == 0)
				_motor.stop();
			else
			{
				setSpeed(rawSpeed);
				if(rawSpeed > 0)
					_motor.forward();
				else
					_motor.backward();
			}
		}
		public void rotateAngle(float rawDistance, float rawSpeed)
		{
			setSpeed(rawSpeed);
			_motor.rotate(Math.round(rawDistance),true);
		}
		
		public float getComponent(float vectorAngle)
		{
			return (float) Math.sin(Math.toRadians(vectorAngle - _angle));
		}
		
		public float getMotorUnitsForTravel(float travelAngle, float travelUnits)
		{
			return travelUnits * _degPerRDist * getComponent(travelAngle);
		}
		
		public float getMotorUnitsForRotation(float rotationUnits)
		{
			return rotationUnits * _degPerRDeg;
		}

		public float getMaxSpeed()
		{
			return Battery.getVoltage() * 100;
		}

		public boolean isMoving()
		{
			return _motor.isMoving();
		}
	}

	protected final OmniMotor[] _motors;
	protected float             _moveSpeed = 100;
	protected float             _turnSpeed = 90;

	public SimpleOmniPilot(OmniMotor... motors)
	{
		_motors = motors;
	}

	protected float[] limitToMaxSpeeds(final float[] rawVelocities)
	{
		return limitToMaxSpeeds(rawVelocities, 1);
	}
	protected float[] raiseToMaxSpeeds(final float[] rawVelocities)
	{
		return limitToMaxSpeeds(rawVelocities, 0);
	}
	protected float[] limitToMaxSpeeds(final float[] rawVelocities, float maxProportion)
	{
		for(int i = 0; i < rawVelocities.length; i++)
		{
			float magnitude = Math.abs(rawVelocities[i]);
			float maxMotorMagnitude = _motors[i].getMaxSpeed();

			if(magnitude / maxMotorMagnitude > maxProportion)
			{
				maxProportion = magnitude / maxMotorMagnitude;
			}
		}
		
		if(maxProportion == 0)
			return rawVelocities;
		else
		{
    		float[] scaledVelocities = new float[rawVelocities.length];
    		for(int i = 0; i < rawVelocities.length; i++)
    		{
    			scaledVelocities[i] = rawVelocities[i] / maxProportion;
    		}
    		return scaledVelocities;
		}
	}
	
	protected void setMotorSpeeds(final float... speeds)
	{
		float[] scaled = limitToMaxSpeeds(speeds);
		for(int i = 0; i < _motors.length; i++)
		{
			_motors[i].rotateSpeed(scaled[i]);
		}
	}

	public float getMoveSpeed()
	{
		return _moveSpeed;
	}
	public void setMoveSpeed(float moveSpeed)
	{
		_moveSpeed = moveSpeed;
	}
	public float getTurnSpeed()
	{
		return _turnSpeed;
	}
	public void setTurnSpeed(float turnSpeed)
	{
		_turnSpeed = turnSpeed;
	}

	protected float[] getMotorTravelVelocities(float angle)
	{
		float[] v = new float[_motors.length];
		for(int i = 0; i < _motors.length; i++)
		{
			v[i] = _motors[i].getMotorUnitsForTravel(angle, _moveSpeed);
		}
		return v;
	}
	protected float[] getMotorTravelDistances(float angle, float distance)
	{
		float[] d = new float[_motors.length];
		for(int i = 0; i < _motors.length; i++)
		{
			d[i] = _motors[i].getMotorUnitsForTravel(angle, distance);
		}
		return d;
	}
	protected float[] getMotorRotationVelocities()
	{
		return getMotorRotationVelocities(_turnSpeed);
	}
	protected float[] getMotorRotationVelocities(float rotationSpeed)
	{
		float[] v = new float[_motors.length];
		for(int i = 0; i < _motors.length; i++)
		{
			v[i] = _motors[i].getMotorUnitsForRotation(rotationSpeed);
		}
		return v;
	}
	protected float[] getMotorRotationDistances(float angle)
	{
		float[] d = new float[_motors.length];
		for(int i = 0; i < _motors.length; i++)
		{
			d[i] = _motors[i].getMotorUnitsForRotation(angle);
		}
		return d;
	}

	public void travel(float angle)
	{
		float[] v = limitToMaxSpeeds(getMotorTravelVelocities(angle));

		for(int i = 0; i < _motors.length; i++)
		{
			_motors[i].rotateSpeed(v[i]);
		}
	}
	public void travel(float angle, float distance)
	{
		travel(angle, distance, false);
	}
	public void travel(float angle, float distance, boolean immediateReturn)
	{
		float[] v = limitToMaxSpeeds(getMotorTravelVelocities(angle));
		float[] d = getMotorTravelDistances(angle, distance);

		for(int i = 0; i < _motors.length; i++)
		{
			_motors[i].rotateAngle(d[i], v[i]);
		}

		if(!immediateReturn)
			while(isMoving())
				Thread.yield();
	}
	
	public void rotate()
	{
		float[] v = limitToMaxSpeeds(getMotorRotationVelocities());

		for(int i = 0; i < _motors.length; i++)
		{
			_motors[i].rotateSpeed(v[i]);
		}
	}
	public void rotate(float angle)
	{
		rotate(angle, false);
	}
	public void rotate(float angle, boolean immediateReturn)
	{
		float[] v = limitToMaxSpeeds(getMotorRotationVelocities());
		float[] d = getMotorRotationDistances(angle);

		for(int i = 0; i < _motors.length; i++)
		{
			_motors[i].rotateAngle(d[i], v[i]);
		}

		if(!immediateReturn)
			while(isMoving())
				Thread.yield();
	}

	public void stop()
	{
		setMotorSpeeds(0,0,0);
	}

	public boolean isMoving()
	{
		boolean moving = false;
		for(OmniMotor motor : _motors)
		{
			moving |= motor.isMoving();
		}
		return moving;
	}
}
/**
 * 
 */
package java.lang;

/**
 * @author Eric
 * 
 */
public class Fraction
{
	private int		numerator;
	private int		denominator;

	private boolean	negative;

	private int		maxDenominator;
	private int		maxNumerator;

	public Fraction(double input)
	{
		this(input, 255, 255);
	}

	public Fraction(int numerator, int denominator, int maxNumerator,
					int maxDenominator)
	{
		this.negative = Math.signum(denominator) != Math.signum(numerator);
		this.numerator = Math.abs(numerator);
		this.denominator = Math.abs(denominator);
		this.maxNumerator = Math.abs(maxNumerator);
		this.maxDenominator = Math.abs(maxDenominator);
	}

	public Fraction(double input, int maxNumerator, int maxDenominator)
	{
		this.setValue(input);
		this.maxNumerator = Math.abs(maxNumerator);
		this.maxDenominator = Math.abs(maxDenominator);
	}

	public int getNumerator()
	{
		return numerator;
	}

	public int getDenominator()
	{
		return denominator;
	}

	public boolean isNegative()
	{
		return negative;
	}

	public void setValue(double value)
	{
		if(value <= 0)
		{
			value = Math.abs(value);
			negative = true;
		}
		int p0 = 1, q0 = 0;
		int p1 = (int) Math.floor(value), q1 = 1;
		int p2, q2;

		double r = value - p1;
		double next_cf;
		while(true)
		{
			r = 1.0 / r;
			next_cf = Math.floor(r);
			p2 = (int) (next_cf * p1 + p0);
			q2 = (int) (next_cf * q1 + q0);

			// Limit the numerator and denominator
			if(p2 > maxNumerator || q2 > maxDenominator)
				break;

			// remember the last two fractions
			p0 = p1;
			p1 = p2;
			q0 = q1;
			q1 = q2;

			r -= next_cf;
		}

		value = (double) p1 / q1;
		if(value > maxNumerator)
		{
			p1 = 255;
			q1 = 1;
		}
		else if(value < 1.0 / maxDenominator)
		{
			p1 = 1;
			q1 = 255;
		}
		numerator = p1;
		denominator = q1;
	}

	public double getValue()
	{
		return getNumerator() / getDenominator();
	}

	@Override
	public boolean equals(Object aOther)
	{
		if(aOther instanceof Fraction)
		{
			Fraction other = (Fraction) aOther;
			return (other.getValue() == this.getValue());
		}
		else
			return false;
	}
}

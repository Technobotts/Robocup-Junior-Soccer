package lejos.util;

public class ColorHSL extends Color
{
	public ColorHSL(float hue, float sat, float lum)
	{
		HSLValues = new HSLHelper(hue,sat,lum);
	}
}

package lejos.util;

public abstract class Color
{
	public static Color	WHITE	= new ColorRGB(255, 255, 255);
	public static Color	BLACK	= new ColorRGB(0, 0, 0);
	public static Color	RED	    = new ColorRGB(255, 0, 0);
	public static Color	GREEN	= new ColorRGB(0, 255, 0);
	public static Color	BLUE	= new ColorRGB(0, 0, 255);
	public static Color	YELLOW	= new ColorRGB(255, 255, 0);
	public static Color	MAGENTA	= new ColorRGB(255, 0, 255);
	public static Color	CYAN	= new ColorRGB(0, 255, 255);

	public static final class RGBHelper
	{
		private final int	_r;
		private final int	_g;
		private final int	_b;

		public RGBHelper(int r, int g, int b)
		{
			_r = r & 0xFF;
			_g = g & 0xFF;
			_b = b & 0xFF;
		}

		public RGBHelper(float r, float g, float b)
		{
			this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5));
		}

		public int getR()
		{
			return _r;
		}

		public int getG()
		{
			return _g;
		}

		public int getB()
		{
			return _b;
		}

		public int getHex()
		{
			return (_r & 0xFF) << 16 | (_g & 0xFF) << 8 | (_b & 0xFF);
		}

		@Override
		public boolean equals(Object aOther)
		{
			if(this == aOther)
				return true;
			if(aOther == null || aOther.getClass() != this.getClass())
				return false;

			RGBHelper that = (RGBHelper) aOther;

			return (this.getR() == that.getR() &&
			        this.getG() == that.getG() && this.getB() == that.getB());
		}
		
		public int distanceTo(RGBHelper that)
		{
			int deltaR = this.getR() - that.getR();
			int deltaG = this.getG() - that.getG();
			int deltaB = this.getB() - that.getB();
			
			return deltaR + deltaB + deltaG;
		}
	}

	public final static class HSLHelper
	{
		private final float	_hue;
		private final float	_sat;
		private final float	_lum;

		public HSLHelper(float hue, float sat, float lum)
		{
			_lum = lum > 1 ? 1 : lum < 0 ? 0 : lum;

			if(_lum == 0 || _lum == 1)
			{
				_hue = 0;
				_sat = 0;
			}
			else
			{
				_sat = sat > 1 ? 1 : sat < 0 ? 0 : sat;
				if(sat == 0)
					_hue = 0;
				else
					_hue = ((hue % 360) + 360) % 360;
			}
		}

		public float getHue()
		{
			return _hue;
		}

		public float getSat()
		{
			return _sat;
		}

		public float getLum()
		{
			return _lum;
		}

		@Override
		public boolean equals(Object aOther)
		{
			final float epsilon = 0.0001f;

			if(this == aOther)
				return true;
			if(aOther == null || aOther.getClass() != this.getClass())
				return false;

			HSLHelper that = (HSLHelper) aOther;

			return (Math.abs(this.getHue() - that.getHue()) < epsilon &&
			        Math.abs(this.getSat() - that.getSat()) < epsilon && Math.abs(this.getLum()
			                                                                      - that.getLum()) < epsilon);
		}
	}

	protected HSLHelper	HSLValues	= null;
	protected RGBHelper	RGBValues	= null;

	protected static HSLHelper RGBToHSL(RGBHelper rgb)
	{
		int r = rgb.getR();
		int g = rgb.getG();
		int b = rgb.getB();

		int max = Math.max(Math.max(r, g), b);
		int min = Math.min(Math.min(r, g), b);

		float hue = 0;
		float sat = 0;
		float lum = (max + min) / 255.0f / 2;

		if(max != min)
		{
			int delta = max - min;
			if(max == r)
				hue = (60.0f * (g - b) / delta + 360) % 360;
			else if(max == g)
				hue = 60.0f * (b - r) / delta + 120;
			else if(max == b)
				hue = 60.0f * (r - g) / delta + 240;

			if(lum <= 0.5)
				sat = (delta / 255.0f) / (2 * lum);
			else
				sat = (delta / 255.0f) / (2 - 2 * lum);
		}
		return new HSLHelper(hue, sat, lum);
	}

	protected static RGBHelper HSLToRGB(HSLHelper hsl)
	{
		float hue = hsl.getHue();
		float sat = hsl.getSat();
		float lum = hsl.getLum();

		float r, g, b;

		if(sat == 0)
		{
			r = g = b = lum;
		}
		else
		{
			float q = lum < 0.5 ?
			                   lum * (1 - sat) :
			                   lum + sat - (lum * sat);

			float p = 2 * lum - q;

			r = HueToRGB(p, q, hue + 120);
			g = HueToRGB(p, q, hue);
			b = HueToRGB(p, q, hue - 120);
		}
		return new RGBHelper(r, g, b);
	}

	private static float HueToRGB(float p, float q, float tc)
	{
		while(tc >= 360)
			tc -= 360;
		while(tc < 0)
			tc += 360;

		if(tc < 60)
			return p + ((q - p) * tc / 60);
		else if(tc < 180)
			return q;
		else if(tc < 240)
			return p + ((q - p) * (240 - tc) / 60);
		else
			return p;
	}

	public HSLHelper getHSL()
	{
		if(HSLValues == null)
		{
			HSLValues = RGBToHSL(RGBValues);
		}
		return HSLValues;
	}

	public RGBHelper getRGB()
	{
		if(RGBValues == null)
		{
			RGBValues = HSLToRGB(HSLValues);
		}
		return RGBValues;
	}

	public String toString()
	{
		return "0x" + Integer.toHexString(0x1000000 | getRGB().getHex()).substring(1).toUpperCase();
	}

	@Override
	public final boolean equals(Object aOther)
	{
		if(this == aOther)
			return true;
		if(!(aOther instanceof Color))
			return false;

		Color that = (Color) aOther;

		if(this.HSLValues != null && that.HSLValues != null)
			return this.HSLValues.equals(that.HSLValues);

		else
			return this.getRGB().equals(that.getRGB());
	}
	
	public int RGBDistanceTo(Color that)
	{
		return this.getRGB().distanceTo(that.getRGB());
	}
}

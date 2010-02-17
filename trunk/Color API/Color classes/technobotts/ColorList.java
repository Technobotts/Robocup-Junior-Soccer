package technobotts;

import java.util.ArrayList;

import lejos.util.Color;

public class ColorList extends ArrayList<Color>
{
	public Color getNearestColor(Color input)
	{
		Color closest = null;
		int bestDistance = 0;
		
		for(Color c : this)
		{
			int curDistance = input.RGBDistanceTo(c);
			if(closest == null || curDistance < bestDistance)
			{
				closest = c;
				bestDistance = curDistance;
			}
		}
		return closest;
	}
}

package lejos.util;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;


public class SmootherTest
{
	public static void main(String... main) throws InterruptedException
	{
		LightSensor l = new LightSensor(SensorPort.S1);
		Smoother s = new Smoother(0.5);
		List<Double> rawReadings = new ArrayList<Double>();
		List<Double> smoothedReadings = new ArrayList<Double>();
		
		Graphics g = new Graphics();
		
		while(true)
		{
			double reading = l.getLightValue();
			if(Button.ENTER.isPressed())
				reading = Double.NaN;
			
			double smoothed = s.getOutput(reading);
			
			if(Double.isNaN(smoothed))
				Sound.beep();
			
			rawReadings.add(reading);
			smoothedReadings.add(smoothed);
			
			while(rawReadings.size()>20)
				rawReadings.remove(0);
			while(smoothedReadings.size()>20)
				smoothedReadings.remove(0);
			
			LCD.clear();
			int x = 0;
			for(double d : rawReadings)
			{
				g.fillRect(x+2, 0, 2, (int) d);
				x+=5;
			}
			
			x = 0;
			for(double d : smoothedReadings)
			{
				g.drawRect(x, 0, 5, (int) d);
				x+=5;
			}
			LCD.refresh();
			
			Thread.sleep(100);
		}
	}
}

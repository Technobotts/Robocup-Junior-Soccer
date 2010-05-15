import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.NXTCam;
import lejos.util.TextMenu;
import technobotts.soccer.util.GoalFinder;

public class CamSpeedTester
{
	public static void main(String[] args) throws InterruptedException
	{
		TextMenu goalSelection = new TextMenu(new String[] {"Blue", "Yellow"}, 0, "Goal Color");
		int goalColor = goalSelection.select();

		NXTCam cam = new NXTCam(SensorPort.S1);

		GoalFinder gFinder = new GoalFinder(cam, goalColor);

//		Smoother s = new Smoother(1);

//		Graphics g = new Graphics();
		long startTime = System.currentTimeMillis();
		
		for(int i=0;i<1000;i++)
		{
			LCD.clear();
/*			Rectangle largest = gFinder.getGoalRectangle();
			if(largest != null)
			{
				int x = largest.x * 64 / 144;
				int y = largest.y * 64 / 144;
				int width = largest.width * 64 / 144;
				int height = largest.height * 64 / 144;
				g.fillRect(x, y, width, height);
			}*/
			float raw = gFinder.getDegreesCartesian();
//			double angle = s.getOutput(raw);
//			LCD.drawString("Angle:" + angle, 0, 0);
			LCD.drawString("RAWR:" + raw, 0, 1);
			LCD.refresh();
			if(i%100 == 0)
			{
				Sound.beep();
			}
		}
		long endTime = System.currentTimeMillis();
		LCD.clear();
		LCD.drawString("T:" + (startTime-endTime), 0, 0);
		Button.ENTER.waitForPressAndRelease();
		
	}
}

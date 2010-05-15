import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.NXTCam;
import lejos.util.Smoother;
import lejos.util.TextMenu;
import technobotts.soccer.util.GoalFinder;

public class TurnToGoal
{
	public static void main(String[] args) throws InterruptedException
	{
		TextMenu goalSelection = new TextMenu(new String[] {"Blue", "Yellow"}, 0, "Goal Color");
		int goalColor = goalSelection.select();

		NXTCam cam = new NXTCam(SensorPort.S1);

		GoalFinder gFinder = new GoalFinder(cam, goalColor);

		Smoother s = new Smoother(1);

//		Graphics g = new Graphics();

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
			double angle = s.getOutput(raw);
			LCD.drawString("" + angle, 0, 0);
			LCD.drawString("" + raw, 0, 1);
			LCD.refresh();
			Thread.sleep(50);
		}
	}
}

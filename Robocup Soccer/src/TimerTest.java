import technobotts.util.Timer;


public class TimerTest
{
	static Timer t = new Timer();
	public static void main(String[] args) throws InterruptedException
    {
		double y = 0;
		t.start();
		for(double x = 0; x<10000; x++)
		{
			y = Math.sin(x);
		}
		t.stop();
		System.out.println(t.getTime()/10000.0);
		Thread.sleep(2000);
		
		t.restart();
		for(double x = 0; x<10000; x++)
		{
			y = Math.sqrt(3);
		}
		t.stop();
		System.out.println(t.getTime()/10000.0);
		Thread.sleep(2000);
    }
}

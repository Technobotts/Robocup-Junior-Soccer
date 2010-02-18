package technobotts.rescue;

public abstract class Task implements Runnable
{
	private volatile Thread	t;
	
	public void start()
	{
		t = new Thread(this);
		t.start();
	}

	public void stop()
	{
		t = null;
	}
	
	public void run()
	{
		//Thread thisThread = Thread.currentThread();
		while(t != null)
		{
			execute();
		}
	}
	
	public abstract void execute();
}

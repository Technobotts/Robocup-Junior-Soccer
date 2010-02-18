package technobotts.rescue;

import javax.microedition.lcdui.Graphics;

public class RescueDisplay extends RescueTask
{
	public RescueDisplay(RescueRobot robot)
    {
	    super(robot);
	    this.setName("Display");
	    this.setDaemon(true);
    }
	private Graphics g = new Graphics();
	private String motorOperator = "None";
	private String status = "";
	
	public void setStatus(String status)
	{
		this.status = status;
		this.motorOperator = Thread.currentThread().getName();
	}
	
	public void drawLL()
	{
		int[] sensors = _robot.lineSensor.getSensors();
		g.fillRect(0, 0, 20, 64);
		g.setColor(0);
		if(sensors != null)
		{
			int len = sensors.length;
			for(int i = 0; i<len; i++)
			{
				int s = sensors[i]*20/100;
				g.fillRect(0, i*8, s, 8);
			}
		}
		g.setColor(1);
	}
	
	public void run()
	{
		while(true)
		{
    		g.clear();
    		drawLL();
    		g.drawString("Owner: "+motorOperator,21,56);
    		g.drawString("Status:",21,48);
    		g.drawString(" "+status,21,36);
    		g.refresh();
    		g.drawString("Color: "+_robot.colors.getSensorColor(_robot.colorSensor),21,0);
    		g.refresh();
    		Thread.yield();
		}
	}

}

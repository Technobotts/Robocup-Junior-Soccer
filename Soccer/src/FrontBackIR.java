import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.util.TextMenu;
import technobotts.nxt.addon.IRSeekerV2;
import technobotts.nxt.addon.IRSeekerV2.Mode;
import technobotts.soccer.util.DualLSFinder;

public class FrontBackIR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		IRSeekerV2 front = new IRSeekerV2(SensorPort.S3, Mode.AC_1200Hz);
		IRSeekerV2 back = new IRSeekerV2(SensorPort.S2, Mode.AC_1200Hz);
		
		DualLSFinder ir = new DualLSFinder(front, 0, back, 180);
		
		while(true){
			LCD.clear();
			LCD.drawString("Front: "+front.getAngle(), 0, 0);
			LCD.drawString("Back: "+back.getAngle(), 0, 1);
			LCD.drawString("Both: "+ir.getAngle(), 0, 2);
			LCD.refresh();
			Delay.msDelay(50);
		}

	}

}

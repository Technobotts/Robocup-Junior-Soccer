package technobotts.comms;

public interface MessageType
{
	public static final byte   KICK     = 0x01;
	public static final byte   US_PING  = 0x02;
	public static final byte   GOAL_POS = 0x04;
	public static final byte   SHUTDOWN = 0x08;
	public static final byte[] MESSAGES = {KICK, US_PING, GOAL_POS, SHUTDOWN};

}

package technobotts.soccer.util;

public enum MessageType {
	KICK(0x01),
	US_PING(0x02),
	GOAL_DIST(0x04),
	GOAL_POS(0x08),
	SHUTDOWN(0x10);

	private byte value;

	MessageType(int value)
	{
		this.value = (byte) value;
	}

	public byte getValue()
	{
		return value;
	}
}

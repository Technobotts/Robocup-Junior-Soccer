package technobotts.comm;

public enum MessageType {
	KICK     (0x01),
	US_PING  (0x02),
	GOAL_POS (0x04),
	SHUTDOWN (0x08);

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

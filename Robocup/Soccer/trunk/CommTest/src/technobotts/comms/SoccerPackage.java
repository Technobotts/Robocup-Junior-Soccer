package technobotts.comms;

public class SoccerPackage
{
	enum MessageType {
		KICK,
		US_PING,
		GOAL_ANGLE;
		/*
		 * private MessageType(int id)
		 * {
		 * this.id = (byte) id;
		 * }
		 * private final byte id;
		 */

	}
	
	public static final byte REQUEST_HEADER = 0x40;
	public static final byte RESPONSE_HEADER = (byte) 0x80;
	public static final byte MSG_TYPE_MASK = (byte) 0x80;

	public static byte[] MakeRequest(MessageType type)
	{
		if(type == null)
			return new byte[] {};

		return new byte[] {
		        REQUEST_HEADER,
		         (byte) (type.ordinal() & MSG_TYPE_MASK)};
	}

	public static byte[] MakeResponse(MessageType type, byte[] response)
	{
		if(type == null)
			return new byte[] {};
		
		
		byte[] ret = new byte[2+response.length];
		ret[0] = RESPONSE_HEADER;
		ret[1] = (byte) (type.ordinal() & MSG_TYPE_MASK);
		
		for(int i = 0; i<response.length; i++)
			ret[2+i] = response[i];
			
		return ret;
	}
}

package de.ecconia.java.opentung.savefile;

//Policy lowest byte first. (Little-Endian)
public class ByteLevelHelper
{
	//Assumes highest bit is not set!
	public static int sizeOfUnsignedInt(int val)
	{
		if(val <= 0b1111111)
		{
			return 1;
		}
		else if(val <= 0b11111111111111)
		{
			return 2;
		}
		else if(val <= 0b111111111111111111111)
		{
			return 3;
		}
		else if(val <= 0b1111111111111111111111111111)
		{
			return 4;
		}
		else
		{
			return 5;
		}
	}
	
	//Assumes highest bit is not set!
	public static int sizeOfUnsignedLong(long val)
	{
		if(val <= 0b1111111L)
		{
			return 1;
		}
		else if(val <= 0b11111111111111L)
		{
			return 2;
		}
		else if(val <= 0b111111111111111111111L)
		{
			return 3;
		}
		else if(val <= 0b1111111111111111111111111111L)
		{
			return 4;
		}
		else if(val <= 0b11111111111111111111111111111111111L)
		{
			return 5;
		}
		else if(val <= 0b111111111111111111111111111111111111111111L)
		{
			return 6;
		}
		else if(val <= 0b1111111111111111111111111111111111111111111111111L)
		{
			return 7;
		}
		else if(val <= 0b11111111111111111111111111111111111111111111111111111111L)
		{
			return 8;
		}
		else
		{
			return 9;
		}
	}
	
	public static void writeUnsignedInt(int val, byte[] array, int index)
	{
		while(true)
		{
			byte newVal = (byte) (val & 0x7F);
			val >>>= 7;
			if(val != 0)
			{
				array[index++] = (byte) (newVal | 0x80);
			}
			else
			{
				array[index] = newVal;
				break;
			}
		}
	}
	
	public static void writeFloat(float val, byte[] array, int index)
	{
		writeUncompressedInt(Float.floatToRawIntBits(val), array, index);
	}
	
	public static void writeUncompressedInt(int val, byte[] array, int index)
	{
		//TBI: Ehhh, the 0xFF is really not required right? To be confirmed.
		array[index++] = (byte) (val & 0xFF);
		array[index++] = (byte) ((val >> 8) & 0xFF);
		array[index++] = (byte) ((val >> 16) & 0xFF);
		array[index] = (byte) ((val >> 24) & 0xFF);
	}
	
	public static void writeUncompressedLong(long val, byte[] array, int index)
	{
		//TBI: Ehhh, the 0xFF is really not required right? To be confirmed.
		array[index++] = (byte) (val & 0xFF);
		array[index++] = (byte) ((val >> 8) & 0xFF);
		array[index++] = (byte) ((val >> 16) & 0xFF);
		array[index++] = (byte) ((val >> 24) & 0xFF);
		array[index++] = (byte) ((val >> 32) & 0xFF);
		array[index++] = (byte) ((val >> 40) & 0xFF);
		array[index++] = (byte) ((val >> 48) & 0xFF);
		array[index] = (byte) ((val >> 56) & 0xFF);
	}
	
	public static byte[] writeUnsignedInt(int val)
	{
		byte[] bytes = new byte[sizeOfUnsignedInt(val)];
		writeUnsignedInt(val, bytes, 0);
		return bytes;
	}
	
	public static byte[] writeBoolean(boolean val)
	{
		return new byte[]{
				val ? (byte) 1 : (byte) 0
		};
	}
	
	public static byte[] writeDouble(double val)
	{
		long otherVal = Double.doubleToRawLongBits(val);
		byte[] array = new byte[8];
		writeUncompressedLong(otherVal, array, 0);
		return array;
	}
}

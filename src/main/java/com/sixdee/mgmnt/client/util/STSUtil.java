package com.sixdee.mgmnt.client.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class STSUtil {

	private static final int	HEADER_LEN		= 9;
	/*private static final int	BUFFER_FLAGS	= 8;
	private static int			transactionId	= 10;
*/
	public static int mcfn_convertByteToInt(byte byteData[], int siL_Start, int siL_End) {
		int siL_Value = 0x00;
		
		for (int i = siL_End, j = (siL_End - siL_Start); i >= siL_Start; i--, j--) {
			siL_Value += ((int) byteData[i] & 0xFF) << j * 8;
		
			
		}
		if (siL_Value < 0) {
			siL_Value = siL_Value + 256;
		}
		return siL_Value;
	}

	public static int mcfn_convertByteToShort(byte byteData[], int siL_Start, int siL_End) {
		short siL_Value = 0x00;

		for (int i = siL_End, j = (siL_End - siL_Start); i >= siL_Start; i--, j--) {
			siL_Value += ((short) byteData[i] & 0xFF) << j * 8;
		}

		return siL_Value;
	}

	public static String mcfn_convertByteToString(byte byteData[], int siL_Start, int siL_End) {
		StringBuffer CL_STRBuffer = new StringBuffer();

		for (int i = siL_Start; i <= siL_End; i++) {
			CL_STRBuffer.append((char) ((char) byteData[i] & 0xFF));
			// System.out.print(":"+(char)((char)byteData[i] & 0xFF));
		}
		return CL_STRBuffer.toString();
	}

	public static double mcfn_convertByteToDouble(byte byteData[], int siL_Start, int siL_End) {
		StringBuffer CL_STRBuffer = new StringBuffer();

		for (int i = siL_Start; i <= siL_End; i++) {
			CL_STRBuffer.append((char) ((char) byteData[i] & 0xFF));
		}
		return Double.parseDouble(CL_STRBuffer.toString());
	}

	public static byte mcfn_convertIntToByte(int siL_Val) {
		byte sbyL_Value = 0x00;

		sbyL_Value = (byte) (siL_Val & 0x000000ff);

		return sbyL_Value;
	}

	public static void mcfn_encodeIntToByte(int siL_Value, byte[] pucL_Byte, int siL_Index) {

		pucL_Byte[siL_Index++] = (byte) siL_Value;
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 8);
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 16);
		pucL_Byte[siL_Index] = (byte) (siL_Value >> 24);
	}

	public static void mcfn_encodeLongIntoByte(long siL_Value, byte[] pucL_Byte, int siL_Index) {
		pucL_Byte[siL_Index++] = (byte) siL_Value;
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 8);
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 16);
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 24);
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 32);
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 40);
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 48);
		pucL_Byte[siL_Index] = (byte) (siL_Value >> 64);
	}

	public static void mcfn_encodeByte(int siL_Value, byte[] pucL_Byte, int siL_Index) {
		pucL_Byte[siL_Index] = (byte) siL_Value;
	}

	public static void mcfn_encodeStringIntoByte(String pCL_String, byte[] pucL_Byte, int siL_Index) {
		byte strBytes[] = pCL_String.getBytes();

		for (int i = 0; i < strBytes.length; i++) {
			pucL_Byte[siL_Index++] = strBytes[i];
		}
	}

	public static void mcfn_encodeShortToByteMSB(int siL_Value, byte[] pucL_Byte, int siL_Index) {
		pucL_Byte[siL_Index++] = (byte) (siL_Value >> 8);
		pucL_Byte[siL_Index++] = (byte) siL_Value;
	}

	public static int byteArrayToInt(byte[] b, int offset) {

		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	public static byte[] intToByteArray(final int integer) throws IOException {
		byte[] b =null;
		ByteArrayOutputStream bos =null;
		DataOutputStream dos = null;
		
		try {
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			dos.writeInt(integer);
			dos.flush();
			b = bos.toByteArray();
		
		} catch (Exception e) {
			
		} finally {
			if (bos != null)
				bos.close();
			if (dos != null)
				dos.close();
		}
		return b;
	}

	public static byte[] getEncodedIntegerPackedString(int i) {
		String intStr = Integer.toString(i);
		byte[] encodedOctetBytes = new byte[4];
		int outIdx = 3;
		boolean highOrder = false;
		for (int idx = (intStr.length() - 1); idx >= 0 && outIdx >= 0; idx--) {
			if (highOrder) {
				encodedOctetBytes[outIdx] += (byte) ((intStr.charAt(idx) & 0X0F) << 4);
				outIdx--;
			} else {
				encodedOctetBytes[outIdx] += (byte) intStr.charAt(idx) & 0X0F;
			}
			highOrder = !highOrder;
		}

		return encodedOctetBytes;
	}

	public static int getDecodedIntegerPackedString(byte[] encodedString) {
		int idx = 0;
		byte highOrderByte;
		byte lowOrderByte;
		String resultDecodedString = "";
		int resultInt = 0;

		for (idx = 0; idx < encodedString.length; idx++) {
			lowOrderByte = (byte) ((encodedString[idx] & 0xF0) >> 4);
			highOrderByte = (byte) (encodedString[idx] & 0x0F);
			resultDecodedString += Byte.toString(lowOrderByte);
			resultDecodedString += Byte.toString(highOrderByte);
		}

		try {
			resultInt = Integer.parseInt(resultDecodedString);
		} catch (NumberFormatException e) {
			//
		}

		return resultInt;
	}

	static final long	LONG_HIGH_ORDER_VALUE	= 0X100000000L;

	public static byte[] unsignedIntToBytes(int intInput) {
		byte byteStr[] = { 0, 0, 0, 0 };
		long theIntInput = unsignedIntToLong(intInput);

		if (theIntInput > 0Xffffff) {
			byteStr[0] = (byte) (theIntInput / 0X1000000L);
			theIntInput = theIntInput % 0X1000000L;
		}

		if (theIntInput > 0Xffff) {
			byteStr[1] = (byte) (theIntInput / 0X10000L);
			theIntInput = theIntInput % 0X10000L;
		}

		if (theIntInput > 0Xff) {
			byteStr[2] = (byte) (theIntInput / 0X100L);
			theIntInput = theIntInput % 0X100L;
		}

		byteStr[3] = (byte) (theIntInput);

		return byteStr;
	}

	public static long unsignedIntToLong(int i) {
		if (i < 0)
			return LONG_HIGH_ORDER_VALUE + i;
		else
			return (long) i;
	}

	// Decode Byte Array to integer:
	public static int unsignedBytesToInt(byte[] inputBytes) {
		int checkInt = 0;
		int pos = 0;
		if (inputBytes.length < 0)
			return 0;
		if (inputBytes.length == 2) {
			checkInt += unsignedByteToInt(inputBytes[pos++]) << 8;
			checkInt += unsignedByteToInt(inputBytes[pos++]) << 0;
		} else {
			checkInt += unsignedByteToInt(inputBytes[pos++]) << 24;
			checkInt += unsignedByteToInt(inputBytes[pos++]) << 16;
			checkInt += unsignedByteToInt(inputBytes[pos++]) << 8;
			checkInt += unsignedByteToInt(inputBytes[pos++]) << 0;
		}

		return checkInt;
	}

	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

	/** ***************************************************************************** */

	public static byte[] getEncodedMessage(int transactionId, String messageXml) {
		byte[] messageBuffer = new byte[messageXml.length() + HEADER_LEN];
		byte resultBuffer[] = null;
		int outBuffIdx = 0;
		int idx = 0;
	/*	byte xmlRequest[] = null;
		int BUFFER_FLAGS = 8;*/

		try {

			resultBuffer = unsignedIntToBytes(transactionId);
			for (idx = 0; idx < resultBuffer.length; idx++) {
				messageBuffer[outBuffIdx++] = resultBuffer[idx];
			}

			resultBuffer = unsignedIntToBytes(messageXml.length()); // Changed
																	// Format to
																	// packed
																	// hex
																	// string
																	// 1234 not
																	// BCD
			// resultBuffer =
			// Conversion.getEncodedIntegerPackedString(messageXml.length( ));
			for (idx = 0; idx < resultBuffer.length; idx++) {
				messageBuffer[outBuffIdx++] = resultBuffer[idx];
			}

			messageBuffer[outBuffIdx++] += 0x00;

			resultBuffer = messageXml.getBytes("UTF8");
			for (idx = 0; outBuffIdx < messageBuffer.length; idx++) {
				messageBuffer[outBuffIdx++] = resultBuffer[idx];
			}
		} catch (Exception e) {
			System.err.println(new Date() + ":IAS TCP XML Write ERROR: Transaction length of "
					+ messageBuffer.length + " suspect. Write aborted.");
			return null;
		}

		return messageBuffer;
	}

}// end class StsUtil

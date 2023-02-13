package com.sixdee.mgmnt.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.sixdee.mgmnt.client.util.STSUtil;


public class MgmntFullCacheRldWithDMN {
	private Socket				socket;
	private InputStream USSDRecvStream =null;
	private OutputStream USSDSendStream =null;

	private static final int	HEADER_LEN				= 9;
	//private static final int	BUFFER_FLAGS			= 8;
	private static final long	LONG_HIGH_ORDER_VALUE	= 0X100000000L;

	public MgmntFullCacheRldWithDMN(){
		//
	}


	public static void main(String arg[])throws Exception{

		if (arg.length == 0 || arg.length<4) {
			System.err.println("Usage: java -jar MgmntClient.jar MgmntFullCacheRldWithDMN IpAddress Port LoginName LoginPassword");
			return;
		}
		for (int i = 0; i < arg.length; i++) {
			System.out.print(arg[i] + ":");
		}
		System.out.println();

		//IpAddress of the Machine
		String address=arg[0].trim();
		// Port Number of the machine
		int port=Integer.parseInt(arg[1].trim());

		String loginName=arg[2].trim();//arg[2] is log.xml
		
		String loginPwd=arg[3].trim();//arg[2] is log.xml

		
		MgmntFullCacheRldWithDMN ad=new MgmntFullCacheRldWithDMN();
		ad.createConnection(address,port);
		ad.runMgmntProcess(loginName,loginPwd);

	}//end main method

	public void createConnection(String Address,int Port) {
		String addr =null;
		int	port = 6677;
		try {
			if(Address==null)
				addr = InetAddress.getLocalHost().toString();
			else
				addr=Address;

			if(Port!=0)
				port=Port;

			socket = new Socket(addr, port);

			USSDRecvStream=socket.getInputStream();
			USSDSendStream=socket.getOutputStream();

		}catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void sendData(String requestString) {

		Random randomGenerator = new Random();
		int transactionId= randomGenerator.nextInt(100);
		try {
			byte[] outByteStream = getEncodedMessage(transactionId, requestString);
			if(USSDSendStream!=null) {
				USSDSendStream.write(outByteStream);
				USSDSendStream.flush();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] getEncodedMessage(int transactionId, String messageXml) {
		byte[] messageBuffer = new byte[messageXml.length() + HEADER_LEN];
		byte resultBuffer[] = null;
		int outBuffIdx = 0;
		int idx = 0;

		try {

			resultBuffer = unsignedIntToBytes(transactionId);
			for (idx = 0; idx < resultBuffer.length; idx++) {
				messageBuffer[outBuffIdx++] = resultBuffer[idx];
			}

			resultBuffer = unsignedIntToBytes(messageXml.length()); // Changed

			for (idx = 0; idx < resultBuffer.length; idx++) {
				messageBuffer[outBuffIdx++] = resultBuffer[idx];
			}

			messageBuffer[outBuffIdx++] += 0x00;

			resultBuffer = messageXml.getBytes("UTF8");
			for (idx = 0; outBuffIdx < messageBuffer.length; idx++) {
				messageBuffer[outBuffIdx++] = resultBuffer[idx];
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.err.println(new Date() + ":IAS TCP XML Write ERROR: Transaction length of "+ messageBuffer.length + " suspect. Write aborted.");
			return null;
		}

		return messageBuffer;
	}

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
			return i;
	}

	public String recieveData() {
		int DataLenValue = -1;
		byte[] recvStream;
		byte[] DataLenStream = new byte[4];
		String xmlStr =null;
		try {


			DataLenValue = USSDRecvStream.read(DataLenStream, 0, 4);

			DataLenValue = USSDRecvStream.read(DataLenStream, 0, 4);

			// int DataLength =
			// STSUtil.getDecodedIntegerPackedString(DataLenStream);
			int DataLength = STSUtil.unsignedBytesToInt(DataLenStream);

			DataLenValue = USSDRecvStream.read(DataLenStream, 0, 1);

			recvStream = new byte[DataLength];

			DataLenValue = USSDRecvStream.read(recvStream, 0, DataLength);
			xmlStr = STSUtil.mcfn_convertByteToString(recvStream, 0, DataLenValue - 1);

		} catch (Exception exp) {
			exp.printStackTrace();
		}

		return xmlStr;
	}

	public String getLoingXML(String logName, String logPwd){
		return "<REQ><CMD>LOGON</CMD><USER>"+logName+"</USER><PASSWORD>"+logPwd+"</PASSWORD></REQ>";
	}// send Login XML

	public Object[] readCommand(String filename) throws Exception {
		ArrayList<String> arrList = new ArrayList<String>();
		arrList = readFile(filename);
		Object array[] = arrList.toArray();
		return array;

	}

	public ArrayList<String> readFile(String fileNameq) throws Exception {
		ArrayList<String>	arryList = new ArrayList<String>();

		FileReader fr = new FileReader(fileNameq);
		BufferedReader buf = new BufferedReader(fr);
		String readData;
		while ((readData = buf.readLine()) != null) {
			arryList.add(readData);
		}// end method readData

		return arryList;
	}// end Method readFile

	public String getAllCommandString() {

		String allCommandString = "<REQ><CMD>FullCacheReloadWithDMN</CMD><PARAMETERS></PARAMETERS></REQ>";

		return allCommandString;
	}// end method getallCommand

	public void runMgmntProcess(String logName, String logPwd) {
		try {

			String sendLogInfo=getLoingXML(logName,logPwd);

			System.out.println("Request Sent For Processing "+sendLogInfo);
			sendData(sendLogInfo);
			String recievedInfo=recieveData();
			System.out.println("Response Recieved ::-->> "+recievedInfo);
			
			if(!recievedInfo.contains("FAILURE")) {
				String xmlData = getAllCommandString();
				System.out.println("Request Sent For Processing ::-->> "+xmlData);
				sendData(xmlData);
				System.out.println("Response Recieved ::-->> "+recieveData());
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}
}//end class USSDAllCommand

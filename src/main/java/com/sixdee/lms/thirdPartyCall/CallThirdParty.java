/**
 * 
 */
package com.sixdee.lms.thirdPartyCall;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestTemplate;

import javassist.expr.NewArray;

/**
 * @author NITHIN
 *
 */
public class CallThirdParty {

	private Logger logger = Logger.getLogger(CallThirdParty.class);

	public String makeThirdPartyCall(String reqJson, String url, int timeout) {

		logger.info("URL"+url+  "Sending 'Json' request to URL : " + reqJson);
		
		StringBuffer response = null;
		try {
			URL obj = new URL(url);

			String output;
			BufferedReader in = null;

			DataOutputStream wr = null;
			HttpURLConnection con = null;
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(timeout);
			con.setReadTimeout(timeout);

			con.setDoOutput(true);
			if (reqJson != null && !reqJson.equalsIgnoreCase("")) {
				
				wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(reqJson);
				wr.flush();
				wr.close();
			}
			int responseCode = con.getResponseCode(); //
			//logger.info(" Response Code : " + responseCode);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			response = new StringBuffer();
			while ((output = in.readLine()) != null) {
				response.append(output);
			}
			in.close();
		} catch (Exception e) {

		}

		
		return response != null ? response.toString() : " null response from bandwidth";

		/*
		 * 
		 * StringBuilder sb = null; URL url = null; HttpURLConnection connection = null;
		 * String decodedString; OutputStreamWriter out = null; BufferedReader in =
		 * null;
		 * 
		 * try {
		 * 
		 * //logger.debug("Calling url "+URL+" With XML ["+reqXML+"]"); sb = new
		 * StringBuilder(); url = new URL(URL.trim()); connection = (HttpURLConnection)
		 * url.openConnection(); connection.setDoOutput(true);
		 * connection.setRequestMethod("POST"); connection.setConnectTimeout(timeout);
		 * connection.setReadTimeout(timeout); out = new
		 * OutputStreamWriter(connection.getOutputStream()); out.write(reqXML);
		 * out.flush();
		 * 
		 * in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		 * 
		 * while ((decodedString = in.readLine()) != null) {
		 * sb.append(decodedString.trim()); } } catch (Exception e) {
		 * e.printStackTrace(); //
		 * logger.error("Error while calling URL:"+reqXML+", Exception:",e); // throw e;
		 * } finally { url = null;
		 * 
		 * if(out !=null){ try { out.close(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } out = null;
		 * 
		 * if (in != null) { try { in.close(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } in = null;
		 * 
		 * if(connection!=null){ connection.disconnect(); }
		 * 
		 * connection = null;
		 * 
		 * decodedString = null; } return sb.toString();
		 * 
		 */}
	
//	public static void main(String[] args) {
//		
//		String req="{\r\n"
//				+ "	\"requestId\": \"14567890\",\r\n"
//				+ "	\"msisdn\": \"2687733838\",\r\n"
//				+ "	\"keyWord\": \"NotifyCustomer\",\r\n"
//				+ "	\"scheduleId\": \"7\",\r\n"
//				+ "	\"dataSet\": {\r\n"
//				+ "		\"parameters\": [{\r\n"
//				+ "			\"name\": \"TOTAL_POINTS\",\r\n"
//				+ "			\"value\": \"1000\"\r\n"
//				+ "		}, {\r\n"
//				+ "			\"name\": \"POINT_CAL_EXPIRY_DATE\",\r\n"
//				+ "			\"value\": \"2024-03-02 19:19:16\"\r\n"
//				+ "		}, {\r\n"
//				+ "			\"name\": \"MSISDN\",\r\n"
//				+ "			\"value\": \"2687733838\"\r\n"
//				+ "		}, {\r\n"
//				+ "			\"name\": \"MSG_CAUSE\",\r\n"
//				+ "			\"value\": \"POINT_ACCUMULATION_SUCCESS\"\r\n"
//				+ "		}, {\r\n"
//				+ "			\"name\": \"CALCULATE_POINTS\",\r\n"
//				+ "			\"value\": \"1000\"\r\n"
//				+ "		}, {\r\n"
//				+ "			\"name\": \"TRIGGER_NAME\",\r\n"
//				+ "			\"value\": \"NotifyCustomer\"\r\n"
//				+ "		}, {\r\n"
//				+ "			\"name\": \"SERVICE_ID\",\r\n"
//				+ "			\"value\": \"144\"\r\n"
//				+ "		}, {\r\n"
//				+ "			\"name\": \"ID\",\r\n"
//				+ "			\"value\": \"1\"\r\n"
//				+ "		}, {\r\n"
//				+ "			\"name\": \"SCHEDULE_ID\",\r\n"
//				+ "			\"value\": \"7\"\r\n"
//				+ "		}]\r\n"
//				+ "	}\r\n"
//				+ "}";
//		String url="http://10.0.12.194:8120/RuleEngine/rule/online/synch-response";
//		String  outPut= makeThirdPartyCall(req,url,1000);
//		System.out.print("outPut"+outPut);
//		
//	}


}

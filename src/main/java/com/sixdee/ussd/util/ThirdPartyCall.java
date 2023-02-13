/**
 * 
 */
package com.sixdee.ussd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class ThirdPartyCall {

	private Logger logger = Logger.getLogger(ThirdPartyCall.class);
	
	public String makeThirdPartyCall(String xml,String tpUrl,int timeout){

		StringBuffer sb = null;
		URL url = null;
		HttpURLConnection connection = null;
		String decodedString;
		OutputStreamWriter out = null;
		BufferedReader in = null;

		try {
			sb = new StringBuffer();
			url = new URL(tpUrl.trim());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(xml);
			out.flush();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((decodedString = in.readLine()) != null) {
				sb.append(decodedString.trim());
			}
		} catch (Exception e) {
			logger.error("Error while calling URL:"+tpUrl+", Exception:",e);
		//	throw e;
		} finally {
			url = null;
			
			if(out !=null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			out = null;

			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			in = null;
			
			if(connection!=null){
				connection.disconnect();
			}
			
			connection = null;
			
			decodedString = null;
		}
		return sb.toString();
	
	}
}

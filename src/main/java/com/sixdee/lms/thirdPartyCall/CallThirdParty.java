/**
 * 
 */
package com.sixdee.lms.thirdPartyCall;

/**
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;


/**
 * @author NITHIN
 *
 */
public class CallThirdParty {

	private Logger logger = Logger.getLogger(CallThirdParty.class);
	
	public String makeThirdPartyCall(String reqXML,String URL,int timeout){

		StringBuilder sb = null;
		URL url = null;
		HttpURLConnection connection = null;
		String decodedString;
		OutputStreamWriter out = null;
		BufferedReader in = null;

		try {
			
			logger.debug("Calling url "+URL+" With XML ["+reqXML+"]");
			sb = new StringBuilder();
			url = new URL(URL.trim());
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);
			out = new OutputStreamWriter(connection.getOutputStream());
			out.write(reqXML);
			out.flush();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while ((decodedString = in.readLine()) != null) {
				sb.append(decodedString.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while calling URL:"+reqXML+", Exception:",e);
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

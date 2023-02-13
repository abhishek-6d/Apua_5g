package com.sixdee.imp.utill;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ResponseSender 
{
	private Logger log =LogManager.getLogger(ResponseSender.class);
	public String sendResponse(String respUrl,String xmlString) throws Exception
	{
		log.info("Request XML >>>>>>>>>>"+xmlString);
		URL url = null;
		HttpURLConnection urlConn = null;
		OutputStreamWriter out = null;
		InputStream in = null;
		StringBuffer sb = new StringBuffer();
		String respXML=null;

		try
		{
			url = new URL(respUrl);
			urlConn = (HttpURLConnection)url.openConnection();
			
			urlConn.setRequestMethod("POST");
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setConnectTimeout(10000);
		    out = new OutputStreamWriter(urlConn.getOutputStream());
	 
		    out.write(xmlString);
		    out.flush();
		    
		    in = urlConn.getInputStream();
			int i;
			while ((i = in.read()) != -1) 
			{
			    sb.append((char) i);
			}
			
			if(sb!=null)
				if(sb!=null)
					respXML=sb.toString();
			in.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		finally
		{
			if(out!=null)
			{
				try
				{
					out.flush();
					out.close();
					out = null;
				}
				catch (Exception e) 
				{
				}
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(urlConn!=null)
			{
				try
				{
					urlConn.disconnect();
					url = null;
				}
				catch (Exception e) 
				{
				}
			}
			url =null;
		
		}
		return respXML;

	}
	
	

}

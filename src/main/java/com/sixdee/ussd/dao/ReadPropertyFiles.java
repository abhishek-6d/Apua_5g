package com.sixdee.ussd.dao;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Globals;
import com.sixdee.ussd.util.AppCache;

public class ReadPropertyFiles {
	
	public static ReadPropertyFiles readPropertyFiles=null;
	private HashMap<String, String>propertymap=new HashMap<String, String>();
	private Properties properties = null;
	private static final Logger logger = Logger.getLogger(ReadPropertyFiles.class);
		
	
	private ReadPropertyFiles()
	{
		
	}
	
	public static ReadPropertyFiles getInstance()
	{
		
		if(readPropertyFiles==null)
		{
			readPropertyFiles=new ReadPropertyFiles();
		}
		
		return readPropertyFiles;
	}
	public void reloadProperties() {
		properties = null;
	}
	
	
	public HashMap<String, String> getPropertyMap()
	{
		
		return propertymap;
	}
	
	public HashMap<String,String> readProperties()
	{
		InputStream in = null;
		Properties properties = null;
		HashMap<String, String> map = new HashMap<String, String>();
		Enumeration enumeration = null;

		try
		{
			in = Globals.class.getClassLoader().getResourceAsStream("UssdManager.properties");
			properties = new Properties();
			properties.load(in);
			
			enumeration = properties.keys();
			
			while (enumeration.hasMoreElements()) 
			{
				String key = (String) enumeration.nextElement();
				
				if(properties.getProperty(key)!=null)
				{
					logger.info("key = "+key+" &  value = "+(String)properties.getProperty(key));
					map.put(key.trim(), (String)properties.getProperty(key).trim());
				}
				else
				{
					logger.info("key = "+key +" & value = NOT FOUND");
					logger.info("Exit ");
					throw new Exception();
				}
				
			}
			//Cache.setCacheMap(map);
			if(map.get("DEFAULT_LANGUAGE")!=null){
				AppCache.langId = Integer.parseInt(map.get("DEFAULT_LANGUAGE"));
			}if(map.get("APP_NAME")!=null){
				AppCache.appName = map.get("APP_NAME");
			}else{
				AppCache.appName = "USSD_DYNAMIC_BROWSER";
			}
		}
		catch (Exception e) 
		{
			logger.error("Error occured while loading system property",e);
			e.printStackTrace();
		//	System.exit(1);
		}
		finally
		{
			try
			{
				if(in!=null)
				{
					in.close();
					in=null;
				}
			}
			catch (Exception e) {
			}
			properties = null;
			enumeration =null;
			
		}
		return map;
	}

}

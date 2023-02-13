package com.sixdee.imp.common.config;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.imp.bo.ExtraRewardPointsBO;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.util.parser.ReResponseParser;

public class ExtraRewardpoints extends HttpServlet{

	private static final Logger log = Logger.getLogger(ExtraRewardpoints.class);
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
		
		execute(req,resp);
		
	}
	
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException{
		
		execute(req,resp);
		
	}
	
	
	private void execute(HttpServletRequest req, HttpServletResponse resp){
		BufferedInputStream bis= null;
		StringBuilder sb = null;
		String xml = "";
		RERequestHeader header = null;
		String msisdn = null;
		ExtraRewardPointsBO extrwrdbo= new ExtraRewardPointsBO();
		
		try{
			bis = new BufferedInputStream(req.getInputStream());
			int i;
			sb = new StringBuilder();
			
			while((i=bis.read())!=-1){
				sb.append((char)i);
			}
			xml = sb.toString();
			log.info("Request =>"+xml);
			
			if(xml != null && !xml.equalsIgnoreCase("")){
				header = (RERequestHeader)ReResponseParser.getInstanceReqStream().fromXML(xml);
				msisdn=header.getMsisdn();
			}
			
			extrwrdbo.assignExtraReward(msisdn);
			
			
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally{
			try{
			if(bis !=null)
				bis.close();
				bis = null;
			}catch(Exception e){
				log.info("Exception while closing the stream "+e);
			}
		}
		
	}
}
		

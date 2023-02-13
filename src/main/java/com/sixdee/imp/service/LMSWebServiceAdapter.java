package com.sixdee.imp.service;

import org.apache.log4j.Logger;

import com.sixdee.fw.adapter.WebServiceAdapter;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.dto.RequestProcessDTO;

public class LMSWebServiceAdapter extends WebServiceAdapter
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger=Logger.getLogger(LMSWebServiceAdapter.class);
	
	public GenericDTO callFeature(String featureName,Object obj)
	{
		GenericDTO dto=null;
		try
		{
			dto = execute(featureName, obj);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception occured in calling feature",e);
		} 
		return dto;
		
	}//callFeature
	
	public GenericDTO callFeature(RequestProcessDTO requestProcessDTO)
	{
		GenericDTO dto=null;
		try
		{
			dto = execute(requestProcessDTO.getFeatureName(),requestProcessDTO.getObject());
		}
		catch (Exception e) {
			logger.info("Exception occured in calling feature",e);
		} 
		return dto;
	}//callFeature
	
	

}

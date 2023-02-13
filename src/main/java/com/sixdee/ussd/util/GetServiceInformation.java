/**
 * 
 */
package com.sixdee.ussd.util;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.KeywordMappingDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;

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
public class GetServiceInformation {

	private static final Logger logger = Logger.getLogger(GetServiceInformation.class);
	
	public KeywordMappingDTO getKeywordInformation(String requestId,String sessionId,Service service,int langId,boolean recuringEnabled){
		String keyToSearch = null;
		KeywordMappingDTO keywordMappingDTO = null;
		HashMap<String,String> serviceMap = null;
		HashMap<String, KeywordMappingDTO> keywordMap = null;
		String nextService = null;
		try{/*
			serviceMap = service.getMap();
			nextService = serviceMap.get(ServiceConstants.nextService);
			if(nextService==null||nextService.trim().equals(ServiceConstants.emptyString)){
				logger.debug("RequestId ["+requestId+"] SessionId ["+sessionId+"] Message : next service recieved is empty ");
				nextService = serviceMap.get(ServiceConstants.messageText);
			}
			logger.debug("RequestId ["+requestId+"] SessionId ["+sessionId+"] Message : next service recieved is "+nextService);
			keyToSearch = nextService+ServiceConstants.delimiter+langId;
			//keywordMap = ;
			keywordMappingDTO = ((HashMap<String, KeywordMappingDTO>) AppCache.util.get(ServiceConstants.keyword)).get(keyToSearch);
			if(keywordMappingDTO==null){
				if(recuringEnabled && langId!=AppCache.langId){
					//logger.debug("");
					keywordMappingDTO=getKeywordInformation(requestId, sessionId, service, AppCache.langId, false);
				}
			}
		*/}catch(Exception e){
			logger.error("RequestId ["+requestId+"] SessionId ["+sessionId+"] Message : Exception occured when finding keyword ",e);
		}
		finally{
			keyToSearch = null;
			serviceMap = null;
			nextService = null;
		}
		return keywordMappingDTO;
	}
}

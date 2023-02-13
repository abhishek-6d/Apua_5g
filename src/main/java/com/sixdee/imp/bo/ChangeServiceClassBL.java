package com.sixdee.imp.bo;

/**
 * 
 * @author Somesh Soni
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>September 04,2013 11:45:30 AM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.ChangeServiceClassDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.util.ThirdPartyCall;
import com.sixdee.imp.util.parser.ReRequestParser;
import com.sixdee.imp.util.parser.ReResponseParser;

public class ChangeServiceClassBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private static final DateFormat df = new SimpleDateFormat("ddMMyyyy hh:mm:ss"); 
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => ChangeServiceClassBL :: Method => buildProcess()");
		try
		{
		
			ChangeServiceClassDTO changeServiceClassDTO = (ChangeServiceClassDTO) genericDTO.getObj();
			
			RERequestHeader header = new RERequestHeader();
			header.setMsisdn(changeServiceClassDTO.getMsisdn());
			header.setRequestId(changeServiceClassDTO.getTransactionId());
			header.setTimeStamp(df.format(new Date()));
			header.setKeyWord("UPDATE_SERVICECLASS");
			
			RERequestDataSet dataSet = new RERequestDataSet();
			header.setDataSet(dataSet);
			ArrayList<ReResponseParameter> al = new ArrayList<ReResponseParameter>();
			
			ReResponseParameter param = new ReResponseParameter();
			param.setId("serviceClassNew");
			param.setValue(changeServiceClassDTO.getClassId());
			al.add(param);
			
			param = new ReResponseParameter();
			param.setId("serviceClassAction");   
			param.setValue("1"); // 1 -set 2 - set orig 3 - set temp  4 - delete temp
			al.add(param);
			
		
	
			param = new ReResponseParameter();
			param.setId("CALLING_FLAG");  //LMS CALL
			param.setValue("false");
			al.add(param);
			
			//dataSet.setResponseParam(al);
			
			ThirdPartyCall call = new ThirdPartyCall();
			String reqXML = ReRequestParser.getInstanceReqStream().toXML(header);
			logger.info(reqXML);
			String responseXML = call.makeThirdPartyCall1(reqXML, Cache.cacheMap.get("MO_ROUTER_URL"), 5000);
			
			header = (RERequestHeader)ReResponseParser.getInstanceReqStream().fromXML(responseXML);
			dataSet= header.getDataSet();
			
			genericDTO.setStatusCode(header.getStatus());
			
			genericDTO.setStatus(header.getStatusDesc());
			
		}
		catch (Exception e) 
		{
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus("Failure");
			
			logger.info("Exception in change class ....");
			e.printStackTrace();
		}
		
		return genericDTO;
	}
}

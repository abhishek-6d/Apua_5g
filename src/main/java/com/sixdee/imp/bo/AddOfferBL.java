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
 * <td>September 04,2013 12:57:23 PM</td>
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
import com.sixdee.imp.dto.AddOfferDTO;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.util.ThirdPartyCall;
import com.sixdee.imp.util.parser.ReRequestParser;
import com.sixdee.imp.util.parser.ReResponseParser;

public class AddOfferBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private static final DateFormat df = new SimpleDateFormat("ddMMyyyy hh:mm:ss"); 
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => AddOfferBL :: Method => buildProcess()");
		try
		{
		
			AddOfferDTO addOfferDTO = (AddOfferDTO) genericDTO.getObj();
			ReResponseParameter param = new ReResponseParameter();
			
			RERequestHeader header = new RERequestHeader();
			header.setMsisdn(addOfferDTO.getMsisdn());
			header.setRequestId(addOfferDTO.getTransactionId());
			header.setTimeStamp(df.format(new Date()));
			
			RERequestDataSet dataSet = new RERequestDataSet();
			header.setDataSet(dataSet);
			ArrayList<ReResponseParameter> al = null;
		
			switch (addOfferDTO.getOperation()) {
			case 1:
				logger.info("SET OPERATION");
				header.setKeyWord("RESET_OFFER");
				al = getSetList(addOfferDTO);
				break;
			case 2:
				logger.info("RE-SET OPERATION");
				header.setKeyWord("RESET_OFFER");
				al = getReSetList(addOfferDTO);
				break;
			case 3:
				logger.info("UPDATE OPERATION");
				header.setKeyWord("UPDATE_OFFER");
				al = getUpdateList(addOfferDTO);
				break;
	
			default:
				logger.info("Unknow Operation.....!!!!!!");
				break;
			}
	
			param = new ReResponseParameter();
			param.setId("CALLING_FLAG");
			param.setValue("false");
			al.add(param);
			
			param = new ReResponseParameter();
			param.setId("CDR_WRITE");
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

			logger.info("Exception in Add Offer BL"+e);
			e.printStackTrace();
		}
		return genericDTO;
	}
	
	
	private ArrayList<ReResponseParameter> getSetList(AddOfferDTO addOfferDTO)
	{
		ArrayList<ReResponseParameter> al = new ArrayList<ReResponseParameter>();
		ReResponseParameter param = null;
		
		param = new ReResponseParameter();
		param.setId("NEW_OFFERID");
		param.setValue(String.valueOf(addOfferDTO.getOfferId()));
		al.add(param);
		
		return al;
	}
	
	private ArrayList<ReResponseParameter> getReSetList(AddOfferDTO addOfferDTO)
	{
		ArrayList<ReResponseParameter> al = new ArrayList<ReResponseParameter>();
		ReResponseParameter param = null;
		
		param = new ReResponseParameter();
		param.setId("DELETE_ALL");
		param.setValue("true");
		al.add(param);
		
		return al;

	}
	
	private ArrayList<ReResponseParameter> getUpdateList(AddOfferDTO addOfferDTO)
	{
		ArrayList<ReResponseParameter> al = new ArrayList<ReResponseParameter>();
		ReResponseParameter param = null;
		
		param = new ReResponseParameter();
		param.setId("offerID");
		param.setValue(String.valueOf(addOfferDTO.getOfferId()));
		al.add(param);
		
		return al;

	}
}

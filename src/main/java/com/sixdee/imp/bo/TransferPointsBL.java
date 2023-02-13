package com.sixdee.imp.bo;

/**
 * 
 * @author Paramesh
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
 * <td>April 24,2013 04:37:12 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.ArrayList;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TransferPointsDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.TransferPointsDTO;
import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.Response;
import com.sixdee.lms.bo.OnlineRuleInitiatorBO;
import com.sixdee.lms.util.constant.SystemConstants;

public class TransferPointsBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => TransferPointsBL :: Method => buildProcess()");
		
		TransferPointsDAO  transferPointsDAO=new TransferPointsDAO();
		TransferPointsDTO transferPointsDTO = (TransferPointsDTO) genericDTO.getObj();
		CommonUtil commonUtil = new CommonUtil();
		ArrayList<String> al = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		int status = 0;
try{
		if(transferPointsDTO.isTransfer())
		{
			// call re ....if success ...proceed to transfer point else give failure response
			transferPointsDTO.setFromLoyaltyId(transferPointsDAO.getLoyaltyId(transferPointsDTO.getSubscriberNumber()));
			transferPointsDTO.setToLoyaltyId(transferPointsDAO.getLoyaltyId(transferPointsDTO.getDestinationSubscriberNumber()));
			
			 loyaltyProfileTabDTO=tableDetailsDAO.getLoyaltyProfileDetails(transferPointsDTO.getFromLoyaltyId());
			 if(loyaltyProfileTabDTO!=null)
			 {
				 if(loyaltyProfileTabDTO.getRewardPoints()>=transferPointsDTO.getTransferPoints()){
					 if(transferPointsDTO.getFromLoyaltyId()>0 && transferPointsDTO.getToLoyaltyId()>0){
							
							Request request = getRuleRequest(transferPointsDTO);
						     genericDTO.setObj(request);
						     genericDTO = new OnlineRuleInitiatorBO().executeBusinessProcess(genericDTO);
						     Response response = (Response) genericDTO.getObj();
						     if(response!=null){
						    	 logger.info(">>>resp code >>>>"+response.getRespCode());
						    	if(response.getRespCode()!=null && response.getRespCode().equalsIgnoreCase("SC0000")){
						    		if(response.getDataSets()!=null){
						    			ArrayList<DataSet> dataSet =response.getDataSets().getDataSet1();
						    			if(dataSet!=null){
						    				ArrayList<Param> paramlist = dataSet.get(0).getParameter1();
						    				for(Param p: paramlist){
						    					if(p.getId().equalsIgnoreCase("STATUS")){
						    						logger.info(">>>>status>>>"+p.getValue());
						    						status = Integer.parseInt(p.getValue());
						    						break;
						    					}
						    				}
						    				if(status==1){
						    			logger.info(">>re gives success , hence processing the transfer point req>>");
							    		  
						    		    String callMethod = "{call UpdateLoyaltyReward_Online(?,?,?,?,?)}";
						    		    al =  commonUtil.callProcedure(callMethod, transferPointsDTO.getFromLoyaltyId()+"", transferPointsDTO.getTransferPoints()+"", 2);
						    			if(al!=null && !al.get(0).equalsIgnoreCase("0")){	
						    				transferPointsDTO.setTransferPoints(Long.parseLong(al.get(0)));
						    		     genericDTO.setObj(transferPointsDTO);
						    		     transferPointsDAO.transferPoints(genericDTO);
						    			   }else{
						    				   genericDTO.setStatusCode("SC0001");
								 		    	 genericDTO.setStatus("Transfer points failed");
						    			   }
						    				}else{
						    					for(Param p: paramlist){
							    					if(p.getId().equalsIgnoreCase("DESCRIPTION")){
							    						logger.info(">>>>DESCRIPTION>>>"+p.getValue());
							    						genericDTO.setStatusCode("SC0002");
										 		    	 genericDTO.setStatus(p.getValue());
							    						break;
							    					}
							    				}
						    				}
						    			}
						    		}
						    		  
						    			  
						    	}else{
						    			 genericDTO.setStatusCode("SC0002");
						 		    	 genericDTO.setStatus("Transfer points failed");
						    		 }
						    			
						    		 }
							   }else{
								   genericDTO.setStatusCode("SC0006");
						    		genericDTO.setStatus("Subscriber doesnot exist");
							   }
				 }else{
	    			 genericDTO.setStatusCode("SC0002");
	 		    	 genericDTO.setStatus("Subscriber doesnot have sufficient points points");
	    		 }
				 
			 }else{
				   genericDTO.setStatusCode("SC0006");
		    		genericDTO.setStatus("Subscriber doesnot exist");
			   }
			
		     }else{
		    	 genericDTO.setStatusCode("SC0002");
		    		genericDTO.setStatus("Failed due to exception");
		     }
			
			
		
		 //		transferPointsDAO.transferPoints(genericDTO);
			
}catch(Exception e){
	e.printStackTrace();
	genericDTO.setStatusCode("SC0001");
	genericDTO.setStatus("Failed due to exception");
}
			return genericDTO;
		}
	
	private Request getRuleRequest(TransferPointsDTO transferPointsDTO) {
		Request request = new Request();
		request.setRequestId(transferPointsDTO.getTransactionId());
		request.setTimeStamp(transferPointsDTO.getTimestamp());
		request.setKeyWord("TransferPoints");
		request.setMsisdn(transferPointsDTO.getFromLoyaltyId()+"");// loyalty_id  in msisdn tag of req
		DataSet dataSet = new DataSet();
		ArrayList<Param> parameter1 = new ArrayList<>();
		parameter1.add(new Param(SystemConstants.MSISDN,transferPointsDTO.getSubscriberNumber()+""));
		parameter1.add(new Param(SystemConstants.LOYALTY_ID,transferPointsDTO.getFromLoyaltyId()+""));
		parameter1.add(new Param(SystemConstants.POINTS,transferPointsDTO.getTransferPoints()+""));
		dataSet.setParameter1(parameter1);
		request.setDataSet(dataSet);
		return request;
	}
}

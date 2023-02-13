package com.sixdee.imp.simulator.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import om.omantel.arbor.inquiryservices.CustIdJB;

import org.apache.log4j.Logger;

import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.SubscriberDetailsDTO;
import com.sixdee.imp.simulator.dao.PromotionSimulatorDAO;
import com.sixdee.imp.simulator.dto.PromotionSubscriberOutPutdto;
import com.sixdee.imp.util.CRMCalling;

public class PromotionSimulatorBL {
	Logger logger = Logger.getLogger(PromotionSimulatorBL.class);
	NationalIdSyncBL nationalIdSyncBL = new NationalIdSyncBL();
	public void populateForPromotion(String subscriberNumber){
		logger.info("PromotionSimulatorBL()");
		PromotionSimulatorDAO dao = new PromotionSimulatorDAO();
		List<CustIdJB> custIdJBs = null;
		ArrayList<String> lineNumbersList = null;
		PromotionSubscriberOutPutdto outPutdto = null;
		
		try{
				custIdJBs=nationalIdSyncBL.callCRMGetNationalIdList(subscriberNumber);
				if(custIdJBs !=null && custIdJBs.size()>0){
					for(CustIdJB custIdJB : custIdJBs){
						lineNumbersList = CallCMRandGetLineNumbers(custIdJB.getCustID(),custIdJB.getCustIdType());
						if(lineNumbersList !=null && lineNumbersList.size()>0){
							for(String lineNumber :lineNumbersList){
							outPutdto = new PromotionSubscriberOutPutdto(subscriberNumber, lineNumber, custIdJB.getCustID().trim(), custIdJB.getCustIdType().trim());
							dao.insertPromoInfo(outPutdto);
							outPutdto = null;
							}
						}
						
					}
				}
			
			
		}catch(Exception e){
			logger.error("Exception in populateForPromotion() "+e);
		}
	}
	
	public ArrayList<String> CallCMRandGetLineNumbers(String NationalId, String NationalIdType){
		logger.info("CallCMRandGetLineNumbers()");
		
		List<NationalNumberTabDTO> custIdList=new ArrayList<NationalNumberTabDTO>();
		NationalNumberTabDTO tabDTO=new NationalNumberTabDTO();
		String crmTransactionID=System.currentTimeMillis()+"";
		CRMCalling calling=null;
		Object[] objects=null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO1 = new LoyaltyProfileTabDTO();
		Map<String,SubscriberDetailsDTO> subscriberAccountMaps=null;
		ArrayList<String> lineNumbersList = new ArrayList<String>();
		
		try{
			
		logger.info(" : CRM Transaction ID is "+crmTransactionID);
		calling=new CRMCalling();
		loyaltyProfileTabDTO1.setDefaultLanguage("1");
		tabDTO.setNationalNumber(NationalId);
		tabDTO.setIdType(NationalIdType);
		custIdList.add(tabDTO);
		loyaltyProfileTabDTO1.setCustIdList(custIdList);
		//commented for testing *****start
	    objects=calling.getAllLineNumber(loyaltyProfileTabDTO1,crmTransactionID);
		
		

		if(objects!=null&&objects[0]!=null)
		{
			subscriberAccountMaps=(Map<String,SubscriberDetailsDTO>)objects[0];
			if(subscriberAccountMaps!=null&&subscriberAccountMaps.size()>0)
			{
				for(String lineNumber:subscriberAccountMaps.keySet())
				{
					String subscriberNumber = subscriberAccountMaps.get(lineNumber).getSubscriberNumber();
					if(subscriberNumber != null){
						lineNumbersList.add(subscriberNumber.trim());
					}
				}
			}
		}
	    
	  //commented for testing *****end
	    
	    //test data **** start
	    /*lineNumbersList.add("6551000201");
	    lineNumbersList.add("6551000202");*/
	    // test data **** end
		}catch (Exception e){
			logger.error("Exception in CallCMRandGetLineNumbers()"+e);
		}
		
		return lineNumbersList;
		
	}
}

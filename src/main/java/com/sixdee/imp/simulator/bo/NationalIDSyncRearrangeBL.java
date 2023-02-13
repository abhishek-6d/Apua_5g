package com.sixdee.imp.simulator.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import om.omantel.arbor.inquiryservices.CustIdJB;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.simulator.dao.NationalIdSyncDAO;
import com.sixdee.imp.simulator.dto.NationalIDMergingTabDTO;
import com.sixdee.imp.simulator.dto.OldAndNewAccNumTabDTO;

public class NationalIDSyncRearrangeBL {
	Logger logger = Logger.getLogger(NationalIDSyncRearrangeBL.class);
	
	
	public void RearrangeNationalId(){
		NationalIdSyncBL nationalIdSyncBL = new NationalIdSyncBL();
		
		logger.info("****getUpdateNationalIds()****");
		
		List<String> subscriberNumberList = null;
		List<CustIdJB> custIdJBs = null;
		List<CustIdJB> filteredCustIdJBList = new ArrayList<CustIdJB>();
		TableInfoDAO infoDAO = new TableInfoDAO();
		
		NationalNumberTabDTO nationalNumberTabDTOTmp = new NationalNumberTabDTO();
		CustIdJB custIdJBTmp = new CustIdJB();
		//List<NationalNumberTabDTO> list =null;
		NationalIdSyncDAO nationalIdSyncDAO = new NationalIdSyncDAO();
		List<String> custIDTypeList=null;
		
		
		
		if(Cache.getCacheMap().get("CUST_ID_TYPE")!=null)
		{
			logger.info("Caching from System.properties for custid");
		  	if(Cache.getCacheMap().get("CUST_ID_TYPE").contains(","))
		  		custIDTypeList=Arrays.asList(Cache.getCacheMap().get("CUST_ID_TYPE").split(","));
		  	else
		  	{
		  		custIDTypeList=new ArrayList<String>();
		  		custIDTypeList.add(Cache.getCacheMap().get("CUST_ID_TYPE"));
		  	}
		}
		logger.info(custIDTypeList);
		
		
		try{
			
			subscriberNumberList = nationalIdSyncDAO.getNullrecords();
			Iterator<String> itSub = subscriberNumberList.iterator();
			while(itSub.hasNext()){
				String subscriberNum =null;
				long loyaltyId;
				subscriberNum = itSub.next().trim();
				loyaltyId=nationalIdSyncDAO.getLoyaltyIDBasedOnsubnum(subscriberNum);
				logger.info("The subscriber is ====>"+subscriberNum);
				logger.info("The loyaltyId is ====>"+loyaltyId);
				custIdJBs = nationalIdSyncBL.callCRMGetNationalIdList(subscriberNum.trim());
				//list=nationalIdSyncDAO.getNationalNumberDetails(infoDAO.getNationalNumberTable(0+ ""),oldAndNewAccNumTabDTO.getloyaltyID());
				if(custIdJBs !=null){
				for(CustIdJB custIdJB : custIdJBs){
					if(custIDTypeList.contains(custIdJB.getCustIdType())){
						filteredCustIdJBList.add(custIdJB);
						
					}
				}
				logger.info("filteredCustIdJBList===>"+filteredCustIdJBList);
				logger.info("filteredCustIdJBList size===>"+filteredCustIdJBList.size());
				}
				
				Iterator<CustIdJB> custIdJbIte = filteredCustIdJBList.iterator();
				while(custIdJbIte.hasNext()){
					logger.info("filteredCustIdJBList has values");
					custIdJBTmp = custIdJbIte.next();
					NationalIDMergingTabDTO idMergingTabDTO = new NationalIDMergingTabDTO();
					idMergingTabDTO.setNewNationalId(custIdJBTmp.getCustID().trim());
					idMergingTabDTO.setNewNationalIdType(custIdJBTmp.getCustIdType().trim());
					idMergingTabDTO.setSubscriberNumber(subscriberNum);
					idMergingTabDTO.setLoyaltyId(loyaltyId);
					
					
					logger.info("NationalID for this subscriber ====> "+custIdJBTmp.getCustID());
					logger.info("NationalIDType for this subscriber ====> "+custIdJBTmp.getCustIdType());
					if(nationalIdSyncDAO.checkNewIDInNewIdField(custIdJBTmp.getCustIdType().trim(),subscriberNum)){
						nationalIdSyncDAO.deleteIdFromNullList(idMergingTabDTO);
					}
						if(nationalIdSyncDAO.checkNewIDInOldIdField(custIdJBTmp.getCustIdType().trim(),subscriberNum)){
							nationalIdSyncDAO.updateNullList(idMergingTabDTO);
						}
						else{
							nationalIdSyncDAO.updateNationalIdMerging(idMergingTabDTO);
						}
							
							
					
					//idMergingTabDTO.setLoyaltyId(oldAndNewAccNumTabDTO.getloyaltyID());
					
					idMergingTabDTO = null;
					
				}
			}
				
		}catch(Exception e){
			logger.error("Exception in getUpdateNationalIds() ====>"+e);
		}
	}
}

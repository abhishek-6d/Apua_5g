package com.sixdee.imp.simulator.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import om.omantel.arbor.inquiryservices.AccountSummaryJB;
import om.omantel.arbor.inquiryservices.CustIdJB;
import om.omantel.arbor.inquiryservices.SubscriberInfoJB;
import om.omantel.arbor.inquiryservices.SubscriberInfoService;
import om.omantel.arborservice.EnquiryGetIDServiceStub;
import om.omantel.arborservice.EnquiryGetIDServiceStub.EaiReference_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetIDReq;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetIDReqestType;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetIDRequest;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetIDRes;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetIDResponse;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetIDResponseE;
import om.omantel.arborservice.EnquiryGetIDServiceStub.IDInfoType;
import om.omantel.arborservice.EnquiryGetIDServiceStub.IDType_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.ID_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.MsgFormat_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.MsgVersion_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.OT_EAI_HEADERType;
import om.omantel.arborservice.EnquiryGetIDServiceStub.ReferenceNo_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.ReferenceNo_type3;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorChannelId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorLanguage_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorSecurityInfo_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorUserId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.ReturnCode_type1;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.service.AccountManagement;
import com.sixdee.imp.service.UpdateAccountInfoResponseDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;
import com.sixdee.imp.service.serviceDTO.req.UpdateAccountInfoRequestDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.simulator.dao.NationalIdSyncDAO;
import com.sixdee.imp.simulator.dto.NationalIDMergingTabDTO;
import com.sixdee.imp.simulator.dto.NationalIDMergingTabDTONumbersFromOmantel;
import com.sixdee.imp.simulator.dto.OldAndNewAccNumTabDTO;
import com.sixdee.imp.simulator.dto.OldAndNewAccNumTabDTOFromOmantel;

public class NationalIdSyncBL {
	Logger logger = Logger.getLogger(NationalIdSyncBL.class);
	
	
public List<CustIdJB> callCRMGetNationalIdList(String subscriberNumber){
	SubscriberInfoService infoService=null;
	SubscriberInfoJB subscriberInfoJB=null;
	String accountNumber = null;
	AccountSummaryJB accountInfo=null;
	List<CustIdJB> custIdInfos= new ArrayList<CustIdJB>();
	String transactionID=System.currentTimeMillis()+"";
	//testing *****start sajith ks 
	CustIdJB CustIdJBtest1 = new CustIdJB();
	CustIdJB CustIdJBtest2 = new CustIdJB();
	CustIdJB CustIdJBtest3 = new CustIdJB();
	CustIdJB CustIdJBtest4 = new CustIdJB();
	//testing *****end sajith ks 
	try{
		
        logger.info(transactionID+" : getBasicSubscriberInfo "+Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));
        logger.info("callCRMGetNationalIdList()");
		
        //commented for testing **** sajith ks start
		infoService=new SubscriberInfoService(Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));
		
		subscriberInfoJB=infoService.getSubscriberInfo("C1_"+transactionID,subscriberNumber);
		
		accountInfo = infoService.getAccountInfo("C2_"+transactionID, subscriberInfoJB.getCustAccountNumber());
		custIdInfos = accountInfo.getCustIdList();
		
		
		if(custIdInfos == null)
		{
			logger.info("NationalID list is null");
		}
      //commented for testing **** sajith ks end
        
        //Testing ****sajith ks Start
       /*CustIdJBtest1.setCustID("611");
        CustIdJBtest1.setCustIdType("National ID Number");
        CustIdJBtest2.setCustID("612");
        CustIdJBtest2.setCustIdType("Residence Number");
        CustIdJBtest3.setCustID("613");
        CustIdJBtest3.setCustIdType("Residence Number");
        CustIdJBtest4.setCustID("614");
        CustIdJBtest4.setCustIdType("Residence Number");
        custIdInfos.add(CustIdJBtest1);
        custIdInfos.add(CustIdJBtest2);
        custIdInfos.add(CustIdJBtest3);
        custIdInfos.add(CustIdJBtest4);*/
        // Testing ***** sajith ks end	
        
		
	}catch(Exception e){
		logger.error("Exception in callCRMGetNationalIdList()===>"+e);
	}
	return custIdInfos;
}
	
public String callCRMToGetAccountNumber(String subscriberNumber){
	SubscriberInfoService infoService=null;
	SubscriberInfoJB subscriberInfoJB=null;
	String accountNumber = null;
	String transactionID=System.currentTimeMillis()+"";
	try{
		logger.info("**** inside callCRMToGetAccountNumber()");
		logger.info(transactionID+" : getBasicSubscriberInfo "+Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));
		
		//commented for testing ****Start
		infoService=new SubscriberInfoService(Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));
		
		subscriberInfoJB=infoService.getSubscriberInfo("C1_"+transactionID,subscriberNumber);
		accountNumber = subscriberInfoJB.getCustAccountNumber();
		//commented for testing ****end
		
	}catch (Exception e){
		logger.error("Exception in callCRMToGetAccountNumber===>"+e);
	}
	
	return accountNumber;
	//return "1234"; //hardcoded for testing
}


public ArrayList<String> callCRMGetSubscriberNumbersBasedOnNationalID(CustIdJB custIdJBTmp){
	logger.info("****callCRMGetSubscriberNumbersBasedOnNationalID****");
	EnquiryGetIDServiceStub stub=null;
	ArrayList<String> subscribersList = new ArrayList<String>();
	//commented for testing **** sajith ks start
	String custId="";
	String custIDTrimmed="";
	GetIDRequest request=new GetIDRequest();
	String transactionId=System.currentTimeMillis()+"";
	logger.info(transactionId+" : getAllLineNumber "+Cache.getCacheMap().get("CRM_CALL_NATIONAL_URL"));
	try{
	stub=new EnquiryGetIDServiceStub(Cache.getCacheMap().get("CRM_CALL_NATIONAL_URL"));
	custIDTrimmed = removeLeadingZeros(custIdJBTmp.getCustID().trim());
	
	GetIDReq getIDReq=new GetIDReq();
	
	GetIDReqestType reqestType=new GetIDReqestType();
	
	ID_type1 id_type1=new ID_type1();
	
	id_type1.setID_type0(custIDTrimmed);

	IDType_type1 type_type1= getIDType(custIdJBTmp.getCustIdType().trim());
	
	custId+=custIDTrimmed.trim()+",";
	
	ReferenceNo_type1 no_type3=new ReferenceNo_type1();
	no_type3.setReferenceNo_type0("C3_"+transactionId);
	reqestType.setReferenceNo(no_type3);
	
	
	reqestType.setID(id_type1);
	reqestType.setIDType(type_type1);
	
	logger.info(transactionId+" :  Cust ID "+reqestType.getID());
	logger.info(transactionId+" :  Cust ID Type "+reqestType.getIDType());
	
	OT_EAI_HEADERType ot_eai_headerType=new OT_EAI_HEADERType();
	MsgFormat_type1 format_type1=new MsgFormat_type1();
	format_type1.setMsgFormat_type0("GETMEDCBILL.SERVICE");
	ot_eai_headerType.setMsgFormat(format_type1);
	MsgVersion_type1 msgVersion_type1=new MsgVersion_type1();
	msgVersion_type1.setMsgVersion_type0("1.0");
	ot_eai_headerType.setMsgVersion(msgVersion_type1);
	RequestorId_type1 requestorId_type1=new RequestorId_type1();
	requestorId_type1.setRequestorId_type0("ARBOR");
	ot_eai_headerType.setRequestorId(requestorId_type1);
	
	RequestorChannelId_type1 requestorChannelId_type1=new RequestorChannelId_type1();
	requestorChannelId_type1.setRequestorChannelId_type0("WS");
	ot_eai_headerType.setRequestorChannelId(requestorChannelId_type1);
	RequestorUserId_type1 requestorUserId_type1=new RequestorUserId_type1();
	requestorUserId_type1.setRequestorUserId_type0("ARBOR");
	ot_eai_headerType.setRequestorUserId(requestorUserId_type1);
	RequestorLanguage_type1 requestorLanguage_type1=new RequestorLanguage_type1();
	requestorLanguage_type1.setRequestorLanguage_type0("E");
	ot_eai_headerType.setRequestorLanguage(requestorLanguage_type1);
	RequestorSecurityInfo_type1 requestorSecurityInfo_type1=new RequestorSecurityInfo_type1();
	requestorSecurityInfo_type1.setRequestorSecurityInfo_type0("Eai");
	ot_eai_headerType.setRequestorSecurityInfo(requestorSecurityInfo_type1);
	EaiReference_type1 eaiReference_type1=new EaiReference_type1();
	eaiReference_type1.setEaiReference_type0("0");
	ot_eai_headerType.setEaiReference(eaiReference_type1);
	ReturnCode_type1 code_type1=new ReturnCode_type1();
	code_type1.setReturnCode_type0("0000");
	ot_eai_headerType.setReturnCode(code_type1);
	
	
	
	
	getIDReq.setRequest(reqestType);
	getIDReq.setOT_EAI_HEADER(ot_eai_headerType);
	
	
	request.setGetIDRequest(getIDReq);
	
	
	// calling service
	GetIDResponseE getIDResponseE= stub.getID(request);
	
	
	GetIDRes getIDRes= getIDResponseE.getGetIDResponse();
	
	GetIDResponse getIDResponse= getIDRes.getResponse();
	
	IDInfoType[] infoTypes= getIDResponse.getIDInfo();
	
	if(infoTypes!=null && infoTypes.length>0){
		
		for(IDInfoType infoType:infoTypes)
		{
			
			logger.info(transactionId+" : _____________________________________________________________________________________________________");
			logger.info(transactionId+" :  getAllLineNumber  Subscriber Number "+infoType.getSubscriberNumber().getSubscriberNumber_type0());
			logger.info(transactionId+" :  getAllLineNumber  Account Number "+infoType.getAccountNumber().getAccountNumber_type2());
			logger.info(transactionId+" :  getAllLineNumber  Account Category "+infoType.getAccountCategory().getAccountCategory_type0());
			
			if(infoType.getSubscriberType().getSubscriberType_type0()==null||Cache.accountTypeMap.get(infoType.getSubscriberType().getSubscriberType_type0().trim())==null)
			{
				logger.info(transactionId+" :  getAllLineNumber  Account Type "+infoType.getSubscriberType().getSubscriberType_type0()+" Ignored ");
				continue;
			}
			
			if(Cache.accountCategoryList==null||infoType.getAccountCategory()==null||infoType.getAccountCategory().getAccountCategory_type0()==null||infoType.getAccountCategory().getAccountCategory_type0().trim().equals("")||!Cache.accountCategoryList.contains(infoType.getAccountCategory().getAccountCategory_type0().trim()))
			{
				logger.info(transactionId+" :  getAllLineNumber  Account Category "+infoType.getAccountCategory()+" not in configured List for "+infoType.getSubscriberNumber().getSubscriberNumber_type0());
				continue;
			}
			
			subscribersList.add(infoType.getSubscriberNumber().getSubscriberNumber_type0().trim());
			
		}
		
	}
	
	
	}catch (Exception e) {
		logger.info(transactionId+" : ",e);
	}finally{
		stub=null;
	}
	//commented for testing **** sajith ks end
	/*subscribersList.add("9689980071524");
	subscribersList.add("9112345678");*/
	
	return subscribersList;
}


// copied for callCRMGetSubscriberNumbersBasedOnNationalID CRM call
public IDType_type1 getIDType(String id)
{
     
	if(id.equalsIgnoreCase("National ID Number")||id.equalsIgnoreCase("101"))
		return IDType_type1.value1;
	
	if(id.equalsIgnoreCase("Passport Number")||id.equalsIgnoreCase("102"))
		return IDType_type1.value2;
	
	if(id.equalsIgnoreCase("Labour Card")||id.equalsIgnoreCase("103"))
		return IDType_type1.value3;
	
	if(id.equalsIgnoreCase("Residence Number")||id.equalsIgnoreCase("104"))
		return IDType_type1.value4;
	
	if(id.equalsIgnoreCase("Company Registration Number")||id.equalsIgnoreCase("105"))
		return IDType_type1.value5;
	
	if(id.equalsIgnoreCase("Government ID")||id.equalsIgnoreCase("106"))
		return IDType_type1.value6;
	
	return IDType_type1.value1;
	
}

public List<OldAndNewAccNumTabDTO> getSubscriberNumber(){
	logger.info("***inside getSubscriberNumber()");
	NationalIdSyncDAO nationalIdSyncDAO = new NationalIdSyncDAO();
	List<OldAndNewAccNumTabDTO> subscriberList = null; 
	try{
		subscriberList = nationalIdSyncDAO.getSubscriberNumberFromTable();
		
	}catch (Exception e){
		logger.error("Exception in getting subscribersList"+e);
	}
	return subscriberList;
}

public boolean updateNewAccountNumber(String subscriberNumber, Long accountNumber,String status){
	logger.info("**** inside updateNewAccountNumber()");
	NationalIdSyncDAO nationalIdSyncDAO = new NationalIdSyncDAO();
	boolean result = false;
	try{
		result = nationalIdSyncDAO.updateAccountNumberInTable(subscriberNumber,accountNumber,status);
		
	}catch(Exception e){
		result = false;
		logger.error("Exception in updateNewAccountNumber :"+e);
	}
	return result;
}


//Getting the subscribers list from the table, calling the CRM to get the account number and putting the status either as p or f.
public void callCRMAndUpdateInMasterTable(OldAndNewAccNumTabDTO oldAndNewAccNumTabDTO){
	
	logger.info("***inside callCRMAndUpdateInMasterTable()");
	/*List<OldAndNewAccNumTabDTO> subscriberNumberList = null;*/
	Long newAccountNumber = 0L;
	String CRMResponse = null;
	boolean noExcep = true;
	/*OldAndNewAccNumTabDTO oldAndNewAccNumTabDTO = null;*/
	try{
		
		/*Iterator<OldAndNewAccNumTabDTO> itr = subscriberNumberList.iterator();*/
		/*while(itr.hasNext()){*/
			int retryCount =4;
			boolean flag =false;
			/*oldAndNewAccNumTabDTO = itr.next();*/
			try{
				CRMResponse = callCRMToGetAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim());
			newAccountNumber = Long.parseLong(CRMResponse);
			}catch(Exception he){
				updateNewAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(),0L,"F");
				logger.info("Unrecognised response from CRM ====> "+CRMResponse);
				logger.error("Unrecognisable response from CRM ====>"+he);
				/*continue;*/
				noExcep = false;
			}
			if(noExcep){
			logger.info("newAccountNumber===>"+newAccountNumber);
			logger.info("oldAccountNumber===>"+oldAndNewAccNumTabDTO.getAccountNumber());
			if(newAccountNumber.equals(oldAndNewAccNumTabDTO.getAccountNumber())){
				logger.info("account numbers are equall");
			if(updateNewAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(),newAccountNumber,"P")){
				logger.info("Updated status as p in master table succesfully");
				if(getUpdateNationalIds(oldAndNewAccNumTabDTO)){
					updateNewAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(),newAccountNumber,"C");
				}
				else{
					while(retryCount>0){
						if(getUpdateNationalIds(oldAndNewAccNumTabDTO)){
							updateNewAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(),newAccountNumber,"C");
							flag = true;
							break;
						}
						retryCount--;
					}
					if(!flag){
						updateNewAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(),newAccountNumber,"UFICT");
					}
				}
			}
			else{
				logger.error("Updation failed");
			}
			}
			else{
				logger.info("account numbers are not equall");
				if(updateNewAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(),newAccountNumber,"F"))
					logger.info("Updated succesfully");
				else
					logger.error("Updation failed");
			}
		/*}*/
	}
		/*subscriberNumberList = null;*/
		
	}catch(Exception e){
		logger.error("Exception in callCRMAndUpdateInTable() : "+e);
	}
	
}

//getting info from NATIONAL_NUMBER and putting into NATIONAL_ID_MERGING table after crm call
public boolean getUpdateNationalIds(OldAndNewAccNumTabDTO oldAndNewAccNumTabDTO){
	
	logger.info("****getUpdateNationalIds()****");
	
	List<OldAndNewAccNumTabDTO> subscriberNumberList = null;
	List<CustIdJB> custIdJBs = null;
	List<CustIdJB> filteredCustIdJBList = new ArrayList<CustIdJB>();
	TableInfoDAO infoDAO = new TableInfoDAO();
	
	NationalNumberTabDTO nationalNumberTabDTOTmp = new NationalNumberTabDTO();
	CustIdJB custIdJBTmp = new CustIdJB();
	List<NationalNumberTabDTO> list =null;
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
			logger.info("The subscriber is ====>"+oldAndNewAccNumTabDTO.getSubscriberNumber());
			custIdJBs = callCRMGetNationalIdList(oldAndNewAccNumTabDTO.getSubscriberNumber().trim());
			list=nationalIdSyncDAO.getNationalNumberDetails(infoDAO.getNationalNumberTable(0+ ""),oldAndNewAccNumTabDTO.getloyaltyID());
			if(custIdJBs !=null){
			for(CustIdJB custIdJB : custIdJBs){
				if(custIDTypeList.contains(custIdJB.getCustIdType())){
					filteredCustIdJBList.add(custIdJB);
					
				}
			}
			logger.info("filteredCustIdJBList===>"+filteredCustIdJBList);
			logger.info("filteredCustIdJBList size===>"+filteredCustIdJBList.size());
			}
			logger.info("national number list===>"+list);
			logger.info("national number list size===>"+list.size());
			
			
			
			Iterator<NationalNumberTabDTO> nationalIte =list.iterator();
			Iterator<CustIdJB> custIdJbIte = filteredCustIdJBList.iterator();
			while(nationalIte.hasNext() || custIdJbIte.hasNext()){
				logger.info("either list or filteredCustIdJBList has values");
				NationalIDMergingTabDTO idMergingTabDTO = new NationalIDMergingTabDTO();
				if(nationalIte.hasNext()){
					logger.info("list has values");
				nationalNumberTabDTOTmp = nationalIte.next();
				//idMergingTabDTO.setCreateTime(nationalNumberTabDTOTmp.getCreateDate());
				
				idMergingTabDTO.setOldNationalId(nationalNumberTabDTOTmp.getNationalNumber());
				idMergingTabDTO.setOldNationalIdType(nationalNumberTabDTOTmp.getIdType());
				//idMergingTabDTO.setStatusId(nationalNumberTabDTOTmp.getStatusId());
				}
				if(custIdJbIte.hasNext()){
					logger.info("filteredCustIdJBList has values");
				custIdJBTmp = custIdJbIte.next();
				//idMergingTabDTO.setLoyaltyId(oldAndNewAccNumTabDTO.getloyaltyID());
				idMergingTabDTO.setNewNationalId(custIdJBTmp.getCustID());
				idMergingTabDTO.setNewNationalIdType(custIdJBTmp.getCustIdType());
				}
				idMergingTabDTO.setLoyaltyId(oldAndNewAccNumTabDTO.getloyaltyID());
				idMergingTabDTO.setSubscriberNumber(oldAndNewAccNumTabDTO.getSubscriberNumber());
				if(!nationalIdSyncDAO.updateNationalIdMerging(idMergingTabDTO)){
					idMergingTabDTO = null;
					return false;
				}
				idMergingTabDTO = null;
				
			}
			return true;
		
		
	}catch(Exception e){
		logger.error("Exception in getUpdateNationalIds() ====>"+e);
		return false;
	}
}


//calling updateaccount using the newly added data

	public void callUpdateAccount() {
		logger.info("****callUpdateAccount()*****BL ****started****");
		List<NationalIDMergingTabDTONumbersFromOmantel> updatedList = new ArrayList<NationalIDMergingTabDTONumbersFromOmantel>();
		NationalIdSyncDAO nationalIdSyncDAO = new NationalIdSyncDAO();
		updatedList = nationalIdSyncDAO.getUpdatedListforUpdation();
		AccountManagement accountManagement = new AccountManagement();
		for (NationalIDMergingTabDTONumbersFromOmantel idMergingTabDTO : updatedList) {
			logger.info("old nationalId ====>"+idMergingTabDTO.getOldNationalId());
			logger.info("New nationalId ====>"+idMergingTabDTO.getNewNationalId());
			logger.info("SubscriberNumber ====>"+idMergingTabDTO.getSubscriberNumber());
			UpdateAccountInfoRequestDTO updateAccountInfo = new UpdateAccountInfoRequestDTO();
			GenericDTO genericDTO = new GenericDTO();
			UpdateAccountInfoResponseDTO accountInfoResponseDTO = new UpdateAccountInfoResponseDTO();
			updateAccountInfo.setChannel("USSD");
			updateAccountInfo.setNewId(idMergingTabDTO.getNewNationalId());
			updateAccountInfo.setNewIdType(idMergingTabDTO.getNewNationalIdType());
			updateAccountInfo.setOldId(idMergingTabDTO.getOldNationalId());
			updateAccountInfo.setOldIdType(idMergingTabDTO.getOldNationalIdType());
			updateAccountInfo.setSubscriberNumber(idMergingTabDTO.getSubscriberNumber());
			updateAccountInfo.setTransactionId(System.currentTimeMillis() + "");
			genericDTO.setObj(updateAccountInfo);
			accountInfoResponseDTO = accountManagement.updateSubscriberAccountInfo(updateAccountInfo);
			logger.info("The response code===> "+ accountInfoResponseDTO.getStatusCode());
			logger.info("The response description===> "+ accountInfoResponseDTO.getStatusDescription());
			updateAccountInfo = null;
		}
		logger.info("****callUpdateAccount()*****BL ****completed****");
	}
	
	/*public void callDeleteAccount() {
		logger.info("****callDeleteAccount()*****BL ****started****");
		List<OldAndNewAccNumTabDTOFromOmantel> updatedList = new ArrayList<OldAndNewAccNumTabDTOFromOmantel>();
		NationalIdSyncDAO nationalIdSyncDAO = new NationalIdSyncDAO();
		updatedList = nationalIdSyncDAO.getUpdatedListForDeletion();
		AccountManagement accountManagement = new AccountManagement();
		Data data[] = new Data[1];
		Data data2 = new Data();
		data[0] = data2;
		data[0].setName("status");
		data[0].setValue("6");
		for (OldAndNewAccNumTabDTOFromOmantel oldNewOmanShared : updatedList) {
			logger.info("SubscriberNumber ====>"+oldNewOmanShared.getSubscriberNumber());
			AccountDTO accountDTO = new AccountDTO();
			GenericDTO genericDTO = new GenericDTO();
			ResponseDTO responseDTO = new ResponseDTO();
			accountDTO.setMoNumber(oldNewOmanShared.getSubscriberNumber());
			accountDTO.setChannel("USSD");
			accountDTO.setTransactionId(System.currentTimeMillis() + "");
			accountDTO.setData(data);
			genericDTO.setObj(accountDTO);
			responseDTO = accountManagement.deleteLoyaltyAccount(accountDTO);
			logger.info("The response code===> "+ responseDTO.getStatusCode());
			logger.info("The response code===> "+ responseDTO.getStatusDescription());
			accountDTO = null;
		}
		logger.info("****callDeleteAccount()*****BL ****completed****");
	}*/

	public void changeStatus(OldAndNewAccNumTabDTO oldAndNewAccNumTabDTO){
		logger.info("****changeStatus()****");
		String CRMResponse=null;
		boolean noExcep=true;
		Long newAccountNumber;
		List<String> custIDTypeList=null;
		List<CustIdJB> custIdJBs = null;
		List<CustIdJB> filteredCustIdJBList = new ArrayList<CustIdJB>();
		CustIdJB custIdJBTmp = new CustIdJB();
		NationalIdSyncDAO nationalIdSyncDAO = new NationalIdSyncDAO();
		ArrayList<String> subscriberNumberTableList =null;
		ArrayList<String> loyaltyRegisterNumberTableList =null;
		subscriberNumberTableList = nationalIdSyncDAO.CacheSubscriberNumberTableUsingLoyaltyID(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(), oldAndNewAccNumTabDTO.getloyaltyID());
		loyaltyRegisterNumberTableList = nationalIdSyncDAO.CacheLoyaltyRegisteredNumberTableUsingLoyaltyID(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(), oldAndNewAccNumTabDTO.getloyaltyID());
		
		try{
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
		logger.info("custIDTypeList ====> "+custIDTypeList);
		
		try{
			CRMResponse = callCRMToGetAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim());
		newAccountNumber = Long.parseLong(CRMResponse);
		}catch(Exception he){
			//updateNewAccountNumber(oldAndNewAccNumTabDTO.getSubscriberNumber().trim(),0L,"F");
			logger.info("Unrecognised response from CRM ====> "+CRMResponse);
			logger.error("Unrecognisable response from CRM ====>"+he);
			/*continue;*/
			noExcep = false;
		}
		
		if (noExcep) {
			logger.info("The subscriber is ====>"+ oldAndNewAccNumTabDTO.getSubscriberNumber());
			custIdJBs = callCRMGetNationalIdList(oldAndNewAccNumTabDTO.getSubscriberNumber().trim());
			if (custIdJBs != null) {
				logger.info("NationalIDList size ==>"+custIdJBs.size());
				for (CustIdJB custIdJB : custIdJBs) {
					if (custIDTypeList.contains(custIdJB.getCustIdType())) {
						filteredCustIdJBList.add(custIdJB);

					}
				}
				logger.info("filteredCustIdJBList===>" + filteredCustIdJBList);
				logger.info("filteredCustIdJBList size===>"+ filteredCustIdJBList.size());
			}
			
			Iterator<CustIdJB> custIdJbIte = filteredCustIdJBList.iterator();
			while(custIdJbIte.hasNext()){
				ArrayList<String> subscribersList = new ArrayList<String>();
				ArrayList<String> subscribersListWithCountryCode = new ArrayList<String>();
				custIdJBTmp = custIdJbIte.next();
				subscribersList=callCRMGetSubscriberNumbersBasedOnNationalID(custIdJBTmp);
				logger.info("subscribersList got from CRM for nationalID "+custIdJBTmp.getCustID()+" is ==>"+subscribersList);
				if(subscribersList!=null && subscribersList.size()>0){
					for(String num :subscribersList){
						num ="968"+num.trim();
						subscribersListWithCountryCode.add(num);
					}
				}
				
				logger.info("CRM response list after adding 968 ==>"+subscribersListWithCountryCode);
				subscriberNumberTableList.removeAll(subscribersListWithCountryCode);
				loyaltyRegisterNumberTableList.removeAll(subscribersListWithCountryCode);
				logger.info("subscribersList to be deleted from SubscriberNumberTable ==>"+subscriberNumberTableList);
				logger.info("subscribers to be deleted from register table is ==>"+loyaltyRegisterNumberTableList);
				if(!subscriberNumberTableList.isEmpty()){
					for(String subscriber :subscriberNumberTableList){
						if(nationalIdSyncDAO.updateInSubscriberNumber(subscriber))
							logger.info("status Successfully updated to 7 in subscriberNumberTable");
						else
							logger.info("Failed to update status in subscriberNumberTable");
					}
				}
				
				if(!loyaltyRegisterNumberTableList.isEmpty()){
					for(String subscriber :loyaltyRegisterNumberTableList){
						if(nationalIdSyncDAO.updateLoyaltyRegisterNumber(subscriber))
							logger.info("status Successfully updated to 7 in LoyaltyRegisterNumberTable");
						else
							logger.info("Failed to update status in LoyaltyRegisterNumberTable");
					}
				}
				
				subscribersList=null;
				subscribersListWithCountryCode=null;
				
			}

		}
		}catch(Exception ex){
			logger.error("Exception in changeStatus()"+ex);
		}
		
	}
	
	public String removeLeadingZeros(String str ){
		if (str == null){
		return null;}
		char[] chars = str.toCharArray();
		int index = 0;
		for (; index < str.length();index++)
		{
		if (chars[index] != '0'){
		break;}
		}
		return (index == 0) ? str :str.substring(index);
		}
	
}

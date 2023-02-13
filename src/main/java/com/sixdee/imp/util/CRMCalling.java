package com.sixdee.imp.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import om.omantel.arbor.inquiryservices.AccountSummaryJB;
import om.omantel.arbor.inquiryservices.CustIdJB;
import om.omantel.arbor.inquiryservices.SubscriberInfoJB;
import om.omantel.arbor.inquiryservices.SubscriberInfoService;
import om.omantel.arborservice.EnquiryGetIDServiceStub;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AccountNumber_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AddAccountInfoReq;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AddAccountInfoRequestType;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AddAccountInfoRes;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AddAccountInfoResponseType;
import om.omantel.arborservice.EnquiryGetIDServiceStub.CustNationality_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.EaiReference_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetAddAccountInfoRequest;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetAddAccountInfoResponse;
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
import om.omantel.arborservice.EnquiryGetIDServiceStub.ReferenceNo_type5;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorChannelId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorLanguage_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorSecurityInfo_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorUserId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.ReturnCode_type1;
import omanmobile.services.inquiry.InquiryServiceMsgSetPortType;
import omanmobile.services.inquiry.InquiryServiceMsgSetSOAP_HTTP_Service;
import omanmobile.services.inquiry.InquiryServiceMsgSetSOAP_HTTP_ServiceLocator;
import omanmobile.services.inquiry._XML_XML1_InternetServiceInformationReply;
import omanmobile.services.inquiry._XML_XML1_InternetServiceInformationRequest;
import omanmobile.services.inquiry._XML_XML1_OM_EAI_MESSAGE_REPLY;
import omanmobile.services.inquiry._XML_XML1_OM_EAI_MESSAGE_REQUEST;
import omanmobile.services.inquiry._XML_XML1_Request;

import org.apache.log4j.Logger;

import com.sixdee.NotificationModule.NotificationTokens;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.SubscriberDetailsDTO;

import com_omanmobile_scp_webservices_oscpws.OSCPWSImplServiceStub;
import com_omanmobile_scp_webservices_oscpws.OSCPWSImplServiceStub.AccountInfo;
import com_omanmobile_scp_webservices_oscpws.OSCPWSImplServiceStub.CustIdInfo;
import com_omanmobile_scp_webservices_oscpws.OSCPWSImplServiceStub.GetAccountInformation;
import com_omanmobile_scp_webservices_oscpws.OSCPWSImplServiceStub.GetAccountInformationResponse;

public class CRMCalling {
	
	private final static Logger logger=Logger.getLogger(CRMCalling.class);
	
	public void getAccountInfo(LoyaltyProfileTabDTO loyaltyProfileTabDTO,String accountNumber)
	{
		try{
			
			/*if(loyaltyProfileTabDTO.getAccountNumber()==null)
				return;*/
			
			GetAccountInformation accountInformation=new GetAccountInformation();
			
			accountInformation.setAccountNumber(accountNumber);
			
			OSCPWSImplServiceStub serviceStub=new OSCPWSImplServiceStub(Cache.getCacheMap().get("CRM_CALL_ACCOUNT_URL"));
			
			GetAccountInformationResponse accountInformationResponse= serviceStub.getAccountInformation(accountInformation);
			
			AccountInfo accountInfo=accountInformationResponse.get_return();
			
			if(accountInfo.getErrorCode()!=null&&accountInfo.getErrorCode().equalsIgnoreCase("0000"))
			{
				if(accountInfo.getCustStatus()!=null&&accountInfo.getCustStatus().equalsIgnoreCase("ACTIVE"))
				{
					loyaltyProfileTabDTO.setDefaultLanguage(accountInfo.getLanguageCode());
					loyaltyProfileTabDTO.setContactNumber(accountInfo.getCustPhone());
					loyaltyProfileTabDTO.setAddress(accountInfo.getCustCityEnglish());
					
					
					CustIdInfo[] custIdInfos= accountInfo.getCustId();
					
					if(custIdInfos!=null)
					{
						for(CustIdInfo custIdInfo:custIdInfos)
						{
							if(custIdInfo.getCustIdType().trim().equalsIgnoreCase("101"))
							{
								//loyaltyProfileTabDTO.setCustID(custIdInfo.getCustID());
								//loyaltyProfileTabDTO.setCustIDType(custIdInfo.getCustIdType());
							}
						}
					}
				}
			}
			
			
			/*_XML_XML1_AccountSummaryRequest accountSummaryRequest=new _XML_XML1_AccountSummaryRequest();
			
			accountSummaryRequest.setCustNum("AccountNumber");
			
			InquiryServiceMsgSetSOAP_HTTP_Service inquiryServiceMsgSetSOAP_HTTP_Service=new InquiryServiceMsgSetSOAP_HTTP_ServiceLocator();
			
			InquiryServiceMsgSetPortType msgSetPortType= inquiryServiceMsgSetSOAP_HTTP_Service.getSOAP_HTTP_Port();
			
			_XML_XML1_Request request=new _XML_XML1_Request();
			
			request.setAccountSummaryRequest(accountSummaryRequest);
			
			_XML_XML1_OM_EAI_MESSAGE_REQUEST _xml_xml1_om_eai_message_request=new _XML_XML1_OM_EAI_MESSAGE_REQUEST();
			
			_xml_xml1_om_eai_message_request.setRequest(request);
			
			_XML_XML1_OM_EAI_MESSAGE_REPLY _xml_xml1_om_eai_message_reply=msgSetPortType.inquiryService(_xml_xml1_om_eai_message_request);
			
			_XML_XML1_AccountSummaryReply accountSummaryReply= _xml_xml1_om_eai_message_reply.getReply().getAccountSummaryReply();
			
			accountSummaryReply.get*/
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getBasicSubscriberInfo(LoyaltyProfileTabDTO loyaltyProfileTabDTO,String subscriberNumber,String transactionID) throws CRMException
	{
	
		SubscriberInfoService infoService=null;
		SubscriberInfoJB subscriberInfoJB=null;
		AccountSummaryJB accountInfo=null;
		List<String> statusList=null;
		//List<String> accountCategoryList=null;
		List<String> custIDTypeList=null;
		String retrunNotificationTemplate=null;
		try{
			
			logger.info(transactionID+" : getBasicSubscriberInfo "+Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));
			
			infoService=new SubscriberInfoService(Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));
			
			subscriberInfoJB=infoService.getSubscriberInfo("C1_"+transactionID,subscriberNumber);
			
			logger.info(transactionID+" : Basic Profile Return Code "+subscriberInfoJB.getReturnCode());
			
			if(subscriberInfoJB.getReturnCode()==null || !subscriberInfoJB.getReturnCode().equalsIgnoreCase("0000")||subscriberInfoJB.getCustAccountNumber()==null||subscriberInfoJB.getCustAccountNumber().trim().equals(""))
			{
				accountInfo = infoService.getAccountInfo("C2_"+transactionID, subscriberNumber);
				logger.info(transactionID+" : Account Return Code  "+accountInfo.getReturnCode());
				if(accountInfo.getReturnCode()==null||!accountInfo.getReturnCode().equalsIgnoreCase("0000"))
					return retrunNotificationTemplate;
				
				loyaltyProfileTabDTO.setAccountNumber(subscriberNumber);
			}
			
			if(subscriberInfoJB.getReturnCode()!=null&&subscriberInfoJB.getReturnCode().equalsIgnoreCase("0000")&&subscriberInfoJB.getCustAccountNumber()!=null&&!subscriberInfoJB.getCustAccountNumber().trim().equalsIgnoreCase(""))
			{
				loyaltyProfileTabDTO.setFirstName(subscriberInfoJB.getCustName());
				loyaltyProfileTabDTO.setAccountNumber(subscriberInfoJB.getCustAccountNumber());
				accountInfo = infoService.getAccountInfo("C2_"+transactionID, subscriberInfoJB.getCustAccountNumber());
			}
			
			
			     
			if(accountInfo==null)
				return retrunNotificationTemplate;
			
				logger.info(transactionID+" : Account Return Code  "+accountInfo.getReturnCode());
				if(accountInfo.getReturnCode()!=null&&accountInfo.getReturnCode().equalsIgnoreCase("0000"))
				{
					logger.info(transactionID+" : Cust Number "+accountInfo.getCustName());
					logger.info(transactionID+" : subscriber status  "+accountInfo.getCustStatus());
					logger.info(transactionID+" :  HVC Flag  "+accountInfo.getHvcFlag());
					logger.info(transactionID+" :  AcctCategory  "+accountInfo.getAcctCategory());
					logger.info(transactionID+" :  Line List "+accountInfo.getSiList());
					logger.info(transactionID+" :  Cust ID List "+accountInfo.getCustIdList());
					
					loyaltyProfileTabDTO.setFirstName(accountInfo.getCustName());
					
					/*if(accountInfo.getCustStatus()==null || !accountInfo.getCustStatus().trim().equalsIgnoreCase("ACTIVE"))
					{
						logger.info(" Subscriber Number : "+subscriberNumber+ " is not active status " );
						return;
					}*/
					
					
					if(Cache.getCacheMap().get("STATUS")!=null)
					{
					  	if(Cache.getCacheMap().get("STATUS").contains(","))
					  		statusList=Arrays.asList(Cache.getCacheMap().get("STATUS").split(","));
					  	else
					  	{
					  		statusList=new ArrayList<String>();
					  		statusList.add(Cache.getCacheMap().get("STATUS"));
					  	}
					}
					
					logger.info(transactionID+" : Configre Status List "+statusList);
					
					if(accountInfo.getCustStatus()==null||!statusList.contains(accountInfo.getCustStatus().trim()))
					{
						logger.info(transactionID+" : Subscriber Number : "+subscriberNumber+ " is not in configured Status List "+statusList);
						return NotificationTokens.createLoyalty_loyalty_StausType_id;
					}
					
					
					/*List<String> hvcFlagList=null;
					if(Cache.getCacheMap().get("HVC_FLAG")!=null)
					{
					  	if(Cache.getCacheMap().get("HVC_FLAG").contains(","))
					  		hvcFlagList=Arrays.asList(Cache.getCacheMap().get("HVC_FLAG").split(","));
					  	else
					  	{
					  		hvcFlagList=new ArrayList<String>();
					  		hvcFlagList.add(Cache.getCacheMap().get("HVC_FLAG"));
					  	}
					}
					
					logger.info("Configre HVC Flag "+hvcFlagList);
					
					if(accountInfo.getHvcFlag()==null||!hvcFlagList.contains(accountInfo.getHvcFlag().trim()))
					{
						logger.info(" Subscriber Number : "+subscriberNumber+ " is not in configured HVC Flag List "+hvcFlagList);
						return;
					}*/
					
					
					
					/*if(Cache.getCacheMap().get("ACCOUNT_CATEGORY")!=null)
					{
					  	if(Cache.getCacheMap().get("ACCOUNT_CATEGORY").contains(","))
					  		accountCategoryList=Arrays.asList(Cache.getCacheMap().get("ACCOUNT_CATEGORY").split(","));
					  	else
					  	{
					  		accountCategoryList=new ArrayList<String>();
					  		accountCategoryList.add(Cache.getCacheMap().get("ACCOUNT_CATEGORY"));
					  	}
					}*/
					
					logger.info(transactionID+": Configured Account Category "+Cache.accountCategoryList);
					
					loyaltyProfileTabDTO.setDefaultLanguage(accountInfo.getLanguageCode());
					loyaltyProfileTabDTO.setCategory(accountInfo.getAcctCategory());
					loyaltyProfileTabDTO.setGender(accountInfo.getCustGender());
					
					if(Cache.accountCategoryList==null || accountInfo.getAcctCategory()==null || !Cache.accountCategoryList.contains(accountInfo.getAcctCategory().trim()))
					{
						logger.info(transactionID+" : Subscriber Number : "+subscriberNumber+ " is not in configured Account Category List "+Cache.accountCategoryList);
						return NotificationTokens.createLoyalty_loyalty_AccCategory_id;
					}
					
						
								
								
								if(Cache.getCacheMap().get("CUST_ID_TYPE")!=null)
								{
								  	if(Cache.getCacheMap().get("CUST_ID_TYPE").contains(","))
								  		custIDTypeList=Arrays.asList(Cache.getCacheMap().get("CUST_ID_TYPE").split(","));
								  	else
								  	{
								  		custIDTypeList=new ArrayList<String>();
								  		custIDTypeList.add(Cache.getCacheMap().get("CUST_ID_TYPE"));
								  	}
								}
								
								logger.info(transactionID+" : Configre Cust ID List "+custIDTypeList);
								
								List<CustIdJB> custIdInfos= accountInfo.getCustIdList();
								List<NationalNumberTabDTO> list=new ArrayList<NationalNumberTabDTO>();
								
								if(custIdInfos!=null&&custIDTypeList!=null)
								{
									for(CustIdJB custIdInfo:custIdInfos)
									{
										logger.info(transactionID+" :  Cust ID  "+custIdInfo.getCustID());
										logger.info(transactionID+" :  Cust ID Type  :"+custIdInfo.getCustIdType()+":");
										
										if(custIDTypeList.contains(custIdInfo.getCustIdType().trim()))
										{
											logger.info(transactionID+" : Consider "+custIdInfo.getCustIdType());
											NationalNumberTabDTO nationalNumberTabDTO=new NationalNumberTabDTO();
											String nationalId=custIdInfo.getCustID().trim();
											nationalId=nationalId.replaceFirst("^0+(?!$)", "");
											
											nationalNumberTabDTO.setNationalNumber(nationalId);
											nationalNumberTabDTO.setIdType(custIdInfo.getCustIdType().trim());
											
											if(!list.contains(nationalNumberTabDTO))
											   list.add(nationalNumberTabDTO);
										}
										logger.info(transactionID+" :  FInal List "+list);
										
									}
								}
								
								loyaltyProfileTabDTO.setCustIdList(list);
								list=null;
								custIdInfos=null;
								
								
								
						if(loyaltyProfileTabDTO.getCustIdList()==null||loyaltyProfileTabDTO.getCustIdList().size()<=0)
						{
							return NotificationTokens.createLoyalty_loyalty_CustType_id;
						}
								
								
					}
					
			
			
	        
	        
			
			
			/*GetBasicSubscriberInfoRequest basicSubscriberInfoRequest=new GetBasicSubscriberInfoRequest();
			basicSubscriberInfoRequest.setReferenceNum(new CommonUtil().getTransaction());
			basicSubscriberInfoRequest.setMsisdn(subscriberNumber);
			
			InquiryServiceMsgSetSOAP_HTTP_Service inquiryServiceMsgSetSOAP_HTTP_Service=new InquiryServiceMsgSetSOAP_HTTP_ServiceLocator();
			
			InquiryServiceMsgSetPortType msgSetPortType= inquiryServiceMsgSetSOAP_HTTP_Service.getSOAP_HTTP_Port(new URL(Cache.getCacheMap().get("CRM_CALL_BASIC_URL")));
			
			
			_XML_XML1_Request request=new _XML_XML1_Request();
			
			request.setGetBasicSubscriberInfoRequest(basicSubscriberInfoRequest);
			
			_XML_XML1_OM_EAI_MESSAGE_REQUEST _xml_xml1_om_eai_message_request=new _XML_XML1_OM_EAI_MESSAGE_REQUEST();
			
			_XML_XML1_OM_EAI_HEADER _xml_xml1_om_eai_header=new _XML_XML1_OM_EAI_HEADER();
			
			_xml_xml1_om_eai_header.setMsgFormat("GETBASICSUBSCRIBERINFO.SERVICE");
			_xml_xml1_om_eai_header.setMsgVersion("0000");
			_xml_xml1_om_eai_header.setRequestorId("OM");
			_xml_xml1_om_eai_header.setRequestorChannelId("IP"); // Our IP
			_xml_xml1_om_eai_header.setRequestorUserId("wbimbtst");
			_xml_xml1_om_eai_header.setRequestorLanguage("E");
			_xml_xml1_om_eai_header.setRequestorSecurityInfo("");
			_xml_xml1_om_eai_header.setEaiReference("0");
			_xml_xml1_om_eai_header.setReturnCode("");
			
			_xml_xml1_om_eai_message_request.setOM_EAI_HEADER(_xml_xml1_om_eai_header);
			
			

			_xml_xml1_om_eai_message_request.setRequest(request);
			
			
			_XML_XML1_OM_EAI_MESSAGE_REPLY _xml_xml1_om_eai_message_reply=msgSetPortType.inquiryService(_xml_xml1_om_eai_message_request);
			
			GetBasicSubscriberInfoResponse basicSubscriberInfoResponse= _xml_xml1_om_eai_message_reply.getReply().getGetBasicSubscriberInfoResponse();
			
			if(basicSubscriberInfoResponse.getReturnCode()!=null&&basicSubscriberInfoResponse.getReturnCode().equalsIgnoreCase("0000"))
			{
				loyaltyProfileTabDTO.setFirstName(basicSubscriberInfoResponse.getCustName());
				loyaltyProfileTabDTO.setAccountNumber(basicSubscriberInfoResponse.getCustAccountNumber());
				basicSubscriberInfoResponse.getCustCompany();
			}
			*/
			
		}catch (Exception e) {
			logger.info(transactionID+" : ",e);
			throw new CRMException("CRM_1000",e.getLocalizedMessage());
		}finally{
			infoService=null;
			subscriberInfoJB=null;
			accountInfo=null;
			statusList=null;
			//accountCategoryList=null;
			custIDTypeList=null;
		}
		
		return retrunNotificationTemplate;
		
		
	}//getBasicSubscriberInfo
	
	public void validateADSL(String adslLoginName)
	{
		
		try{
			
			_XML_XML1_InternetServiceInformationRequest informationRequest=new _XML_XML1_InternetServiceInformationRequest();
			
			informationRequest.setLoginName(adslLoginName);
			
			
			//InquiryServiceMsgSetSOAP_HTTP_Service inquiryServiceMsgSetSOAP_HTTP_Service=new InquiryServiceMsgSetSOAP_HTTP_ServiceLocator();
			
			//InquiryServiceMsgSetPortType msgSetPortType= inquiryServiceMsgSetSOAP_HTTP_Service.getSOAP_HTTP_Port();
			
			_XML_XML1_OM_EAI_MESSAGE_REQUEST _xml_xml1_om_eai_message_request=new _XML_XML1_OM_EAI_MESSAGE_REQUEST();
			
			_XML_XML1_Request request=new _XML_XML1_Request();
			
			request.setInternetServiceInformationRequest(informationRequest);
			
			_xml_xml1_om_eai_message_request.setRequest(request);
			
			//_XML_XML1_OM_EAI_MESSAGE_REPLY _xml_xml1_om_eai_message_reply=msgSetPortType.inquiryService(_xml_xml1_om_eai_message_request);
	
			//_XML_XML1_InternetServiceInformationReply internetServiceInformationReply= _xml_xml1_om_eai_message_reply.getReply().getInternetServiceInformationReply();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}//validateADSL

	public Object[] getAllLineNumber(LoyaltyProfileTabDTO loyaltyProfileTabDTO,String transactionId)throws CRMException
	{
		Map<String,SubscriberDetailsDTO> map=new HashMap<String, SubscriberDetailsDTO>();
		Map<String,List<String>> accountLineMap=new HashMap<String, List<String>>();
		Object[] objects=new Object[2];
		String custId="";
		
		EnquiryGetIDServiceStub stub=null;
		GetIDRequest request=new GetIDRequest();
		
		try{

			logger.info(transactionId+" : getAllLineNumber "+Cache.getCacheMap().get("CRM_CALL_NATIONAL_URL"));
			
			stub=new EnquiryGetIDServiceStub(Cache.getCacheMap().get("CRM_CALL_NATIONAL_URL"));

			if(loyaltyProfileTabDTO.getCustIdList()!=null)
			{
				
				for(NationalNumberTabDTO nationalNumberTabDTO:loyaltyProfileTabDTO.getCustIdList())
				{
					
					GetIDReq getIDReq=new GetIDReq();
					
					GetIDReqestType reqestType=new GetIDReqestType();
					
					ID_type1 id_type1=new ID_type1();
					
					id_type1.setID_type0(nationalNumberTabDTO.getNationalNumber());

					IDType_type1 type_type1= getIDType(nationalNumberTabDTO.getIdType());
					
					custId+=nationalNumberTabDTO.getNationalNumber()+",";
					
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
					
					
					
					String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
					Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
					
					
					if(infoTypes!=null&&infoTypes.length>0)
					{
						SubscriberDetailsDTO subscriberDetailsDTO=null;
						for(IDInfoType infoType:infoTypes)
						{
							subscriberDetailsDTO=new SubscriberDetailsDTO();
							
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
							
							subscriberDetailsDTO.setSubscriberNumber(infoType.getSubscriberNumber().getSubscriberNumber_type0());
							subscriberDetailsDTO.setAccountType(Integer.parseInt(Cache.accountTypeMap.get(infoType.getSubscriberType().getSubscriberType_type0().trim())));
							subscriberDetailsDTO.setAccountNumber(infoType.getAccountNumber().getAccountNumber_type2()==null?null:infoType.getAccountNumber().getAccountNumber_type2());
							subscriberDetailsDTO.setAccountCategory(infoType.getAccountCategory().getAccountCategory_type0());
			
							logger.info(transactionId+" : getAllLineNumber  Account Type "+infoType.getSubscriberType().getSubscriberType_type0()+"  "+subscriberDetailsDTO.getAccountType());
							logger.info(transactionId+" : _____________________________________________________________________________________________________");
							
							
							
							if(subscriberDetailsDTO.getSubscriberNumber()!=null&&subscriberDetailsDTO.getSubscriberNumber().trim().length()==subscriberSize)
							{
								subscriberDetailsDTO.setSubscriberNumber(subscriberCountryCode+subscriberDetailsDTO.getSubscriberNumber());
							}else{
								continue;
							}
							
							map.put(subscriberDetailsDTO.getSubscriberNumber(),subscriberDetailsDTO);
			
							if(subscriberDetailsDTO.getAccountNumber()!=null)
							{
								if(accountLineMap.get(subscriberDetailsDTO.getAccountNumber()+"")!=null)
								{
									accountLineMap.get(subscriberDetailsDTO.getAccountNumber()+"").add(subscriberDetailsDTO.getSubscriberNumber());
								}else{
									List<String> list=new ArrayList<String>();
									list.add(subscriberDetailsDTO.getSubscriberNumber());
									accountLineMap.put(subscriberDetailsDTO.getAccountNumber()+"",list);
								}
							}
							
						}
					}
					
				}
				
				
			}
			
			if(!custId.equals("")&&custId.charAt(custId.length()-1)==',')
				custId=custId.substring(0,custId.length()-1);
			
			loyaltyProfileTabDTO.setCustID(custId);
			
			 
		
		}catch (Exception e) {
			logger.info(transactionId+" : ",e);
			throw new CRMException("CRM_1000",e.getLocalizedMessage());
		}finally{
			stub=null;
			request=null;
		}
		objects[0]=map;
		objects[1]=accountLineMap;
		return objects;
		
	}//getAllLineNumber
	
	
	
	public void getAdditionalAccountInfo(LoyaltyProfileTabDTO loyaltyProfileTabDTO,String transactionID) throws CRMException
	{
		SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
		EnquiryGetIDServiceStub serviceStub=null;
		GetAddAccountInfoResponse accountInfoResponse=null;
 		CustNationality_type1 custNationality_type1 =null;
		try{
			
			logger.info(transactionID+" : getAdditionalAccountInfo "+Cache.getCacheMap().get("CRM_CALL_NATIONAL_URL"));
			
			serviceStub=new EnquiryGetIDServiceStub(Cache.getCacheMap().get("CRM_CALL_NATIONAL_URL"));
		 	
		 	GetAddAccountInfoRequest accountInfoRequest=new GetAddAccountInfoRequest();
		 	
		 	AddAccountInfoRequestType accountInfoRequestType=new AddAccountInfoRequestType();
		 	
		 	AccountNumber_type1 accountNumber_type1=new AccountNumber_type1();
		 	accountNumber_type1.setAccountNumber_type0(loyaltyProfileTabDTO.getAccountNumber());
		 	
		 	
		 	accountInfoRequestType.setAccountNumber(accountNumber_type1);
		 	ReferenceNo_type5 no_type1=new ReferenceNo_type5();
		 	no_type1.setReferenceNo_type4("C4_"+transactionID);
		 	accountInfoRequestType.setReferenceNo(no_type1);
		 	
		 	AddAccountInfoReq accountInfoReq=new AddAccountInfoReq();
		 	
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
		 	
		 	
		 	accountInfoReq.setOT_EAI_HEADER(ot_eai_headerType);
		 	accountInfoReq.setRequest(accountInfoRequestType);
		 	
		 	accountInfoRequest.setGetAddAccountInfoRequest(accountInfoReq);
		 	
		 	accountInfoResponse= serviceStub.getAdditionalAccountInfo(accountInfoRequest);
		 	
		 	AddAccountInfoRes accountInfoRes= accountInfoResponse.getGetAddAccountInfoResponse();
		 	
		 	AddAccountInfoResponseType accountInfoResponseType= accountInfoRes.getResponse();
		 	
		 	logger.info(transactionID+" :  getAdditionalAccountInfo Return Code "+accountInfoResponseType.getReturnCode().getReturnCode_type2());
		 	
		 	
		 	if(accountInfoResponseType.getReturnCode()!=null&&accountInfoResponseType.getReturnCode().getReturnCode_type2()!=null&&accountInfoResponseType.getReturnCode().getReturnCode_type2().equalsIgnoreCase("0000"))
		 	{
		 		if(accountInfoResponseType.getCustNameEnglish()!=null)
		 		logger.info(transactionID+" :  getAdditionalAccountInfo Name "+accountInfoResponseType.getCustNameEnglish().getCustNameEnglish_type0());
		 		if(accountInfoResponseType.getCustNameArabic()!=null)
		 		logger.info(transactionID+" :  getAdditionalAccountInfo Arbic Name "+accountInfoResponseType.getCustNameArabic().getCustNameArabic_type0());
		 		if(accountInfoResponseType.getDateOfBirth()!=null)
		 		logger.info(transactionID+" :  getAdditionalAccountInfo DOB "+accountInfoResponseType.getDateOfBirth().getDateOfBirth_type0());
		 		if(accountInfoResponseType.getCustPhone()!=null)
		 		logger.info(transactionID+" :  getAdditionalAccountInfo Cust Phone "+accountInfoResponseType.getCustPhone().getCustPhone_type0());
		 		if(accountInfoResponseType.getEmailId()!=null)
		 		logger.info(transactionID+" :  getAdditionalAccountInfo  Email "+accountInfoResponseType.getEmailId().getEmailId_type0());
		 		if(accountInfoResponseType.getCustGender()!=null)
		 			logger.info(transactionID+" :  getAdditionalAccountInfo  Gender "+accountInfoResponseType.getCustGender().getCustGender_type0());
		 		
		 		if(accountInfoResponseType.getCustNameEnglish()!=null)
		 		   loyaltyProfileTabDTO.setFirstName(accountInfoResponseType.getCustNameEnglish().getCustNameEnglish_type0());
		 		
		 		if(accountInfoResponseType.getCustNameArabic()!=null)
		 		   loyaltyProfileTabDTO.setArbicFirstName(accountInfoResponseType.getCustNameArabic().getCustNameArabic_type0());
		 		
		 		
		 		if(accountInfoResponseType.getDateOfBirth()!=null&&accountInfoResponseType.getDateOfBirth().getDateOfBirth_type0()!=null)
		 		   loyaltyProfileTabDTO.setDateOfBirth(dateFormat.parse(accountInfoResponseType.getDateOfBirth().getDateOfBirth_type0()));
		 		
		 		if(accountInfoResponseType.getCustPhone()!=null)
		 			loyaltyProfileTabDTO.setContactNumber(accountInfoResponseType.getCustPhone().getCustPhone_type0());
		 		
		 		if(accountInfoResponseType.getEmailId()!=null)
		 			loyaltyProfileTabDTO.setEmailID(accountInfoResponseType.getEmailId().getEmailId_type0());
		 		
		 		if(accountInfoResponseType.getCustGender()!=null)
		 			loyaltyProfileTabDTO.setGender(accountInfoResponseType.getCustGender().getCustGender_type0());
		 		
		 		
		 		
		 		String address="";
		 		address=(accountInfoResponseType.getCustEngAddr1()==null?"":accountInfoResponseType.getCustEngAddr1().getCustEngAddr1_type0());
		 		address=(address.trim().equalsIgnoreCase("")?"":address+",")+(accountInfoResponseType.getCustEngAddr2()==null?"":accountInfoResponseType.getCustEngAddr2().getCustEngAddr2_type0());
		 		address=(address.trim().equalsIgnoreCase("")?"":address+",")+(accountInfoResponseType.getCustEngAddr3()==null?"":accountInfoResponseType.getCustEngAddr3().getCustEngAddr3_type0());
		 		
		 		loyaltyProfileTabDTO.setAddress(address);
		 		
		 		address=(accountInfoResponseType.getCustArabicAddr1()==null?"":accountInfoResponseType.getCustArabicAddr1().getCustArabicAddr1_type0());
		 		address=(address.trim().equalsIgnoreCase("")?"":address+",")+(accountInfoResponseType.getCustArabicAddr2()==null?"":accountInfoResponseType.getCustArabicAddr2().getCustArabicAddr2_type0());
		 		address=(address.trim().equalsIgnoreCase("")?"":address+",")+(accountInfoResponseType.getCustArabicAddr3()==null?"":accountInfoResponseType.getCustArabicAddr3().getCustArabicAddr3_type0());
		 		
		 		loyaltyProfileTabDTO.setArbicAddress(address);
		 		custNationality_type1 = accountInfoResponseType.getCustNationality();
		 		String nationalityType = custNationality_type1!=null?custNationality_type1.getCustNationality_type0():Constants.emptyString;
		 		loyaltyProfileTabDTO.setNationality(nationalityType!=null?nationalityType:Constants.emptyString);
		 		
		 		
		 	}
		 	
		}catch (Exception e) {
			logger.info(transactionID+" : ",e);
			throw new CRMException("CRM_1000",e.getLocalizedMessage());
		}finally{
			serviceStub=null;
			accountInfoResponse=null;
		}
		
		
		
	}//getAdditionalAccountInfo
	
	
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
	
	
	public int getBasicSubscriberInfo1(LoyaltyProfileTabDTO loyaltyProfileTabDTO,String subscriberNumber,String transactionID) throws CRMException
	{
	
		SubscriberInfoService infoService=null;
		SubscriberInfoJB subscriberInfoJB=null;
		AccountSummaryJB accountInfo=null;
		int retrunNotificationTemplate=0;
		try{
			logger.info(transactionID+" : getBasicSubscriberInfo1 "+Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));
			
			infoService=new SubscriberInfoService(Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));
			
			subscriberInfoJB=infoService.getSubscriberInfo("C1_"+transactionID,subscriberNumber);
			
			logger.info(transactionID+" : Basic Profile Return Code "+subscriberInfoJB.getReturnCode()+" and custType "+subscriberInfoJB.getCustType());
			
			loyaltyProfileTabDTO.setCustID(subscriberInfoJB.getCustType());
			
			if(subscriberInfoJB.getReturnCode()==null || !subscriberInfoJB.getReturnCode().equalsIgnoreCase("0000")||subscriberInfoJB.getCustAccountNumber()==null||subscriberInfoJB.getCustAccountNumber().trim().equals(""))
			{
				accountInfo = infoService.getAccountInfo("C2_"+transactionID, subscriberNumber);
				logger.info(transactionID+" : Account Return Code  "+accountInfo.getReturnCode());
				if(accountInfo.getReturnCode()==null||!accountInfo.getReturnCode().equalsIgnoreCase("0000"))
					return retrunNotificationTemplate;
				
				loyaltyProfileTabDTO.setAccountNumber(subscriberNumber);
			}
			
			if(subscriberInfoJB.getReturnCode()!=null&&subscriberInfoJB.getReturnCode().equalsIgnoreCase("0000")&&subscriberInfoJB.getCustAccountNumber()!=null&&!subscriberInfoJB.getCustAccountNumber().trim().equalsIgnoreCase(""))
			{
				//loyaltyProfileTabDTO.setFirstName(subscriberInfoJB.getCustName());
				loyaltyProfileTabDTO.setAccountNumber(subscriberInfoJB.getCustAccountNumber());
				accountInfo = infoService.getAccountInfo("C2_"+transactionID, subscriberInfoJB.getCustAccountNumber());
			}
			
			
			     
			if(accountInfo==null)
				return retrunNotificationTemplate;
			
			
			logger.info(transactionID+" : Account Return Code  "+accountInfo.getReturnCode());
			if(accountInfo.getReturnCode()!=null&&accountInfo.getReturnCode().equalsIgnoreCase("0000"))
			{
				logger.info(transactionID+" :  AcctCategory "+accountInfo.getAcctCategory());
			}
			
			getAdditionalAccountInfo(loyaltyProfileTabDTO, transactionID); // Calling for aditional info
			
/*			if(!Cache.accountCategoryServiceList.contains(accountInfo.getAcctCategory().trim()))
				//if(!accountInfo.getAcctCategory().equalsIgnoreCase("Residential"))
			{
				retrunNotificationTemplate = 1; // Failure
				return retrunNotificationTemplate;
			}
			else
*/				
		/*	if(loyaltyProfileTabDTO.getDateOfBirth()!=null && loyaltyProfileTabDTO.getNationality()!=null && !loyaltyProfileTabDTO.getNationality().equalsIgnoreCase("") &&
					loyaltyProfileTabDTO.getGender()!=null && !loyaltyProfileTabDTO.getGender().equalsIgnoreCase(""))
*/				retrunNotificationTemplate=2;  // Success
/*			else
				retrunNotificationTemplate =3 ; //Failure
*/						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			infoService=null;
			subscriberInfoJB=null;
			accountInfo=null;
		}
		return retrunNotificationTemplate;
	}
	
	
	
}//

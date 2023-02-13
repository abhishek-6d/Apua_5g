/**
 * 
 */
package com.sixdee.ussd.Adapter;

import java.io.BufferedInputStream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.ussd.dto.KeywordMappingDTO;
import com.sixdee.ussd.dto.MessageTemplateDTO;
import com.sixdee.ussd.dto.TransactionHistoryDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.serviceCall.GenerateServiceCall;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.parser.UssdRequestParser;
import com.sixdee.ussd.util.parser.UssdResponseParser;

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
public class UssdClientAdapter extends HttpServlet {

	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(UssdClientAdapter.class);
	private static final long serialVersionUID = 8260535684096193868L;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		execute(req,resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp) {
		/*
		 * Has to parse the xml recieved and has to validate it 
		 * Pooling will not be the case here since flow will be synchronous
		 */
		UssdRequestDTO ussdReqDTO = null;
		ArrayList<Object> objList =null;
		ArrayList<Service> responseList = null;
		ServiceList serviceList = null;
		Service service = null;
		UssdResponseDTO ussdResponseDTO = null;
		GenerateServiceCall generateServiceCall = null;
		InputStream inputStream = null;
		StringBuffer sb = null;
		MessageTemplateDTO messageTemplateDTO = null;
		TransactionHistoryDTO transactionHistoryDTO = null;
		String xml = null;
		String respXML = null;
		int langId = 0;
		InputStream is = null;
		try{
					is = req.getInputStream();
					inputStream = new BufferedInputStream(is,32 * 1024);
					sb = new StringBuffer();
					int character = -1;
					while ((character = inputStream.read()) != -1) {
						sb.append((char) character);
					}
					  if (sb.toString() != null && !sb.toString().equals(""))
							xml = sb.toString();
			//System.out.println(xml);
			if(logger.isDebugEnabled())
				logger.debug("Xml recieved "+xml);
			if(xml == null){
				logger.error("xml is null");
				respXML = "XML Recieved is null";
			}else{
			ussdReqDTO = validateRequest(xml);
				if (ussdReqDTO != null) {
					logger.info("Valid Request recieved for Request Id ["
							+ ussdReqDTO.getTransactionId() + "]");

					transactionHistoryDTO = new TransactionHistoryDTO();
					transactionHistoryDTO.setTransactionId(Long.parseLong(ussdReqDTO
							.getTransactionId()));
					transactionHistoryDTO.setSessionId(Long
							.parseLong(ussdReqDTO.getSessionId()));
					transactionHistoryDTO
							.setMessageText(ussdReqDTO.getMsisdn());
					transactionHistoryDTO.setTraversalPath(ussdReqDTO
							.getTraversalPath());
					boolean isLangId = false;
					for(Parameters param :ussdReqDTO.getServiceList().getServices().get(0).getParamList()){
						if(param.getId().equalsIgnoreCase("LanguageId")){
							langId = param.getValue()!=null?Integer.parseInt(param.getValue()):AppCache.langId;
							ussdReqDTO.setLangId(langId);
							//logger.info("")
							isLangId = true;
							break;
						}
						
					}
					if(!isLangId)
						ussdReqDTO.setLangId(1);
					transactionHistoryDTO.setMessageText(ussdReqDTO
							.getServiceList().getServices().get(0)
							.getMessageText());
					transactionHistoryDTO.setReqXml(xml);
					transactionHistoryDTO.setStage(1);
					//ThreadInitiator.auditPool.addTask(transactionHistoryDTO);
					objList = getKeyWordAndResponse(ussdReqDTO);

					if (objList.get(0) != null && objList.size() > 0) {
						/*
						 * Call to the service
						 */
						generateServiceCall = new GenerateServiceCall();
						ussdResponseDTO = generateServiceCall.makeServiceCall(
								ussdReqDTO, objList);
						if (ussdResponseDTO.getStatus() == null)
							ussdResponseDTO.setStatus("SC0000");
						if (ussdResponseDTO.getStatusDesc() == null)
							ussdResponseDTO.setStatusDesc("SUCCESS");
					} else {
						/*
						 * Send service not configured make response xml
						 */
						logger.info("No Service found for request ["
								+ ussdReqDTO.getTransactionId() + "]");
						responseList = new ArrayList<Service>();
						service = new Service();
						service.setMenuIndex(""+1+"");
						messageTemplateDTO = ((HashMap<String, MessageTemplateDTO>) AppCache.util
								.get("MESSAGE_TEMPLATE")).get("FC0005_"+ussdReqDTO.getLangId());
						if (messageTemplateDTO != null) {
							service.setMessageText(messageTemplateDTO
									.getTemplateMessage());
						} else {
							service.setMessageText("Service Unavailable.Please try after some time");
						}
						ussdResponseDTO = new UssdResponseDTO();
						ussdResponseDTO.setMsisdn(ussdReqDTO.getMsisdn());
						ussdResponseDTO.setTransactionId(ussdReqDTO
								.getTransactionId());
						ussdResponseDTO.setStatus("FC0005");
						ussdResponseDTO.setStatusDesc("KeyWord Not Found");
						ussdResponseDTO.setEos(true);
						responseList.add(service);
						serviceList = new ServiceList();
						serviceList.setServices(responseList);
						ussdResponseDTO.setServiceList(serviceList);
					}
				}else{
				/*
				 * Send invalid request type
				 */
				ussdResponseDTO = new UssdResponseDTO();
				
				ussdResponseDTO.setStatus("FC0006");
				ussdResponseDTO.setStatusDesc("INVALID REQUEST");
				responseList = new ArrayList<Service>();
				service = new Service();
				service.setMenuIndex(""+1+"");
				messageTemplateDTO = ((HashMap<String, MessageTemplateDTO>)AppCache.util.get("MESSAGE_TEMPLATE")).get("FC0005_0_"+AppCache.langId);
				if(messageTemplateDTO!=null){
					service.setMessageText(messageTemplateDTO.getTemplateMessage());
				}else{
					service.setMessageText("System Error");
				}
				ussdResponseDTO.setEos(true);
				responseList.add(service);
				serviceList = new ServiceList();
				serviceList.setServices(responseList);
				ussdResponseDTO.setServiceList(serviceList);
		
			
			}
		//	ussdResponseDTO.setMsisdn(new UssdUtil().appendCountryCode(ussdResponseDTO.getMsisdn()));
			respXML  = UssdResponseParser.getUssdRespXStream().toXML(ussdResponseDTO);
			}
		}catch (CommonException e) {
			ussdResponseDTO = new UssdResponseDTO();
				ussdResponseDTO.setMsisdn(ussdResponseDTO.getMsisdn());
			ussdResponseDTO.setTransactionId(ussdReqDTO.getTransactionId());
			service = new Service();
			service.setMenuIndex(""+1+"");
			messageTemplateDTO = ((HashMap<String, MessageTemplateDTO>)AppCache.util.get("MESSAGE_TEMPLATE")).get("FC0005_0_"+ussdReqDTO.getLangId());
			if(messageTemplateDTO!=null){
				service.setMessageText(messageTemplateDTO.getTemplateMessage());
			}else{
				service.setMessageText("Service Unavailable.Please try after some time");
			}
			ussdResponseDTO.setStatus("FC0008");
			ussdResponseDTO.setStatusDesc(e.getMessage());
			ussdResponseDTO.setEos(true);
			responseList = new ArrayList<Service>();
			responseList.add(service);
			serviceList = new ServiceList();
			serviceList.setServices(responseList);
			ussdResponseDTO.setServiceList(serviceList);
		
			respXML = UssdResponseParser.getUssdRespXStream().toXML(ussdResponseDTO);
			logger.error("Exception occured ",e);
			e.printStackTrace();
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			ussdResponseDTO = new UssdResponseDTO();
			ussdResponseDTO.setStatus("FC0009");
			ussdResponseDTO.setStatusDesc("Unknown Failure");
				ussdResponseDTO.setMsisdn(ussdResponseDTO.getMsisdn());
			
			ussdResponseDTO.setTransactionId(ussdReqDTO.getTransactionId());
			service = new Service();
			service.setMenuIndex(""+1);
			messageTemplateDTO = ((HashMap<String, MessageTemplateDTO>)AppCache.util.get("MESSAGE_TEMPLATE")).get("FC0005_0_"+ussdReqDTO.getLangId());
			if(messageTemplateDTO!=null){
				service.setMessageText(messageTemplateDTO.getTemplateMessage());
			}else{
				service.setMessageText("System Error.Please try after some time");
			}
			ussdResponseDTO.setEos(true);
			responseList = new ArrayList<Service>();
			responseList.add(service);
			serviceList = new ServiceList();
			serviceList.setServices(responseList);
			ussdResponseDTO.setServiceList(serviceList);
		
			respXML = UssdResponseParser.getUssdRespXStream().toXML(ussdResponseDTO);
		}
		finally{
				SendRespMsg(resp,respXML);
	/*			transactionHistoryDTO = new TransactionHistoryDTO();
				transactionHistoryDTO.setTransactionId(Long.parseLong(ussdResponseDTO.getTransactionId()));
				transactionHistoryDTO.setSuccessCode(ussdResponseDTO.getStatus());
				transactionHistoryDTO.setSuccessDesc(ussdResponseDTO.getStatusDesc());
				transactionHistoryDTO.setStage(2);
*///		/		ThreadInitiator.auditPool.addTask(transactionHistoryDTO);
			
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	public void SendRespMsg(HttpServletResponse res, String xmlStrSend){

		
			BufferedWriter writer = null;
			OutputStreamWriter outputStreamWriter = null;
			OutputStream os = null;
			try {
				os = res.getOutputStream();
				outputStreamWriter = new OutputStreamWriter(os);
				writer = new BufferedWriter(outputStreamWriter);
				
				if (xmlStrSend == null) {
					throw new Exception("cannot send ack message");
				}
				if(logger.isDebugEnabled())
					logger.debug(xmlStrSend);
				writer.write(xmlStrSend);
				writer.flush();
			} catch (Exception exp) {
				logger.error("Got Exception:"+exp);
			} finally {
				
				if(writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					writer = null;
				}
				if(outputStreamWriter != null){
					try {
						outputStreamWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				xmlStrSend = null;
			}
		
	}
	
	/**
	 * Will get the response from user and find the service to be called next
	 * @param UssdRequestDTO which is parsed from user requsest
	 * @return ArrayList of objects of length 2
	 * At 0 index it will be KeywordMappingDTO
	 * At 1 index it will be response from user
	 */
	private ArrayList<Object> getKeyWordAndResponse(UssdRequestDTO ussdReqDTO) throws Exception {
		KeywordMappingDTO keywordMappingDTO = null;
		ArrayList<Service> serviceList = null;

		ArrayList<Object> objectList = new ArrayList<Object>();
		try{
			serviceList = ussdReqDTO.getServiceList().getServices();
			for(Service service : serviceList){
				if(service.getNextService()!=null){
					logger.info(AppCache.util.get("KEYWORDS")+" "+service.getNextService().toUpperCase()+"_"+ussdReqDTO.getLangId());
					keywordMappingDTO = ((HashMap<String,KeywordMappingDTO>)AppCache.util.get("KEYWORDS")).get(service.getNextService().toUpperCase()+"_"+ussdReqDTO.getLangId());
					//logger.info(AppCache.util.get("KEYWORDS"));
					if(keywordMappingDTO==null){
						logger.info("Keyword not found for Requested Lnaguage,searching for default ["+service.getNextService().toUpperCase()+"_"+AppCache.langId+"]");
						keywordMappingDTO = ((HashMap<String,KeywordMappingDTO>)AppCache.util.get("KEYWORDS")).get(service.getNextService().toUpperCase()+"_"+AppCache.langId);
						
					}
//						logger.info("Identified Keyword ["+service.getNextService()+"] serviceid ["+keywordMappingDTO.getServiceId()+"]");
						objectList.add(keywordMappingDTO);
				}else{
					logger.debug(AppCache.util.get("KEYWORDS")+" "+service.getMessageText().toUpperCase()+"_"+ussdReqDTO.getLangId());
					
					keywordMappingDTO = ((HashMap<String,KeywordMappingDTO>)AppCache.util.get("KEYWORDS")).get(service.getMessageText().trim().toUpperCase()+"_"+ussdReqDTO.getLangId());
					logger.debug(keywordMappingDTO+" "+(keywordMappingDTO==null));
					if(keywordMappingDTO==null){
						logger.debug("Keyword not found for Requested Lnaguage,searching for default ["+service.getMessageText().toUpperCase()+"_"+AppCache.langId+"]");
						
						keywordMappingDTO = ((HashMap<String,KeywordMappingDTO>)AppCache.util.get("KEYWORDS")).get(service.getMessageText().toUpperCase()+"_"+AppCache.langId);
						//logger.info(message)
						logger.debug(keywordMappingDTO);
					}
				
					objectList.add(keywordMappingDTO);
				}
				if(ussdReqDTO.getTraversalPath()==null || !(ussdReqDTO.getTraversalPath().endsWith("_UI"))){
					objectList.add(service.getMessageText());
				}else{
					for(Parameters param : service.getParamList()){
						if(param.getId().equalsIgnoreCase("USERDATA")){
							objectList.add(param.getValue());
							break;
						}
					}
				}
			}
			logger.debug(objectList);
			
		}catch (Exception e) {
			throw e;
		}finally{
			keywordMappingDTO = null;
			ussdReqDTO = null;
		}
		return objectList;
	}

	private UssdRequestDTO validateRequest(String xml) throws Exception {
		UssdRequestDTO ussdRequestDTO = null;
		Service serviceDTO = null;
		try{
			ussdRequestDTO = (UssdRequestDTO) UssdRequestParser.getUssdParser().fromXML(xml);
			//ussdRequestDTO = (UssdRequestDTO) ussdReqXsteam.fromXML(xml);
			if(ussdRequestDTO.getTransactionId()==null || ussdRequestDTO.getTransactionId().trim().equals("")){
				throw new CommonException("TransactionId is empty");
			}/*else if(ussdRequestDTO.getSessionId()== null || ussdRequestDTO.getSessionId().trim().equals("")){
				throw new CommonException("SessionId is empty");
			}*/else if(ussdRequestDTO.getTimeStamp() == null || ussdRequestDTO.getTimeStamp().trim().equals("")){
				throw new CommonException("Timestamp is empty");
			}else if(ussdRequestDTO.getMsisdn()==null || ussdRequestDTO.getMsisdn().trim().equals("") /* !(new Validations().validateSubsNumber(ussdRequestDTO.getMsisdn()))*/){
				throw new CommonException("Msisdn is empty or invalid "+ussdRequestDTO.getMsisdn());
			}else if(ussdRequestDTO.getStarCode()== null || ussdRequestDTO.getStarCode().trim().equals("")){
				throw new CommonException("StarCode is empty");
			}else if(ussdRequestDTO.getApplication()==null || ussdRequestDTO.getApplication().trim().equals("")){
				throw new CommonException("Application is empty");
			}else if(ussdRequestDTO.getChannel()==null || ussdRequestDTO.getChannel().trim().equals("")){
				throw new CommonException("Channel is empty");
			}
			serviceDTO = ussdRequestDTO.getServiceList().getServices().get(0);
			if(serviceDTO == null || serviceDTO.getMessageText() == null || serviceDTO.getMessageText().trim().equals("")){
				throw new CommonException("Message Text is empty");
			}
		/*	ussdUtl = new UssdUtil();
			ussdRequestDTO.setMsisdn(ussdUtl.removalOfCountryCode(ussdRequestDTO.getMsisdn()));
		*/	/*
			 * Validate if any
			 */
		}finally{
			serviceDTO = null;
		}
		
		return ussdRequestDTO;
	}
}

/**
 * 
 */
package com.sixdee.ussd.serviceCall;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.TransactionManagementStub;
import com.sixdee.ussd.webserviceHandler.TransactionManagementStub.GetTransactionDetails;
import com.sixdee.ussd.webserviceHandler.TransactionManagementStub.GetTransactionDetailsResponse;
import com.sixdee.ussd.webserviceHandler.TransactionManagementStub.TransactionDTO;
import com.sixdee.ussd.webserviceHandler.TransactionManagementStub.TransactionDetailsDTO;
import com.sixdee.ussd.webserviceHandler.TransactionManagementStub.TransactionInfoDTO;

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
public class TransactionDetailCall implements WSCallInter {

	private static final Logger logger = Logger.getLogger(TransactionDetailCall.class);
	
	
	@SuppressWarnings("unchecked")
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		UssdResponseDTO ussdResponseDTO = null;
		GetTransactionDetails getTransactionDetails = null;
		TransactionDTO transactionDTO = null;
		TransactionManagementStub transactionManagementStub = null;
		GetTransactionDetailsResponse getTransactionDetailsResponse = null;
		TransactionInfoDTO transactionInfoDTO = null;
		ArrayList<Service> recoList = null;
		ServiceList reccomendations = null;
		StringBuilder resp = null;
		ArrayList<Parameters> paramList = null;
		try{
			logger.info("RequestId ["+ussdRequestDTO.getTransactionId()+"] Msisdn ["+ussdRequestDTO.getMsisdn()+"] " +
					"URL ["+serviceRequestDTO.getUrl()+"] Message : Calling GetTransactionDetails API");
			transactionManagementStub = new TransactionManagementStub(serviceRequestDTO.getUrl());
			transactionDTO = new TransactionDTO();
			getTransactionDetails = new GetTransactionDetails();
			transactionDTO.setTransactionId(ussdRequestDTO.getTransactionId());
			transactionDTO.setTimestamp(ussdRequestDTO.getTimeStamp());
			transactionDTO.setChannel("USSD");
			transactionDTO.setNoOfLastTransaction(3);
//			transactionDTO.setPin(Integer.parseInt(response));
			transactionDTO.setSubscriberNumber(ussdRequestDTO.getMsisdn());
//			transactionDTO.setPin(Integer.parseInt(response));
			getTransactionDetails.setTransactionDTO(transactionDTO);
			getTransactionDetailsResponse = transactionManagementStub.getTransactionDetails(getTransactionDetails);
			transactionInfoDTO = getTransactionDetailsResponse.get_return();
			ussdResponseDTO = new UssdResponseDTO();
			String nextService = ((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId())!=null?((HashMap<Integer, ArrayList<String>>)AppCache.util.get("CHILD_RELATION")).get(serviceRequestDTO.getKeyWordId()).get(0):null;
			
			int j = 1;

			if (transactionInfoDTO.getStatusCode().equals("SC0000")) {
				ussdResponseDTO.setStatus("SC0000");
				//Service  reccomendation = new Service();
				
				for (TransactionDetailsDTO transactionDetails : transactionInfoDTO
						.getTransactionDetails()) {

					if (transactionDetails != null) {
					
						if(recoList == null){
							recoList = new ArrayList<Service>();
							Service reccomendation = new Service();
							reccomendation.setMenuIndex(""+1);
							reccomendation.setMessageText(((HashMap<String, String>)AppCache.util.get("USSD_MANAGER")).get("GET_TRANS_HEADER_"+ussdRequestDTO.getLangId()));
							recoList.add(reccomendation);
							
						}
						Service reccomendation = new Service();
						reccomendation.setMenuIndex(""+(++j));
						reccomendation.setMessageText(transactionDetails
								.getAccountLineNumber()
								+ ", "
								+ transactionDetails.getActivity()
								+ ", "
								+ transactionDetails.getLoyaltyPoints());
						recoList.add(reccomendation);
						Parameters param1 = new Parameters();
						param1.setId("LanguageId");
						param1.setValue(ussdRequestDTO.getLangId()+"");
						paramList = new ArrayList<Parameters>();
						paramList.add(param1);
						reccomendation.setParamList(paramList);
				
						
						// reccomendation.setNextService(nextService);
					} else {
						ussdResponseDTO.setStatus("F0102");
						ussdResponseDTO.setStatusDesc("No Transactions Found");
					}
				}
				
				

			}else{
				Service reccomendation  = new Service();
				reccomendation.setMenuIndex(""+(++j));
				reccomendation.setMessageText(transactionInfoDTO.getStatusDescription()!=null?transactionInfoDTO.getStatusDescription():"No Transaction Found");
				reccomendation.setNextService(nextService);
				recoList = new ArrayList<Service>();
				recoList.add(reccomendation);
			
			}
			reccomendations = new ServiceList();
		
			reccomendations.setServices(recoList);
			ussdResponseDTO.setEos(true);
			ussdResponseDTO.setServiceList(reccomendations);
		
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			throw e;
		}finally{
			ussdRequestDTO = null;
			transactionManagementStub = null;
		}
		return ussdResponseDTO;
	}

}

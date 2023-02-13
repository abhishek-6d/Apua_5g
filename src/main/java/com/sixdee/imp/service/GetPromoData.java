package com.sixdee.imp.service;

import com.sixdee.imp.service.serviceDTO.req.ActivatePackDTO;
import com.sixdee.imp.service.serviceDTO.req.EligiblePromoDTO;
import com.sixdee.imp.service.serviceDTO.req.TransactionHistoryDTO;
import com.sixdee.imp.service.serviceDTO.resp.EligiblePromoDetailsDTO;
import com.sixdee.imp.service.serviceDTO.resp.EligiblePromoInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;
import com.sixdee.imp.service.serviceDTO.resp.TransactionHistoryDetails;
import com.sixdee.imp.service.serviceDTO.resp.TransactionHistoryInfoDTO;

public class GetPromoData 
{
	public TransactionHistoryInfoDTO getTransactionHistory(TransactionHistoryDTO transactionHistoryDTO)
	{
		TransactionHistoryInfoDTO historyInfoDTO = new TransactionHistoryInfoDTO();
		TransactionHistoryDetails details[] = new TransactionHistoryDetails[1];
		details[0]	= new TransactionHistoryDetails();
		details[0].setActivationDate("22/04/2013");
		details[0].setStartDate("22/04/2013");
		details[0].setEndDate("22/05/2013");
		details[0].setServiceName("Bonus pack");
		details[0].setTransactionType("Credit");
		
		historyInfoDTO.setTransactionHistoryDetails(details);
		historyInfoDTO.setTranscationId(transactionHistoryDTO.getTransactionId());
		historyInfoDTO.setTimestamp("22042013174444");
		historyInfoDTO.setStatusCode("SC0000");
		historyInfoDTO.setStatusDescription("SUCCESS");
		
		return historyInfoDTO;
	}
	
	public EligiblePromoInfoDTO getEligiblePromo(EligiblePromoDTO eligiblePromoDTO)
	{
		EligiblePromoInfoDTO eligiblePromoInfoDTO = new EligiblePromoInfoDTO();
		EligiblePromoDetailsDTO detailsDTO[]= new EligiblePromoDetailsDTO[1] ;
		detailsDTO[0] = new EligiblePromoDetailsDTO();
		detailsDTO[0].setDate("22/04/2013") ;
		detailsDTO[0].setPromoName("Bonous Pack") ;
		detailsDTO[0].setSubscriberNumber("9742772892") ;
		
		eligiblePromoInfoDTO.setEligiblePromoDetailsDTO(detailsDTO);
		eligiblePromoInfoDTO.setTimestamp("22042013174444");
		eligiblePromoInfoDTO.setTranscationId(eligiblePromoDTO.getTransactionId());
		eligiblePromoInfoDTO.setStatusCode("SC0000");
		eligiblePromoInfoDTO.setStatusDescription("SUCCESS");
		
		return eligiblePromoInfoDTO;
	}
	
	public ResponseDTO activatePack(ActivatePackDTO activatePackDTO)
	{
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setTimestamp("22042013174444");
		responseDTO.setTranscationId(activatePackDTO.getTransactionId());
		responseDTO.setStatusCode("SC0000");
		responseDTO.setStatusDescription("SUCCESS");
		
		return responseDTO;
	}
	
	public ResponseDTO deActivatePack(ActivatePackDTO activatePackDTO)
	{
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setTimestamp("22042013174444");
		responseDTO.setTranscationId(activatePackDTO.getTransactionId());
		responseDTO.setStatusCode("SC0000");
		responseDTO.setStatusDescription("SUCCESS");
		
		return responseDTO;
	}
}

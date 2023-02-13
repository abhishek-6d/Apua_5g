package com.sixdee.imp.service;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.MonthlyBillServiceDTO;
import com.sixdee.imp.dto.UserprofileDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.EBillDTO;
import com.sixdee.imp.service.serviceDTO.req.MonthlyBillDTO;
import com.sixdee.imp.service.serviceDTO.resp.MonthlyBillRespDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class EBillManagement 
{
	private static final Logger log=Logger.getLogger(EBillManagement.class);
	
	public ResponseDTO eBillNotification(EBillDTO ebillDTO)
	{
		log.info("Transaction id -"+ebillDTO.getTransactionId()+" Channel -"+ebillDTO.getChannel()+" Msisdn -"+ebillDTO.getMsisdn()+" AccountNo -"+ebillDTO.getAccountNo()+" Request Recived ");
		ResponseDTO resp = null;
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		try
		{
			resp = new ResponseDTO();
			resp.setTranscationId(ebillDTO.getTransactionId());
			resp.setTimestamp(ebillDTO.getTimestamp());

			if(ebillDTO.getTransactionId() == null || ebillDTO.getTransactionId().trim().equalsIgnoreCase("")) {
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Transaction ID cannot be null");
				log.info(resp.getStatusDescription());
				return resp;
			}
			if(ebillDTO.getChannel()==null || ebillDTO.getChannel().equalsIgnoreCase("") )
			{
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Channel cannot be null");
				return resp;
			}
			if(ebillDTO.getAccountNo() == null || ebillDTO.getAccountNo().trim().equalsIgnoreCase("")) {
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Account number cannot be null");
				log.info(resp.getStatusDescription());
				return resp;
			}
			
			if(ebillDTO.getCustomerType() == null || ebillDTO.getCustomerType().trim().equalsIgnoreCase("")) {
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Customer Type cannot be null");
				log.info(resp.getStatusDescription());
				return resp;
			}

			adapter = new LMSWebServiceAdapter();
			genericDTO = (GenericDTO) adapter.callFeature("EbillNotification", ebillDTO);
			resp.setStatusCode(genericDTO.getStatusCode());
			resp.setStatusDescription(genericDTO.getStatus());
		}
		catch (Exception e) {
			log.error("Exception for transaction id = "+ebillDTO.getTransactionId(),e);
		}
		finally
		{
			log.info("Transaction id -"+ebillDTO.getTransactionId()+" Channel -"+ebillDTO.getChannel()+" Msisdn -"+ebillDTO.getMsisdn()+" AccountNo -"+ebillDTO.getAccountNo()+" Request Leaving System ");
			adapter = null;
			genericDTO = null;
		}
		return resp;
	}
	
	
	@SuppressWarnings("unused")
	public MonthlyBillRespDTO getMonthlyBill(MonthlyBillDTO monthlyBillDTO)
	{
		log.info("Transaction id -"+monthlyBillDTO.getTransactionId()+" Channel -"+monthlyBillDTO.getChannel()+" Msisdn -"+monthlyBillDTO.getMsisdn()+" AccountNo -"+monthlyBillDTO.getAccountNo()+" Request Recived ");
		MonthlyBillRespDTO resp = null;
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		MonthlyBillServiceDTO dto=null;
		try
		{
			resp = new MonthlyBillRespDTO();
			resp.setTranscationId(monthlyBillDTO.getTransactionId());
			resp.setTimestamp(monthlyBillDTO.getTimestamp());

			if(monthlyBillDTO.getTransactionId() == null || monthlyBillDTO.getTransactionId().trim().equalsIgnoreCase("")) {
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Transaction ID cannot be null");
				log.info(resp.getStatusDescription());
				return resp;
			}
			if(monthlyBillDTO.getChannel()==null || monthlyBillDTO.getChannel().equalsIgnoreCase("") )
			{
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Channel cannot be null");
				return resp;
			}
			if(monthlyBillDTO.getAccountNo() == null || monthlyBillDTO.getAccountNo().trim().equalsIgnoreCase("")) {
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Account number cannot be null");
				log.info(resp.getStatusDescription());
				return resp;
			}
			
			

			adapter = new LMSWebServiceAdapter();
			genericDTO = (GenericDTO) adapter.callFeature("MonthlyBillService", monthlyBillDTO);
			if(genericDTO!=null)
				dto = (MonthlyBillServiceDTO) genericDTO.getObj();
			log.info("SATATUS CODE COMING"+dto.getStatusCode());
			if (genericDTO == null) {
				log.info("Transaction id -"+monthlyBillDTO.getTransactionId()+" Channel -"+monthlyBillDTO.getChannel()+" Msisdn -"+monthlyBillDTO.getMsisdn()+" AccountNo -"+monthlyBillDTO.getAccountNo()+" GOT FAILURE");
				
				resp.setStatusCode(Cache.getServiceStatusMap().get("MONTHLY_API_NOT"+1).getStatusCode());
				resp.setStatusDescription(Cache.getServiceStatusMap().get("MONTHLY_API_NOT"+1).getStatusDesc());
			}
			else if (dto.getStatusCode().equalsIgnoreCase("SC0000")) {
				dto = (MonthlyBillServiceDTO) genericDTO.getObj();
				// profileDTO.setStatusCode("SC0000");//profileDTO.setStatusDescription("SUCCESS");
				log.info("Transaction id -"+monthlyBillDTO.getTransactionId()+" Channel -"+monthlyBillDTO.getChannel()+" Msisdn -"+monthlyBillDTO.getMsisdn()+" AccountNo -"+monthlyBillDTO.getAccountNo()+" GOT Success");
				
				resp.setAccountNo(dto.getAccountNo());
				resp.setLoyalityId(dto.getLoyalityId());
				resp.setTierName(dto.getTierName());
				resp.setOpeningBalance(dto.getOpeningBalance());
				resp.setPointsEarned(dto.getPointsEarned());
				resp.setPointsRedeemed(dto.getPointsRedeemed());
				resp.setClosingBalance(dto.getClosingBalance());
				resp.setStatusPoints(dto.getStatusPoints());
				resp.setStatmentDate(dto.getStatmentDate());
				resp.setBillCycleStartDate(dto.getBillCycleStartDate());
				resp.setStatusCode(dto.getStatusCode());
				resp.setStatusDescription(dto.getStatusDescription());
			}else
			{
				dto = (MonthlyBillServiceDTO) genericDTO.getObj();
				resp.setStatusCode(dto.getStatusCode());
				resp.setStatusDescription(dto.getStatusDescription());
			}

			
			
		}
		catch (Exception e) {
			log.error("Exception for transaction id = "+monthlyBillDTO.getTransactionId(),e);
		}
		finally
		{
			log.info("Transaction id -"+monthlyBillDTO.getTransactionId()+" Channel -"+monthlyBillDTO.getChannel()+" Msisdn -"+monthlyBillDTO.getMsisdn()+" AccountNo -"+monthlyBillDTO.getAccountNo()+" Request Leaving System ");
			adapter = null;
			genericDTO = null;
		}
		return resp;
	}
	
}

package com.sixdee.imp.service;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.DeviceDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class DeviceManagement 
{
	Logger log = Logger.getLogger(DeviceManagement.class);
	public ResponseDTO createDeviceSegment(DeviceDTO deviceDTO)
	{
		log.info("Transaction id -"+deviceDTO.getTransactionId()+" Channel -"+deviceDTO.getChannel()+" Msisdn -"+deviceDTO.getSubscriberNumber()+" model -"+deviceDTO.getModelName()+" Request Recived ");
		ResponseDTO resp = null;
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		try
		{
			resp = new ResponseDTO();
			resp.setTranscationId(deviceDTO.getTransactionId());
			resp.setTimestamp(deviceDTO.getTimestamp());

			if(deviceDTO.getSubscriberNumber() == null || deviceDTO.getSubscriberNumber().trim().equalsIgnoreCase("")) {
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Subscriber number can not be null");
				log.info(resp.getStatusDescription());
				return resp;
			}
			if(deviceDTO.getChannel()==null || deviceDTO.getChannel().equalsIgnoreCase("") )
			{
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Channel Can not be null");
				return resp;
			}
			if(deviceDTO.getModelName()==null || deviceDTO.getModelName().equalsIgnoreCase("") )
			{
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Model Name can not be null");
				return resp;
			}
			
			Data data[] = new Data[1];
			data[0] = new Data();
			data[0].setName("Action");
			data[0].setValue("I");
			deviceDTO.setData(data);

			adapter = new LMSWebServiceAdapter();
			genericDTO = (GenericDTO) adapter.callFeature("DeviceMnagement", deviceDTO);
			resp.setStatusCode(genericDTO.getStatusCode());
			resp.setStatusDescription(genericDTO.getStatus());

		}
		catch (Exception e) {
			log.error("Exception for transaction id = "+deviceDTO.getTransactionId(),e);
		}
		finally
		{
			log.info("Transaction id -"+deviceDTO.getTransactionId()+" Channel -"+deviceDTO.getChannel()+" Msisdn -"+deviceDTO.getSubscriberNumber()+" Request Leaving ");
			adapter = null;
			genericDTO = null;
		
		}
		return resp;
	}
	
	public ResponseDTO deleteDeviceSegment(DeviceDTO deviceDTO)
	{
		log.info("Transaction id -"+deviceDTO.getTransactionId()+" Channel -"+deviceDTO.getChannel()+" Msisdn -"+deviceDTO.getSubscriberNumber()+" model -"+deviceDTO.getModelName()+" Request Recived ");
		ResponseDTO resp = null;
		LMSWebServiceAdapter adapter = null;
		GenericDTO genericDTO = null;
		try
		{
			resp = new ResponseDTO();
			resp.setTranscationId(deviceDTO.getTransactionId());
			resp.setTimestamp(deviceDTO.getTimestamp());

			if(deviceDTO.getSubscriberNumber() == null || deviceDTO.getSubscriberNumber().trim().equalsIgnoreCase("")) {
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Subscriber number can not be null");
				log.info(resp.getStatusDescription());
				return resp;
			}
			if(deviceDTO.getChannel()==null || deviceDTO.getChannel().equalsIgnoreCase("") )
			{
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Channel Can not be null");
				return resp;
			}
			if(deviceDTO.getModelName()==null || deviceDTO.getModelName().equalsIgnoreCase("") )
			{
				resp.setStatusCode("SC0001");
				resp.setStatusDescription("Model Name can not be null");
				return resp;
			}
			
			Data data[] = new Data[1];
			data[0] = new Data();
			data[0].setName("Action");
			data[0].setValue("D");
			deviceDTO.setData(data);

			adapter = new LMSWebServiceAdapter();
			genericDTO = (GenericDTO) adapter.callFeature("DeviceMnagement", deviceDTO);
			resp.setStatusCode(genericDTO.getStatusCode()!=null?genericDTO.getStatusCode():"SC0001");
			resp.setStatusDescription(genericDTO.getStatus()!=null?genericDTO.getStatus():"Failure !!");

		}
		catch (Exception e) {
			log.error("Exception for transaction id = "+deviceDTO.getTransactionId(),e);
		}
		finally
		{
			log.info("Transaction id -"+deviceDTO.getTransactionId()+" Channel -"+deviceDTO.getChannel()+" Msisdn -"+deviceDTO.getSubscriberNumber()+" Request Leaving ");
			adapter = null;
			genericDTO = null;
		
		}
		return resp;
	}
}

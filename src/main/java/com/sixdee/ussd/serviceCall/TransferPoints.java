package com.sixdee.ussd.serviceCall;

import com.sixdee.imp.service.PointManagement;
import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.PointManagementStub;

public class TransferPoints implements WSCallInter {

	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		UssdResponseDTO ussdResponseDTO = null;
		PointManagementStub pointManagementStub = null ;
		TransferPoints transferPoints = null;
		String url = null;
		try{
			transferPoints = new TransferPoints();
			
			url = serviceRequestDTO.getUrl();
			pointManagementStub  = new PointManagementStub(url);
			//pointManagementStub.transferPoints(transferPoints2)
		}finally{
			
		}
		return ussdResponseDTO;
	}

}

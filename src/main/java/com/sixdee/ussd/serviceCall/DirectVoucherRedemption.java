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
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantPackages;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantPackagesResponse;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.MerchantDetailsDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.MerchantInfoDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.MerchantPackageDetailsDTO;

/**
 * @author Bhavya
 *
 */
public class DirectVoucherRedemption implements WSCallInter {

	private static final Logger logger = Logger
			.getLogger(DirectVoucherRedemption.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sixdee.ussd.util.inter.WSCallInter#callService(com.sixdee.ussd.dto
	 * .ServiceRequestDTO,
	 * com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO, java.lang.String)
	 */
	@Override
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response) throws Exception {
		// TODO Auto-generated method stub
		UssdResponseDTO ussdResponseDTO = null;
		String merchantId = null;
		String msisdn = null;
		ArrayList<Parameters> values = null;
		Parameters parameters = null;
		String transactionId = null;
		String timeStamp = null;
		String channel = null;
		String statusCode = null;
		String statusDesc = null;
		try {
			ServiceList serviceList = new ServiceList();
			parameters = new Parameters();
			ArrayList<Service> serviceArray = new ArrayList<Service>();
			Service service = null;
			Service serviceResp = new Service();
			ussdResponseDTO = new UssdResponseDTO();

			msisdn = ussdRequestDTO.getMsisdn();
			transactionId = ussdRequestDTO.getTransactionId();
			timeStamp = ussdRequestDTO.getTimeStamp();
			channel = ussdRequestDTO.getChannel();

			logger.info("Transaction id [" + transactionId + "]::Session Id ["
					+ ussdRequestDTO.getSessionId()
					+ "] Request recieved for Merchant ,calling url ["
					+ serviceRequestDTO.getUrl() + "]");

			service = ussdRequestDTO.getServiceList().getServices().get(0);
			values = service.getParamList();

			for (Parameters param : values) {
				if (param.getId().equalsIgnoreCase("MerchantId")) {
					merchantId = param.getValue();
				}
			}
			UserProfileManagementStub userProfileManagementStub = new UserProfileManagementStub(
					serviceRequestDTO.getUrl());
			GetMerchantPackages getMerchantPackages = new GetMerchantPackages();
			MerchantDetailsDTO merchantDetailsDTO = new MerchantDetailsDTO();
			GetMerchantPackagesResponse getMerchantPackagesResponse = new GetMerchantPackagesResponse();
			MerchantInfoDTO merchantInfoDTO = new MerchantInfoDTO();
			MerchantPackageDetailsDTO packageDetailsDTOArray = new MerchantPackageDetailsDTO();

			merchantDetailsDTO.setTransactionId(transactionId);
			merchantDetailsDTO.setTimestamp(timeStamp);
			merchantDetailsDTO.setSubscriberNumber(msisdn);
			merchantDetailsDTO.setChannel(channel);
			merchantDetailsDTO.setMerchantId(merchantId);

			getMerchantPackages.setMerchantDTO(merchantDetailsDTO);
			getMerchantPackagesResponse = userProfileManagementStub
					.getMerchantPackages(getMerchantPackages);
			merchantInfoDTO = getMerchantPackagesResponse.get_return();
			merchantInfoDTO.getData();
			statusCode = merchantInfoDTO.getStatusCode();
			statusDesc = merchantInfoDTO.getStatusDescription();
			MerchantPackageDetailsDTO[] packageDetailsDTOArray1 = merchantInfoDTO
					.getPackages();
			String nextService = ((HashMap<Integer, ArrayList<String>>) AppCache.util
					.get("CHILD_RELATION")).get(serviceRequestDTO
					.getKeyWordId()) != null ? ((HashMap<Integer, ArrayList<String>>) AppCache.util
					.get("CHILD_RELATION")).get(
					serviceRequestDTO.getKeyWordId()).get(0) : null;

			int i = 0;

			for (MerchantPackageDetailsDTO packageDetailsDTO : packageDetailsDTOArray1) {
				++i;

				if (packageDetailsDTO != null) {
					MerchantPackageDetailsDTO[] packages = packageDetailsDTO
							.getSubPackageDetails();
					for (int j = 0; j < packages.length; j++) {
						serviceResp = new Service();
						serviceResp.setMenuIndex(j + 1);
						serviceResp
								.setMessageText(packages[j].getPackageName());
						serviceResp.setNextService(nextService);
						serviceArray.add(serviceResp);
						ArrayList<Parameters> paramList = new ArrayList<Parameters>();
						Parameters parameters2 = new Parameters();
						parameters2.setId("TYPE");
						parameters2.setValue(msisdn+"_"+ussdRequestDTO.getLangId());
						Parameters parameters3 = new Parameters();
						parameters3.setId("parent");
						parameters3.setValue(packages[j].getPackageID() + "");
						paramList.add(parameters2);
						paramList.add(parameters3);
						serviceResp.setParamList(paramList);
					}
				} else {
					serviceResp = new Service();
					serviceResp.setMenuIndex(1);
					serviceResp.setMessageText(statusDesc);
					serviceArray.add(serviceResp);
					ussdResponseDTO.setEos(true);
				}
			}

			serviceList.setServices(serviceArray);
			ussdResponseDTO.setServiceList(serviceList);
			logger.info("Transaction id [" + transactionId + "]::Session Id ["
					+ ussdRequestDTO.getSessionId() + "]::Message text::"
					+ statusDesc);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Transaction id [" + transactionId + "]::Session Id ["
					+ ussdRequestDTO.getSessionId()
					+ "]Exception occured in try block]", e);
			ussdResponseDTO.setEos(true);
		} finally {
			ussdResponseDTO.setStatus(statusCode);
			ussdResponseDTO.setStatusDesc(statusDesc);
		}
		return ussdResponseDTO;
	}

}

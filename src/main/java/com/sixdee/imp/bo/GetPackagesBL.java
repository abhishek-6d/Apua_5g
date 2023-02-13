package com.sixdee.imp.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.CommonUtilDAO;
import com.sixdee.imp.dao.GetPackagesDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.GetPackagesDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.MerchantNomenclatureTabDto;
import com.sixdee.imp.dto.OfferMasterTabDto;
import com.sixdee.imp.dto.OfferNomenclatureTabDto;
import com.sixdee.imp.dto.OfferTeleAttruibutes;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.service.serviceDTO.resp.PackageDetailsDTO;
import com.sixdee.imp.service.serviceDTO.resp.PackageInfoDTO;

public class GetPackagesBL extends BOCommon {

	private final static Logger logger = Logger.getLogger(GetPackagesBL.class);
	private static final String DATE_FORMAT = "dd-MM-yy";
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {
		logger.info("Class => GetPackagesBL :: Method => buildProcess()");

		GetPackagesDAO getPackagesDAO = null;
		GetPackagesDTO getPackagesDTO = (GetPackagesDTO) genericDTO.getObj();
		List<Integer> channelTypeOffer = null;
		List<Integer> accountTypeOffer = null;
		List<Integer> tierMappingOffer = null;
		List<LoyaltyProfileTabDTO> loyaltyProfileInfo = null;
		PackageInfoDTO packageInfoDTO = null;
		boolean isSubPkgDetails = false;
		String key=null;
		String statusCode=null;
		String statusDescription=null;
		int languageId;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO=null;
		TableDetailsDAO tableDetailsDAO =null; 
		try {
			getPackagesDAO = new GetPackagesDAO();
			packageInfoDTO = new PackageInfoDTO();
			languageId=getPackagesDTO.getLanguageId();
			tableDetailsDAO = new TableDetailsDAO();
			logger.info("Transaction id "+getPackagesDTO.getTransactionId() +"  language id "+getPackagesDTO.getLanguageId());
			
			customerProfileTabDTO = tableDetailsDAO.getCustomerProfile(getPackagesDTO.getSubscriberNumber());
			if(customerProfileTabDTO!=null){
				loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(customerProfileTabDTO.getLoyaltyId());
				if(loyaltyProfileTabDTO!=null){
				  if(customerProfileTabDTO.getStatusId()!=null){	
					if(customerProfileTabDTO.getStatusId().equalsIgnoreCase("5")){
						key="CUSTOMER_IN_SOFT_DELETE_";
						genericDTO=customeValidation(key,getPackagesDTO,genericDTO);
						statusCode=genericDTO.getStatusCode();
						statusDescription=genericDTO.getStatus();
						throw new CommonException();
					}else if(customerProfileTabDTO.getStatusId().equalsIgnoreCase("10")){
						key="CUSTOMER_IS_IN_FRAUD_";
						genericDTO=customeValidation(key,getPackagesDTO,genericDTO);
						statusCode=genericDTO.getStatusCode();
						statusDescription=genericDTO.getStatus();
						throw new CommonException();
					}
				  }
				}else{
					key="USER_NOT_EXIST_";
					genericDTO=customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					throw new CommonException();
				}
			}else{
				key="USER_NOT_EXIST_";
				genericDTO=customeValidation(key,getPackagesDTO,genericDTO);
				statusCode=genericDTO.getStatusCode();
				statusDescription=genericDTO.getStatus();
				throw new CommonException();
			}
			
			// Validation for giving subPackageDetails
			if (getPackagesDTO.getIsFirstLevel() != null) {
				if (getPackagesDTO.getIsFirstLevel().equalsIgnoreCase("true")) {
					isSubPkgDetails = false;
					if (getPackagesDTO.getOfferId() != null) {
						isSubPkgDetails = true;
					}
				} else {
					isSubPkgDetails = true;
				}

			}
			if (getPackagesDTO.getOfferType().equalsIgnoreCase("-1") && getPackagesDTO.getOfferId() == null) {
				// Cache.channelDetails.get(getPackagesDTO.getChannel());
				List<OfferMasterTabDto> validOfferDtoPointValidation = null;
				String channelId = Cache.channelDetails.get(getPackagesDTO.getChannel());
				if (channelId == null) {
					key="CHANNEL_MAP_REQ_";
					genericDTO=customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					logger.info("Channel configuration not available in the LMS_CNFG_CHANNEL_DETAILS Table for the channel id "+getPackagesDTO.getChannel()+" Transcation id "+getPackagesDTO.getTransactionId());
					throw new CommonException();
				}
				logger.info("Request for Sending all Offers TransactionId " + getPackagesDTO.getTransactionId());
				channelTypeOffer = getPackagesDAO.getOfferBasedOnChannelTypeDB(channelId);
				if (channelTypeOffer == null || channelTypeOffer.size() < 1) {
					key="CHANNEL_MAP_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					logger.info("Channel Mapping configuration not available in the LMS_CSEG_OFFER_CHANNEL Table for the channel id "+channelId+" Transcation id "+getPackagesDTO.getTransactionId());
					throw new CommonException();
				}
				
				loyaltyProfileInfo = loyaltyProfileDetails(getPackagesDTO);
				
				if (loyaltyProfileInfo == null) {
					key="LOYALTY_PROFILE_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					logger.info("Loyalty information not available , please check the subscriber and loyaltyprofile table for the subscriber Number "+getPackagesDTO.getSubscriberNumber()+" Transcation id "+getPackagesDTO.getTransactionId());
					throw new CommonException();
				}
				
				accountTypeOffer = getOfferBasedonAccountType(getPackagesDTO, channelTypeOffer);

				if (accountTypeOffer == null || accountTypeOffer.size() < 1) {
					key="ACCOUNT_TYPE_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					logger.info("Account Type information not available , please check these two tables LMS_CSEG_OFFER_ACCOUNT_MAPPING , CUSTOMER_PROFILE Transcation id "+getPackagesDTO.getTransactionId());
					throw new CommonException();
				}
				
				if (loyaltyProfileInfo != null) {
					int tierId = loyaltyProfileInfo.get(0).getTierId();
					tierMappingOffer = getPackagesDAO.getOfferBasedOnTierId(tierId, accountTypeOffer);
				}
				logger.info("Calling BuildPkgDetails");
				
				if (tierMappingOffer == null || tierMappingOffer.size() < 1) {
					key="OFFER_NOT_AVAILABLE_TIER_MAPPING_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					throw new CommonException();
				}
				
				validOfferDtoPointValidation = pointValidationOffer(loyaltyProfileInfo, tierMappingOffer);
				
				if (validOfferDtoPointValidation == null || validOfferDtoPointValidation.size() < 1) {
					key="OFFER_POINTS_NOT_VALIDATED_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					throw new CommonException();
				}
				
				packageInfoDTO = buildPackageInfoDetails(validOfferDtoPointValidation,isSubPkgDetails,languageId);

				if (packageInfoDTO == null) {
					key="PACKAGE_INFO_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					throw new CommonException();
				} else {
					key="GET_PACKAGE_SUCCESS_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
				}

				packageInfoDTO = setStatusGetpkg(packageInfoDTO, getPackagesDTO, statusCode, statusDescription);

				genericDTO.setObj(packageInfoDTO);
			} else if (getPackagesDTO.getOfferId() != null) {
				List<OfferMasterTabDto> validOfferDtoPointValidation = null;
				logger.info("calling getofferid not equal to null condition ");
				tierMappingOffer = new ArrayList<Integer>();
				tierMappingOffer.add(Integer.valueOf(getPackagesDTO.getOfferId()));
				loyaltyProfileInfo = loyaltyProfileDetails(getPackagesDTO);

				if (loyaltyProfileInfo == null) {
					key="LOYALTY_PROFILE_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					throw new CommonException();
				}

				validOfferDtoPointValidation = pointValidationOffer(loyaltyProfileInfo, tierMappingOffer);
				if (validOfferDtoPointValidation == null || validOfferDtoPointValidation.size() < 1) {
					key="OFFER_POINTS_NOT_VALIDATED_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					throw new CommonException();
				}
				
				packageInfoDTO = buildPackageInfoDetails(validOfferDtoPointValidation,isSubPkgDetails,languageId);

				if (packageInfoDTO == null) {
					key="PACKAGE_INFO_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					throw new CommonException();
				} else {
					key="GET_PACKAGE_SUCCESS_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
				}

				packageInfoDTO = setStatusGetpkg(packageInfoDTO, getPackagesDTO, statusCode, statusDescription);
				genericDTO.setObj(packageInfoDTO);

			} else if (!getPackagesDTO.getOfferType().equalsIgnoreCase("-1") && getPackagesDTO.getOfferId() == null) {
				logger.info("Request reached for get pkg by offerType");
				logger.info("Is subpkg flag " + isSubPkgDetails);
				List<OfferMasterTabDto> validOfferDtoPointValidation = null;
				
				String channelId = Cache.channelDetails.get(getPackagesDTO.getChannel());
				if (channelId == null) {
					key="CHANNEL_MAP_REQ_";
					genericDTO=customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					logger.info("Channel configuration not available in the LMS_CNFG_CHANNEL_DETAILS Table for the channel id "+getPackagesDTO.getChannel()+" Transcation id "+getPackagesDTO.getTransactionId());
					throw new CommonException();
				}
				logger.info("Request for Sending all Offers TransactionId " + getPackagesDTO.getTransactionId());
				channelTypeOffer = getPackagesDAO.getOfferBasedOnChannelTypeDB(channelId);
				if (channelTypeOffer == null || channelTypeOffer.size() < 1) {
					key="CHANNEL_MAP_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					logger.info("Channel Mapping configuration not available in the LMS_CSEG_OFFER_CHANNEL Table for the channel id "+channelId+" Transcation id "+getPackagesDTO.getTransactionId());
					throw new CommonException();
				}
				
				List<Integer> offerMasterTabDto = null;
				loyaltyProfileInfo = loyaltyProfileDetails(getPackagesDTO);
				if (loyaltyProfileInfo == null) {
					key="LOYALTY_PROFILE_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					logger.info("Loyalty information not available , please check the subscriber and loyaltyprofile table for the subscriber Number "+getPackagesDTO.getSubscriberNumber()+" Transcation id "+getPackagesDTO.getTransactionId());
					throw new CommonException();
				}
				offerMasterTabDto = getPackagesDAO.getOfferMasterDetailsBasedOfferTypeIDAndChannelValidation(Integer.valueOf(getPackagesDTO.getOfferType()),channelTypeOffer);
				
				accountTypeOffer = getOfferBasedonAccountType(getPackagesDTO, offerMasterTabDto);

				if (accountTypeOffer == null || accountTypeOffer.size() < 1) {
					key="ACCOUNT_TYPE_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					logger.info("Account Type information not available , please check these two tables LMS_CSEG_OFFER_ACCOUNT_MAPPING , CUSTOMER_PROFILE Transcation id "+getPackagesDTO.getTransactionId());
					throw new CommonException();
				}
				
				if (loyaltyProfileInfo != null) {
					int tierId = loyaltyProfileInfo.get(0).getTierId();
					tierMappingOffer = getPackagesDAO.getOfferBasedOnTierId(tierId, accountTypeOffer);
				}
				
				validOfferDtoPointValidation = pointValidationOffer(loyaltyProfileInfo, tierMappingOffer);
				
				if (validOfferDtoPointValidation == null || validOfferDtoPointValidation.size() < 1) {
						key="OFFER_POINTS_NOT_VALIDATED_";
						customeValidation(key,getPackagesDTO,genericDTO);
						statusCode=genericDTO.getStatusCode();
						statusDescription=genericDTO.getStatus();
						throw new CommonException();
					}
					
				packageInfoDTO = buildPackageInfoDetails(validOfferDtoPointValidation,isSubPkgDetails,languageId);
					
				
				if (packageInfoDTO == null) {
					key="PACKAGE_INFO_REQ_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
					throw new CommonException();
				} else {
					key="GET_PACKAGE_SUCCESS_";
					customeValidation(key,getPackagesDTO,genericDTO);
					statusCode=genericDTO.getStatusCode();
					statusDescription=genericDTO.getStatus();
				}
				packageInfoDTO = setStatusGetpkg(packageInfoDTO, getPackagesDTO, statusCode, statusDescription);
				genericDTO.setObj(packageInfoDTO);
			}

		} catch (CommonException e) {
			e.printStackTrace();
			packageInfoDTO = setStatusGetpkg(packageInfoDTO, getPackagesDTO, statusCode, statusDescription);
			genericDTO.setObj(packageInfoDTO);
			genericDTO.setStatus(statusDescription);
		} catch (Exception e) {
			if (packageInfoDTO != null) {
				packageInfoDTO = new PackageInfoDTO();
			}
			packageInfoDTO.setStatusCode("SC0001");
			genericDTO.setObj(packageInfoDTO);
			packageInfoDTO.setStatusDescription(e.getMessage());
			logger.info("Error", e);
			e.printStackTrace();
		} finally {

			getPackagesDAO = null;
			getPackagesDTO = null;
			channelTypeOffer = null;
			accountTypeOffer = null;
			tierMappingOffer = null;
			loyaltyProfileInfo = null;
			packageInfoDTO = null;
			isSubPkgDetails = false;
			key = null;
			statusCode = null;
			statusDescription = null;
			customerProfileTabDTO = null;
			loyaltyProfileTabDTO=null;
			tableDetailsDAO =null; 
		}

		return genericDTO;
	}

	public PackageInfoDTO buildPackageInfoDetails(List<OfferMasterTabDto> validOfferIdList, boolean flag,int languageId) throws Exception {
		PackageInfoDTO packageInfoDTO = null;
		PackageDetailsDTO[] packageDetailsDTOs = null;
		HashMap<String,List<OfferMasterTabDto>> hashmap=null; 
		try {
			packageInfoDTO = new PackageInfoDTO();
			hashmap=new HashMap<String,List<OfferMasterTabDto>>();
			
			if(Cache.getConfigParameterMap().get("GET_PKGS_SUBPKG_ENABLE").getParameterValue()!=null && Cache.getConfigParameterMap().get("GET_PKGS_SUBPKG_ENABLE").getParameterValue().equalsIgnoreCase("1"))
			{
				hashmap=buildingMapBasedOnOfferType(validOfferIdList);
				packageDetailsDTOs=buildingBasePackage(hashmap,flag,languageId);
				
			}else
			{
				packageDetailsDTOs=buildingBasePackageWithOneHierarchy(validOfferIdList,flag,languageId);
			}
			
			if(packageDetailsDTOs!=null)
			{
			packageInfoDTO.setPackages(packageDetailsDTOs);
			}else
			{
			packageInfoDTO=null;
			}
		} catch (Exception e) {
			packageInfoDTO = null;
			logger.info("Exception e " + e.getMessage());
			e.printStackTrace();
		}finally
		{
			hashmap=null;
		}
		return packageInfoDTO;

	}

	public List<OfferMasterTabDto> pointValidationOffer(List<LoyaltyProfileTabDTO> loyaltyProfileInfo,
			List<Integer> OfferIdList) {
		GetPackagesDAO getPackagesDAO = null;
		List<OfferMasterTabDto> offerMasterTabDto = null;
		Double points = 0.0;
		List<OfferMasterTabDto> validOfferIdDto = null;
		logger.info("inside pointValidationOFfer ");
		int offerPoints = 0;
		try {
			validOfferIdDto = new ArrayList<>();
			getPackagesDAO = new GetPackagesDAO();
			Date currentDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
			logger.info("inside pointValidationOFfer "+loyaltyProfileInfo.size());
			logger.info(" OfferIdList  "+OfferIdList.size());
			if (loyaltyProfileInfo != null && OfferIdList != null) {
				points = loyaltyProfileInfo.get(0).getRewardPoints();

				for (int offerId = 0; offerId < OfferIdList.size(); offerId++) {
					offerMasterTabDto = getPackagesDAO.getOfferMasterDetails(OfferIdList.get(offerId),currentDate);

					if (offerMasterTabDto != null && offerMasterTabDto.size()>0) {
						logger.debug("offerMasterTabDto Points " + offerMasterTabDto.get(0).getPoints());
						offerPoints = offerMasterTabDto.get(0).getPoints();
						logger.debug("Validation of offerpoints " + offerPoints + " points " + points);
						if (offerPoints <= points) {
							logger.debug("Validation of points success");
							validOfferIdDto.addAll(offerMasterTabDto);
						} 
					}
				}
				if(validOfferIdDto!=null && validOfferIdDto.size()<1) {
					throw new Exception("points are not enough ");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e " + e.getMessage());
		}
		return validOfferIdDto;

	}

	public List<LoyaltyProfileTabDTO> loyaltyProfileDetails(GetPackagesDTO getPackagesDTO) {
		TableInfoDAO tableInfoDAO = null;
		String subscriberTableName = null;
		CommonUtilDAO commonUtilDAO = null;
		Long loyaltyId = null;
		ArrayList<SubscriberNumberTabDTO> subscriberInfoList = null;
		SubscriberNumberTabDTO subscriberInfoDTO = null;
		String loyaltyTableName = null;
		List<LoyaltyProfileTabDTO> loyaltyProfileInfo = null;
		GetPackagesDAO getPackagesDAO = null;

		try {
			tableInfoDAO = new TableInfoDAO();
			commonUtilDAO = new CommonUtilDAO();
			getPackagesDAO = new GetPackagesDAO();

			subscriberTableName = tableInfoDAO.getSubscriberNumberTable(getPackagesDTO.getSubscriberNumber());
			subscriberInfoList = commonUtilDAO.getSubscriberInformation(getPackagesDTO.getTransactionId(),
					subscriberTableName, getPackagesDTO.getSubscriberNumber());

			if (subscriberInfoList != null && subscriberInfoList.size() != 0) {
				subscriberInfoDTO = subscriberInfoList.get(0);
				loyaltyId = subscriberInfoDTO.getLoyaltyID();
				loyaltyTableName = tableInfoDAO.getLoyaltyProfileTable(loyaltyId + "");
				loyaltyProfileInfo = getPackagesDAO.getLoyaltyProfileInfo(getPackagesDTO.getTransactionId(),
						loyaltyTableName, loyaltyId);
				if(loyaltyProfileInfo==null)
				{
					logger.info("LoyaltyProfile information not available in the system for loyaltyId "+loyaltyId +"loyaltyTableName "+loyaltyTableName+" TransactionID "+getPackagesDTO.getTransactionId());
				}
			}
			else
			{
				logger.info("Subscriber information not available in the system for "+getPackagesDTO.getSubscriberNumber() +"Subscriber Table name "+subscriberTableName+" TransactionID "+getPackagesDTO.getTransactionId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception " + e.getMessage());
		}
		return loyaltyProfileInfo;
	}

	public List<Integer> getOfferBasedonAccountType(GetPackagesDTO getPackagesDTO, List<Integer> channelTypeOffer) {
		GetPackagesDAO getPackagesDAO = null;
		List<CustomerProfileTabDTO> customerProfileDetails = null;
		List<Integer> accountTypeOffer = null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			customerProfileDetails = getPackagesDAO.getCustomerProfileInfo(getPackagesDTO.getSubscriberNumber());
			if (customerProfileDetails != null) {
				logger.info("Get customerProfileDetails" + customerProfileDetails.toString());
			}
			if (customerProfileDetails != null) {
				String accountType = customerProfileDetails.get(0).getAccountCategoryType();
				accountTypeOffer = getPackagesDAO.getOfferBasedOnAccountType(accountType, channelTypeOffer);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception "+e.getMessage());

		}
		return accountTypeOffer;
	}
	

	public List<MerchantNomenclatureTabDto> getMerchantNomenClatureDetails(int merchantId,int languageId) {
		GetPackagesDAO getPackagesDAO = null;
		List<MerchantNomenclatureTabDto> merchantNomenDetails = null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			merchantNomenDetails = getPackagesDAO.getOfferDetailsMerchantNomenclatureTab(merchantId,languageId);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception " + e.getMessage());
		}finally
		{
			getPackagesDAO=null;
		}
		return merchantNomenDetails;
	}

	public List<Integer> getOfferIdMerchantNomenClatureOfferMasterMapping(int offerId) {
		GetPackagesDAO getPackagesDAO = null;
		List<Integer> merchantId = null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			merchantId = getPackagesDAO.getOfferMasterAndMerchantMapping(offerId);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e "+e.getMessage());
		}
		return merchantId;
	}

	public List<Integer> getOfferMasterTierMapping(int offerId) {
		GetPackagesDAO getPackagesDAO = null;
		List<Integer> offerTierMapping = null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			offerTierMapping = getPackagesDAO.getOfferMasterTierMapping(offerId);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e "+e.getMessage());
		}finally
		{
			getPackagesDAO=null;
		}
		return offerTierMapping;
	}

	public PackageInfoDTO setStatusGetpkg(PackageInfoDTO packageInfoDTO, GetPackagesDTO getPackagesDTO,
			String statusCode, String statusDescription) {
		if (packageInfoDTO == null)
			packageInfoDTO = new PackageInfoDTO();

		packageInfoDTO.setStatusCode(statusCode);
		packageInfoDTO.setStatusDescription(statusDescription);
		packageInfoDTO.setTimestamp(getPackagesDTO.getTimestamp());
		packageInfoDTO.setTranscationId(getPackagesDTO.getTransactionId());

		logger.info("SetStatusPkg TransactionId  " + getPackagesDTO.getTransactionId() + "packageInfoDTO "
				+ packageInfoDTO.getTranscationId());
		return packageInfoDTO;
	}

	public List<OfferTeleAttruibutes> getOfferCategoryMapping(int offerId) {
		GetPackagesDAO getPackagesDAO = null;
		List<OfferTeleAttruibutes> offerCategoryMappingList = null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			offerCategoryMappingList = getPackagesDAO.getOfferCategoryMapping(offerId);

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e ");
		}
		return offerCategoryMappingList;
	}

	public List<OfferMasterTabDto> getTelecomOfferDetails() {
		GetPackagesDAO getPackagesDAO = null;
		List<OfferMasterTabDto> TelecomOffers = null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			TelecomOffers = getPackagesDAO.getTelecomOffersFromOfferMaster();
			logger.info("Telecom offerSize " + TelecomOffers.size());

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e ");
		}
		return TelecomOffers;

	}

	
	public HashMap<String,List<OfferMasterTabDto>> buildingMapBasedOnOfferType(List<OfferMasterTabDto> offerList)
	{
		HashMap<String, List<OfferMasterTabDto>> offerTypeBasedMap = null;
		List<OfferMasterTabDto> offerMapList = null;
		try {
			offerTypeBasedMap = new HashMap<String, List<OfferMasterTabDto>>();
			for (OfferMasterTabDto offer : offerList) {
				if (offerTypeBasedMap.containsKey(String.valueOf(offer.getOfferTypeId()))) {
					offerTypeBasedMap.get(String.valueOf(offer.getOfferTypeId())).add(offer);
				} else {
					offerMapList = new ArrayList<OfferMasterTabDto>();
					offerMapList.add(offer);
					offerTypeBasedMap.put(String.valueOf(offer.getOfferTypeId()), offerMapList);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			offerMapList=null;
		}
		return offerTypeBasedMap;

	}
	
	public PackageDetailsDTO[] buildingBasePackageWithOneHierarchy(List<OfferMasterTabDto> offerList,boolean flag,int languageId) throws Exception
	{

		PackageDetailsDTO[] packageDetailsDTOs = null;
		GetPackagesDAO getPackagesDAO = null;
		List<OfferNomenclatureTabDto> offerDetails = null;
		List<Integer> merchantId = null;
		List<MerchantNomenclatureTabDto> merchantNomenclatureDetails = null;
		List<Integer> offerTierDiscount = null;
		PackageDetailsDTO packages=null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			
			logger.info("Building subpkg details inside subpkg details ");
			if (offerList != null && offerList.size() > 0) {
				packageDetailsDTOs = new PackageDetailsDTO[offerList.size()];

				logger.info(offerList.toString());
				for (int offerId = 0; offerId < offerList.size(); offerId++) {
					packages = new PackageDetailsDTO();
					offerDetails = getPackagesDAO.getOfferDetailsNomenclatureTab(offerList.get(offerId).getOfferId(),languageId);
					merchantId = getOfferIdMerchantNomenClatureOfferMasterMapping(
							offerList.get(offerId).getOfferId());
					if (merchantId != null && merchantId.size() > 0) {
						merchantNomenclatureDetails = getMerchantNomenClatureDetails(merchantId.get(0),languageId);
					}

					offerTierDiscount = getOfferMasterTierMapping(offerList.get(offerId).getOfferId());
					logger.info("OfferTierDiscount list size " + offerTierDiscount.size());

					if (offerTierDiscount != null && offerTierDiscount.size() > 0) {
						// packages = new PackageDetailsDTO();
						logger.info("OfferTierDiscount list 0th value " + offerTierDiscount.get(0));
						String discount = String.valueOf(offerTierDiscount.get(0));
						packages.setDiscount(discount);
					}
					if (offerDetails != null && offerDetails.size() > 0) {
						logger.info("Subpkg offerdetails list size " + offerDetails.size() + "offerId  value " + offerId);
						String offerName = offerDetails.get(0).getOfferName();
						packages.setPackageName(offerName);
						packages.setCost(0);
						String offerType = String.valueOf(offerList.get(offerId).getOfferTypeId());
						String packageID = String.valueOf(offerList.get(offerId).getOfferId());
						packages.setCategory(Cache.offerCategoryMapInfo.get(offerType));
						int redeeempoint = offerList.get(offerId).getPoints();
						String info = offerDetails.get(0).getDescription();
						packages.setOfferType(offerType);
						packages.setPackageID(packageID);
						packages.setRedeemPoints(redeeempoint);
						packages.setInfo(info);
						packages.setSquareImagePath("NA");
						packages.setRectangleImagePath("NA");
						packages.setTypeName("NA");
						packages.setCost(Integer.valueOf(offerList.get(offerId).getCost()));
						if(Cache.TelecomofferTypeIdMapInfo.get(String.valueOf(offerList.get(offerId).getTeleCategory()))!=null)
						packages.setTypeId(Integer.valueOf(Cache.TelecomofferTypeIdMapInfo.get(String.valueOf(offerList.get(offerId).getTeleCategory()))));
						if (merchantNomenclatureDetails != null && merchantNomenclatureDetails.size() > 0) {
							String merchantName = merchantNomenclatureDetails.get(0).getMerchantName();
							logger.info("Merchant Name inside buildingSubPackageDetails "+merchantName);
							packages.setMerchantName(merchantName);
						}
						packageDetailsDTOs[offerId] = packages;
					}
					else
					{
						packageDetailsDTOs=null;
					}
				}
				logger.info("Building subpkg details inside subpkg details length of subpkg "+ packageDetailsDTOs.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e " + e.getMessage());
		}finally
		{
			getPackagesDAO = null;
			offerDetails = null;
			merchantId = null;
			merchantNomenclatureDetails = null;
			offerTierDiscount = null;
			packages=null;
		}
		return packageDetailsDTOs;

	}
	
	public PackageDetailsDTO[] buildingSubPackageDetails(List<OfferMasterTabDto> telecomOffers,int languageId) {

		PackageDetailsDTO[] packageDetailsDTOs = null;
		GetPackagesDAO getPackagesDAO = null;
		List<OfferNomenclatureTabDto> offerDetails = null;
		List<Integer> merchantId = null;
		List<MerchantNomenclatureTabDto> merchantNomenclatureDetails = null;
		List<Integer> offerTierDiscount = null;
		PackageDetailsDTO packages=null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			
			logger.info("Building subpkg details inside subpkg details ");
			if (telecomOffers != null && telecomOffers.size() > 0) {
				packageDetailsDTOs = new PackageDetailsDTO[telecomOffers.size()];

				logger.info(telecomOffers.toString());
				for (int offerId = 0; offerId < telecomOffers.size(); offerId++) {
					packages = new PackageDetailsDTO();
					offerDetails = getPackagesDAO.getOfferDetailsNomenclatureTab(telecomOffers.get(offerId).getOfferId(),languageId);
					merchantId = getOfferIdMerchantNomenClatureOfferMasterMapping(
							telecomOffers.get(offerId).getOfferId());
					if (merchantId != null && merchantId.size() > 0) {
						merchantNomenclatureDetails = getMerchantNomenClatureDetails(merchantId.get(0),languageId);
					}

					offerTierDiscount = getOfferMasterTierMapping(telecomOffers.get(offerId).getOfferId());
					logger.info("OfferTierDiscount list size " + offerTierDiscount.size());

					if (offerTierDiscount != null && offerTierDiscount.size() > 0) {
						// packages = new PackageDetailsDTO();
						logger.info("OfferTierDiscount list 0th value " + offerTierDiscount.get(0));
						String discount = String.valueOf(offerTierDiscount.get(0));
						packages.setDiscount(discount);
					}
					if (offerDetails != null && offerDetails.size() > 0) {
						logger.info("Subpkg offerdetails list size " + offerDetails.size() + "offerId  value " + offerId);
						String offerName = offerDetails.get(0).getOfferName();
						packages.setPackageName(offerName);
						packages.setCost(0);
						String offerType = String.valueOf(telecomOffers.get(offerId).getOfferTypeId());
						String packageID = String.valueOf(telecomOffers.get(offerId).getOfferId());
						int cost=Integer.valueOf(telecomOffers.get(offerId).getCost());
						packages.setCategory(Cache.offerCategoryMapInfo.get(offerType));
						int redeeempoint = telecomOffers.get(offerId).getPoints();
						String info = offerDetails.get(0).getDescription();
						packages.setOfferType(offerType);
						packages.setPackageID(packageID);
						packages.setRedeemPoints(redeeempoint);
						packages.setInfo(info);
						packages.setSquareImagePath("NA");
						packages.setRectangleImagePath("NA");
						packages.setTypeName("NA");
						packages.setCost(cost);
						if(merchantNomenclatureDetails!=null)
						{
							packages.setSubPackageDetails(buildingMerchantPackageDetails(merchantNomenclatureDetails,telecomOffers.get(offerId).getOfferId(),offerType,redeeempoint,cost,languageId));
						}
						
						if (merchantNomenclatureDetails != null && merchantNomenclatureDetails.size() > 0) {
							String merchantName = merchantNomenclatureDetails.get(0).getMerchantName();
							logger.info("Merchant Name inside buildingSubPackageDetails "+merchantName);
							String merchandetailsId = String.valueOf(merchantNomenclatureDetails.get(0).getMerchantId());
							packages.setMerchantName(merchantName);
							packages.setPackageID(merchandetailsId);
						}
						packageDetailsDTOs[offerId] = packages;
					}
				}
				logger.info("Building subpkg details inside subpkg details length of subpkg "+ packageDetailsDTOs.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e " + e.getMessage());
		}
		return packageDetailsDTOs;

	}
	
	public PackageDetailsDTO[] buildingMerchantPackageDetails(List<MerchantNomenclatureTabDto> merchantNomenclatureDetails ,int packageId,String offerType,int redeeempoint,int cost,int languageId) {

		PackageDetailsDTO[] packageDetailsDTOs = null;
		GetPackagesDAO getPackagesDAO = null;
		List<OfferNomenclatureTabDto> offerDetails = null;
		List<Integer> offerTierDiscount = null;
		PackageDetailsDTO packages=null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			
			logger.info("Building merchangt subpkg details inside subpkg details ");
			if (merchantNomenclatureDetails != null && merchantNomenclatureDetails.size() > 0) {
				packageDetailsDTOs = new PackageDetailsDTO[merchantNomenclatureDetails.size()];

				logger.info(merchantNomenclatureDetails.toString());
				for (int offerId = 0; offerId < merchantNomenclatureDetails.size(); offerId++) {
					packages = new PackageDetailsDTO();
					offerDetails = getPackagesDAO.getOfferDetailsNomenclatureTab(packageId,languageId);
					offerTierDiscount = getOfferMasterTierMapping(packageId);
					logger.info("OfferTierDiscount list size " + offerTierDiscount.size());

					if (offerTierDiscount != null && offerTierDiscount.size() > 0) {
						// packages = new PackageDetailsDTO();
						logger.info("OfferTierDiscount list 0th value " + offerTierDiscount.get(0));
						String discount = String.valueOf(offerTierDiscount.get(0));
						packages.setDiscount(discount);
					}
					if (offerDetails != null && offerDetails.size() > 0) {
						logger.info("Subpkg offerdetails list size " + offerDetails.size() + "offerId  value " + offerId);
						String offerName = offerDetails.get(0).getOfferName();
						packages.setPackageName(offerName);
						packages.setCost(0);
						packages.setCategory(Cache.offerCategoryMapInfo.get(offerType));
						String info = offerDetails.get(0).getDescription();
						packages.setOfferType(offerType);
						packages.setPackageID(String.valueOf(packageId));
						packages.setRedeemPoints(redeeempoint);
						packages.setInfo(info);
						packages.setSquareImagePath("NA");
						packages.setRectangleImagePath("NA");
						packages.setTypeName("NA");
						packages.setCost(cost);
						if (merchantNomenclatureDetails != null && merchantNomenclatureDetails.size() > 0) {
							String merchantName = merchantNomenclatureDetails.get(offerId).getMerchantName();
							logger.info("Merchant Name inside buildingMerchantPackageDetails "+merchantName);
							packages.setMerchantName(merchantName);
						}
						packageDetailsDTOs[offerId] = packages;
					}
				}
				logger.info("Building subpkg details inside subpkg details length of subpkg "+ packageDetailsDTOs.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e " + e.getMessage());
		}
		return packageDetailsDTOs;

	}
	
	
	public PackageDetailsDTO[] buildingTelecomCategoryPackageDetails(List<OfferMasterTabDto> telecomOffers,int languageId) {

		HashMap<String,List<OfferMasterTabDto>> offerMap=buildingTelecomCategoryMap(telecomOffers);
		PackageDetailsDTO[] packageDetailsDTOs = null;
		GetPackagesDAO getPackagesDAO = null;
		PackageDetailsDTO packages = null;
		List<OfferNomenclatureTabDto> offerDetails = null;
		List<Integer> merchantId = null;
		List<MerchantNomenclatureTabDto> merchantNomenclatureDetails = null;
		List<Integer> offerTierDiscount = null;
		int count=0;
		try {
			getPackagesDAO=new GetPackagesDAO();
			packageDetailsDTOs = new PackageDetailsDTO[offerMap.size()];
			for (Entry<String, List<OfferMasterTabDto>> entry : offerMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().size() > 0) {
					logger.info("buildingTelecomCategoryPackageDetails size "+offerMap.size());
					logger.info("List Size"+entry.getValue().size());
					for (int offerId = 0; offerId < 1; offerId++) {
						packages = new PackageDetailsDTO();
						logger.info("OfferMap offerId value  "+entry.getValue().get(offerId).getOfferId());
						offerDetails = getPackagesDAO.getOfferDetailsNomenclatureTab(entry.getValue().get(offerId).getOfferId(),languageId);
						merchantId = getOfferIdMerchantNomenClatureOfferMasterMapping(
								entry.getValue().get(offerId).getOfferId());
						if (merchantId != null && merchantId.size() > 0) {
							merchantNomenclatureDetails = getMerchantNomenClatureDetails(merchantId.get(0),languageId);
						}
						offerTierDiscount = getOfferMasterTierMapping(entry.getValue().get(offerId).getOfferId());
						logger.info("OfferTierDiscount list size " + offerTierDiscount.size());

						if (offerTierDiscount != null && offerTierDiscount.size() > 0) {
							logger.info("OfferTierDiscount list 0th value " + offerTierDiscount.get(0));
							String discount = String.valueOf(offerTierDiscount.get(0));
							packages.setDiscount(discount);
						}
						if (offerDetails != null && offerDetails.size() > 0) {

							logger.info("offerdetails list size " + offerDetails.size() + "offerId  value " + offerId);

							String offerName = offerDetails.get(0).getOfferName();
							String offerType = String.valueOf(entry.getValue().get(offerId).getOfferTypeId());
							packages.setPackageName(offerName);
							packages.setCost(0);
							logger.info("Category map "+String.valueOf(entry.getValue().get(offerId).getTeleCategory()));
							packages.setCategory(Cache.TelecomofferCategoryMapInfo.get(String.valueOf(entry.getValue().get(offerId).getTeleCategory())));
							int redeeempoint = entry.getValue().get(offerId).getPoints();
							String packageId = String.valueOf(entry.getValue().get(offerId).getOfferId());
							String info = offerDetails.get(0).getDescription();
							packages.setOfferType(offerType);
							packages.setPackageID(packageId);
							packages.setRedeemPoints(redeeempoint);
							packages.setInfo(info);
							packages.setSquareImagePath("NA");
							packages.setRectangleImagePath("NA");
							packages.setTypeName("NA");
							packages.setCost(Integer.valueOf(entry.getValue().get(offerId).getCost()));
							if(Cache.TelecomofferTypeIdMapInfo.get(String.valueOf(entry.getValue().get(offerId).getTeleCategory()))!=null)
							packages.setTypeId(Integer.valueOf(Cache.TelecomofferTypeIdMapInfo.get(String.valueOf(entry.getValue().get(offerId).getTeleCategory()))));
							packages.setSubPackageDetails(buildingTelecomSubPackageDetails(entry.getValue(),languageId));
							if (merchantNomenclatureDetails != null && merchantNomenclatureDetails.size() > 0) {
								String merchantName = merchantNomenclatureDetails.get(0).getMerchantName();
								String merchandetailsId = String.valueOf(merchantNomenclatureDetails.get(0).getMerchantId());
								packages.setMerchantName(merchantName);
								packages.setPackageID(merchandetailsId);
							}else
							{
							packages.setPackageID(packageId);
							}
							packageDetailsDTOs[count] = packages;
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packageDetailsDTOs;
	}
	
	
	public PackageDetailsDTO[] buildingTelecomSubPackageDetails(List<OfferMasterTabDto> telecomOffers,int languageId) {

		PackageDetailsDTO[] packageDetailsDTOs = null;
		GetPackagesDAO getPackagesDAO = null;
		List<OfferNomenclatureTabDto> offerDetails = null;
		List<Integer> merchantId = null;
		List<MerchantNomenclatureTabDto> merchantNomenclatureDetails = null;
		List<Integer> offerTierDiscount = null;
		PackageDetailsDTO packages=null;
		try {
			getPackagesDAO = new GetPackagesDAO();
			
			logger.info("Building buildingTelecomSubPackageDetails details inside subpkg details ");
			if (telecomOffers != null && telecomOffers.size() > 0) {
				packageDetailsDTOs = new PackageDetailsDTO[telecomOffers.size()];

				logger.info(telecomOffers.toString());
				for (int offerId = 0; offerId < telecomOffers.size(); offerId++) {
					packages = new PackageDetailsDTO();
					offerDetails = getPackagesDAO.getOfferDetailsNomenclatureTab(telecomOffers.get(offerId).getOfferId(),languageId);
					merchantId = getOfferIdMerchantNomenClatureOfferMasterMapping(
							telecomOffers.get(offerId).getOfferId());
					if (merchantId != null && merchantId.size() > 0) {
						merchantNomenclatureDetails = getMerchantNomenClatureDetails(merchantId.get(0),languageId);
					}

					offerTierDiscount = getOfferMasterTierMapping(telecomOffers.get(offerId).getOfferId());
					logger.info("OfferTierDiscount list size " + offerTierDiscount.size());

					if (offerTierDiscount != null && offerTierDiscount.size() > 0) {
						// packages = new PackageDetailsDTO();
						logger.info("OfferTierDiscount list 0th value " + offerTierDiscount.get(0));
						String discount = String.valueOf(offerTierDiscount.get(0));
						packages.setDiscount(discount);
					}
					if (offerDetails != null && offerDetails.size() > 0) {
						logger.info("Subpkg offerdetails list size " + offerDetails.size() + "offerId  value " + offerId);
						String offerName = offerDetails.get(0).getOfferName();
						packages.setPackageName(offerName);
						packages.setCost(0);
						String offerType = String.valueOf(telecomOffers.get(offerId).getOfferTypeId());
						String packageID = String.valueOf(telecomOffers.get(offerId).getOfferId());
						packages.setCategory(Cache.TelecomofferCategoryMapInfo.get(String.valueOf(telecomOffers.get(offerId).getTeleCategory())));
						int redeeempoint = telecomOffers.get(offerId).getPoints();
						String info = offerDetails.get(0).getDescription();
						packages.setOfferType(offerType);
						packages.setPackageID(packageID);
						packages.setRedeemPoints(redeeempoint);
						packages.setInfo(info);
						packages.setSquareImagePath("NA");
						packages.setRectangleImagePath("NA");
						packages.setTypeName("NA");
						if(Cache.TelecomofferTypeIdMapInfo.get(String.valueOf(telecomOffers.get(offerId).getTeleCategory()))!=null)
							packages.setTypeId(Integer.valueOf(Cache.TelecomofferTypeIdMapInfo.get(String.valueOf(telecomOffers.get(offerId).getTeleCategory()))));
						if (merchantNomenclatureDetails != null && merchantNomenclatureDetails.size() > 0) {
							String merchantName = merchantNomenclatureDetails.get(0).getMerchantName();
							packages.setMerchantName(merchantName);
						}
						packageDetailsDTOs[offerId] = packages;
					}
				}
				logger.info("Building subpkg details inside subpkg details length of subpkg "+ packageDetailsDTOs.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception e " + e.getMessage());
		}
		return packageDetailsDTOs;

	}
	
	
	public HashMap<String,List<OfferMasterTabDto>> buildingTelecomCategoryMap(List<OfferMasterTabDto> offerList)
	{
		HashMap<String, List<OfferMasterTabDto>> TelecomOfferTypeBasedMap = null;
		List<OfferMasterTabDto> offerMapList = null;
		try {
			TelecomOfferTypeBasedMap = new HashMap<String, List<OfferMasterTabDto>>();
			for (OfferMasterTabDto offer : offerList) {
				if (TelecomOfferTypeBasedMap.containsKey(String.valueOf(offer.getTeleCategory()))) {
					TelecomOfferTypeBasedMap.get(String.valueOf(offer.getTeleCategory())).add(offer);
				} else {
					offerMapList = new ArrayList<OfferMasterTabDto>();
					offerMapList.add(offer);
					TelecomOfferTypeBasedMap.put(String.valueOf(offer.getTeleCategory()), offerMapList);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return TelecomOfferTypeBasedMap;

	}
	
	public GenericDTO customeValidation(String key,GetPackagesDTO getPackagesDTO,GenericDTO genericDTO) throws CommonException
	{
		CommonUtil commonUtil = new CommonUtil();
		return genericDTO = commonUtil.getStatusCodeDescription(genericDTO, key + getPackagesDTO.getLanguageId(),getPackagesDTO.getTransactionId());
	}
	
	
	public PackageDetailsDTO[] buildingBasePackage(HashMap<String,List<OfferMasterTabDto>> offerMap,boolean flag,int languageId) throws Exception
	{

		PackageDetailsDTO[] packageDetailsDTOs = null;
		GetPackagesDAO getPackagesDAO = null;
		PackageDetailsDTO packages = null;
		List<OfferNomenclatureTabDto> offerDetails = null;
		List<Integer> merchantId = null;
		List<MerchantNomenclatureTabDto> merchantNomenclatureDetails = null;
		List<Integer> offerTierDiscount = null;
		int count=0;
		try {
			getPackagesDAO=new GetPackagesDAO();
			packageDetailsDTOs = new PackageDetailsDTO[offerMap.size()];
			for (Entry<String, List<OfferMasterTabDto>> entry : offerMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().size() > 0) {
					logger.info("OfferMap size "+offerMap.size());
					logger.info("List Size"+entry.getValue().size());
					for (int offerId = 0; offerId < 1; offerId++) {
						packages = new PackageDetailsDTO();
						logger.info("OfferMap offerId value  "+entry.getValue().get(offerId).getOfferId());
						offerDetails = getPackagesDAO.getOfferDetailsNomenclatureTab(entry.getValue().get(offerId).getOfferId(),languageId);
						merchantId = getOfferIdMerchantNomenClatureOfferMasterMapping(
								entry.getValue().get(offerId).getOfferId());
						if (merchantId != null && merchantId.size() > 0) {
							merchantNomenclatureDetails = getMerchantNomenClatureDetails(merchantId.get(0),languageId);
						}
						offerTierDiscount = getOfferMasterTierMapping(entry.getValue().get(offerId).getOfferId());
						logger.info("OfferTierDiscount list size " + offerTierDiscount.size());

						if (offerTierDiscount != null && offerTierDiscount.size() > 0) {
							logger.info("OfferTierDiscount list 0th value " + offerTierDiscount.get(0));
							String discount = String.valueOf(offerTierDiscount.get(0));
							packages.setDiscount(discount);
						}
						if (offerDetails != null && offerDetails.size() > 0) {

							logger.info("offerdetails list size " + offerDetails.size() + "offerId  value " + offerId);

							String offerName = offerDetails.get(0).getOfferName();
							String offerType = String.valueOf(entry.getValue().get(offerId).getOfferTypeId());
							packages.setPackageName(offerName);
							packages.setCost(0);
							packages.setCategory(Cache.offerCategoryMapInfo.get(offerType));
							int redeeempoint = entry.getValue().get(offerId).getPoints();
							String packageId = String.valueOf(entry.getValue().get(offerId).getOfferId());
							String info = offerDetails.get(0).getDescription();
							packages.setOfferType(offerType);
							packages.setPackageID(offerType);
							packages.setRedeemPoints(redeeempoint);
							packages.setInfo(info);
							packages.setSquareImagePath("NA");
							packages.setRectangleImagePath("NA");
							packages.setTypeName("NA");
							packages.setCost(Integer.valueOf(entry.getValue().get(offerId).getCost()));
							if(Cache.TelecomofferTypeIdMapInfo.get(String.valueOf(entry.getValue().get(offerId).getTeleCategory()))!=null)
								packages.setTypeId(Integer.valueOf(Cache.TelecomofferTypeIdMapInfo.get(String.valueOf(entry.getValue().get(offerId).getTeleCategory()))));
							if(flag)
							{
							if(offerType.equalsIgnoreCase("5"))
							{
								packages.setSubPackageDetails(buildingTelecomCategoryPackageDetails(entry.getValue(),languageId));
								
							}else
							{
								packages.setSubPackageDetails(buildingSubPackageDetails(entry.getValue(),languageId));
							}
							}
							
							if (merchantNomenclatureDetails != null && merchantNomenclatureDetails.size() > 0) {
								String merchantName = merchantNomenclatureDetails.get(0).getMerchantName();
								packages.setMerchantName(merchantName);
								logger.info("Merchant Name inside buildingBasePackage "+merchantName);
							}
							packageDetailsDTOs[count] = packages;
							count++;
						}
						else
						{
							packageDetailsDTOs=null;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			getPackagesDAO = null;
			packages = null;
			offerDetails = null;
			merchantId = null;
			merchantNomenclatureDetails = null;
			offerTierDiscount = null;
		}
		return packageDetailsDTOs;
	}
	
}

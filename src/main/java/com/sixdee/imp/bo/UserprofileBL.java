package com.sixdee.imp.bo;

/**
 * 
 * @author Geevan
 * @version 1.0
 * 
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%"><b>Date</b></td>
 *          <td width="20%"><b>Author</b></td>
 *          </tr>
 *          <tr>
 *          <td>May 11,2013 08:47:22 AM</td>
 *          <td>Geevan</td>
 *          </tr>
 *          </table>
 *          </p>
 */

import java.text.SimpleDateFormat;
import java.util.Map;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.UserprofileDAO;
import com.sixdee.imp.dao.VoucherPromoDAO;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.TierInfoDTO;
import com.sixdee.imp.dto.UserprofileDTO;
import com.sixdee.imp.dto.VoucherPromoTranverseDTO;

public class UserprofileBL extends BOCommon {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * This method is called from BOCommon. Access the DAO object is to create
	 * CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	@SuppressWarnings({ "unchecked", "null" })
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => UserprofileBL :: Method => buildProcess()");

		UserprofileDAO userprofileDAO = new UserprofileDAO();
		TableDetailsDAO tabDAO = new TableDetailsDAO();
		SubscriberNumberTabDTO subscriberNumDTO = new SubscriberNumberTabDTO();
		LoyaltyProfileTabDTO loyaltyProfileDTO = new LoyaltyProfileTabDTO();
		NationalNumberTabDTO nationalNumDTo = new NationalNumberTabDTO();
		AccountNumberTabDTO accntNumDTO = new AccountNumberTabDTO();
		CommonUtil commonUtil = new CommonUtil();
		UserprofileDTO userprofileDTO = (UserprofileDTO) genericDTO.getObj();
		boolean isAuthenticate = false;
		boolean process = false;
		try {
			userprofileDAO = new UserprofileDAO();
			tabDAO = new TableDetailsDAO();
			subscriberNumDTO = new SubscriberNumberTabDTO();
			loyaltyProfileDTO = new LoyaltyProfileTabDTO();
			nationalNumDTo = new NationalNumberTabDTO();
			accntNumDTO = new AccountNumberTabDTO();
			commonUtil = new CommonUtil();
			userprofileDTO = (UserprofileDTO) genericDTO.getObj();
		if (userprofileDTO.getSubscriberNumber() != null) {

			logger.info("SUBSCRIBER NUMBER:" + userprofileDTO.getSubscriberNumber());
			logger.info("PIN:" + userprofileDTO.getPin());
			logger.info("IS_DOD VALUE:" + userprofileDTO.isDealOfDay());
			if(userprofileDTO.getSubscriberNumber()!=null &&userprofileDTO.isDealOfDay() ){
				userprofileDTO.setActive(userprofileDAO.getSubscriberNumberDetails(userprofileDTO.getSubscriberNumber()));
			}else
			 if (!userprofileDTO.isNationalID() && commonUtil.isItChar(userprofileDTO.getSubscriberNumber())) {
				// ADSL Number Entered
				logger.info("ADSL NO ENTERED>>>>>>>>>>>>");
				ADSLTabDTO tabDTO = tabDAO.getADSLDetails(userprofileDTO.getSubscriberNumber());
				if (tabDTO != null)
					if (tabDTO.getStatusID() != 1) {
						logger.info("Subscriber Number is not Active Mode : " + userprofileDTO.getSubscriberNumber() + "  Mode : " + Cache.statusMap.get(userprofileDTO.getStatusID()));
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SUBS_NOT_ACT_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
						/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + userprofileDTO.getLanguageId()).getStatusCode());
						genericDTO.setStatus(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + userprofileDTO.getLanguageId()).getStatusDesc());
						*/
						throw new CommonException(genericDTO.getStatus());

					} else {
						loyaltyProfileDTO = tabDAO.getLoyaltyProfileDetails(tabDTO.getLoyaltyID());
					}

				/*
				 * if(loyaltyProfileDTO!=null &&
				 * loyaltyProfileDTO.getDefaultLanguage()==null) {
				 * loyaltyProfileDTO
				 * .setDefaultLanguage(Cache.defaultLanguageID); } else {
				 * genericDTO.setStatusCode
				 * (Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_"
				 * +userprofileDTO .getLanguageId()).getStatusCode());
				 * genericDTO.setStatus(Cache.getServiceStatusMap().get(
				 * "GET_USER_PROFILE_INVALID_"
				 * +userprofileDTO.getLanguageId()).getStatusDesc());
				 * //genericDTO.setStatusCode("1");//
				 * genericDTO.setStatus("FAILURE ..NO Details Found");
				 * logger.info("No details found in any TABLES...!!!"); throw
				 * new CommonException(); }
				 */

				if (userprofileDTO.isValidate()) {
					if (loyaltyProfileDTO.getPin().equals(userprofileDTO.getPin())) {
						isAuthenticate = true;
						// genericDTO.setStatusCode("0");//genericDTO.setStatus("SUCCESS");
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
						
						/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId()).getStatusCode());
						genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId()).getStatusDesc());
*/
					} else {
						// genericDTO.setStatusCode("1");//
						// genericDTO.setStatus("FAILURE ..Loyalty ID and PIN is not matching");
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_VALIDATION_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
						/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_VALIDATION_" + userprofileDTO.getLanguageId()).getStatusCode());
						genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_USER_PROFILE_VALIDATION_" + userprofileDTO.getLanguageId()).getStatusDesc());
*/
					}

				}

			} // ADSL Part over
			else // SubscriberNo,LoyalityId,AccountNo part Entered
			{
				logger.info("loyaltyProfileDTO value before proccesing in else blk" + loyaltyProfileDTO);
				// Long
				// subscriberNum=Long.parseLong(userprofileDTO.getSubscriberNumber());
				logger.info("SUB>>>>>>" + userprofileDTO.getSubscriberNumber());
				if (userprofileDTO.isNationalID()) {
					String nationalId = userprofileDTO.getSubscriberNumber().replaceFirst("^0+(?!$)", "");
					logger.info("NATIONAL ID FINAL NUMBER::" + nationalId);
					nationalNumDTo = tabDAO.getNationalNumberDetails(nationalId);

					if (nationalNumDTo != null) {
						logger.info("Loyalty ID from NationalNumber Table>>>>>>" + nationalNumDTo.getLoyaltyID());
						loyaltyProfileDTO = tabDAO.getLoyaltyProfileDetails(nationalNumDTo.getLoyaltyID());
					} else {
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_INVALID_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
						/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + userprofileDTO.getLanguageId()).getStatusCode());
						genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + userprofileDTO.getLanguageId()).getStatusDesc());
					*/}
				} else if (userprofileDTO.getSubscriberNumber() != null && Long.parseLong(userprofileDTO.getSubscriberNumber()) > 0) {
					Long subscriberNum = Long.parseLong(userprofileDTO.getSubscriberNumber());
					if (subscriberNum > 0) {
						subscriberNumDTO = tabDAO.getSubscriberNumberDetails(Long.parseLong(userprofileDTO.getSubscriberNumber()));

						if (subscriberNumDTO != null) {
							logger.info(">>>status map >>"+Cache.statusMap);
							if (subscriberNumDTO.getStatusID() != 1) {
								logger.info("Subscriber Number is not Active Mode : " + userprofileDTO.getSubscriberNumber() + "  Mode : " + Cache.statusMap.get(subscriberNumDTO.getStatusID()));
								genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SUBS_NOT_ACT_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
								/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + userprofileDTO.getLanguageId()).getStatusCode());
								genericDTO.setStatus(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + userprofileDTO.getLanguageId()).getStatusDesc());
								*/
								throw new CommonException(genericDTO.getStatus());

							} else
								loyaltyProfileDTO = tabDAO.getLoyaltyProfileDetails(subscriberNumDTO.getLoyaltyID());
						} else {
							accntNumDTO = tabDAO.getAccountNumberDetails(subscriberNum+"");
							if (accntNumDTO != null) {
								if (accntNumDTO.getStatusID() != 1) {
									logger.info("Subscriber Number is not Active Mode : " + userprofileDTO.getSubscriberNumber() + "  Mode : " + Cache.statusMap.get(userprofileDTO.getStatusID()));
									genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SUBS_NOT_ACT_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
									/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + userprofileDTO.getLanguageId()).getStatusCode());
									genericDTO.setStatus(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + userprofileDTO.getLanguageId()).getStatusDesc());
									*/
									throw new CommonException(genericDTO.getStatus());

								} else
									loyaltyProfileDTO = tabDAO.getLoyaltyProfileDetails(accntNumDTO.getLoyaltyID());
							} else {
								loyaltyProfileDTO = tabDAO.getLoyaltyProfileDetails(subscriberNum);

								if (loyaltyProfileDTO == null) {
									logger.info("### No details found in  Loyality TABLES for the Subscriber ####");
									logger.info("##Default language taking from Cache ..No details found in table ##");
									logger.info("Contains  " + Cache.getServiceStatusMap().containsKey("GET_USER_PROFILE_INVALID_1"));
									logger.info("StatusCode" + Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + userprofileDTO.getLanguageId()).getStatusCode());
									logger.info("Status" + Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + userprofileDTO.getLanguageId()).getStatusDesc());
									genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_INVALID_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
									/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + userprofileDTO.getLanguageId()).getStatusCode());
									genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_" + userprofileDTO.getLanguageId()).getStatusDesc());*/
								}

							}
						}

						if (userprofileDTO.isValidate()) {

							if (loyaltyProfileDTO.getPin().equals("" + userprofileDTO.getPin())) {
								isAuthenticate = true;
								// genericDTO.setStatusCode("0");//
								// genericDTO.setStatus("SUCCESS");
								genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
								/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId()).getStatusCode());
								genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId()).getStatusDesc());*/
							} else {
								// genericDTO.setStatusCode("1");
								// //genericDTO.setStatus("FAILURE ..Loyalty ID and PIN is not matching");
								genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_VALIDATION_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
								/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_VALIDATION_" + userprofileDTO.getLanguageId()).getStatusCode());
								genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_USER_PROFILE_VALIDATION_" + userprofileDTO.getLanguageId()).getStatusDesc());*/
							}

						}

					}
				} else {
					// genericDTO.setStatusCode("1");
					// //genericDTO.setStatus("FAILURE");
					genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_INVALID_SUB_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
					/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_SUB_" + userprofileDTO.getLanguageId()).getStatusCode());
					genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_USER_PROFILE_INVALID_SUB_" + userprofileDTO.getLanguageId()).getStatusDesc());*/
					logger.info("###Subscriber Number Required##");
				}
			}
			
			if(userprofileDTO.isDealOfDay() ){
				
				Long subscriberNum = Long.parseLong(userprofileDTO.getSubscriberNumber());
				if (subscriberNum > 0) {
					subscriberNumDTO = tabDAO.getSubscriberNumberDetails(Long.parseLong(userprofileDTO.getSubscriberNumber()));

					if (subscriberNumDTO != null) {
						if (subscriberNumDTO.getStatusID() != 1) {
							logger.info("Subscriber Number is not Active Mode : " + userprofileDTO.getSubscriberNumber() + "  Mode : " + Cache.statusMap.get(userprofileDTO.getStatusID()));
							genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "SUBS_NOT_ACT_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
							/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + userprofileDTO.getLanguageId()).getStatusCode());
							genericDTO.setStatus(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_" + userprofileDTO.getLanguageId()).getStatusDesc());*/
							throw new CommonException(genericDTO.getStatus());

						} else
							loyaltyProfileDTO = tabDAO.getLoyaltyProfileDetails(subscriberNumDTO.getLoyaltyID());
					} 

					

				}
				VoucherPromoDAO voucherPromoDAO=new VoucherPromoDAO();
				VoucherPromoTranverseDTO voucherPromoDTO=new VoucherPromoTranverseDTO();
				logger.info("IS_ACTIVE:" + userprofileDTO.isActive());
				if(userprofileDTO.isActive()){
					
					
					boolean flag=false;
					
					voucherPromoDTO.setSubscriberNo(userprofileDTO.getSubscriberNumber());
					voucherPromoDTO.setMerchandID(Cache.getCacheMap().get("DOD_MERCHANT_ID"));
					try {
						voucherPromoDTO=voucherPromoDAO.getSubscriberPromoValues(voucherPromoDTO);
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					if(!voucherPromoDTO.isVoucherFlag())
					{
					try {
						voucherPromoDAO.assignPromoCode(voucherPromoDTO,null);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("Promocode obtained is---->"+voucherPromoDTO.getPromoCode());
					if (voucherPromoDTO.getPromoCode() != null && !voucherPromoDTO.getPromoCode().equalsIgnoreCase("")) {
						
						
						try {
							process = voucherPromoDAO.updatePromoValues(voucherPromoDTO,null);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("the value of prcess---->"+process);
						if (process) {
							try {
								process = voucherPromoDAO.assignValues(voucherPromoDTO,loyaltyProfileDTO);
								//new NotificationPanel().isDODEligibleNotification(loyaltyProfileDTO,userprofileDTO.getSubscriberNumber(),voucherPromoDTO,1);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "DOD_PROMO_SUCESS_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
							userprofileDTO.setStatusCode(genericDTO.getStatusCode());
							userprofileDTO.setStatusDesc(genericDTO.getStatus());
						} else {
							System.out.println("Promocode is exhausted for the Merchant with id:" + voucherPromoDTO.getMerchandID());
							genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "DOD_PROMO_EXHAUST_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
							userprofileDTO.setStatusCode(genericDTO.getStatusCode());
							userprofileDTO.setStatusDesc(genericDTO.getStatus());
							throw new CommonException(userprofileDTO.getStatusCode(), Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_" + userprofileDTO.getLanguageId()).getStatusDesc());

						}
					} else {
						System.out.println("Promocode is exhausted for the Merchant with id:" + voucherPromoDTO.getMerchandID());
						genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "DOD_PROMO_EXHAUST_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
						userprofileDTO.setStatusCode(genericDTO.getStatusCode());
						userprofileDTO.setStatusDesc(genericDTO.getStatus());
						throw new CommonException(userprofileDTO.getStatusCode(), Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_" + userprofileDTO.getLanguageId()).getStatusDesc());

					}
					}
					else
					//new NotificationPanel().isDODEligibleNotification(loyaltyProfileDTO,userprofileDTO.getSubscriberNumber(),voucherPromoDTO,5);
					userprofileDTO.setPromoCode(voucherPromoDTO.getPromoCode());
				}else if(!userprofileDTO.isActive())
				{
				//new NotificationPanel().isDODEligibleNotification(loyaltyProfileDTO,userprofileDTO.getSubscriberNumber(),voucherPromoDTO,2);
				}
			}
			if (userprofileDTO.isValidate() && isAuthenticate) {
				process = true;
			} else if (userprofileDTO.isValidate() && !isAuthenticate) {
				// userprofileDTO.isValidate() && isAuthenticate
			} else {
				if (loyaltyProfileDTO != null)
					process = true;
			}

			if (process) {
				
				if(!(loyaltyProfileDTO.getStatusID()==10)){

				if (loyaltyProfileDTO.getLoyaltyID() != null) {
					System.out.println("Details >>>>>>>>>>>>");

					// logger.info("##DEFAULT LANGUAGE IN LOYALITY TABLE##"+loyaltyProfileDTO.getDefaultLanguage());
					/*
					 * if(loyaltyProfileDTO!=null &&
					 * loyaltyProfileDTO.getDefaultLanguage()==null) {
					 * logger.info("##DEFAULT LANGUAGE GETTING FROM CACHE##");
					 * loyaltyProfileDTO
					 * .setDefaultLanguage(Cache.defaultLanguageID); }
					 */

					if (userprofileDTO.getLanguageId() != null && userprofileDTO.getLanguageId().equals("1")) {
						logger.info("##DEFAULT LANGUAGE IS ENGLISH LOYALITY##");
						logger.info("FirstName>>" + loyaltyProfileDTO.getFirstName());
						userprofileDTO.setFirstName(loyaltyProfileDTO.getFirstName());

						logger.info("LastName>>" + loyaltyProfileDTO.getLastName());
						userprofileDTO.setLastName(loyaltyProfileDTO.getLastName());

						logger.info("Address>>" + loyaltyProfileDTO.getAddress());
						userprofileDTO.setAddress(loyaltyProfileDTO.getAddress());
					}

					if (userprofileDTO.getLanguageId() != null && userprofileDTO.getLanguageId().equals("11")) {
						logger.info("##DEFAULT LANGUAGE IS ARABIC ##");

						logger.info("FirstName>>" + loyaltyProfileDTO.getArbicFirstName());
						userprofileDTO.setFirstName(loyaltyProfileDTO.getArbicFirstName());

						logger.info("LastName>>" + loyaltyProfileDTO.getArbicLastName());
						userprofileDTO.setLastName(loyaltyProfileDTO.getArbicLastName());

						logger.info("Address>>" + loyaltyProfileDTO.getArbicAddress());
						userprofileDTO.setAddress(loyaltyProfileDTO.getArbicAddress());

					}

					logger.info("LoyalityId>>" + loyaltyProfileDTO.getLoyaltyID());
					userprofileDTO.setLoyaltyID(loyaltyProfileDTO.getLoyaltyID());

					logger.info("EmailId>>" + loyaltyProfileDTO.getEmailID());
					userprofileDTO.setEmailID(loyaltyProfileDTO.getEmailID());

					logger.info("ContactNo>>" + loyaltyProfileDTO.getContactNumber());
					userprofileDTO.setContactNumber(loyaltyProfileDTO.getContactNumber());

					logger.info("Create Time>>" + subscriberNumDTO.getCreateDate());
					userprofileDTO.setCreateTime(subscriberNumDTO.getCreateDate());

					logger.info("loyaltyProfileDTO.getDateOfBirth()" + loyaltyProfileDTO.getDateOfBirth());
					if (loyaltyProfileDTO.getDateOfBirth() != null)
						userprofileDTO.setDateOfBirth(loyaltyProfileDTO.getDateOfBirth() != null ? dateFormat.format(loyaltyProfileDTO.getDateOfBirth()) : null);
					logger.info("Date of Birth" + userprofileDTO.getDateOfBirth());

					/*
					 * logger.info("Address>>"+loyaltyProfileDTO.getAddress());
					 * userprofileDTO
					 * .setAddress(loyaltyProfileDTO.getAddress());
					 */

					logger.info("Category>>" + loyaltyProfileDTO.getCategory());
					userprofileDTO.setCategory(loyaltyProfileDTO.getCategory());

					logger.info("Occupation>>" + loyaltyProfileDTO.getOccupation());
					userprofileDTO.setOccupation(loyaltyProfileDTO.getOccupation());

					logger.info("Industry>>" + loyaltyProfileDTO.getTypeOfForm());
					userprofileDTO.setIndustry(loyaltyProfileDTO.getTypeOfForm());
					
					logger.info("vipCode>>" + loyaltyProfileDTO.getVipCode());
					userprofileDTO.setVipCode(loyaltyProfileDTO.getVipCode());

					logger.info("NationalId>>" + loyaltyProfileDTO.getNationality_Id());
					userprofileDTO.setNationalID(loyaltyProfileDTO.getNationality_Id());

					logger.info("StatusID>>" + loyaltyProfileDTO.getStatusID());
					userprofileDTO.setStatusID(loyaltyProfileDTO.getStatusID());
					logger.info("StatusUpdatedate>>" + loyaltyProfileDTO.getStatusUpdatedDate());
					if(loyaltyProfileDTO.getStatusUpdatedDate()!=null)
					  userprofileDTO.setStatusUpdatedDate(loyaltyProfileDTO.getStatusUpdatedDate() == null ? null : dateFormat.format(loyaltyProfileDTO.getStatusUpdatedDate()));

					logger.info("tier>>" + loyaltyProfileDTO.getTierId());
					
					
					TierInfoDTO infoDTO =Cache.getTierInfoMap().get(loyaltyProfileDTO.getTierId());
					if(infoDTO!=null)
					   userprofileDTO.setTierName(infoDTO.getTierName());

					logger.info("tierUpdateDate>>" + loyaltyProfileDTO.getTierUpdatedDate());
					
					if(loyaltyProfileDTO.getTierUpdatedDate()!=null)
					  userprofileDTO.setTierUpdatedDate(loyaltyProfileDTO.getTierUpdatedDate() == null ? null : dateFormat.format(loyaltyProfileDTO.getTierUpdatedDate()));

					int rewardPoint =0;
					
					if(loyaltyProfileDTO.getRewardPoints()>0)
					  rewardPoint = (int) Math.round(loyaltyProfileDTO.getRewardPoints());
					logger.info("RewardPoint" + rewardPoint);
					userprofileDTO.setRewardPoints(rewardPoint);
					
					
					int statusPoint=0;
					
					if(loyaltyProfileDTO.getStatusPoints()>0)
					   statusPoint = (int) Math.round(loyaltyProfileDTO.getStatusPoints());

					logger.info("StatusPoint" + statusPoint);
					userprofileDTO.setStatusPoints(statusPoint);
					
					int tierPoints = (int) Math.round(loyaltyProfileDTO.getTierPoints());
					logger.info("TierPoints" + tierPoints);
					userprofileDTO.setTierpoints(tierPoints);

					int bonusPoint=0;
					if(loyaltyProfileDTO.getBonusPoints()>0)
					  bonusPoint = (int) Math.round(loyaltyProfileDTO.getBonusPoints());

					logger.info("BonusPoint" + bonusPoint);
					userprofileDTO.setBonuspoints(bonusPoint);
					
					//getting next tier
					/*NextTierInfoDTO nextTierInfoDTO = new NextTierInfoDTO();
					TierDetails tierDetails = null;
					NextTierInfoBO infoBO=new NextTierInfoBO();
					nextTierInfoDTO.setTransactionId(userprofileDTO.getTransactionId());
					nextTierInfoDTO.setLangId(userprofileDTO.getLanguageId());
					nextTierInfoDTO.setLoyaltyId(userprofileDTO.getLoyaltyID()+"");
					nextTierInfoDTO.setChannel(userprofileDTO.getChannel());
					nextTierInfoDTO.setSubscriberNumber(userprofileDTO.getSubscriberNumber());
					tierDetails = infoBO.getNextTierInfo(nextTierInfoDTO);
					if(tierDetails!=null){
						userprofileDTO.setNextTierName(tierDetails.getNextTierName());

						userprofileDTO.setPointsToNextTier(tierDetails.getPointsToNextTier()+"");
						}*/
					// /Expiry Points

					/*logger.info("Going to call PointDetailsBL with LoyaltyID:[" + loyaltyProfileDTO.getLoyaltyID() + "] Language ID:[" + userprofileDTO.getLanguageId() + "]");
					pointDetailsDTO.setDefaultLanguage(userprofileDTO.getLanguageId());
					pointDetailsDTO.setLoyaltyID(loyaltyProfileDTO.getLoyaltyID());
					pointDetailsBL.getExpiryPoints(pointDetailsDTO);
					expiryList = pointDetailsDTO.getExpiryPointsList();
					logger.info("List got from BL::" + expiryList);
					if (expiryList != null && expiryList.size() > 0) {
						pointInfoDTO = new PointDetailsInfoDTO();
						pointInfoDTO = expiryList.get(0);
						if (pointInfoDTO.getExpiryDate() != null && !pointInfoDTO.getExpiryDate().equalsIgnoreCase("null"))
							userprofileDTO.setExpiryDate(pointInfoDTO.getExpiryDate());
						else
							userprofileDTO.setExpiryDate("");
						if (pointInfoDTO.getRewardPoints() != null && !pointInfoDTO.getRewardPoints().equalsIgnoreCase("null"))
							userprofileDTO.setExpiryRewardPoint(pointInfoDTO.getRewardPoints());
						else
							userprofileDTO.setExpiryRewardPoint("0");
					} else {
						userprofileDTO.setExpiryDate("");
						userprofileDTO.setExpiryRewardPoint("0");
					}
					
					 * if(loyaltyProfileDTO!=null &&
					 * loyaltyProfileDTO.getDefaultLanguage()==null) {
					 * loyaltyProfileDTO
					 * .setDefaultLanguage(Cache.defaultLanguageID); }
					 

					// genericDTO.setStatusCode("0");//genericDTO.setStatus("SUCCESS");
*/
					//ArrayList<String> travellerAppList = userprofileDAO.getTravellerAppWhiteListSubscribers();

					/*
					 * if (userprofileDTO.isIdentifier() &&
					 * Cache.getCacheMap().keySet().contains("MIN_STATUS_POINT")
					 * && minStatusPoint.length() > 0 &&
					 * userprofileDTO.getStatusPoints() >=
					 * Integer.parseInt(minStatusPoint) &&
					 * userprofileDTO.getSubscriberNumber
					 * ().trim().equalsIgnoreCase
					 * (loyaltyProfileDTO.getContactNumber().trim()) &&
					 * (travellerAppList!=null &&
					 * travellerAppList.contains(userprofileDTO
					 * .getSubscriberNumber().trim()))) {
					 */
					
					logger.info("Tier Id is>>>>>>>>>>>"+userprofileDTO.getTierId());

				/*	if (userprofileDTO.isIdentifier() && userprofileDTO.getSubscriberNumber().trim().equalsIgnoreCase(loyaltyProfileDTO.getContactNumber().trim())
							&& userprofileDTO.getTierId()>=2 && (travellerAppList != null && travellerAppList.contains(userprofileDTO.getSubscriberNumber().trim()))) {
						Long loyaltyId = loyaltyProfileDTO.getLoyaltyID();
						logger.info("getting loyalty id " + loyaltyId);

						userIdentifierDTO = userprofileDAO.getUserIdentifier(null, loyaltyId);

						if ((userIdentifierDTO != null && loyaltyId.equals(userIdentifierDTO.getLoyaltyID())) && (userIdentifierDTO.getUserName() != null || userIdentifierDTO.getPassWord() != null)) {
							logger.info("userIdentifierDTO ::" + userIdentifierDTO.toString());
							if (userIdentifierDTO.getUserName() == null || userIdentifierDTO.getUserName().equalsIgnoreCase("") || userIdentifierDTO.getUserName().equalsIgnoreCase("null")) {
								userprofileDTO.setUserName("");
							} else {
								userprofileDTO.setUserName(userIdentifierDTO.getUserName());
							}
							if (userIdentifierDTO.getPassWord() == null || userIdentifierDTO.getPassWord().equalsIgnoreCase("") || userIdentifierDTO.getPassWord().equalsIgnoreCase("null")) {
								userprofileDTO.setPassWord("");
							} else {
								userprofileDTO.setPassWord(encryptPassword.decryptPassWord(userIdentifierDTO.getPassWord()));
							}

						} else {
							userLoginInfoDTO = userprofileDAO.getAuthInfo();
							if (userLoginInfoDTO == null || (userLoginInfoDTO.getUserName() == null && userLoginInfoDTO.getPassWord() == null)) {
								logger.info("not able to find and new user pass");
							} else {
								if (userLoginInfoDTO.getUserName() == null || userLoginInfoDTO.getUserName().equalsIgnoreCase("") || userLoginInfoDTO.getUserName().equalsIgnoreCase("null")) {
									userprofileDTO.setUserName("");
								} else {
									userprofileDTO.setUserName(userLoginInfoDTO.getUserName());
								}
								if (userLoginInfoDTO.getPassWord() == null || userLoginInfoDTO.getPassWord().equalsIgnoreCase("") || userLoginInfoDTO.getPassWord().equalsIgnoreCase("null")) {
									userprofileDTO.setPassWord("");
									userLoginInfoDTO.setPassWord("");
								} else {
									userprofileDTO.setPassWord(userLoginInfoDTO.getPassWord());
									userLoginInfoDTO.setPassWord(encryptPassword.encryptPassword(userLoginInfoDTO.getPassWord()));
								}

								Boolean insertData = userprofileDAO.insertIdentifierInfo(loyaltyId, userLoginInfoDTO);
								logger.info("new record in identifier table");
								if (insertData)
									userprofileDAO.updateLoginInfo(userLoginInfoDTO);
								logger.info("updated login info");
							}
						}

					}*/
					logger.info("##Successfull##");
					/*logger.info("##StatusCode setting##" + Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId()).getStatusCode());
					logger.info("##Status setting##" + Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId()).getStatusDesc());
					*/// logger.info("Contains  "+Cache.getServiceStatusMap().containsKey("GET_USER_PROFILE_SUCCESS_1"));

				//	genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
					/*genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId()).getStatusCode());
					genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_USER_PROFILE_SUCCESS_" + userprofileDTO.getLanguageId()).getStatusDesc());*/
					genericDTO.setStatusCode("SC0000");
					genericDTO.setStatus("SUCCESS");
				}

				Map<Integer, TierInfoDTO> TierInfoMap;
				// TierInfoDTO tierinfo=new TierInfoDTO();
				/*String Statusdec = null;
				logger.info("TierId" + loyaltyProfileDTO.getTierId());
				logger.info("TierInfoLanguageMap" + Cache.getTierLanguageInfoMap().get(loyaltyProfileDTO.getTierId() + "_" + userprofileDTO.getLanguageId()));
				String tierName = Cache.getTierLanguageInfoMap().get(loyaltyProfileDTO.getTierId() + "_" + userprofileDTO.getLanguageId());
				if (loyaltyProfileDTO != null && loyaltyProfileDTO.getTierId() != null) {
					// tierinfo= (TierInfoDTO)
					// com.sixdee.imp.common.config.Cache.getTierLanguageInfoMap().get(loyaltyProfileDTO.getTierId());
					Statusdec = com.sixdee.imp.common.config.Cache.getStatusMap().get("" + loyaltyProfileDTO.getStatusID() + "_" + userprofileDTO.getLanguageId());

					logger.info("StatusDesc >>>" + Statusdec);
					userprofileDTO.setStatusDesc(Statusdec);
				}

				if (tierName != null) {
					logger.info("###TierInfo Details###");
					logger.info("TierId" + loyaltyProfileDTO.getTierId());
					logger.info("TierName" + tierName);
					userprofileDTO.setTierName(tierName);
				}*/
			  }else{
				  logger.info("##FAILURE...Customer is in soft delete>>>");
					//genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_INVALID_SUB_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
					logger.info("StatusCode" + "SC0002");
					logger.info("Status" + "Customer is in soft delete, register again");
					// genericDTO.setStatusCode("1");//genericDTO.setStatus("FAILURE...Subscriber Number is NULL");
					genericDTO.setStatusCode("SC0002");
					genericDTO.setStatus("Customer is in soft delete, register again");
			  }
			}
		} else {
			if (userprofileDTO.getSubscriberNumber() == null)// GET_USER_PROFILE_INVALID_SUB_
			{
				logger.info("##FAILURE...Subscriber Number is NULL##");
				genericDTO = commonUtil.getStatusCodeDescription(genericDTO, "GET_USER_PROFILE_INVALID_SUB_" + userprofileDTO.getLanguageId(), userprofileDTO.getTransactionId());
				logger.info("StatusCode" + genericDTO.getStatusCode());
				logger.info("Status" + genericDTO.getStatus());
				// genericDTO.setStatusCode("1");//genericDTO.setStatus("FAILURE...Subscriber Number is NULL");
				genericDTO.setStatusCode(genericDTO.getStatusCode());
				genericDTO.setStatus(genericDTO.getStatus());
			}
			
		}
		genericDTO.setObj(userprofileDTO);
		}catch(Exception e){
			e.printStackTrace();
			
			
		}finally{
			 userprofileDAO = null;
			 tabDAO = null;
			 subscriberNumDTO = null;
			 loyaltyProfileDTO = null;
			 nationalNumDTo = null;
			 accntNumDTO = null;
			 commonUtil = null;
			 isAuthenticate = false;
			 process = false;
		}
		return genericDTO;
	}

}

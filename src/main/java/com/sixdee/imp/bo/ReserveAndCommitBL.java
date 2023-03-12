package com.sixdee.imp.bo;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.PointExpiryDAO;
import com.sixdee.imp.dao.ReserveAndCommitDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.OnmRedemptionTransactionDTO;
import com.sixdee.imp.dto.ReserveAndCommitReqDto;
import com.sixdee.imp.dto.ReserveAndCommitRespDto;
import com.sixdee.imp.service.ReServices.DAO.TierAndBonusCalculationDAO;
import com.sixdee.imp.util.CDRLoggerUtil;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.lms.util.selections.CDRCommandID;

public class ReserveAndCommitBL extends BOCommon {
	private Logger logger = LogManager.getLogger(ReserveAndCommitBL.class);

	@Override
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {
		// TODO Auto-generated method stub
		logger.info("**ReserveAndCommitBL***");
		ReserveAndCommitReqDto reserveAndCommitReqDto = null;
		
		ReserveAndCommitRespDto reserveAndCommitRespDto = new ReserveAndCommitRespDto();
		TableDetailsDAO tableDetailsDAO = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		
		TierAndBonusCalculationDAO tierAndBonusCalculationDAO = null;
		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO=null;
		String txnId = null;
		String timeStamp = null;
		
		ReserveAndCommitDAO reserveAndCommitDAO = null;
		double points = 0.0;
		Boolean isUpdate = null;
		Boolean flag=false;
		PointExpiryDAO pointExpiryDAO=null;
		OnmRedemptionTransactionDTO onmRedemptionTransactionDTO = null;
		CDRInformationDTO cdrInformationDTO = null;
		int reservePoints = 0;
		int updatePoints = 0;
		double pointsExpired = 0.0;
		try {

			reserveAndCommitReqDto = (ReserveAndCommitReqDto) genericDTO.getObj();
			logger.info("**Point***"+reserveAndCommitReqDto.getPoints());
			txnId = reserveAndCommitReqDto.getRequestId();
			timeStamp = reserveAndCommitReqDto.getTimestamp();
			loyaltyProfileTabDTO = new LoyaltyProfileTabDTO();
			tableDetailsDAO = new TableDetailsDAO();
			tierAndBonusCalculationDAO = new TierAndBonusCalculationDAO();
			customerProfileTabDTO = new CustomerProfileTabDTO();
			
			reserveAndCommitDAO = new ReserveAndCommitDAO();

			
			
			
			if(reserveAndCommitReqDto.getServiceId()!=null && !reserveAndCommitReqDto.getServiceId().equalsIgnoreCase("")) {
				customerProfileTabDTO = tableDetailsDAO.getCustomerProfile(reserveAndCommitReqDto.getServiceId());
                  }else {
                	  customerProfileTabDTO=tableDetailsDAO.getCustomerProfile(reserveAndCommitReqDto.getMsisdn());
                  }

			if (customerProfileTabDTO != null) {
				loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(customerProfileTabDTO.getLoyaltyId());
                 
	
				if (loyaltyProfileTabDTO != null) {

					if (reserveAndCommitReqDto.getKeyword().equalsIgnoreCase("reserve")) {

						boolean ispresent = reserveAndCommitDAO.checkonmRedemptionDetails(txnId,
								String.valueOf(loyaltyProfileTabDTO.getLoyaltyID()));

						if (!ispresent) {
							if (reserveAndCommitReqDto.getPoints() != null
									&& !reserveAndCommitReqDto.getPoints().equalsIgnoreCase("")) {

								if (loyaltyProfileTabDTO.getRewardPoints() >= Double
										.parseDouble(reserveAndCommitReqDto.getPoints())) {

									points = Double.parseDouble(reserveAndCommitReqDto.getPoints());
									
									loyaltyProfileTabDTO.setStatusUpdatedDate(new Date());
								
									loyaltyProfileTabDTO.setReservePoints(points);
									isUpdate = reserveAndCommitDAO.updatePointDetails(txnId, loyaltyProfileTabDTO);

									if (isUpdate) {
                                        onmRedemptionTransactionDTO = new OnmRedemptionTransactionDTO();
										onmRedemptionTransactionDTO.setCreateTime(new Date());
										onmRedemptionTransactionDTO.setUpdateTime(new Date());
										onmRedemptionTransactionDTO
												.setLoyaltyId(String.valueOf(loyaltyProfileTabDTO.getLoyaltyID()));
										onmRedemptionTransactionDTO.setStatus(1);
										onmRedemptionTransactionDTO.setMoNumber(reserveAndCommitReqDto.getMsisdn());
										onmRedemptionTransactionDTO
												.setProcessStartTime((int) System.currentTimeMillis());
										onmRedemptionTransactionDTO.setProcessEndTime((int) System.currentTimeMillis());
										onmRedemptionTransactionDTO.setTransactionId(txnId);
										onmRedemptionTransactionDTO.setRedeemPoints((int) points);
										onmRedemptionTransactionDTO.setProductId(reserveAndCommitReqDto.getProductId());
										onmRedemptionTransactionDTO
												.setProductName(reserveAndCommitReqDto.getProductName());
										onmRedemptionTransactionDTO.setDescription(reserveAndCommitReqDto.getDescription());
										 flag=reserveAndCommitDAO.inserOnmRedemptionTransaction(onmRedemptionTransactionDTO);
										
										if(flag) {
										reserveAndCommitRespDto.setRequestId(txnId);
										reserveAndCommitRespDto.setTimestamp(timeStamp);
										reserveAndCommitRespDto.setRespCode("SC0000");
										reserveAndCommitRespDto.setRespDesc("SUCCESS");
										}else {
											reserveAndCommitRespDto.setRequestId(txnId);
											reserveAndCommitRespDto.setTimestamp(timeStamp);
											reserveAndCommitRespDto.setRespCode("SC0001");
											reserveAndCommitRespDto.setRespDesc("FAILURE");
										}
									}

								} else {
									logger.info("Transaction id {} Points Are Not Enough To reserve",txnId);
									reserveAndCommitRespDto.setRequestId(txnId);
									reserveAndCommitRespDto.setTimestamp(timeStamp);
									reserveAndCommitRespDto.setRespCode("SC0001");
									reserveAndCommitRespDto.setRespDesc("FAILURE");
								}

							} else {
								logger.info("Transaction id {} Missing Points In Reserve Request",txnId);
								reserveAndCommitRespDto.setRequestId(txnId);
								reserveAndCommitRespDto.setTimestamp(timeStamp);
								reserveAndCommitRespDto.setRespCode("SC0001");
								reserveAndCommitRespDto.setRespDesc("FAILURE");
							}

						} else {

							logger.info("Transaction id {} Duplicate Txn Id",txnId);
							reserveAndCommitRespDto.setRequestId(txnId);
							reserveAndCommitRespDto.setTimestamp(timeStamp);
							reserveAndCommitRespDto.setRespCode("SC0001");
							reserveAndCommitRespDto.setRespDesc("FAILURE");

						}

					} else if (reserveAndCommitReqDto.getKeyword().equalsIgnoreCase("commit")) {

						logger.info("Transaction Id {} Committing Points With TxnId {}",txnId,txnId);
						
						onmRedemptionTransactionDTO = reserveAndCommitDAO.getonmRedemptionDetails(txnId,loyaltyProfileTabDTO.getLoyaltyID(),1);
						if(onmRedemptionTransactionDTO != null) {
							logger.info("Transaction Id {} Points Committed For Txn {}",txnId,onmRedemptionTransactionDTO.getRedeemPoints());
							reservePoints =onmRedemptionTransactionDTO.getRedeemPoints();
							updatePoints = reservePoints;
							pointExpiryDAO = new PointExpiryDAO();
							pointsExpired = pointExpiryDAO.pointsExpiry(txnId, loyaltyProfileTabDTO,
									Long.valueOf(reservePoints), "1");
							logger.debug("Transaction Id {} Points From Onm Table {}",txnId,pointsExpired);
							if((pointsExpired != 0 ) && (pointsExpired != (double)reservePoints))
							{
								logger.debug("Transaction id {} Difference {}",txnId,(reservePoints-pointsExpired));
								updatePoints= (int) (reservePoints-pointsExpired);
							}
			
							loyaltyProfileTabDTO.setStatusUpdatedDate(new Date());
							
							loyaltyProfileTabDTO.setReservePoints(updatePoints);
							isUpdate =reserveAndCommitDAO.commitpointDetails(txnId, loyaltyProfileTabDTO);
							if(isUpdate) {
								//update txn 
								
								loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
								loyaltyTransactionTabDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
								if(reserveAndCommitReqDto.getServiceId()!=null && !reserveAndCommitReqDto.getServiceId().equalsIgnoreCase("")) {
									loyaltyTransactionTabDTO.setSubscriberNumber(reserveAndCommitReqDto.getServiceId());
								}else {
									loyaltyTransactionTabDTO.setSubscriberNumber(reserveAndCommitReqDto.getMsisdn());
								}
								loyaltyTransactionTabDTO.setAccountNumber(customerProfileTabDTO.getAccountNo());
								loyaltyTransactionTabDTO.setReqTransactionID(txnId);
								loyaltyTransactionTabDTO.setChannel(reserveAndCommitReqDto.getChannel());
								
								loyaltyTransactionTabDTO.setRewardPoints((double)reservePoints);
								loyaltyTransactionTabDTO.setPreRewardPoints(loyaltyProfileTabDTO.getRewardPoints()+reservePoints);
								loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
								loyaltyTransactionTabDTO.setCurTierId(loyaltyProfileTabDTO.getTierId());

								loyaltyTransactionTabDTO.setPackageId(reserveAndCommitReqDto.getProductId());
								//loyaltyTransactionTabDTO.setServiceId(reserveAndCommitReqDto.getServiceId());
								loyaltyTransactionTabDTO.setStatusID(8);
								//loyaltyTransactionTabDTO.setStatusDesc(reserveAndCommitReqDto.getProductName());
								logger.info("statusDesc:{}",onmRedemptionTransactionDTO.getDescription());
								loyaltyTransactionTabDTO.setStatusDesc(onmRedemptionTransactionDTO.getDescription());
								tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);
								
								onmRedemptionTransactionDTO.setStatus(2);
								onmRedemptionTransactionDTO.setUpdateTime(new Date());
								reserveAndCommitDAO.updateOnmRedemptionTransaction(txnId,onmRedemptionTransactionDTO);
								reserveAndCommitRespDto.setRequestId(txnId);
								reserveAndCommitRespDto.setTimestamp(timeStamp);
								reserveAndCommitRespDto.setRespCode("SC0000");
								reserveAndCommitRespDto.setRespDesc("SUCCESS");
							}
							
						}else {
							logger.info("Transaction id {} No Points Was Reserved With TxnId {}",txnId,txnId) ;
							reserveAndCommitRespDto.setRequestId(txnId);
							reserveAndCommitRespDto.setTimestamp(timeStamp);
							reserveAndCommitRespDto.setRespCode("SC0001");
							reserveAndCommitRespDto.setRespDesc("FAILURE");
						}
						
					

						
						
						
					} else if (reserveAndCommitReqDto.getKeyword().equalsIgnoreCase("rollback")) {

						logger.info("Transaction Id {} Committing Points With TxnId {}",txnId,txnId);
						onmRedemptionTransactionDTO = reserveAndCommitDAO.getonmRedemptionDetails(txnId,
								loyaltyProfileTabDTO.getLoyaltyID(), 1);
						if (onmRedemptionTransactionDTO != null) {
							logger.info("Transaction Id {} Points Committed For Txn {}",txnId,onmRedemptionTransactionDTO.getRedeemPoints());
							points = onmRedemptionTransactionDTO.getRedeemPoints();
							reservePoints = onmRedemptionTransactionDTO.getRedeemPoints();
					
						
							
							loyaltyProfileTabDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
							loyaltyProfileTabDTO.setRewardPoints(points);
							loyaltyProfileTabDTO.setReservePoints(reservePoints);
							loyaltyProfileTabDTO.setStatusUpdatedDate(new Date());
							isUpdate = reserveAndCommitDAO.rollbackPointDetails(txnId, loyaltyProfileTabDTO);
							if (isUpdate) {
								// update txn
								onmRedemptionTransactionDTO.setStatus(3);
								onmRedemptionTransactionDTO.setUpdateTime(new Date());
								reserveAndCommitDAO.updateOnmRedemptionTransaction(txnId, onmRedemptionTransactionDTO);
								reserveAndCommitRespDto.setRequestId(txnId);
								reserveAndCommitRespDto.setTimestamp(timeStamp);
								reserveAndCommitRespDto.setRespCode("SC0000");
								reserveAndCommitRespDto.setRespDesc("SUCCESS");
							}

						} else {
							logger.info("Transaction id {} No Points Was Reserved With TxnId {}",txnId,txnId);
							reserveAndCommitRespDto.setRequestId(txnId);
							reserveAndCommitRespDto.setTimestamp(timeStamp);
							reserveAndCommitRespDto.setRespCode("SC0001");
							reserveAndCommitRespDto.setRespDesc("FAILURE");
						}

					} else {

						logger.info("Transaction id {} Invalid keyword",txnId);
						reserveAndCommitRespDto.setRequestId(txnId);
						reserveAndCommitRespDto.setTimestamp(timeStamp);
						reserveAndCommitRespDto.setRespCode("SC0001");
						reserveAndCommitRespDto.setRespDesc("FAILURE");

					}

				} else {
					logger.info("Transaction id {} Loyalty Points Not Available",txnId);
					reserveAndCommitRespDto.setRequestId(txnId);
					reserveAndCommitRespDto.setTimestamp(timeStamp);
					reserveAndCommitRespDto.setRespCode("SC0001");
					reserveAndCommitRespDto.setRespDesc("FAILURE");
				}

			} else {

				logger.info("Transaction id {} Loyalty Details Not Available For User",txnId);
				reserveAndCommitRespDto.setRequestId(txnId);
				reserveAndCommitRespDto.setTimestamp(timeStamp);
				reserveAndCommitRespDto.setRespCode("SC0001");
				reserveAndCommitRespDto.setRespDesc("FAILURE");
			}
			logger.info("Transaction Id {} responseCode {} response desc {}",txnId,reserveAndCommitRespDto.getRespCode(),reserveAndCommitRespDto.getRespDesc());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			if(reserveAndCommitReqDto.getKeyword() !=null && reserveAndCommitReqDto.getKeyword().equalsIgnoreCase("commit")) {
				cdrInformationDTO = new CDRInformationDTO();
				cdrInformationDTO.setTransactionId(txnId);
				cdrInformationDTO.setCommandId(CDRCommandID.Redemption.getId());
				if(reserveAndCommitReqDto.getChannel() !=null && !reserveAndCommitReqDto.getChannel().equalsIgnoreCase(""))
				cdrInformationDTO.setChannelID(Cache.channelDetails.get(reserveAndCommitReqDto.getChannel()));
				if(loyaltyProfileTabDTO !=null) {
					cdrInformationDTO.setLoyaltyId(String.valueOf(loyaltyProfileTabDTO.getLoyaltyID()));
					cdrInformationDTO.setPreviousTier(loyaltyProfileTabDTO.getTierId());
					cdrInformationDTO.setCurrentTier(loyaltyProfileTabDTO.getTierId());
					//cdrInformationDTO.setPreRewardPoints(loyaltyProfileTabDTO.getRewardPoints() + (double)updatePoints);
					//cdrInformationDTO.setCurRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
					//cdrInformationDTO.setPreStatusPoints(loyaltyProfileTabDTO.getStatusPoints());
					//cdrInformationDTO.setCurStatusPoints(loyaltyProfileTabDTO.getStatusPoints());
					//cdrInformationDTO.setField1(String.valueOf(customerProfileTabDTO.getServiceType()));
				}
				if(reserveAndCommitReqDto.getServiceId()!=null && !reserveAndCommitReqDto.getServiceId().equalsIgnoreCase(""))					
				{
					//cdrInformationDTO.setServiceId(reserveAndCommitReqDto.getServiceId());
				}
				else if(reserveAndCommitReqDto.getMsisdn() !=null && !reserveAndCommitReqDto.getMsisdn().equalsIgnoreCase(""))
					cdrInformationDTO.setSubscriberNumber(reserveAndCommitReqDto.getMsisdn());
				if(reserveAndCommitRespDto.getRespCode() !=null) {
				cdrInformationDTO.setStatusCode(reserveAndCommitRespDto.getRespCode());
				cdrInformationDTO.setStatusDescription(reserveAndCommitRespDto.getRespDesc());
				}else {
					cdrInformationDTO.setStatusCode("SC0001");
					cdrInformationDTO.setStatusDescription("FAILURE");
				}
				//cdrInformationDTO.setRewardPoints(updatePoints);
				if(reserveAndCommitReqDto.getProductId() >0) 
					cdrInformationDTO.setOfferId(String.valueOf(reserveAndCommitReqDto.getProductId()));
				if(reserveAndCommitReqDto.getProductName() !=null && !reserveAndCommitReqDto.getProductName().equalsIgnoreCase(""))
					//cdrInformationDTO.setOfferName(reserveAndCommitReqDto.getProductName());
				logger.info("CDR {}",cdrInformationDTO.toString());
				CDRLoggerUtil.flushFatalCDR(cdrInformationDTO);
			}
			genericDTO.setObj(reserveAndCommitRespDto);
			reserveAndCommitRespDto= null;
			reserveAndCommitReqDto = null;
			loyaltyProfileTabDTO = null;
			tableDetailsDAO = null;
			tierAndBonusCalculationDAO = null;
		}

		return genericDTO;
	}

}

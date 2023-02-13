package com.sixdee.imp.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.TierAndBonusPointDetailsDTO;

public class PointExpiryDAO {

	private static final Logger logger = Logger.getLogger(PointExpiryDAO.class);

	public Double pointsExpiry(String transactionId, LoyaltyProfileTabDTO loyaltyProfileTabDTO, Long points,
			String pointType) {
		double rewardPointFrmTxn = 0.0;
		double totalrewardPointFrmTxn = 0.0;
		long t1 = System.currentTimeMillis();
		TableInfoDAO infoDAO = null;
		ArrayList<TierAndBonusPointDetailsDTO> tierAndBonusPointDetailsDTOs = null;
		double actualPointsExpired = 0.0;
		double expiryPoints = points;
		double requestedExpiry = points;
		boolean isUpdate = false;
		try {
			infoDAO = new TableInfoDAO();
			String tableName = "TierAndBonusPointDetailsDTO";
			tierAndBonusPointDetailsDTOs = getFirstExpiringRewardPoints(transactionId,
					loyaltyProfileTabDTO.getLoyaltyID(), tableName);
			if (tierAndBonusPointDetailsDTOs != null && tierAndBonusPointDetailsDTOs.size() > 0) {
				if (pointType.equalsIgnoreCase("1")) {
					logger.info("Point Type is " + pointType);
					for (TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO : tierAndBonusPointDetailsDTOs) {
						rewardPointFrmTxn = 0.0;
						if (tierAndBonusPointDetailsDTO.getTierPoints() >= expiryPoints) {
							tierAndBonusPointDetailsDTO
									.setTierPoints(tierAndBonusPointDetailsDTO.getTierPoints() - expiryPoints);
							 tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTotalPoints() - expiryPoints);
							rewardPointFrmTxn = expiryPoints;
						} else {
							rewardPointFrmTxn = tierAndBonusPointDetailsDTO.getTierPoints();
							tierAndBonusPointDetailsDTO.setTierPoints(0.0);
						}

						if (rewardPointFrmTxn > 0) {
							actualPointsExpired = actualPointsExpired + rewardPointFrmTxn;
							expiryPoints = expiryPoints - rewardPointFrmTxn;
						}
						isUpdate = updateOnmTable(tierAndBonusPointDetailsDTO, tableName);
						if (isUpdate) {
							totalrewardPointFrmTxn = totalrewardPointFrmTxn + rewardPointFrmTxn;
							logger.info("totalrewardPointFrmTxn: " + totalrewardPointFrmTxn + "requestedExpiry :"
									+ requestedExpiry);

							if (totalrewardPointFrmTxn == requestedExpiry) {
								logger.debug(" Inisde break");
								break;
							} else {
								logger.debug("Step 2.2 expiryPoints :" + expiryPoints);
							}
						} else {
							expiryPoints = expiryPoints + rewardPointFrmTxn;
						}
					}
				} else {
					logger.info("Transaction Id :" + transactionId + " No Onm Details Available for Loyaltyid :"
							+ loyaltyProfileTabDTO.getLoyaltyID());
				}

				if (pointType.equalsIgnoreCase("2")) {
					logger.info("Point Type is " + pointType);
					for (TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO : tierAndBonusPointDetailsDTOs) {
						rewardPointFrmTxn = 0.0;
						if (tierAndBonusPointDetailsDTO.getBonusPoints() >= expiryPoints) {
							logger.info("statu points before expiry: " + tierAndBonusPointDetailsDTO.getBonusPoints());
							tierAndBonusPointDetailsDTO
									.setBonusPoints(tierAndBonusPointDetailsDTO.getBonusPoints() - expiryPoints);
							tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTotalPoints() - expiryPoints);
							logger.info("statu points after expiry: " + tierAndBonusPointDetailsDTO.getBonusPoints());
							rewardPointFrmTxn = expiryPoints;
						} else {
							rewardPointFrmTxn = tierAndBonusPointDetailsDTO.getBonusPoints();
							tierAndBonusPointDetailsDTO.setBonusPoints(0.0);
						}

						if (rewardPointFrmTxn > 0) {
							actualPointsExpired = actualPointsExpired + rewardPointFrmTxn;
							expiryPoints = expiryPoints - tierAndBonusPointDetailsDTO.getBonusPoints();
						}
						isUpdate = updateOnmTable(tierAndBonusPointDetailsDTO, tableName);
						if (isUpdate) {
							totalrewardPointFrmTxn = totalrewardPointFrmTxn + rewardPointFrmTxn;
							if (totalrewardPointFrmTxn == requestedExpiry) {
								logger.debug(" Inisde break");
								break;
							} else {
								logger.debug("Step 2.2 expiryPoints :" + expiryPoints);
							}
						} else {
							expiryPoints = expiryPoints + rewardPointFrmTxn;
						}
					}
				} else {
					logger.info("Transaction Id :" + transactionId + " No Onm Details Available for Loyaltyid :"
							+ loyaltyProfileTabDTO.getLoyaltyID());
				}
			}
		} catch (Exception exception) {
			logger.error("Exception " + exception);
		} finally {
			logger.info("Total Time taken for pointExpiry " + (System.currentTimeMillis() - t1));
		}
		return totalrewardPointFrmTxn;
	}

	public String getTierAndBonusPointTable(String subscriberNumber) {
		// String tablePrefix="SUBSCRIBER_NUMBER";
		String tablePrefix = "TIER_BONUS_POINTS_DAY_INFO_ENTITY";
		int index = Integer
				.parseInt(Cache.getConfigParameterMap().get("TIER_BONUS_DAY_SUFFIX_LENGTH").getParameterValue());
		String tableName = "_0";
		if (index > 0)
			tableName = "_" + subscriberNumber.substring(subscriberNumber.length() - index);

		return tablePrefix + tableName;

	}

	public ArrayList<TierAndBonusPointDetailsDTO> getFirstExpiringRewardPoints(String transactionId, long loyaltyID,
			String tableName) {

		Session session = null;
		ArrayList<TierAndBonusPointDetailsDTO> tierAndBonusList = new ArrayList<TierAndBonusPointDetailsDTO>();
		long t1 = System.currentTimeMillis();
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			String sql = " FROM " + tableName + " WHERE loyaltyId=? order by expiryDate ASC";
			Query query = session.createQuery(sql);
			query.setParameter(0, loyaltyID + "");
			tierAndBonusList = (ArrayList<TierAndBonusPointDetailsDTO>) query.list();
		} catch (Exception e) {
			logger.error("Transaction ID " + transactionId + " Exception occured ", e);
			e.printStackTrace();

		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
			logger.info(
					"Transaction " + transactionId + " Expiry points updated in " + (System.currentTimeMillis() - t1));

		}
		return tierAndBonusList;

	}
	private boolean updateOnmTable(TierAndBonusPointDetailsDTO tbDTO,String onmTableName) {
        boolean isUpdate = false;
        Session session = null;
        Transaction tx = null;
        Criteria criteria = null;
        long t1 = System.currentTimeMillis();
        Query query = null;
        try {
            logger.info(" Inside updateOnmTable for loyaltyId : " + tbDTO.getLoyaltyId()  +
                    " Expiry Date :" + tbDTO.getExpiryDate() + " Tier points :" + tbDTO.getTierPoints() + " Id :" + tbDTO.getId());
            session = HiberanteUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            query = session.createQuery("UPDATE "+onmTableName+" set tierPoints =:tierPoints ,totalPoints=:totalPoints, bonusPoints =:bonusPoints "
                    + "WHERE expiryDate =:expiryDate AND loyaltyId=:loyaltyId AND id=:id");
            query.setParameter("tierPoints", tbDTO.getTierPoints());
            query.setParameter("bonusPoints", tbDTO.getBonusPoints());
            //query.setParameter("statusPoints", tbDTO.getStatusPoints());
            query.setParameter("loyaltyId", tbDTO.getLoyaltyId());
            query.setParameter("expiryDate", tbDTO.getExpiryDate());
            query.setParameter("totalPoints", tbDTO.getTotalPoints());
            query.setParameter("id", tbDTO.getId());
            /*query.setParameter("msisdn", tbDTO.getMsisdn());*/
            int i = query.executeUpdate();
            if( i >0)
                isUpdate = true;
            tx.commit();
        }catch(Exception e) {
            if(tx!=null){
                tx.rollback();
            }
            e.printStackTrace();

        }finally {

            if(session!=null && session.isOpen()){
                session.close();
                session=null;
            }
            logger.info(" Time took for Update Onm Table "+(System.currentTimeMillis()-t1) + " Isupdate :" + isUpdate);

        }
        return isUpdate;

    }

}

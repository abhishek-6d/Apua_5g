package com.sixdee.imp.dao;

/**
 * 
 * @author @jith
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>June 25,2015 11:18:20 AM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.MerchantPromoMappingDTO;
import com.sixdee.imp.dto.PackageCategory;
import com.sixdee.imp.dto.PackageDetails;
import com.sixdee.imp.dto.RedeemPointsDTO;
import com.sixdee.imp.dto.VoucherPromoTranverseDTO;
import com.sixdee.util.ConnectionPool5;

public class VoucherPromoDAO {
	private Logger logger = Logger.getLogger(VoucherPromoDAO.class);
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
	private SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date date = new Date();
	int counter = 0;

	public void assignPromoCode(VoucherPromoTranverseDTO voucherPromoDTO, Session session) throws Exception {
		// Session session=null;
		Transaction transaction = null;
		long t1 = System.currentTimeMillis();
		try {

			if (session == null)
				session = HiberanteUtil.getSessionFactory().openSession();
			// transaction=session.beginTransaction();

			String sql = " SELECT PROMO_CODE,EXPIRY_DATE FROM  OFFER_MERCHANT_PROMO  WHERE MERCHANT_ID=? AND  ASSIGNED=0";

			Query query = session.createSQLQuery(sql).setParameter(0, voucherPromoDTO.getMerchandID());
			logger.info("the merchantID---->" + voucherPromoDTO.getMerchandID());

			List<Object[]> list = query.list();

			logger.info("is list empty---->" + list.isEmpty());

			if (list != null && list.size() > 0) {
				for (Object[] obj : list) {
					if (obj[1] != null) {
						//logger.info("========(new Date()))======" + dateFormat.format(new Date()));
						//logger.info("========dateFormat.parse(obj[1].toString())======" + dateFormat.format(obj[1]));
						int result = (((Date) obj[1]).compareTo(new Date()));

						//logger.info("========RESULT ID======" + result);
						if (result >= 0) {

							voucherPromoDTO.setPromoCode(obj[0].toString());
							// logger.info("========PROMOCODE in DATE CHAECK======"+obj[0].toString());
							break;
						}
					} else {
						voucherPromoDTO.setPromoCode(obj[0].toString());
						// logger.info("========PROMOCODE in NORMAL======"+obj[0].toString());
						break;
					}

				}
				logger.info(" Service : VoucherPromo -- Transaction ID :" + voucherPromoDTO.getTranscationId() + " MerchantID " + voucherPromoDTO.getMerchandID() + "PromoCode is" + voucherPromoDTO.getPromoCode());

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null)
				session.close();
			session = null;
			transaction = null;
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID FOR MERCHANT REDEMPTION GETTING PROMO CODE:" + voucherPromoDTO.getTranscationId() + " Request Leaving System , Processing Time " + (t2 - t1));

		}

	}// assignPromoCode

	public VoucherPromoTranverseDTO getSubscriberPromoValues(VoucherPromoTranverseDTO voucherPromoDTO) throws Exception {
		// Session session=null;
		//Transaction transaction = null;
		long t1 = System.currentTimeMillis();
		Session session = null;
		int counter=0;
		String Voucher=null;
		try {
			logger.info("Transaction ID FOR DOD :" + voucherPromoDTO.getTranscationId());

			session = HiberanteUtil.getSessionFactory().openSession();

			String sql = " select CREATE_DATE,PROMO_CODE FROM SUBSCRIBER_PROMO_CHECK_0 where SUBSCRIBER_NUMBER=? and MERCHANT_ID=?" + " ORDER BY CREATE_DATE desc";

			Query query = session.createSQLQuery(sql).setParameter(0, voucherPromoDTO.getSubscriberNo()).setParameter(1, voucherPromoDTO.getMerchandID());

			List<Object[]> list = query.list();

			if (list != null && list.size() > 0) {
				for (Object[] obj : list) {
					if (obj[0] != null) {
						
						//logger.info(">>>>>>>>>>>>>>>DATE FROM DB>>>>>>>>>>>>>>>>>>>>>> :" +  obj[0].toString());
						String d1=dateFormat.format(new Date());
						String d2=dateFormat.format((Date) obj[0]);
						//logger.info(">>>>>>>>>>>>>>>current date>>>>>>>>>>>>>>>>>>>>>> :" + d1);
						
						int result = ((dateFormat.parse(d2)).compareTo(dateFormat.parse(d1)));
						//logger.info(">>>>>>>>>>>>>>>RESULT>>>>>>>>>>>>>>>>>>>>>> :" + result);
						if (result==0){
							counter++;
							
							if(counter>=Integer.parseInt(Cache.getCacheMap().get("DOD_LIMIT"))){
								voucherPromoDTO.setVoucherFlag(true);
								Voucher=Voucher+","+obj[1].toString();
								logger.info(">>>>>>>>>>>>>>>COUNTER ABOVE 3 AND VOUCHER FLAG SET TO TRUE>>>>>>>>>>>>>>>>>>>>>> :" );
								break;
							}
							Voucher=Voucher+","+obj[1].toString();
							//logger.info(">>>>>>>>>>>>>>>COUNTER>>>>>>>>>>>>>>>>>>>>>> :" + counter);
							//logger.info(">>>>>>>>>>>>>>>VOUCHER>>>>>>>>>>>>>>>>>>>>>> :" + Voucher);
															
						}
						
						
					}
					
					
				}
				voucherPromoDTO.setPromoCode(Voucher.replaceFirst("null,", ""));
				logger.info(">>>>>>>>>>>>>>>VOUCHER>>>>>>>>>>>>>>>>>>>>>> :" + voucherPromoDTO.getPromoCode());
			}
			logger.info("VOUCHER FLAG IS"+voucherPromoDTO.isVoucherFlag());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (session != null)
				session.close();
			session = null;
			//transaction = null;
			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID FOR PROMO CODE:" + voucherPromoDTO.getTranscationId() + " Request Leaving System , Processing Time " + (t2 - t1));

		}
		return voucherPromoDTO;

	}// assignPromoCode

	public boolean getLimitNumber(VoucherPromoTranverseDTO voucherPromoDTO, LoyaltyProfileTabDTO loyaltyProfileTabDTO) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String sql = null;
		String sql1 = null;
		Query query = null;
		Query query1 = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		String tableName = null;
		boolean limitFlag = false;
		try {
			Calendar c = Calendar.getInstance(); // this takes current date
			c.set(Calendar.DAY_OF_MONTH, 1);

			session = HiberanteUtil.getSessionFactory().openSession();
			// transaction=session.beginTransaction();
			tableName = tableInfoDAO.getSubscriberPromoTable(voucherPromoDTO.getSubscriberNo());

			// sql=" SELECT COUNT(*) FROM  "+tableName+"  WHERE MERCHANT_ID=? AND SUBSCRIBER_NUMBER=? AND TO_DATE(CREATE_DATE ,'DD-MM-YY') =  TO_DATE('"+dateFormat.format(date)+"', 'DD-MM-YY')";
			sql = "SELECT COUNT(*) FROM  " + tableName + "  WHERE MERCHANT_ID=? AND LOYALTY_ID=? AND trim(CREATE_DATE) BETWEEN TO_DATE ('" + dateFormat.format(c.getTime()) + "', 'DD-MM-YY') AND TO_DATE ('" + dateFormat.format(date) + "', 'DD-MM-YY')";

			query = session.createSQLQuery(sql).setParameter(0, voucherPromoDTO.getMerchandID()).setParameter(1, loyaltyProfileTabDTO.getLoyaltyID());

			List<BigDecimal> list = query.list();
			logger.info(" Service : VoucherPromo -- Transaction ID :" + voucherPromoDTO.getTranscationId() + " MerchantID " + voucherPromoDTO.getMerchandID() + "USERLIMIT is" + list.get(0));
			voucherPromoDTO.setUserlimit(list.get(0).toString());

			/*
			 * sql1=" SELECT COUNT(*) FROM  "+tableName+
			 * "  WHERE MERCHANT_ID=?  AND TO_DATE(CREATE_DATE ,'DD-MM-YY') =  TO_DATE('"
			 * +dateFormat.format(date)+"', 'DD-MM-YY')";
			 * 
			 * 
			 * query1=session.createSQLQuery(sql1)
			 * .setParameter(0,voucherPromoDTO.getMerchandID());
			 * 
			 * 
			 * List<BigDecimal> list1=query1.list();
			 * 
			 * logger.info(" Service : VoucherPromo -- Transaction ID :"+voucherPromoDTO
			 * .getTranscationId()+" MerchantID "+voucherPromoDTO.getMerchandID()+
			 * "WholeLimit is"+list1.get(0));
			 * 
			 * voucherPromoDTO.setWholelimit(list1.get(0).toString());
			 */

			limitFlag = true;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {

			if (session != null)
				session.close();

			session = null;
			transaction = null;
			sql = null;
			query = null;
			sql1 = null;
			query1 = null;
		}

		return limitFlag;

	}// getLimitNumber

	public boolean assignValues(VoucherPromoTranverseDTO voucherPromoDTO, LoyaltyProfileTabDTO loyaltyProfileTabDTO) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String sql = null;
		String sql1 = null;
		Query query = null;
		Query query1 = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		String tableName = null;
		boolean flag = false;
		int mainCounter = 0;
		long t1 = System.currentTimeMillis();
		try {
			logger.info(" Service : VoucherPromo -- Transaction ID :" + voucherPromoDTO.getTranscationId() + " MerchantID " + voucherPromoDTO.getMerchandID() + "PromoCode is" + voucherPromoDTO.getPromoCode() + "UPDATING IN SUBSCRIBER_PROMO_CHECK_0");

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			int loopCounter = 0;
			tableName = tableInfoDAO.getSubscriberPromoTable(voucherPromoDTO.getSubscriberNo());

			while (loopCounter < Integer.parseInt(Cache.getCacheMap().get("RETRY_COUNT"))) {

				sql1 = "insert into " + tableName + " (ID,SUBSCRIBER_NUMBER,MERCHANT_ID,CREATE_DATE,PROMO_CODE,LOYALTY_ID)" + "values (SUBSCRIBER_PROMO_CHECK_0_SEQ.nextVal,?,?,?,?,?)";

				query1 = session.createSQLQuery(sql1).setParameter(0, voucherPromoDTO.getSubscriberNo()).setParameter(1, voucherPromoDTO.getMerchandID()).setParameter(2, new Date()).setParameter(3, voucherPromoDTO.getPromoCode()).setParameter(4, loyaltyProfileTabDTO.getLoyaltyID());
				int j = query1.executeUpdate();

				if (j > 0) {
					transaction.commit();
					flag = true;
					break;
				}

				loopCounter++;

			}// while

			if (loopCounter >= 1) {
				sql = "UPDATE OFFER_MERCHANT_PROMO SET  ASSIGNED=0 " + " WHERE MERCHANT_ID=? AND PROMO_CODE=? AND ASSIGNED=1";

				query = session.createSQLQuery(sql).setParameter(0, voucherPromoDTO.getMerchandID()).setParameter(1, voucherPromoDTO.getPromoCode());

				int i = query.executeUpdate();
				if (i > 0) {
					transaction.commit();
					flag = false;
				}

				logger.info("RETRY FAILED AND ROLL BACK IS DONE:::" + voucherPromoDTO.getTranscationId());
				// throw new
				// CommonException(Cache.getServiceStatusMap().get("REW_FAIL_"+calculationDTO.getDefaultLanguage()).getStatusCode(),Cache.getServiceStatusMap().get("REW_FAIL_"+calculationDTO.getDefaultLanguage()).getStatusDesc());

			}

		} catch (Exception e) {
			logger.error("ERROR WHILE UPDATING SUBSCRIBER_PROMO_CHECK AND TRANSACTION ID IS:::" + voucherPromoDTO.getTranscationId());
			e.printStackTrace();

		} finally {
			if (session != null)
				session.close();
			session = null;
			transaction = null;
			sql = null;
			query = null;
			sql1 = null;
			query1 = null;

			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID FOR MERCHANT REDEMPTION UPDATION IN SUBSCRIBER_PROMO_CHECK:" + voucherPromoDTO.getTranscationId() + " Request Leaving System , Processing Time " + (t2 - t1));

		}
		return flag;

	}// assignPromoCode

	public boolean updatePromoValues(VoucherPromoTranverseDTO voucherPromoDTO, Session session) throws Exception {
		Transaction transaction = null;
		String sql = null;
		String sql1 = null;
		Query query = null;
		Query query1 = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		VoucherPromoDAO voucherPromoDAO = new VoucherPromoDAO();
		String tableName = null;
		boolean flag = false;
		boolean retryflag = false;

		long t1 = System.currentTimeMillis();
		try {

			logger.info(" Service : VoucherPromo -- Transaction ID :" + voucherPromoDTO.getTranscationId() + " MerchantID " + voucherPromoDTO.getMerchandID() + "PromoCode is" + voucherPromoDTO.getPromoCode() + "UPDATING IN OFFER_MERCHANT_PROMO");

			if (session == null)
				session = HiberanteUtil.getSessionFactory().openSession();

			transaction = session.beginTransaction();
			int loopCounter = 0;
			tableName = tableInfoDAO.getSubscriberPromoTable(voucherPromoDTO.getSubscriberNo());

			sql = "UPDATE OFFER_MERCHANT_PROMO SET  ASSIGNED=1 " + " WHERE MERCHANT_ID=? AND PROMO_CODE=? AND ASSIGNED=0";

			query = session.createSQLQuery(sql).setParameter(0, voucherPromoDTO.getMerchandID()).setParameter(1, voucherPromoDTO.getPromoCode());

			int i = query.executeUpdate();

			if (i > 0) {
				transaction.commit();
				flag = true;
			} else {

				while (counter <= 3) {
					voucherPromoDAO.assignPromoCode(voucherPromoDTO,null);
					retryflag = updatePromoValues(voucherPromoDTO,null);
					if (retryflag)
						return retryflag;
					if (counter == 3)
						break;
					counter++;
				}
			}

		} catch (Exception e) {
			logger.error("ERROR WHILE UPDATING OFFER_MERCHANT_PROMO AND TRANSACTION ID IS" + voucherPromoDTO.getTranscationId());
			e.printStackTrace();

		} finally {
			if (session != null && session.isOpen())
				session.close();

			session = null;
			transaction = null;
			sql = null;
			query = null;
			sql1 = null;
			query1 = null;

			long t2 = System.currentTimeMillis();
			logger.info("Transaction ID FOR MERCHANT REDEMPTION UPDATING IN OFFER_MERCHANT PROMO:" + voucherPromoDTO.getTranscationId() + " Request Leaving System , Processing Time " + (t2 - t1));

		}
		return flag;

	}// assignPromoCode

	public String getMerchantName(String merchantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		String sql = null;
		Query query = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		String tableName = null;
		boolean flag = false;
		String merchantName = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			sql = "FROM MerchantPromoMappingDTO where merchantId=:merchnatID";

			query = session.createQuery(sql);
			query.setParameter("merchnatID", Integer.parseInt(merchantID));

			List<MerchantPromoMappingDTO> list2 = query.list();
			if (list2 != null && list2.size() > 0) {

				merchantName = list2.get(0).getMerchantName();
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (session != null)
				session.close();
			session = null;
			transaction = null;
			sql = null;
			query = null;
		}
		return merchantName;

	}// formerchantName
	
	
	public String setFlagToSendPromoCode(
			VoucherPromoTranverseDTO voucherPromoDTO) throws Exception {
		// Session session=null;
				Transaction transaction = null;
				long t1 = System.currentTimeMillis();
				Session session = null;
				int counter=1;
				String Voucher=null;
				boolean flag=false;
				String subcriberNo=null;
				String subscriberNumber=null;
				try {
					logger.info("Transaction ID FOR DOD :" + voucherPromoDTO.getTranscationId());
					//logger.info(">>>>>>>>>>>>inside method setflagtosendpromocode>>>>>>>>>>");

					session = HiberanteUtil.getSessionFactory().openSession();

					String sql = "select * from (select PROMO_CODE,CREATE_DATE,SUBSCRIBER_NUMBER FROM SUBSCRIBER_PROMO_CHECK_0 where LOYALTY_ID=? and MERCHANT_ID=? ORDER BY CREATE_DATE desc ) where rownum<2";

					Query query = session.createSQLQuery(sql).setParameter(0, voucherPromoDTO.getLoyaltyId()).setParameter(1, voucherPromoDTO.getMerchandID());

					List<Object[]> list = query.list();

					if (list != null && list.size() > 0) {
						for (Object[] obj : list) {
							if (obj[1] != null) {
								String d1=dateFormat.format(obj[1]);
								subcriberNo=obj[2].toString();
								    // Go backward to get Thursday
								   Calendar thursday=Calendar.getInstance();
								   thursday.setFirstDayOfWeek(Calendar.THURSDAY);
								    thursday.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
								    String thu=dateFormat.format(thursday.getTime());
                                    Date D1=dateFormat.parse(thu);
                                    Date D=dateFormat.parse(d1);
                                  // logger.info(">>>>>>>>>>>>d1>>>>>>>>>>"+D1);
                                    logger.info(">>>>>>>>>>>>d>>>>>>>>>"+D);
								int result = (D1.compareTo(D));
								logger.info(">>>>>>>>>>>>Result value>>>>>>>>>>"+result+"  COUNTER"+counter);
								if(result<0)
									counter++;
								if (result>0 && counter<Integer.parseInt(Cache.getCacheMap().get("DOD_WEEK_LIMIT"))){
									counter++;
									flag=true;
								}
								
								
									
							}
						}
						
					}
					else
						flag=true;
					logger.info("FLAG IS"+flag);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {
					if (session != null)
						session.close();
					session = null;
					transaction = null;
					long t2 = System.currentTimeMillis();
					logger.info("Transaction ID FOR PROMO CODE:" + voucherPromoDTO.getTranscationId() + " Request Leaving System , Processing Time " + (t2 - t1));

				}
				logger.info(">>>>>>>>>>>>>>>>>>>flag>>>>>>>>>>>"+flag);
				if(flag)
					subscriberNumber=null;
				else
					subscriberNumber=subcriberNo;
		return subscriberNumber;
	}
	public PackageDetails getPackageDetails(VoucherPromoTranverseDTO promoTranverseDTO) {
		Session session = null;
		// Transaction trx = null;
		PackageDetails pd = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();

			Query query = session.createQuery("FROM PackageDetails WHERE packageId=:packageId and languageId=:languageId");
			// query.setCacheable(true);
			query.setInteger("packageId", Integer.parseInt(promoTranverseDTO.getMerchandID()));
			query.setInteger("languageId", Integer.parseInt(promoTranverseDTO.getLanguageID()));
			long l1 = System.currentTimeMillis();
			logger.info("Start Time for package" + l1);
			List<PackageDetails> packageList = query.list();
			logger.info("Total Time = " + (System.currentTimeMillis() - l1));

			if (packageList != null && packageList.size() > 0)
				pd = packageList.get(0);

			logger.info(pd.getPackageId());
			logger.info(pd.getPackageName());
			logger.info(pd.getRedeemPoints());
			logger.info(pd.getQuantity());
			logger.info(pd.getDialCode());
			
			// trx.commit();
			logger.info("Total time = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			// if(trx != null)
			// trx.rollback();
			logger.info("Exception ", e);
		} finally {
			if (session != null)
				session.close();
		}

		return pd;
	}
	public boolean updateLoyaltyProfile(PackageDetails details, LoyaltyProfileTabDTO loyaltyDTO) throws CommonException {
		Query query = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		Session session = null;
		boolean flag = false;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			String sql = " UPDATE " + tableInfoDAO.getLoyaltyProfileTable(loyaltyDTO.getLoyaltyID().toString()) + " SET rewardPoints=rewardPoints-? , counter=?" + " WHERE loyaltyID=? AND counter=?";

			query = session.createQuery(sql);
			query.setParameter(0, details.getRedeemPoints());
			query.setParameter(1, (loyaltyDTO.getCounter() >= Integer.MAX_VALUE ? 1 : loyaltyDTO.getCounter() + 1));
			query.setParameter(2, loyaltyDTO.getLoyaltyID());
			query.setParameter(3, loyaltyDTO.getCounter());

			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			logger.info("Time to update profile table == " + (System.currentTimeMillis() - l1));

			if (i > 0)
				flag = true;
		} finally {
			query = null;
			tableInfoDAO = null;
		}
		return flag;
	}
}

package com.sixdee.imp.dao;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>January 02,2015 07:09:43 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import om.omantel.arbor.inquiryservices.AccountSummaryJB;
import om.omantel.arbor.inquiryservices.CustIdJB;
import om.omantel.arbor.inquiryservices.SubscriberInfoJB;
import om.omantel.arbor.inquiryservices.SubscriberInfoService;
import om.omantel.arborservice.EnquiryGetIDServiceStub;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AccountNumber_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AddAccountInfoReq;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AddAccountInfoRequestType;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AddAccountInfoRes;
import om.omantel.arborservice.EnquiryGetIDServiceStub.AddAccountInfoResponseType;
import om.omantel.arborservice.EnquiryGetIDServiceStub.CustNationality_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.EaiReference_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetAddAccountInfoRequest;
import om.omantel.arborservice.EnquiryGetIDServiceStub.GetAddAccountInfoResponse;
import om.omantel.arborservice.EnquiryGetIDServiceStub.MsgFormat_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.MsgVersion_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.OT_EAI_HEADERType;
import om.omantel.arborservice.EnquiryGetIDServiceStub.ReferenceNo_type5;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorChannelId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorLanguage_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorSecurityInfo_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.RequestorUserId_type1;
import om.omantel.arborservice.EnquiryGetIDServiceStub.ReturnCode_type1;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtilProfile;
import com.sixdee.imp.common.util.HiberanteUtilRule;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.PrepaidInstantDTO;
import com.sixdee.imp.dto.PrepaidProfileDTO;
import com.sixdee.imp.dto.ProfileSubscriberLanguageDTO;
import com.sixdee.imp.dto.StartServiceDTO;
import com.sixdee.imp.util.CRMException;
import com.sixdee.imp.util.Constants;

public class StartServiceDAO {

	private Logger logger = Logger.getLogger(StartServiceDAO.class);
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat txnFormat = new SimpleDateFormat("ddMMyyHHmmSS");

	public boolean insertInstantDetails(StartServiceDTO startServiceDTO) {

		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		PrepaidInstantDTO prepaidDTO = null;
		Date now = new Date();
		try {
			long start = System.currentTimeMillis();
			session = HiberanteUtilRule.getSessionFactory().openSession();
			tx = session.beginTransaction();

			prepaidDTO = new PrepaidInstantDTO();
			prepaidDTO.setMsisdn(startServiceDTO.getSubscriberNumber());
			if (startServiceDTO.getServiceName() != null && !startServiceDTO.getServiceName().equalsIgnoreCase(""))
				prepaidDTO.setServiceName(startServiceDTO.getServiceName());
			else
				prepaidDTO.setServiceName("ACTIVATION");
			prepaidDTO.setCreateDate(now);
			prepaidDTO.setLanguageId(startServiceDTO.getLanguageId() + "");
			prepaidDTO.setDate(now);
			prepaidDTO.setCreatedBy("LMS");

			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Values to Insert in [PREPAID_INSTANT_CDR_0] = MSISDN:[" + prepaidDTO.getMsisdn() + "],LANGUAGE ID:[" + prepaidDTO.getLanguageId() + "],CREATED DATE:["
					+ prepaidDTO.getCreateDate() + "],SERVICE NAME:[" + prepaidDTO.getServiceName() + "]");

			session.save(prepaidDTO);
			tx.commit();
			long finaltime = System.currentTimeMillis() - start;
			logger.info("Time taken for Inserting into DB:" + finaltime);
			flag = true;
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + "Going for Profile DB checking");
			checkingData(startServiceDTO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;

	}

	public boolean insertProfileDetails(StartServiceDTO startServiceDTO) {

		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		PrepaidProfileDTO prepaidDTO = null;
		Date now = new Date();
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {
			long start = System.currentTimeMillis();
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			tx = session.beginTransaction();

			prepaidDTO = new PrepaidProfileDTO();
			prepaidDTO.setMsisdn(startServiceDTO.getSubscriberNumber());
			prepaidDTO.setLanguageId(startServiceDTO.getLanguageId() + "");
			prepaidDTO.setActivationDate(now);
			prepaidDTO.setKeyword("HSK_REGD");
			prepaidDTO.setStatus("A");
			if (startServiceDTO.getDob() != null)
				prepaidDTO.setDob(dateFormat.parse(startServiceDTO.getDob()));
			if (startServiceDTO.getNationality() != null)
				prepaidDTO.setNationality(startServiceDTO.getNationality());
				prepaidDTO.setNationalId(startServiceDTO.getNationalId());
			if (startServiceDTO.getEnglishFirstName() != null)
				prepaidDTO.setEnglishFirstName(startServiceDTO.getEnglishFirstName());
			if (startServiceDTO.getArbicFirstName() != null)
				prepaidDTO.setArabicFirstName(startServiceDTO.getArbicFirstName());
			if (startServiceDTO.getCustomerPhoneType() != null)
				prepaidDTO.setCustomerPhoneType(startServiceDTO.getCustomerPhoneType());
			if (startServiceDTO.getGender() != null)
				prepaidDTO.setGender(startServiceDTO.getGender());
			if (startServiceDTO.isPortLn())
				prepaidDTO.setPortIn("1");
			else
				prepaidDTO.setPortIn("0");
			
			String tableName = infoDAO.getPrepaidProfileName(startServiceDTO.getSubscriberNumber());
			long id = (Long) session.save(tableName, prepaidDTO);
			tx.commit();
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Values to Insert in [" + tableName + "] = MSISDN:[" + prepaidDTO.getMsisdn() + "],LANGUAGE ID:[" + prepaidDTO.getLanguageId() + "],ACTIVATION DATE:["
					+ prepaidDTO.getActivationDate() + "],KEYWORD:[" + prepaidDTO.getKeyword() + "],DOB:[" + startServiceDTO.getDob() + "],NATIONALITY:[" + startServiceDTO.getNationality() + "],NATIONAL ID:[" + startServiceDTO.getNationalId()
					+ "],ENGLISH NAME:[" + startServiceDTO.getEnglishFirstName() + "],ARABIC NAME:[" + startServiceDTO.getArbicFirstName() + "],CUSTOMER PHONE TYPE:[" + startServiceDTO.getCustomerPhoneType() + "],STATUS:[A]");
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Inserted ID from Table:" + id);
			long finaltime = System.currentTimeMillis() - start;
			logger.info("Time taken for Inserting into Profile DB:" + finaltime);
			flag = true;
		} catch (HibernateException e) {
			e.printStackTrace();
			logger.error("Hibernate Exception occured ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;
	}

	public boolean insertProfileLanguageDetails(StartServiceDTO startServiceDTO) {

		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		ProfileSubscriberLanguageDTO prepaidDTO = null;
		Date now = new Date();
		String sql=null;
		Query query=null;
		try {
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Values to Insert in [ PROFILE_SUBSCRIBER_LANGUAGE ] = MSISDN:[" + startServiceDTO.getSubscriberNumber() + "],LANGUAGE ID:[" + startServiceDTO.getLanguageId() + "],ACTIVATION DATE:["
					+ "],DOB:[" + startServiceDTO.getDob()+"MSIDN ACTIVE DATE"+ dateFormat.format(now)+ "],NATIONALITY:[" + startServiceDTO.getNationality() + "],NATIONAL ID:[" + startServiceDTO.getNationalId()
					+ "],ENGLISH NAME:[" + startServiceDTO.getEnglishFirstName() + "],ARABIC NAME:[" + startServiceDTO.getArbicFirstName() + "],CUSTOMER PHONE TYPE:[" + startServiceDTO.getCustomerPhoneType() + "],STATUS:[A]");
			
			long start = System.currentTimeMillis();
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			tx = session.beginTransaction();

			 sql="INSERT INTO PROFILE_SUBSCRIBER_LANGUAGE  (ID,MSISDN,LANGUAGE_ID,MSISDN_ACTIVE_DATE,STATUS," +
			 		"DOB,NATIONALITY,NATIONALID,ENGLISH_NAME,ARABIC_NAME,GENDER,PORT_IN)" +"VALUES (PROFILE_SUB_LANGUAGE_ID_1SEQ.nextVal,?,?,TO_DATE(?,'DD-MM-YYYY'),?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,?,?,?)";
				query = session.createSQLQuery(sql);
				query.setParameter(0,startServiceDTO.getSubscriberNumber());
				query.setParameter(1,startServiceDTO.getLanguageId() + "");
				query.setParameter(2,dateFormat.format(now));
				query.setParameter(3,"A");
				query.setParameter(4,startServiceDTO.getDob());
				query.setParameter(5,startServiceDTO.getNationality()!=null?startServiceDTO.getNationality():"");
				query.setParameter(6,startServiceDTO.getNationalId()!=null?startServiceDTO.getNationalId():"");
				query.setParameter(7,startServiceDTO.getEnglishFirstName()!=null?startServiceDTO.getEnglishFirstName():"");
				query.setParameter(8,startServiceDTO.getArbicFirstName()!=null?startServiceDTO.getArbicFirstName():"");
				query.setParameter(9,startServiceDTO.getGender()!=null?startServiceDTO.getGender():"");
				if(startServiceDTO.isPortLn())
					query.setParameter(10,1);
				else
					query.setParameter(10,0);
				
				query.executeUpdate();
			tx.commit();
			
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Inserted ID from Table:" );
			long finaltime = System.currentTimeMillis() - start;
			logger.info("Time taken for Inserting into Profile DB:" + finaltime);
			flag = true;
		} catch (HibernateException e) {
			e.printStackTrace();
			logger.error("Hibernate Exception occured ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;
	}

	public boolean checkProfileDetails(StartServiceDTO startServiceDTO) {

		Session session = null;
		Criteria criteria = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		boolean flag = false;
		

		try {
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			long t1 = System.currentTimeMillis();

			String tableName = infoDAO.getPrepaidProfileName(startServiceDTO.getSubscriberNumber());
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("msisdn", startServiceDTO.getSubscriberNumber()));
			criteria.setProjection(Projections.rowCount());
			Integer count = (Integer) criteria.uniqueResult();

			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Count from PROFILE Table:" + count);

			session.close();
			session = null;

			long t2 = System.currentTimeMillis();
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Time Taken for Selecting PROFILE Table DB Selection " + (t2 - t1));

			flag = getCRMDetails(startServiceDTO, count);

			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Flag:" + flag);
		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null)
				session.close();
		}

		return flag;

	}
	
	public boolean checkPostpaidProfileDetails(StartServiceDTO startServiceDTO) {

		Session session = null;
		Criteria criteria = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		boolean flag = false;
		Integer count=0;
		Transaction transaction=null;
		String sql=null;
		Query query=null;
		logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " INSIDE COUNT METHOD" + count);
		try {
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			long t1 = System.currentTimeMillis();
			transaction = session.beginTransaction();
			sql = "SELECT COUNT(*) FROM PROFILE_SUBSCRIBER_LANGUAGE  WHERE MSISDN=:msisdn";
			query = session.createSQLQuery(sql);
			query.setParameter("msisdn", startServiceDTO.getSubscriberNumber());
			count = ((BigDecimal) query.uniqueResult()).intValue();
			transaction.commit();

			long t2 = System.currentTimeMillis();
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Time Taken for Selecting PROFILE Table DB Selection " + (t2 - t1));

			flag = getCRMDetails(startServiceDTO, count);

			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Flag:" + flag);
		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null)
				session.close();
		}

		return flag;

	}

	private boolean getCRMDetails(StartServiceDTO startServiceDTO, Integer count) {

		LoyaltyProfileTabDTO loyaltyDTO = null;
		Date today = new Date();
		boolean flag = false;
		try {
			// Call CRM from here & take Nationality , DOB & National ID
			loyaltyDTO = new LoyaltyProfileTabDTO();
			String txnId = txnFormat.format(today);
			getBasicSubscriberInfo(loyaltyDTO, startServiceDTO.getSubscriberNumber(), txnId);
			if (loyaltyDTO.getNationality() != null)
				startServiceDTO.setNationality(loyaltyDTO.getNationality());
			if (loyaltyDTO.getDateOfBirth() != null)
				startServiceDTO.setDob(dateFormat.format(loyaltyDTO.getDateOfBirth()).toString());
			if (loyaltyDTO.getFirstName() != null && !loyaltyDTO.getFirstName().equalsIgnoreCase(""))
				startServiceDTO.setEnglishFirstName(loyaltyDTO.getFirstName());
			if (loyaltyDTO.getArbicFirstName() != null && !loyaltyDTO.getArbicFirstName().equalsIgnoreCase(""))
				startServiceDTO.setArbicFirstName(loyaltyDTO.getArbicFirstName());
			if (loyaltyDTO.getCustomerPhoneType() != null && !loyaltyDTO.getCustomerPhoneType().equalsIgnoreCase(""))
				startServiceDTO.setCustomerPhoneType(loyaltyDTO.getCustomerPhoneType());
			if (loyaltyDTO.getGender() != null && !loyaltyDTO.getGender().equalsIgnoreCase(""))
				startServiceDTO.setGender(loyaltyDTO.getGender());
			
			startServiceDTO.setNationalId(loyaltyDTO.getCustID());

			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " NATIONALITY:" + loyaltyDTO.getNationality());
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " DOB:" + startServiceDTO.getDob());
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " NATIONAL ID:" + loyaltyDTO.getCustID());
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " CUST.ENGLISH_NAME:" + loyaltyDTO.getFirstName());
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " CUST.ARABIC_NAME:" + loyaltyDTO.getArbicFirstName());
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " CUSTOMER PHONE TYPE:" + loyaltyDTO.getCustomerPhoneType());
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " GENDER:" + loyaltyDTO.getGender());
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " ACCOUNT TYPE:" + startServiceDTO.getAccountType());
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + "COUNT:" + count);
			if(startServiceDTO.getAccountType()==14){
			if (count!=null && count > 0)
				flag = updateProfileDetails(startServiceDTO);
			else
				flag = insertProfileDetails(startServiceDTO);
			}
			else if(startServiceDTO.getAccountType()==9){
				if (count!=null &&count > 0)
					flag = updateProfileLanguageDetails(startServiceDTO);
				else
					flag = insertProfileLanguageDetails(startServiceDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		}

		return flag;
	}

	private boolean updateProfileDetails(StartServiceDTO startServiceDTO) {

		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		Query query = null;
		String sql = "";
		try {
			long start = System.currentTimeMillis();
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			tx = session.beginTransaction();

			String tableName = infoDAO.getPrepaidProfileTableName(startServiceDTO.getSubscriberNumber());

			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Values to Update in [" + tableName + "] = MSISDN:[" + startServiceDTO.getSubscriberNumber() + "],LANGUAGE ID:[" + startServiceDTO.getLanguageId() + "],DATE:[" + new Date()
					+ "],KEYWORD:[HSK_REGD],DOB:[select" + startServiceDTO.getDob() + "],NATIONALITY:[" + startServiceDTO.getNationality() + "],NATIONAL ID:[" + startServiceDTO.getNationalId() + "],ENGLISH NAME:[" + startServiceDTO.getEnglishFirstName()
					+ "],ARABIC NAME:[" + startServiceDTO.getArbicFirstName() + "],CUSTOMER PHONE TYPE:[" + startServiceDTO.getCustomerPhoneType() + "],STATUS: [A]");

			sql = "UPDATE "
					+ tableName
					+ " set FIELD61 = :langId,FIELD36 = :now,FIELD170 = :keyword, FIELD5=:status, FIELD214 = TO_DATE(:dob, 'DD-MM-YYYY'),FIELD47= :nationality,FIELD49= :nationalID,FIELD120=:englishName,FIELD121=:arabicName,FIELD122=:customerPhoneType,FIELD48=:gender  where FIELD2 = :subsNumber";
			query = session.createSQLQuery(sql);
			query.setParameter("langId", startServiceDTO.getLanguageId());
			query.setParameter("now", new Date());
			query.setParameter("keyword", "HSK_REGD");
			query.setParameter("status", "A");
			query.setParameter("dob", startServiceDTO.getDob());
			query.setParameter("nationality", startServiceDTO.getNationality());
			query.setParameter("nationalID", startServiceDTO.getNationalId());
			query.setParameter("englishName", startServiceDTO.getEnglishFirstName());
			query.setParameter("arabicName", startServiceDTO.getArbicFirstName());
			query.setParameter("customerPhoneType", startServiceDTO.getCustomerPhoneType());
			query.setParameter("gender", startServiceDTO.getGender());
			query.setParameter("subsNumber", startServiceDTO.getSubscriberNumber());

			int x = query.executeUpdate();
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Rows Updated:" + x);
			tx.commit();
			long finaltime = System.currentTimeMillis() - start;
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Time taken for Updating Profile DB:" + finaltime);
			if (x > 0)
				flag = true;
		} catch (HibernateException e) {
			e.printStackTrace();
			logger.error("Hibernate Exception occured ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;
	}
	
	private boolean updateProfileLanguageDetails(StartServiceDTO startServiceDTO) {

		Session session = null;
		Transaction tx = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		Query query = null;
		String sql = "";
		try {
			long start = System.currentTimeMillis();
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			tx = session.beginTransaction();


			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Values to Update in [PROFILE_SUBSCRIBER_LANGUAGE] = MSISDN:[" + startServiceDTO.getSubscriberNumber() + "],LANGUAGE ID:[" + startServiceDTO.getLanguageId() + "],DATE:[" + new Date()
					+ "],KEYWORD:[HSK_REGD],DOB:[" + startServiceDTO.getDob() + "],NATIONALITY:[" + startServiceDTO.getNationality() + "],NATIONAL ID:[" + startServiceDTO.getNationalId() + "],ENGLISH NAME:[" + startServiceDTO.getEnglishFirstName()
					+ "],ARABIC NAME:[" + startServiceDTO.getArbicFirstName() + "],CUSTOMER PHONE TYPE:[" + startServiceDTO.getCustomerPhoneType() + "],STATUS: [A]");

			sql = "UPDATE PROFILE_SUBSCRIBER_LANGUAGE set " +
					"LANGUAGE_ID = :langId," +
					"MSISDN_ACTIVE_DATE = :now," +
					"STATUS=:status, " +
					"DOB = TO_DATE(:dob, 'DD-MM-YYYY')," +
					"NATIONALITY= :nationality," +
					"NATIONALID= :nationalID," +
					"ENGLISH_NAME=:englishName," +
					"ARABIC_NAME=:arabicName," +
					"GENDER=:gender  where " +
					"MSISDN = :subsNumber";
			query = session.createSQLQuery(sql);
			query.setParameter("langId", startServiceDTO.getLanguageId());
			query.setParameter("now", new Date());
			query.setParameter("status", "A");
			query.setParameter("dob", startServiceDTO.getDob());
			query.setParameter("nationality", startServiceDTO.getNationality());
			query.setParameter("nationalID", startServiceDTO.getNationalId());
			query.setParameter("englishName", startServiceDTO.getEnglishFirstName());
			query.setParameter("arabicName", startServiceDTO.getArbicFirstName());
			query.setParameter("gender", startServiceDTO.getGender());

			query.setParameter("subsNumber", startServiceDTO.getSubscriberNumber());

			int x = query.executeUpdate();
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Rows Updated:" + x);
			tx.commit();
			long finaltime = System.currentTimeMillis() - start;
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Time taken for Updating Profile DB:" + finaltime);
			if (x > 0)
				flag = true;
		} catch (HibernateException e) {
			e.printStackTrace();
			logger.error("Hibernate Exception occured ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here ==>", e);
		} finally {
			if (session != null)
				session.close();
		}
		return flag;
	}


	public void getBasicSubscriberInfo(LoyaltyProfileTabDTO loyaltyProfileTabDTO, String subscriberNumber, String transactionID) throws CRMException {

		SubscriberInfoService infoService = null;
		SubscriberInfoJB subscriberInfoJB = null;
		AccountSummaryJB accountInfo = null;
		try {
			logger.info(transactionID + " : getBasicSubscriberInfo1 " + Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));

			infoService = new SubscriberInfoService(Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));

			subscriberInfoJB = infoService.getSubscriberInfo("C1_" + transactionID, subscriberNumber);

			logger.info(transactionID + " : Basic Profile Return Code " + subscriberInfoJB.getReturnCode() + " and custType " + subscriberInfoJB.getCustType());

			loyaltyProfileTabDTO.setCustID(subscriberInfoJB.getCustType());

			if (subscriberInfoJB.getReturnCode() == null || !subscriberInfoJB.getReturnCode().equalsIgnoreCase("0000") || subscriberInfoJB.getCustAccountNumber() == null || subscriberInfoJB.getCustAccountNumber().trim().equals("")) {
				accountInfo = infoService.getAccountInfo("C2_" + transactionID, subscriberNumber);
				logger.info(transactionID + " : Account Return Code  " + accountInfo.getReturnCode());
				if (accountInfo.getReturnCode() == null || !accountInfo.getReturnCode().equalsIgnoreCase("0000"))
					return;

				loyaltyProfileTabDTO.setAccountNumber(subscriberNumber);
			}

			if (subscriberInfoJB.getReturnCode() != null && subscriberInfoJB.getReturnCode().equalsIgnoreCase("0000") && subscriberInfoJB.getCustAccountNumber() != null && !subscriberInfoJB.getCustAccountNumber().trim().equalsIgnoreCase("")) {
				loyaltyProfileTabDTO.setAccountNumber(subscriberInfoJB.getCustAccountNumber());
				accountInfo = infoService.getAccountInfo("C2_" + transactionID, subscriberInfoJB.getCustAccountNumber());
			}

			if (accountInfo == null)
				return;

			logger.info(transactionID + " : Account Return Code  " + accountInfo.getReturnCode());
			if (accountInfo.getReturnCode() != null && accountInfo.getReturnCode().equalsIgnoreCase("0000")) {
				logger.info(transactionID + " :  AcctCategory " + accountInfo.getAcctCategory());
				loyaltyProfileTabDTO.setCustomerPhoneType(accountInfo.getAcctCategory());
			}

			for (CustIdJB custID : accountInfo.getCustIdList()) {
				logger.info("Cust ID:" + custID.getCustID());
				logger.info("Cust ID Type:" + custID.getCustIdType());
				loyaltyProfileTabDTO.setCustID(custID.getCustID());
			}

			getAdditionalAccountInfo(loyaltyProfileTabDTO, transactionID); // Calling
																			// for
																			// aditional
																			// info

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			infoService = null;
			subscriberInfoJB = null;
			accountInfo = null;
		}
	}

	public void getAdditionalAccountInfo(LoyaltyProfileTabDTO loyaltyProfileTabDTO, String transactionID) throws CRMException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		EnquiryGetIDServiceStub serviceStub = null;
		GetAddAccountInfoResponse accountInfoResponse = null;
		CustNationality_type1 custNationality_type1 = null;
		try {

			logger.info(transactionID + " : getAdditionalAccountInfo " + Cache.getCacheMap().get("CRM_CALL_NATIONAL_URL"));

			serviceStub = new EnquiryGetIDServiceStub(Cache.getCacheMap().get("CRM_CALL_NATIONAL_URL"));

			GetAddAccountInfoRequest accountInfoRequest = new GetAddAccountInfoRequest();

			AddAccountInfoRequestType accountInfoRequestType = new AddAccountInfoRequestType();

			AccountNumber_type1 accountNumber_type1 = new AccountNumber_type1();
			accountNumber_type1.setAccountNumber_type0(loyaltyProfileTabDTO.getAccountNumber());

			accountInfoRequestType.setAccountNumber(accountNumber_type1);
			ReferenceNo_type5 no_type1 = new ReferenceNo_type5();
			no_type1.setReferenceNo_type4("C4_" + transactionID);
			accountInfoRequestType.setReferenceNo(no_type1);

			AddAccountInfoReq accountInfoReq = new AddAccountInfoReq();

			OT_EAI_HEADERType ot_eai_heayderType = new OT_EAI_HEADERType();

			MsgFormat_type1 format_type1 = new MsgFormat_type1();
			format_type1.setMsgFormat_type0("GETMEDCBILL.SERVICE");
			ot_eai_heayderType.setMsgFormat(format_type1);
			MsgVersion_type1 msgVersion_type1 = new MsgVersion_type1();
			msgVersion_type1.setMsgVersion_type0("1.0");
			ot_eai_heayderType.setMsgVersion(msgVersion_type1);
			RequestorId_type1 requestorId_type1 = new RequestorId_type1();
			requestorId_type1.setRequestorId_type0("ARBOR");
			ot_eai_heayderType.setRequestorId(requestorId_type1);

			RequestorChannelId_type1 requestorChannelId_type1 = new RequestorChannelId_type1();
			requestorChannelId_type1.setRequestorChannelId_type0("WS");
			ot_eai_heayderType.setRequestorChannelId(requestorChannelId_type1);
			RequestorUserId_type1 requestorUserId_type1 = new RequestorUserId_type1();
			requestorUserId_type1.setRequestorUserId_type0("ARBOR");
			ot_eai_heayderType.setRequestorUserId(requestorUserId_type1);
			RequestorLanguage_type1 requestorLanguage_type1 = new RequestorLanguage_type1();
			requestorLanguage_type1.setRequestorLanguage_type0("E");
			ot_eai_heayderType.setRequestorLanguage(requestorLanguage_type1);
			RequestorSecurityInfo_type1 requestorSecurityInfo_type1 = new RequestorSecurityInfo_type1();
			requestorSecurityInfo_type1.setRequestorSecurityInfo_type0("Eai");
			ot_eai_heayderType.setRequestorSecurityInfo(requestorSecurityInfo_type1);
			EaiReference_type1 eaiReference_type1 = new EaiReference_type1();
			eaiReference_type1.setEaiReference_type0("0");
			ot_eai_heayderType.setEaiReference(eaiReference_type1);
			ReturnCode_type1 code_type1 = new ReturnCode_type1();
			code_type1.setReturnCode_type0("0000");
			ot_eai_heayderType.setReturnCode(code_type1);

			accountInfoReq.setOT_EAI_HEADER(ot_eai_heayderType);
			accountInfoReq.setRequest(accountInfoRequestType);

			accountInfoRequest.setGetAddAccountInfoRequest(accountInfoReq);

			accountInfoResponse = serviceStub.getAdditionalAccountInfo(accountInfoRequest);

			AddAccountInfoRes accountInfoRes = accountInfoResponse.getGetAddAccountInfoResponse();

			AddAccountInfoResponseType accountInfoResponseType = accountInfoRes.getResponse();

			logger.info(transactionID + " :  getAdditionalAccountInfo Return Code " + accountInfoResponseType.getReturnCode().getReturnCode_type2());

			if (accountInfoResponseType.getReturnCode() != null && accountInfoResponseType.getReturnCode().getReturnCode_type2() != null && accountInfoResponseType.getReturnCode().getReturnCode_type2().equalsIgnoreCase("0000")) {
				if (accountInfoResponseType.getCustNameEnglish() != null)
					logger.info(transactionID + " :  getAdditionalAccountInfo Name " + accountInfoResponseType.getCustNameEnglish().getCustNameEnglish_type0());
				if (accountInfoResponseType.getCustNameArabic() != null)
					logger.info(transactionID + " :  getAdditionalAccountInfo Arbic Name " + accountInfoResponseType.getCustNameArabic().getCustNameArabic_type0());
				if (accountInfoResponseType.getDateOfBirth() != null)
					logger.info(transactionID + " :  getAdditionalAccountInfo DOB " + accountInfoResponseType.getDateOfBirth().getDateOfBirth_type0());
				if (accountInfoResponseType.getCustPhone() != null)
					logger.info(transactionID + " :  getAdditionalAccountInfo Cust Phone " + accountInfoResponseType.getCustPhone().getCustPhone_type0());
				if (accountInfoResponseType.getEmailId() != null)
					logger.info(transactionID + " :  getAdditionalAccountInfo  Email " + accountInfoResponseType.getEmailId().getEmailId_type0());
				if (accountInfoResponseType.getCustGender() != null)
					logger.info(transactionID + " :  getAdditionalAccountInfo  Gender " + accountInfoResponseType.getCustGender().getCustGender_type0());

				if (accountInfoResponseType.getCustNameEnglish() != null)
					loyaltyProfileTabDTO.setFirstName(accountInfoResponseType.getCustNameEnglish().getCustNameEnglish_type0());

				if (accountInfoResponseType.getCustNameArabic() != null)
					loyaltyProfileTabDTO.setArbicFirstName(accountInfoResponseType.getCustNameArabic().getCustNameArabic_type0());

				if (accountInfoResponseType.getDateOfBirth() != null && accountInfoResponseType.getDateOfBirth().getDateOfBirth_type0() != null)
					loyaltyProfileTabDTO.setDateOfBirth(dateFormat.parse(accountInfoResponseType.getDateOfBirth().getDateOfBirth_type0()));

				if (accountInfoResponseType.getCustPhone() != null)
					loyaltyProfileTabDTO.setContactNumber(accountInfoResponseType.getCustPhone().getCustPhone_type0());

				if (accountInfoResponseType.getEmailId() != null)
					loyaltyProfileTabDTO.setEmailID(accountInfoResponseType.getEmailId().getEmailId_type0());

				if (accountInfoResponseType.getCustGender() != null)
					loyaltyProfileTabDTO.setGender(accountInfoResponseType.getCustGender().getCustGender_type0());

				String address = "";
				address = (accountInfoResponseType.getCustEngAddr1() == null ? "" : accountInfoResponseType.getCustEngAddr1().getCustEngAddr1_type0());
				address = (address.trim().equalsIgnoreCase("") ? "" : address + ",") + (accountInfoResponseType.getCustEngAddr2() == null ? "" : accountInfoResponseType.getCustEngAddr2().getCustEngAddr2_type0());
				address = (address.trim().equalsIgnoreCase("") ? "" : address + ",") + (accountInfoResponseType.getCustEngAddr3() == null ? "" : accountInfoResponseType.getCustEngAddr3().getCustEngAddr3_type0());

				loyaltyProfileTabDTO.setAddress(address);

				address = (accountInfoResponseType.getCustArabicAddr1() == null ? "" : accountInfoResponseType.getCustArabicAddr1().getCustArabicAddr1_type0());
				address = (address.trim().equalsIgnoreCase("") ? "" : address + ",") + (accountInfoResponseType.getCustArabicAddr2() == null ? "" : accountInfoResponseType.getCustArabicAddr2().getCustArabicAddr2_type0());
				address = (address.trim().equalsIgnoreCase("") ? "" : address + ",") + (accountInfoResponseType.getCustArabicAddr3() == null ? "" : accountInfoResponseType.getCustArabicAddr3().getCustArabicAddr3_type0());

				loyaltyProfileTabDTO.setArbicAddress(address);
				custNationality_type1 = accountInfoResponseType.getCustNationality();
				String nationalityType = custNationality_type1 != null ? custNationality_type1.getCustNationality_type0() : Constants.emptyString;
				loyaltyProfileTabDTO.setNationality(nationalityType != null ? nationalityType : Constants.emptyString);
			}

		} catch (Exception e) {
			logger.info(transactionID + " : ", e);
			throw new CRMException("CRM_1000", e.getLocalizedMessage());
		} finally {
			serviceStub = null;
			accountInfoResponse = null;
		}

	}// getAdditionalAccountInfo

	public boolean checkingData(StartServiceDTO startServiceDTO) {

		Session session = null;
		Criteria criteria = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		boolean flag = false;

		try {
			session = HiberanteUtilProfile.getSessionFactory().openSession();
			long t1 = System.currentTimeMillis();

			String tableName = infoDAO.getPrepaidProfileName(startServiceDTO.getSubscriberNumber());
			criteria = session.createCriteria(tableName);
			criteria.add(Restrictions.eq("msisdn", startServiceDTO.getSubscriberNumber()));
			criteria.setProjection(Projections.rowCount());
			Integer count = (Integer) criteria.uniqueResult();

			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " SUB NUM: " + startServiceDTO.getSubscriberNumber() + " Final Checking COUNT from PROFILE Table :" + count);

			session.close();
			session = null;

			long t2 = System.currentTimeMillis();
			logger.info("TXN ID: " + startServiceDTO.getTransactionId() + " Time Taken for Final Selecting from PROFILE Table " + (t2 - t1));
		} catch (HibernateException e) {
			logger.error("Hibernate Exception occured ", e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occured  ", e);
		} finally {
			if (session != null)
				session.close();
		}

		return flag;

	}

}

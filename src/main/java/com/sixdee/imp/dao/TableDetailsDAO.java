package com.sixdee.imp.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.ActionServiceDetailsDTO;
import com.sixdee.imp.dto.BlackListDTO;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LmsOnmPointSnapshotDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.PromoTableDTO;
import com.sixdee.imp.dto.ServiceManagementDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;




public class TableDetailsDAO {

	Logger logger = Logger.getLogger(TableDetailsDAO.class);

	public SubscriberNumberTabDTO getSubscriberNumberDetails(Long subscriberNumber) {
		return getSubscriberNumberDetails(subscriberNumber, null);

	}// getSubscriberNumberDetails

	public boolean getBlackListDetails(String msisdn,int status) {
		Session session = null;
		boolean isBlacklisted=false;
		long l1 = System.currentTimeMillis();
		String msisdnData = null;
		try {
			msisdnData=msisdn;
			session = HiberanteUtil.getSessionFactory().openSession();
			msisdnData=msisdnData.substring(1,msisdnData.length());
			logger.info("Msisdn "+msisdn+" Msisdn Details "+msisdnData);
			Query query = session.createQuery("FROM BlackListDTO WHERE (msisdn=:msisdn or msisdn=:msisdnData) and status=:status");
			query.setString("msisdn", msisdn);
			query.setString("msisdnData", msisdnData);
			query.setInteger("status", status);
			List<BlackListDTO> blackListDTOs = query.list();
			logger.info(" blackListDTOs Size "+blackListDTOs.size());
			if (blackListDTOs != null && blackListDTOs.size() > 0) {
				isBlacklisted=true;
			}
			l1 = System.currentTimeMillis();
		} catch (Exception e) {
			logger.info("Exception ", e);
		} finally {
			if (session != null)
				session.close();
			logger.info("Total Time = " + (System.currentTimeMillis() - l1));
		}
		return isBlacklisted;
	}
	
	public CustomerProfileTabDTO getCustomerProfile(String msisdn) {
		Session session = null;
		CustomerProfileTabDTO customerProfileTabDTO = null;
		String packName = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();

			Query query = session.createQuery("FROM CustomerProfileTabDTO WHERE msisdn=:msisdn");
			query.setString("msisdn", msisdn);
			long l1 = System.currentTimeMillis();
			logger.info("Start Time for package" + l1);
			List<CustomerProfileTabDTO> profileTabDTOs = query.list();
			logger.info("Total Time = " + (System.currentTimeMillis() - l1));

			if (profileTabDTOs != null && profileTabDTOs.size() > 0) {
				customerProfileTabDTO = profileTabDTOs.get(0);
			}
			l1 = System.currentTimeMillis();
			logger.info("Start Time for commit" + l1);
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

		return customerProfileTabDTO;
	}
	
	public List<CustomerProfileTabDTO> getCustomerProfileInfoBasedonAccountNumber(String accountNumber , String TransactionId)
	{

		Session session = HiberanteUtil.getSessionFactory().openSession();
		//Transaction tx = null;
		Query query = null;
		List<CustomerProfileTabDTO> customerProfileDto = null;
		try {
			//tx = session.beginTransaction();
			query = session.createQuery(" FROM CustomerProfileTabDTO where accountNo=:accountNo");
			query.setParameter("accountNo", accountNumber);
			customerProfileDto = query.list();
			logger.debug("List of offerType " + customerProfileDto.size());
			//tx.commit();
		} catch (HibernateException e) {
			/*
			 * if (tx != null) tx.rollback();
			 */
			e.printStackTrace();
		} finally {
			session.close();
		}
		return customerProfileDto;

	}
	public SubscriberNumberTabDTO getSubscriberNumberDetails(Long subscriberNumber, Long loyaltyID) {
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		Session session = null;
		// Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {
			long start = System.currentTimeMillis();
logger.info("inside getSubscriberNumberDetails"+" subscriber nuber "+subscriberNumber);
			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();

			String tableName = infoDAO.getSubscriberNumberTable(subscriberNumber + "");

			String sql = " FROM " + tableName + " WHERE (subscriberNumber=? OR loyaltyID=?)";
			logger.info("query "+ sql);
			if (loyaltyID != null && loyaltyID > 0)
				sql += " AND loyaltyID=? ";

			Query query = session.createQuery(sql);
			query.setParameter(0, subscriberNumber);
			query.setParameter(1, subscriberNumber);
			if (loyaltyID != null && loyaltyID > 0)
				query.setParameter(2, loyaltyID);

			List<SubscriberNumberTabDTO> list = query.list();

			if (list.size() > 0) {
				logger.info("found data");
				subscriberNumberTabDTO = list.get(0);

				/*if (Cache.accountTypeMap.get(subscriberNumberTabDTO.getAccountTypeId()) != null)
					subscriberNumberTabDTO.setAccountTypeName(Cache.accountTypeMap.get(subscriberNumberTabDTO.getAccountTypeId()));
				else
					subscriberNumberTabDTO.setAccountTypeName("Undefind");*/
			}

			/*
			 * String
			 * tableName=infoDAO.getSubscriberNumberDBTable(subscriberNumber+"");
			 * 
			 * String sql=" SELECT SUBSCRIBER_NUMBER,ACCOUNT_NO,LOYALTY_ID," + "
			 * POINTS,STATUS_ID,STATUS_UPDATED_DATE,COUNTER,ACCOUNT_TYPE " + "
			 * FROM "+tableName+" WHERE SUBSCRIBER_NUMBER=? ";
			 * 
			 * if(loyaltyID!=null&&loyaltyID>0) sql+=" AND LOYALTY_ID=? ";
			 * 
			 * start=System.currentTimeMillis(); Query
			 * query=session.createSQLQuery(sql); query.setParameter(0,
			 * subscriberNumber); if(loyaltyID!=null&&loyaltyID>0)
			 * query.setParameter(1,loyaltyID);
			 * 
			 * System.out.println(" Query "+(System.currentTimeMillis()-start));
			 * 
			 * start=System.currentTimeMillis(); List<Object[]>
			 * list=query.list(); System.out.println(" execute
			 * "+(System.currentTimeMillis()-start));
			 * start=System.currentTimeMillis(); if(list.size()>0) { Object[]
			 * obj=list.get(0); subscriberNumberTabDTO=new
			 * SubscriberNumberTabDTO();
			 * subscriberNumberTabDTO.setSubscriberNumber(Long.parseLong(obj[0].toString()));
			 * subscriberNumberTabDTO.setAccountNumber(obj[1]==null?null:Long.parseLong(obj[1].toString()));
			 * subscriberNumberTabDTO.setLoyaltyID(Long.parseLong(obj[2].toString()));
			 * subscriberNumberTabDTO.setPoints(Double.parseDouble(obj[3].toString()));
			 * subscriberNumberTabDTO.setStatusID(Integer.parseInt(obj[4].toString()));
			 * subscriberNumberTabDTO.setStatusUpdatedDate((Date)obj[5]);
			 * subscriberNumberTabDTO.setCounter(Long.parseLong(obj[6].toString()));
			 * 
			 * Integer
			 * accountType=(obj[7]==null?null:Integer.parseInt(obj[7]+""));
			 * 
			 * if(Cache.accountTypeMap.get(accountType)!=null) {
			 * subscriberNumberTabDTO.setAccountTypeId(accountType);
			 * subscriberNumberTabDTO.setAccountTypeName(Cache.accountTypeMap.get(accountType)); }
			 * else subscriberNumberTabDTO.setAccountTypeName("Undefind"); }
			 * System.out.println(" Populate
			 * "+(System.currentTimeMillis()-start));
			 */
			// trx.commit();

		} catch (Exception e) {
			// trx.rollback();
			// e.printStackTrace();
			logger.info("", e);
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return subscriberNumberTabDTO;

	}// getSubscriberNumberDetails

	public SubscriberNumberTabDTO getSubscriberNumberDetails(Long subscriberNumber, Long accountNumber, Long loyaltyID) {
		//Transaction transaction = null;
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		Session session = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			
			String tableName = infoDAO.getSubscriberNumberTable(subscriberNumber + "");

			//transaction = session.beginTransaction();

			/*
			 * String sql=" SELECT SUBSCRIBER_NUMBER subscriberNumber,ACCOUNT_NO
			 * accountNumber,LOYALTY_ID loyaltyID," + " POINTS points,STATUS_ID
			 * statusID,STATUS_UPDATED_DATE statusUpdatedDate " + " FROM
			 * "+tableName+" WHERE SUBSCRIBER_NUMBER=? AND ACCOUNT_NO=?";
			 * 
			 * if(loyaltyID!=null&&loyaltyID>0) sql+=" AND LOYALTY_ID=? ";
			 * 
			 * logger.info(sql);
			 * 
			 * Query query=session.createSQLQuery(sql)
			 * .addScalar("subscriberNumber",Hibernate.LONG)
			 * .addScalar("accountNumber",Hibernate.LONG)
			 * .addScalar("loyaltyID",Hibernate.LONG)
			 * .addScalar("points",Hibernate.INTEGER)
			 * .addScalar("statusID",Hibernate.INTEGER)
			 * .addScalar("statusUpdatedDate",Hibernate.DATE)
			 * .setResultTransformer(Transformers.aliasToBean(SubscriberNumberTabDTO.class));
			 */

			String sql = "FROM " + tableName + " TAB WHERE TAB.subscriberNumber=:subscriberNumber AND TAB.accountNumber=:accountNumber ";

			if (loyaltyID != null && loyaltyID > 0)
				sql += " AND TAB.loyaltyID=:loyaltyID";

			Query query = session.createQuery(sql);
			query.setParameter("subscriberNumber", subscriberNumber);
			query.setParameter("accountNumber", accountNumber);

			if (loyaltyID != null && loyaltyID > 0)
				query.setParameter("loyaltyID", loyaltyID);

			List<SubscriberNumberTabDTO> list = query.list();

			if (list.size() > 0)
				subscriberNumberTabDTO = list.get(0);

			//transaction.commit();

		} catch (Exception e) {
			//transaction.rollback();
			e.printStackTrace();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return subscriberNumberTabDTO;

	}// getSubscriberNumberDetails

	public AccountNumberTabDTO getAccountNumberDetails(String accountNumber) {
		return getAccountNumberDetails(accountNumber, null, null);
	}// getAccountNumberDetails

	public AccountNumberTabDTO getAccountNumberDetails(String accountNumber, Long loyaltyID) {
		return getAccountNumberDetails(accountNumber, loyaltyID, null);
	}// //getAccountNumberDetails

	public AccountNumberTabDTO getAccountNumberDetails(String accountNumber, Session session) {
		return getAccountNumberDetails(accountNumber, null, session);

	}// //getAccountNumberDetails

	public AccountNumberTabDTO getAccountNumberDetails(String accountNumber, Long loyaltyID, Session session1) {
		Session session = null;
		AccountNumberTabDTO accountNumberTabDTO = null;
		// Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {
			if (session1 == null) {
				 
				session = HiberanteUtil.getSessionFactory().openSession();
				
				// trx = session.beginTransaction();
			} else {
				session = session1;
			}

			String tableName = infoDAO.getAccountNumberTable(accountNumber + "");

			String sql = " FROM " + tableName + " WHERE accountNumber=? ";

			if (loyaltyID != null && loyaltyID > 0)
				sql += " AND loyaltyID=? ";

			Query query = session.createQuery(sql);

			query.setParameter(0, accountNumber);

			if (loyaltyID != null && loyaltyID > 0)
				query.setParameter(1, loyaltyID);

			List<AccountNumberTabDTO> list = query.list();

			if (list.size() > 0)
				accountNumberTabDTO = list.get(0);
              logger.info("size>>>>>>>>>>>>>>>>>"+list.size());
			/*
			 * String
			 * tableName=infoDAO.getAccountNumberDBTable(accountNumber+"");
			 * 
			 * 
			 * String sql=" SELECT
			 * ACCOUNT_NO,LOYALTY_ID,POINTS,STATUS_ID,STATUS_UPDATED_DATE,COUNTER " + "
			 * FROM "+tableName+" WHERE ACCOUNT_NO=? ";
			 * 
			 * if(loyaltyID!=null&&loyaltyID>0) sql+=" AND LOYALTY_ID=? ";
			 * 
			 * Query query=session.createSQLQuery(sql);
			 * 
			 * query.setParameter(0,accountNumber);
			 * 
			 * if(loyaltyID!=null&&loyaltyID>0) query.setParameter(1,loyaltyID);
			 * 
			 * List<Object[]> list=query.list();
			 * 
			 * if(list.size()>0) { Object[] obj=list.get(0);
			 * accountNumberTabDTO=new AccountNumberTabDTO();
			 * accountNumberTabDTO.setAccountNumber(Long.parseLong(obj[0].toString()));
			 * accountNumberTabDTO.setLoyaltyID(Long.parseLong(obj[1].toString()));
			 * accountNumberTabDTO.setPoints(Double.parseDouble(obj[2].toString()));
			 * accountNumberTabDTO.setStatusID(Integer.parseInt(obj[3].toString()));
			 * accountNumberTabDTO.setStatusUpdatedDate((Date)obj[4]);
			 * accountNumberTabDTO.setCounter(Long.parseLong(obj[5].toString())); }
			 * 
			 * //if(session1==null) //trx.commit();
			 */
		} catch (Exception e) {

			// if(session1==null)
			// trx.rollback();

			// e.printStackTrace();
			logger.info("", e);
		} finally {
			infoDAO = null;
			try {
				if (session1 == null && session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return accountNumberTabDTO;

	}// getAccountNumberDetails

	public NationalNumberTabDTO getNationalNumberDetails(String nationalNumber) {
		return getNationalNumberDetails(nationalNumber, null);

	}// getNationalNumberDetails

	public NationalNumberTabDTO getNationalNumberDetails(String nationalNumber, Long loyaltyID) {
		Session session = null;
		NationalNumberTabDTO nationalNumberTabDTO = null;
		//Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();
			String tableName = infoDAO.getNationalNumberDBTable(nationalNumber + "");

			String sql = " SELECT NATIONAL_ID,LOYALTY_ID " + " FROM " + tableName + " WHERE NATIONAL_ID=? ";

			if (loyaltyID != null && loyaltyID > 0)
				sql += " AND LOYALTY_ID=?1 ";

			Query query = session.createSQLQuery(sql);

			query.setParameter(0, nationalNumber);

			if (loyaltyID != null && loyaltyID > 0)
				query.setParameter(1, loyaltyID);

			List<Object[]> list = query.list();

			if (list.size() > 0) {
				Object[] obj = list.get(0);
				nationalNumberTabDTO = new NationalNumberTabDTO();
				nationalNumberTabDTO.setNationalNumber((String) obj[0]);
				nationalNumberTabDTO.setLoyaltyID(Long.parseLong(obj[1].toString()));
			}
			// trx.commit();

		} catch (Exception e) {
			// trx.rollback();
			e.printStackTrace();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return nationalNumberTabDTO;

	}// getNationalNumberDetails
	
	public List<NationalNumberTabDTO> getNationalNumberDetailsByLoyaltyId(Long loyaltyID) {
		
		Session session = null;
		List<NationalNumberTabDTO> list=null;
		
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();
			String tableName ="NATIONAL_NUMBER_ENTITY_0";

			String sql = " FROM " + tableName + " WHERE LOYALTY_ID=? ";

			Query query = session.createQuery(sql);

			query.setParameter(0, loyaltyID);

			list = query.list();

			 
			// trx.commit();

		} catch (Exception e) {
			// trx.rollback();
			e.printStackTrace();
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return list;

	}// getNationalNumberDetails

	public List<LoyaltyRegisteredNumberTabDTO> getLoyaltyRegisteredNumberDetails(Long loyaltyID) {
		return getLoyaltyRegisteredNumberDetails(loyaltyID, new ArrayList<String>());
	}// getLoyaltyRegisteredNumberDetails

	public List<LoyaltyRegisteredNumberTabDTO> getLoyaltyRegisteredNumberDetails(Long loyaltyID, List<String> lineNumber) {
		Session session = null;
		List<LoyaltyRegisteredNumberTabDTO> loyaltyRegisteredNumberTabDTO = null;
		// Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();
			String tableName = infoDAO.getLoyaltyRegisteredNumberTable(loyaltyID + "");

			String sql = "FROM " + tableName + "  WHERE loyaltyID=:lID";

			if (lineNumber != null && lineNumber.size() > 0) {
				sql += " AND linkedNumber IN (:lineNumber) ";
			}

			Query query = session.createQuery(sql);
			query.setParameter("lID", loyaltyID);

			if (lineNumber != null && lineNumber.size() > 0) {
				query.setParameterList("lineNumber", lineNumber);
			}

			loyaltyRegisteredNumberTabDTO = query.list();

			// trx.commit();

			/*
			 * session=HiberanteUtil.getSessionFactory().openSession(); //trx =
			 * session.beginTransaction();
			 * tableName=infoDAO.getLoyaltyRegisteredNumberDBTable(loyaltyID+"");
			 * 
			 * sql=" SELECT
			 * LOYALTY_ID,ACCOUNT_NO,LINKED_NO,STATUS_ID,STATUS_UPDATED_DATE,ACCOUNT_TYPE " + "
			 * FROM "+tableName+" WHERE LOYALTY_ID=? ";
			 * 
			 * if(lineNumber!=null&&lineNumber.size()>0) { sql+=" AND LINKED_NO
			 * IN (:lineNumber) "; }
			 * 
			 * Query query=session.createSQLQuery(sql);
			 * query.setParameter(0,loyaltyID);
			 * 
			 * if(lineNumber!=null&&lineNumber.size()>0) {
			 * query.setParameterList("lineNumber",lineNumber); }
			 * 
			 * List<Object[]> list=query.list();
			 * 
			 * logger.info(" "+list);
			 * 
			 * LoyaltyRegisteredNumberTabDTO tabDTO=null;
			 * 
			 * for(Object[] obj:list) { tabDTO=new
			 * LoyaltyRegisteredNumberTabDTO();
			 * tabDTO.setLoyaltyID(Long.parseLong(obj[0].toString()));
			 * tabDTO.setAccountNumber(obj[1]==null?null:Long.parseLong(obj[1]+""));
			 * tabDTO.setLinkedNumber(obj[2].toString());
			 * 
			 * System.out.println(tabDTO.getLinkedNumber());
			 * 
			 * tabDTO.setStatusID(Integer.parseInt(obj[3].toString()));
			 * tabDTO.setStatusUpdatedDate((Date)obj[4]);
			 * 
			 * Integer
			 * accountType=(obj[5]==null?null:Integer.parseInt(obj[5]+""));
			 * 
			 * tabDTO.setAccountTypeId(accountType);
			 * 
			 * if(Cache.accountTypeMap.get(accountType)!=null) {
			 *  } else{ tabDTO.setAccountTypeId(0);
			 * tabDTO.setAccountTypeName("Undefind"); }
			 * 
			 * if(loyaltyRegisteredNumberTabDTO==null)
			 * loyaltyRegisteredNumberTabDTO=new ArrayList<LoyaltyRegisteredNumberTabDTO>();
			 * 
			 * loyaltyRegisteredNumberTabDTO.add(tabDTO);
			 *  } //trx.commit();
			 * 
			 */

		} catch (Exception e) {
			// trx.rollback();
			// e.printStackTrace();

			/*
			 * StringWriter stringWriter=new StringWriter();
			 * e.printStackTrace(new PrintWriter(stringWriter));
			 * 
			 * logger.info(stringWriter.toString());
			 */

			logger.info("", e);

		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return loyaltyRegisteredNumberTabDTO;

	}// getLoyaltyRegisteredNumberDetails

	public List<LoyaltyRegisteredNumberTabDTO> getLoyaltyRegisteredNumberDetails(Long loyaltyID, Long accountNumber) {
		Session session = null;
		List<LoyaltyRegisteredNumberTabDTO> loyaltyRegisteredNumberTabDTO = null;
		//Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			//trx = session.beginTransaction();
			String tableName = infoDAO.getLoyaltyRegisteredNumberDBTable(loyaltyID + "");

			String sql = " SELECT LOYALTY_ID,ACCOUNT_NO,LINKED_NO,STATUS_ID,STATUS_UPDATED_DATE,ACCOUNT_TYPE " + " FROM " + tableName + " WHERE LOYALTY_ID=? AND ACCOUNT_NO=?";

			Query query = session.createSQLQuery(sql);
			query.setParameter(0, loyaltyID);
			query.setParameter(1, accountNumber);

			List<Object[]> list = query.list();

			LoyaltyRegisteredNumberTabDTO tabDTO = null;

			for (Object[] obj : list) {
				tabDTO = new LoyaltyRegisteredNumberTabDTO();
				tabDTO.setLoyaltyID(Long.parseLong(obj[0].toString()));
				tabDTO.setAccountNumber(obj[1].toString());
				tabDTO.setLinkedNumber(obj[2].toString());
				tabDTO.setStatusID(Integer.parseInt(obj[3].toString()));
				tabDTO.setStatusUpdatedDate((Date) obj[4]);

				Integer accountType = (obj[5] == null ? null : Integer.parseInt(obj[5] + ""));

				tabDTO.setAccountTypeId(accountType);
				if (Cache.accountTypeMap.get(accountType) != null) {
					
					tabDTO.setAccountTypeName(Cache.accountTypeMap.get(accountType));
				} else
					tabDTO.setAccountTypeName("Undefind");

				
				if (loyaltyRegisteredNumberTabDTO == null)
					loyaltyRegisteredNumberTabDTO = new ArrayList<LoyaltyRegisteredNumberTabDTO>();

				loyaltyRegisteredNumberTabDTO.add(tabDTO);

			}
			//trx.commit();

		} catch (Exception e) {
			//trx.rollback();
			e.printStackTrace();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return loyaltyRegisteredNumberTabDTO;

	}// getLoyaltyRegisteredNumberDetails

	public LoyaltyRegisteredNumberTabDTO getLoyaltyRegisteredNumberDetails(Long loyaltyID, String subsNumber) {
		Session session = null;
		// List<LoyaltyRegisteredNumberTabDTO>
		// loyaltyRegisteredNumberTabDTO=null;
		LoyaltyRegisteredNumberTabDTO tabDTO = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		//Transaction trx = null;
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			//trx = session.beginTransaction();
			String tableName = infoDAO.getLoyaltyRegisteredNumberDBTable(loyaltyID + "");

			String sql = " SELECT LOYALTY_ID,ACCOUNT_NO,LINKED_NO,STATUS_ID,STATUS_UPDATED_DATE,ACCOUNT_TYPE " + " FROM " + tableName + " WHERE LOYALTY_ID=? AND LINKED_NO=?";

			logger.info("Query for " + loyaltyID + " " + subsNumber);
			Query query = session.createSQLQuery(sql);
			query.setParameter(0, loyaltyID);
			query.setParameter(1, subsNumber);

			List<Object[]> list = query.list();

			for (Object[] obj : list) {
				tabDTO = new LoyaltyRegisteredNumberTabDTO();
				tabDTO.setLoyaltyID(Long.parseLong(obj[0].toString()));
				tabDTO.setAccountNumber(obj[1] != null ? obj[1].toString() : "0l");
				tabDTO.setLinkedNumber(obj[2].toString());
				tabDTO.setStatusID(Integer.parseInt(obj[3].toString()));
				tabDTO.setStatusUpdatedDate((Date) obj[4]);

				Integer accountType = (obj[5] == null ? null : Integer.parseInt(obj[5] + ""));
				logger.info(accountType);
				if (Cache.accountTypeMap.get(accountType) != null) {
					tabDTO.setAccountTypeId(accountType);
					tabDTO.setAccountTypeName(Cache.accountTypeMap.get(accountType));
				} else {
					tabDTO.setAccountTypeId(accountType);

					tabDTO.setAccountTypeName("Undefind");
				}

				// loyaltyRegisteredNumberTabDTO.add(tabDTO);

			}
			//trx.commit();

		} catch (Exception e) {
			//trx.rollback();
			e.printStackTrace();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return tabDTO;

	}// getLoyaltyRegisteredNumberDetails

	public LoyaltyProfileTabDTO getLoyaltyProfileDetails(Long loyaltyID) {
		Session session = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		//Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			//trx = session.beginTransaction();
			String tableName = infoDAO.getLoyaltyProfileDBTable(loyaltyID + "");

			String sql = "SELECT LOYALTY_ID,PIN,CONTACT_NUMBER,FIRSTNAME,LASTNAME,DEFAULTLANGUAGE,EMAIL_ID," + "DOB,ADDRESS,OCCUPATION,VIP_CODE,CATEGORY,CREATE_TIME,TYPEOFFIRM,ZODIACSIGN,TARROT,ISDISTRIBUTOR,"
					+ "IS_PRIVILEGE_MEMBER,FAXNUMBER,BLOCK_SUBSCRIBER,MYOFFER_DETAILS,NATIONAL_ID,STATUS_ID,TIER_ID,STATUS_POINTS," + "STATUS_UPDATED_DATE,REWARD_POINTS,COUNTER,TIER_UPDATED_DATE,ARBIC_FIRST_NAME,ARBIC_LAST_NAME,ARBIC_ADDRESS,TIER_POINTS,BONUS_POINTS,NATIONALITY_ID FROM "
					+ tableName + " WHERE LOYALTY_ID="+loyaltyID;

			logger.info(sql);

			Query query = session.createSQLQuery(sql);

			List<Object[]> list = query.list();

			if (list.size() > 0) {
				Object[] obj = list.get(0);
				loyaltyProfileTabDTO = new LoyaltyProfileTabDTO();
				loyaltyProfileTabDTO.setLoyaltyID(Long.parseLong(obj[0].toString()));
				//loyaltyProfileTabDTO.setEncryptPin(obj[1].toString());
			//	loyaltyProfileTabDTO.setPin(new EncrptPassword().decryptPassWord(loyaltyProfileTabDTO.getEncryptPin()));
				loyaltyProfileTabDTO.setContactNumber(obj[2] != null ? obj[2].toString() : null);
				loyaltyProfileTabDTO.setFirstName(obj[3] != null ? obj[3].toString() : null);
				loyaltyProfileTabDTO.setLastName(obj[4] != null ? obj[4].toString() : null);
				loyaltyProfileTabDTO.setDefaultLanguage(obj[5] != null ? obj[5].toString() : null);
				loyaltyProfileTabDTO.setEmailID(obj[6] != null ? obj[6].toString() : null);
				loyaltyProfileTabDTO.setDateOfBirth((Date) obj[7]);
				loyaltyProfileTabDTO.setAddress(obj[8] != null ? obj[8].toString() : null);
				loyaltyProfileTabDTO.setOccupation(obj[9] != null ? obj[9].toString() : null);
				loyaltyProfileTabDTO.setVipCode(obj[10] != null ? obj[10].toString() : null);
				loyaltyProfileTabDTO.setCategory(obj[11] != null ? obj[11].toString() : null);
				loyaltyProfileTabDTO.setCreateTime((Date) obj[12]);
				loyaltyProfileTabDTO.setTypeOfForm(obj[13] != null ? obj[13].toString() : null);
				loyaltyProfileTabDTO.setZodiacSign(obj[14] != null ? obj[14].toString() : null);
				loyaltyProfileTabDTO.setTarrot(obj[15] != null ? obj[15].toString() : null);
				loyaltyProfileTabDTO.setDistributor(obj[16] != null ? obj[16].toString() : null);
				loyaltyProfileTabDTO.setPrivilegeMember(obj[17] != null ? obj[17].toString() : null);
				loyaltyProfileTabDTO.setFaxNumber(obj[18] != null ? obj[18].toString() : null);
				loyaltyProfileTabDTO.setBlockSubscriber(obj[19] != null ? obj[19].toString() : null);
				loyaltyProfileTabDTO.setMyOfferDetails(obj[20] != null ? obj[20].toString() : null);
				loyaltyProfileTabDTO.setCustID(obj[21] != null ? obj[21].toString() : null);
				loyaltyProfileTabDTO.setStatusID(obj[22] != null ? Integer.parseInt(obj[22].toString()) : 0);
				loyaltyProfileTabDTO.setTierId(obj[23] != null ? Integer.parseInt(obj[23].toString()) : 0);
				loyaltyProfileTabDTO.setStatusPoints(obj[24] != null ? Double.parseDouble(obj[24].toString()) : null);
				loyaltyProfileTabDTO.setStatusUpdatedDate((Date) obj[25]);
				loyaltyProfileTabDTO.setRewardPoints(obj[26] != null ? Double.parseDouble(obj[26].toString()) : null);
				loyaltyProfileTabDTO.setCounter(obj[27] != null ? Integer.valueOf((obj[27].toString())) : 0);
				loyaltyProfileTabDTO.setTierUpdatedDate((Date) obj[28]);

				loyaltyProfileTabDTO.setArbicFirstName(obj[29] != null ? obj[29].toString() : null);
				loyaltyProfileTabDTO.setArbicLastName(obj[30] != null ? obj[30].toString() : null);
				loyaltyProfileTabDTO.setArbicAddress(obj[31] != null ? obj[31].toString() : null);
				loyaltyProfileTabDTO.setTierPoints(obj[32] != null ? Double.parseDouble(obj[32].toString()) : null);
				loyaltyProfileTabDTO.setBonusPoints(obj[33] != null ? Double.parseDouble(obj[33].toString()) : null);
				loyaltyProfileTabDTO.setNationality_Id(obj[34] != null ? obj[34].toString() : null);
			}

			//trx.commit();

			
		} catch (Exception e) {
			//trx.rollback();
			e.printStackTrace();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return loyaltyProfileTabDTO;

	}// getLoyaltyProfileDetails

	
	public boolean updateSubscriberNumberDetails(Long subscriberNumber, String[] columnNames, Object[] columnValues) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			String tableName = infoDAO.getSubscriberNumberDBTable(subscriberNumber + "");

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String columns = "";
			for (String columnName : columnNames)
				columns += "TAB." + columnName + "=?, ";

			columns = columns.substring(0, columns.lastIndexOf(","));

			String sql = " UPDATE " + tableName + " TAB SET " + columns + " WHERE TAB.SUBSCRIBER_NUMBER=?";

			Query query = session.createSQLQuery(sql);

			int index = 0;
			for (Object object : columnValues) {
				query.setParameter(index++, object);
			}
			query.setLong(index, subscriberNumber);

			if (query.executeUpdate() > 0) {
				flag = true;
				transaction.commit();
			} else {
				transaction.rollback();
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null )
				transaction.rollback();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return flag;

	}// updateSubscriberNumberDetails

	public boolean updateADSLDetails(String adlsName, String[] columnNames, Object[] columnValues) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			String tableName = infoDAO.getADSLNumberDBTable(adlsName);

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String columns = "";
			for (String columnName : columnNames)
				columns += "TAB." + columnName + "=?, ";

			columns = columns.substring(0, columns.lastIndexOf(","));

			String sql = " UPDATE " + tableName + " TAB SET " + columns + " WHERE TAB.ADSL_NO=?";

			Query query = session.createSQLQuery(sql);

			int index = 0;
			for (Object object : columnValues) {
				query.setParameter(index++, object);
			}
			query.setString(index, adlsName);

			if (query.executeUpdate() > 0) {
				flag = true;
				transaction.commit();
			} else {
				transaction.rollback();
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null )
				transaction.rollback();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return flag;

	}// updateADSLDetails

	public boolean updateLoyaltyProfile(Long loyaltyID, String[] columnNames, Object[] columnValues) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			String tableName = infoDAO.getLoyaltyProfileDBTable(loyaltyID + "");

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String columns = "";
			for (String columnName : columnNames)
				columns += "TAB." + columnName + "=?, ";

			columns = columns.substring(0, columns.lastIndexOf(","));

			String sql = " UPDATE " + tableName + " TAB SET " + columns + " WHERE TAB.LOYALTY_ID=?";

			Query query = session.createSQLQuery(sql);

			int index = 0;
			for (Object object : columnValues) {
				query.setParameter(index++, object);
			}
			query.setLong(index, loyaltyID);

			if (query.executeUpdate() > 0) {
				flag = true;
				transaction.commit();
			} else {
				transaction.rollback();
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null )
				transaction.rollback();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return flag;

	}// updateLoyaltyProfile

	public ADSLTabDTO getADSLDetails(String adslNumber) {
		return getADSLDetails(adslNumber, null);

	}// getADSLDetails

	public ADSLTabDTO getADSLDetails(String adslNumber, Long loyaltyID) {
		ADSLTabDTO tabDTO = null;
		Session session = null;
		//Transaction transaction = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();

			String tableName = infoDAO.getADSLNumberDBTable(adslNumber);

			String sql = " SELECT ADSL_NO,ACCOUNT_NO,LOYALTY_ID," + " POINTS,STATUS_ID,STATUS_UPDATED_DATE,COUNTER,ACCOUNT_TYPE " + " FROM " + tableName + " WHERE ADSL_NO=? ";

			if (loyaltyID != null && loyaltyID > 0)
				sql += " AND LOYALTY_ID=?1 ";

			Query query = session.createSQLQuery(sql);

			query.setParameter(0, adslNumber);

			if (loyaltyID != null && loyaltyID > 0)
				query.setParameter(1, loyaltyID);

			List<Object[]> list = query.list();

			if (list.size() > 0) {
				Object[] obj = list.get(0);
				tabDTO = new ADSLTabDTO();

				tabDTO.setADSLNumber(obj[0].toString());
				tabDTO.setAccountNumber(obj[1] == null ? null : obj[1].toString());
				tabDTO.setLoyaltyID(Long.parseLong(obj[2].toString()));
				tabDTO.setPoints(Double.parseDouble(obj[3].toString()));
				tabDTO.setStatusID(Integer.parseInt(obj[4].toString()));
				tabDTO.setStatusUpdatedDate((Date) obj[5]);
				tabDTO.setCounter(Long.parseLong(obj[6].toString()));

				Integer accountType = (obj[7] == null ? null : Integer.parseInt(obj[7] + ""));

				if (Cache.accountTypeMap.get(accountType) != null) {
					tabDTO.setAccountTypeId(accountType);
					tabDTO.setAccountTypeName((Cache.accountTypeMap.get(accountType)));
				} else
					tabDTO.setAccountTypeName("Undefind");

			}

			//transaction.commit();

		} catch (Exception e) {
			//transaction.rollback();
			e.printStackTrace();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return tabDTO;

	}// getADSLDetails

	public boolean updateAccountNumberDetails(String accountNumber, String[] columnNames, Object[] columnValues) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			String tableName = infoDAO.getAccountNumberDBTable(accountNumber + "");

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String columns = "";
			for (String columnName : columnNames)
				columns += "TAB." + columnName + "=?, ";

			columns = columns.substring(0, columns.lastIndexOf(","));

			String sql = " UPDATE " + tableName + " TAB SET " + columns + " WHERE TAB.ACCOUNT_NO=?";

			Query query = session.createSQLQuery(sql);

			int index = 0;
			for (Object object : columnValues) {
				query.setParameter(index++, object);
			}
			query.setString(index, accountNumber);

			if (query.executeUpdate() > 0) {
				flag = true;
				transaction.commit();
			} else {
				transaction.rollback();
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null )
				transaction.rollback();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return flag;

	}// updateAccountNumberDetails

	public boolean updateLoyalProfileDetails(Long loyaltyID, String[] columnNames, Object[] columnValues) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			String tableName = infoDAO.getLoyaltyProfileDBTable(loyaltyID + "");

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String columns = "";
			for (String columnName : columnNames)
				columns += "TAB." + columnName + "=?, ";

			columns = columns.substring(0, columns.lastIndexOf(","));

			String sql = " UPDATE " + tableName + " TAB SET " + columns + " WHERE TAB.LOYALTY_ID=? ";

			Query query = session.createSQLQuery(sql);

			int index = 0;
			for (Object object : columnValues) {
				query.setParameter(index++, object);
			}
			query.setParameter(index, loyaltyID);

			if (query.executeUpdate() > 0) {
				flag = true;
				transaction.commit();
			} else {
				transaction.rollback();
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null)
				transaction.rollback();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return flag;

	}// updateAccountNumberDetails

	public boolean updateAccountRegisteredNumberDetails(Long loyaltyID, String[] columnNames, Object[] columnValues) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			String tableName = infoDAO.getLoyaltyRegisteredNumberDBTable(loyaltyID + "");

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String columns = "";
			for (String columnName : columnNames)
				columns += "TAB." + columnName + "=?, ";

			columns = columns.substring(0, columns.lastIndexOf(","));

			String sql = " UPDATE " + tableName + " TAB SET " + columns + " WHERE TAB.LOYALTY_ID=?";

			Query query = session.createSQLQuery(sql);

			int index = 0;
			for (Object object : columnValues) {
				query.setParameter(index++, object);
			}
			query.setLong(index, loyaltyID);

			if (query.executeUpdate() > 0) {
				flag = true;
				transaction.commit();
			} else {
				transaction.rollback();
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null )
				transaction.rollback();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return flag;

	}// updateAccountRegisteredNumberDetails

	public String getSubscriberProfileEntity(String subscriberNumber) {
		String tablePrefix = "SUBSCRIBER_PROFILE_ENTITY";
		int index = Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_PROFILE_SUFFIX_LENGTH").getParameterValue());
		String tableName = "_0";
		if (index > 0)
			tableName = "_" + subscriberNumber.substring(subscriberNumber.length() - index, subscriberNumber.length());

		return tablePrefix + tableName;

	}

	public boolean checkSubscriberStatusDetails(SubscriberNumberTabDTO subscriberNumberTabDTO) {

		if (subscriberNumberTabDTO.getStatusID() == 2) {
			// Subscriber Number is preactive,make it active
			updateSubscriberNumberDetails(subscriberNumberTabDTO.getSubscriberNumber(), new String[] { "STATUS_ID", "STATUS_UPDATED_DATE" }, new Object[] { 1, new Date() });
			logger.info("Subscriber Number : " + subscriberNumberTabDTO.getSubscriberNumber() + " is Activated from Preactive");

		} else if (subscriberNumberTabDTO.getStatusID() == 3) {
			logger.info("Subscriber Number :" + subscriberNumberTabDTO.getSubscriberNumber() + " is softdele mode");
			CommonUtil commonUtil = new CommonUtil();
			if (commonUtil.isSoftDeletePeriodOver(subscriberNumberTabDTO.getStatusUpdatedDate())) {
				logger.info("Subscriber Number :" + subscriberNumberTabDTO.getSubscriberNumber() + " softdelete period is over.");
				return false;

			} else {

				updateSubscriberNumberDetails(subscriberNumberTabDTO.getSubscriberNumber(), new String[] { "STATUS_ID", "STATUS_UPDATED_DATE" }, new Object[] { 1, new Date() });
				logger.info("Subscriber Number : " + subscriberNumberTabDTO.getSubscriberNumber() + " is Activated from Softdelete");
			}

		} else if (subscriberNumberTabDTO.getStatusID() > 3) {
			logger.info("Subscriber Number :" + subscriberNumberTabDTO.getSubscriberNumber() + " is Inactive mode");
			return false;
		}

		return true;

	}// checkSubscriberStatusDetails

	public boolean checkADSLStatusDetails(ADSLTabDTO tabDTO) {

		if (tabDTO.getStatusID() == 2) {
			// Subscriber Number is preactive,make it active
			updateADSLDetails(tabDTO.getADSLNumber(), new String[] { "STATUS_ID", "STATUS_UPDATED_DATE" }, new Object[] { 1, new Date() });
			logger.info("ADSL  : " + tabDTO.getADSLNumber() + " is Activated from Preactive");

		} else if (tabDTO.getStatusID() == 3) {
			logger.info("Subscriber Number :" + tabDTO.getADSLNumber() + " is softdele mode");
			CommonUtil commonUtil = new CommonUtil();
			if (commonUtil.isSoftDeletePeriodOver(tabDTO.getStatusUpdatedDate())) {
				logger.info("ADSL   :" + tabDTO.getADSLNumber() + " softdelete period is over.");
				return false;

			} else {

				updateADSLDetails(tabDTO.getADSLNumber(), new String[] { "STATUS_ID", "STATUS_UPDATED_DATE" }, new Object[] { 1, new Date() });
				logger.info("ADSL : " + tabDTO.getADSLNumber() + " is Activated from Softdelete");
			}

		} else if (tabDTO.getStatusID() > 3) {
			logger.info("ADSL :" + tabDTO.getADSLNumber() + " is Inactive mode");
			return false;
		}

		return true;

	}// checkSubscriberStatusDetails

	public boolean checkAccountStatusDetails(AccountNumberTabDTO accountNumberTabDTO) {

		if (accountNumberTabDTO.getStatusID() == 2) {
			// Subscriber Number is preactive,make it active
			updateAccountNumberDetails(accountNumberTabDTO.getAccountNumber(), new String[] { "STATUS_ID", "STATUS_UPDATED_DATE" }, new Object[] { 1, new Date() });
			logger.info("Account Number : " + accountNumberTabDTO.getAccountNumber() + " is Activated from Preactive");

		} else if (accountNumberTabDTO.getStatusID() == 3) {
			logger.info("Account Number :" + accountNumberTabDTO.getAccountNumber() + " is softdele mode");

			CommonUtil commonUtil = new CommonUtil();
			if (commonUtil.isSoftDeletePeriodOver(accountNumberTabDTO.getStatusUpdatedDate())) {
				logger.info("Account Number :" + accountNumberTabDTO.getAccountNumber() + " softdelete period is over.");
				return false;

			} else {

				updateAccountNumberDetails(accountNumberTabDTO.getAccountNumber(), new String[] { "STATUS_ID", "STATUS_UPDATED_DATE" }, new Object[] { 1, new Date() });
				logger.info("Account Number : " + accountNumberTabDTO.getAccountNumber() + " is Activated from Softdelete");
			}

		} else if (accountNumberTabDTO.getStatusID() > 3) {
			logger.info("Account Number :" + accountNumberTabDTO.getAccountNumber() + " is " + Cache.getStatusMap().get(accountNumberTabDTO.getStatusID() + " Mode"));
			return false;
		}

		return true;

	}// checkAccountStatusDetails

	public ActionServiceDetailsDTO getServiceDetails(String serviceName) throws CommonException {
		if (Cache.serviceDetailsMap.get(serviceName.toUpperCase()) != null)
			return getServiceDetails(Cache.serviceDetailsMap.get(serviceName.toUpperCase()).getServiceID());
		else
			throw new CommonException(serviceName.toUpperCase() + " Not found ");
	}// getServiceDetails

	public ActionServiceDetailsDTO getServiceDetails(Integer serviceId) {
		Session session = null;
		ActionServiceDetailsDTO actionServiceDetailsDTO = null;
		//Transaction trx = null;
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			//trx = session.beginTransaction();

			String sql = "SELECT SERVICE_ID,SERVICE_NAME,ACTION_NAME,ACTION_TYPE,NO_OF_TIMES,VALIDITY,VALIDITY_TYPE,START_TIME,END_TIME," + "IS_EVERY_DAY,ISREPEAT,PARENT_ID,START_DATE,END_DATE FROM ACTION_SERVICE_DETAILS WHERE  SERVICE_ID=? ";

			Query query = session.createSQLQuery(sql).setParameter(0, serviceId);

			List<Object[]> list = query.list();

			if (list.size() > 0) {
				Object[] obj = list.get(0);

				actionServiceDetailsDTO = new ActionServiceDetailsDTO();

				actionServiceDetailsDTO.setServiceID(Integer.parseInt(obj[0].toString()));
				actionServiceDetailsDTO.setServiceName((String) obj[1]);
				actionServiceDetailsDTO.setActionName((String) obj[2]);
				actionServiceDetailsDTO.setActionType(Integer.parseInt(obj[3].toString()));
				actionServiceDetailsDTO.setNoOfTimes(Integer.parseInt(obj[4].toString()));
				actionServiceDetailsDTO.setValidity(obj[5] == null ? null : Integer.parseInt(obj[5].toString()));
				actionServiceDetailsDTO.setValidityType((String) obj[6]);
				actionServiceDetailsDTO.setStartTime((String) obj[7]);
				actionServiceDetailsDTO.setEndTime((String) obj[8]);
				actionServiceDetailsDTO.setEveryDay((String) obj[9]);
				actionServiceDetailsDTO.setRepeat("" + (Character) obj[10]);
				actionServiceDetailsDTO.setParentID(obj[11] == null ? null : Integer.parseInt(obj[11].toString()));
				actionServiceDetailsDTO.setStartDate((Date) obj[12]);
				actionServiceDetailsDTO.setEndDate((Date) obj[13]);

			}

			//trx.commit();

		} catch (Exception e) {
			//trx.rollback();
			e.printStackTrace();
		} finally {
			if (serviceId != null)
				session.close();
			session = null;
		}

		return actionServiceDetailsDTO;

	}// getServiceDetails

	@SuppressWarnings("unchecked")
	public LoyaltyProfileTabDTO getLoyaltyProfile(Long loyaltyID) {
		logger.info("inside getLoyaltyProfile() ");
	Session session = null;
	LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
	 //Transaction transaction = null;
	try {
        logger.info(">>>>>>>loyalty_id>>>>>>>>>>>>>"+loyaltyID);
       
        session = HiberanteUtil.getSessionFactory().openSession();
        String tableName = "LOYALTY_PROFILE_ENTITY_0";
		Query query = session.createQuery("FROM "+ tableName +" WHERE loyaltyID=:loyaltyID");
		query.setParameter("loyaltyID",loyaltyID);	
			
			List<LoyaltyProfileTabDTO> list =query.list();
			
			logger.info(">>>>>>>list>>>>>>>>>>>>>"+list.size());
			
			if (list != null && list.size() > 0) {
				loyaltyProfileTabDTO = list.get(0);
			}
		
		//transaction.commit();
	} catch (Exception e) {
		 e.printStackTrace();
		logger.error("", e);
	} finally {
		try {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		} catch (Exception e) {
		}
	}
   logger.info("succesfully executed getLoyaltyProfile");
	return loyaltyProfileTabDTO;
}// getLoyaltyProfile

	public boolean isLoyaltyIdExists(Long loyaltyID) {
		Session session = null;
		boolean flag = false;
		//Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {
			 
			session = HiberanteUtil.getSessionFactory().openSession();
			

			String sql = "SELECT LOYALTY_ID " + " FROM " + infoDAO.getLoyaltyProfileDBTable(loyaltyID + "") + " WHERE LOYALTY_ID = ?";

			Query query = session.createSQLQuery(sql);
			query.setParameter(0, loyaltyID);

			List<Long> list = query.list();

			if (list.size() > 0) {
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return flag;

	}// getLoyaltyProfile

	public boolean updateLoyaltyRegisteredNumberDetails(String lineNumber, Long loyaltyID, String[] columnNames, Object[] columnValues) {
		Transaction transaction = null;
		Session session = null;
		boolean flag = false;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {

			String tableName = infoDAO.getLoyaltyRegisteredNumberDBTable(loyaltyID + "");

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String columns = "";
			for (String columnName : columnNames)
				columns += "TAB." + columnName + "=?, ";

			columns = columns.substring(0, columns.lastIndexOf(","));

			String sql = " UPDATE " + tableName + " TAB SET " + columns + " WHERE TAB.LOYALTY_ID=? AND LINKED_NO=?";

			Query query = session.createSQLQuery(sql);

			int index = 0;
			for (Object object : columnValues) {
				query.setParameter(index++, object);
			}
			query.setLong(index++, loyaltyID);
			query.setString(index++, lineNumber);

			System.out.println("TABLE NAME" + tableName);
			System.out.println("COLUMNS " + columns);
			System.out.println("LOYALTY ID " + loyaltyID);
			System.out.println("LINKED NUMBER" + lineNumber);
			if (query.executeUpdate() > 0) {
				flag = true;
				transaction.commit();
			} else {
				transaction.rollback();
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (transaction != null)
				transaction.rollback();
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return flag;

	}// updateAccountRegisteredNumberDetails

	public List<Long> getReleateIds(Long Id) {
		List<Long> list = new ArrayList<Long>();
		Long parentId = findParent(Id);

		list.addAll(findChild(parentId));

		list.add(0, parentId);

		return list;
	}

	private List<Long> findChild(Long Id) {
		List<Long> allChilds = new ArrayList<Long>();
		List<Object> list = null;
		Session session = null;
		Query query = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			String sql = "SELECT CHILD_LOYALTY_ID FROM LOYALTY_HIERARCHY_DETAILS WHERE PARENT_LOYALTY_ID = ?";
			query = session.createSQLQuery(sql);
			query.setParameter(0, Id);

			list = query.list();

			for (Object all : list) {
				allChilds.add(Long.parseLong(all.toString()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();

			list = null;
			session = null;
			query = null;
		}

		return allChilds;
	}// find child

	public Long findParent(Long Id) {
		Session session = null;
		Query query = null;
		List<Object> list = null;
		Object all = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			String sql = "SELECT PARENT_LOYALTY_ID FROM LOYALTY_HIERARCHY_DETAILS WHERE CHILD_LOYALTY_ID =?";
			query = session.createSQLQuery(sql);
			query.setParameter(0, Id);

			list = query.list();

			if (list.size() > 0) {
				// Id = findParent(dto.getParentLoyaltyId());
				all = list.get(0);
				Id = Long.parseLong(all.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}

		return Id;
	} // Find parent
	
	//getting the promodetails ***start sajith k s
	public PromoTableDTO getPromoDetails(Long subscriberNumber) {
		PromoTableDTO promoTableDTO = null;
		Session session = null;
		// Transaction trx = null;
		try {
			long start = System.currentTimeMillis();

			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();

			String sql = " FROM  PromoTableDTO WHERE subscriberNumber=? ";
			Query query = session.createQuery(sql);
			query.setParameter(0, subscriberNumber);

			List<PromoTableDTO> list = query.list();

			if (list.size() > 0) {
				promoTableDTO = list.get(0);

			}
			
		} catch (Exception e) {
			// trx.rollback();
			// e.printStackTrace();
			logger.info("", e);
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return promoTableDTO;

	}//getting the promodetails ***end sajith k s
	
	//updating the promodetails ***start sajith k s
	public void updatePromoDetails(Long subscriberNumber,Long loyaltyID) {
		PromoTableDTO promoTableDTO = null;
		Session session = null;
		Transaction tx=null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			tx=session.beginTransaction();
			promoTableDTO=new PromoTableDTO(loyaltyID,subscriberNumber.toString());
			session.save(promoTableDTO);
			tx.commit();
			logger.info("commited successfully");
			
		} catch(Exception e){
			if(tx!=null)
				tx.rollback();
			logger.info("Exception in commiting the data-->"+e);
		}
		finally{
			session.close();
		}

	}//updating the promodetails ***end sajith k s
	
	public SubscriberNumberTabDTO getSubscriberProfile(String msisdn) {
		logger.info("inside getSubscriberProfile() ");

		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		Session session = null;
		// Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try {
			long start = System.currentTimeMillis();

			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();

			String tableName = infoDAO.getSubscriberNumberTable(msisdn + "");

			String sql = " FROM " + tableName + " WHERE accountNumber=? ";

		

			Query query = session.createQuery(sql);
			query.setParameter(0, msisdn);
			

			List<SubscriberNumberTabDTO> list = query.list();

			if (list.size() > 0) {
				subscriberNumberTabDTO = list.get(0);
			}
			

		} catch (Exception e) {
			// trx.rollback();
			// e.printStackTrace();
			logger.info("", e);
		} finally {
			infoDAO = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		return subscriberNumberTabDTO;
	
	}
	
	public LmsOnmPointSnapshotDTO getLmsOnmPOintSnapshotDTO(Long loyaltyID) {
		logger.info("inside getLmsOnmPOintSnapshotDTO() ");
		Session session = null;
		LmsOnmPointSnapshotDTO pointSnapshotDTO = null;
		 //Transaction transaction = null;
		try {
	        logger.info(">>>>>>>loyalty_id>>>>>>>>>>>>>"+loyaltyID);
	       
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			Criteria criteria=session.createCriteria(LmsOnmPointSnapshotDTO.class);
					criteria.add(Restrictions.eq("loyaltyId", loyaltyID));	
				
				List<LmsOnmPointSnapshotDTO> list =criteria.list();

				if(list.size()>0)
					pointSnapshotDTO=list.get(0);
			
			//transaction.commit();
		} catch (Exception e) {
			logger.error("", e);
			 e.printStackTrace();
			
		} finally {
			try {
				if (session != null && session.isOpen()) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}
	   logger.info("succesfully executed getLmsOnmPOintSnapshotDTO");
	return pointSnapshotDTO;
	}
	
	public List<LoyaltyProfileTabDTO> getLoyaltyProfileInfoBasedOnMsisdn(ServiceManagementDTO serviceManagementDTO, String tableName)
			throws CommonException {
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		ArrayList<LoyaltyProfileTabDTO> loyaltyProfileDetails = null;
		try {
			logger.info("Transaction ID : " + serviceManagementDTO.getTransactionId() + " contactNumber : " + serviceManagementDTO.getMoNumber()+ " Getting loyalty linked numbers from " + tableName);
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(tableName).add(Restrictions.eq("contactNumber",serviceManagementDTO.getMoNumber().trim()));
			loyaltyProfileDetails = (ArrayList<LoyaltyProfileTabDTO>) criteria.list();
			//transaction.commit();
		} catch (HibernateException e) {
			logger.error("Transaction ID : " + serviceManagementDTO.getTransactionId() + " contactNumber : " + serviceManagementDTO.getMoNumber()+" Hibernate Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			throw new CommonException(e.getMessage());

		} catch (Exception e) {
			logger.error("Transaction ID : " + serviceManagementDTO.getTransactionId() + " contactNumber : " + serviceManagementDTO.getMoNumber()+ "  Exception occured ", e);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */
			throw new CommonException(e.getMessage());

		} finally {

			if (session != null) {
				session.close();
			}
		}
		return loyaltyProfileDetails;
	}
	//
	public boolean adjustPoints(String requestId, Long loyaltyId, boolean isAdd, double points) {
		Session session = null;
		long t1 = System.currentTimeMillis();	
		int result=0;
		TableInfoDAO tableInfoDAO = null;
		String tableName;
		boolean isUpdated = false;
		Transaction tx=null;
		Query query=null;
		String hql=null;
	
		try {
			
			
			session = HiberanteUtil.getSessionFactory().openSession();
		    tx=session.beginTransaction();
			tableInfoDAO = new TableInfoDAO();
			tableName = tableInfoDAO.getLoyaltyProfileTable(String.valueOf(loyaltyId));
			logger.info("tableName:"+tableName);
			if(isAdd)
				hql="UPDATE " +tableName+" set REWARD_POINTS=:points where LOYALTY_ID=:loyaltyId";
			else
				hql="UPDATE " +tableName+" set REWARD_POINTS=:points where LOYALTY_ID=:loyaltyId";
			query=session.createQuery(hql);
			query.setParameter("points",points);
			query.setParameter("loyaltyId",loyaltyId);			
			result = query.executeUpdate();			
			tx.commit();
			if(result>0)
			{
				isUpdated=true;
			}
			logger.info("IsUpdated:"+isUpdated);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
			logger.info(" Transaction Id :" + requestId + " Time to update"
					+ (System.currentTimeMillis() - t1));
		}
		return isUpdated;
	}
	public LoyaltyTransactionTabDTO setTransactionParameters(Long loyaltyId,Long destLoyaltyId,String msisdn,Double preRewardPoints,Double preStatusPoints,int status,String channel,String packId,Double rewardPoint,Double statusPoint,int tierId,String reqTranId,String statusDesc) {

		LoyaltyTransactionTabDTO loyaltyTransactionTabDTO = null;
		try {
			loyaltyTransactionTabDTO = new LoyaltyTransactionTabDTO();
			loyaltyTransactionTabDTO.setLoyaltyID(loyaltyId);
			if(destLoyaltyId !=null)
				loyaltyTransactionTabDTO.setDestLoyaltyID(destLoyaltyId);
			loyaltyTransactionTabDTO.setSubscriberNumber(msisdn);
			loyaltyTransactionTabDTO.setAccountNumber(msisdn);
			loyaltyTransactionTabDTO.setPreRewardPoints(preRewardPoints);
			if(status == 3 )
				loyaltyTransactionTabDTO.setCurRewardPoints(preRewardPoints - rewardPoint);
			else
				loyaltyTransactionTabDTO.setCurRewardPoints(preRewardPoints + rewardPoint);
			loyaltyTransactionTabDTO.setPreStatusPoints(preStatusPoints);
			loyaltyTransactionTabDTO.setCurStatusPoints(preStatusPoints + statusPoint);
			loyaltyTransactionTabDTO.setStatusID(status);
			loyaltyTransactionTabDTO.setChannel(channel);
			loyaltyTransactionTabDTO.setCreateTime(new Date());
			if(packId !=null)
				loyaltyTransactionTabDTO.setPackageId(Integer.parseInt(packId));
			loyaltyTransactionTabDTO.setRewardPoints(rewardPoint);
			loyaltyTransactionTabDTO.setStatusPoints(statusPoint);
			loyaltyTransactionTabDTO.setPreTierId(tierId);
			loyaltyTransactionTabDTO.setCurTierId(tierId);
			loyaltyTransactionTabDTO.setReqTransactionID(reqTranId);
			loyaltyTransactionTabDTO.setStatusDesc(statusDesc);
		}catch(Exception e) {
			logger.info("Exception in setTransactionParameters :"+e);
			e.printStackTrace();
		}
		return loyaltyTransactionTabDTO;
	}
	public boolean insertLoyaltyTransaction(LoyaltyTransactionTabDTO loyaltyTransactionTabDTO) {

		Session session = null;
		Transaction trx = null;
		String tableName = null;
		TableInfoDAO tableInfoDAO = null;
		boolean flag = false;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			tableInfoDAO = new TableInfoDAO();
			tableName = tableInfoDAO.getLoyaltyTransactionTable(String.valueOf(loyaltyTransactionTabDTO.getLoyaltyID()));
			session.save(tableName, loyaltyTransactionTabDTO);
			flag = true;
			trx.commit();
		}catch(Exception e) {
			logger.info("Exception in insertLoyaltyTransaction :"+e);
			e.printStackTrace();
		}finally {
			if(session !=null && session.isOpen()) {
				session.close();
				session = null;
			}if(trx !=null) {
				trx = null;
			}
		}
		return flag;
	}
	public LoyaltyProfileTabDTO getLoyaltyDetails(Long loyaltyID) {

		Session session = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		Transaction trx = null;
		Criteria criteria = null;
		List<LoyaltyProfileTabDTO> loyaltyList = null;
		TableInfoDAO tableInfoDAO = null;
		String tableName = null;
		
		Query query = null;
		try {
			logger.info(" LoyaltyId :" + loyaltyID);
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			tableInfoDAO = new TableInfoDAO();
			tableName = tableInfoDAO.getLoyaltyProfileDBTable(String.valueOf(loyaltyID));
			query = session.createQuery("from "+tableName+ " where loyaltyID = :loyaltyID");
			query.setParameter("loyaltyID", loyaltyID);
			loyaltyList = query.list();
			if(loyaltyList !=null && loyaltyList.size() >0)
				loyaltyProfileTabDTO = loyaltyList.get(0);
			trx.commit();
		}catch(Exception e) {
			logger.info("Exception in getLoyaltyDetails :"+e);
			e.printStackTrace();
		}finally {
			if(session !=null && session.isOpen()) {
				session.close();
				session = null;
			}if(trx !=null) {
				trx = null;
			}
		}
		return loyaltyProfileTabDTO;

	}
}

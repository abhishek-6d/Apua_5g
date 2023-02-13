package com.sixdee.imp.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.LoyaltyRandomNumber;
import com.sixdee.imp.common.util.LoyaltyTransactionComparator;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyHierarchyDetailsDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.OfferMasterTabDto;
import com.sixdee.imp.dto.PackageCategory;
import com.sixdee.imp.dto.PackageDetails;
import com.sixdee.imp.dto.RedeemPointsDTO;
import com.sixdee.imp.dto.RevertLoyalty;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.VoucherOrderDetailsDTO;
import com.sixdee.imp.dto.hbmdto.TelecomOfferAttributes;
import com.sixdee.imp.dto.parser.RERequestDataSet;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.ReResponseParameter;
import com.sixdee.imp.util.LoyalityCommonTransaction;
import com.sixdee.imp.util.LoyalityTransactionConstants;
import com.sixdee.imp.util.ThirdPartyCall;

/**
 * 
 * @author Somesh
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
 *          <td>May 15,2013 12:24:17 PM</td>
 *          <td>Somesh</td>
 *          </tr>
 *          </table>
 *          </p>
 */

public class RedeemPointsDAO {
	private static Logger logger = Logger.getLogger(RedeemPointsDAO.class);

	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static final DateFormat df1 = new SimpleDateFormat("ddMMyyyyHHmmss");

	public PackageDetails getPackage(GenericDTO genericDTO) {
		RedeemPointsDTO dto = (RedeemPointsDTO) genericDTO.getObj();
		Session session = null;
		// Transaction trx = null;
		PackageDetails pd = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();

			Query query = session.createQuery("FROM PackageDetails WHERE packageId=:packageId and languageId=:languageId");
			// query.setCacheable(true);
			query.setString("packageId", dto.getPackageId());
			query.setInteger("languageId", dto.getDefaultLanguage());
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
			if (pd.getQuantity() != null && pd.getQuantity() > 0) {
				query = session.createQuery("FROM PackageCategory WHERE categoryId=:packageId and languageId=:languageId");
				query.setInteger("packageId", pd.getCategoryId());
				query.setInteger("languageId", pd.getLanguageId());
				List<PackageCategory> categoryList = query.list();
				if (categoryList != null && categoryList.size() > 0) {
					pd.setPackageCategory(categoryList.get(0));
				}
				logger.info("Category id = " + pd.getPackageCategory().getCategoryName());
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

		return pd;
	}

	public TelecomOfferAttributes getOfferAttributes(int offerId) {
		Session session = null;
		TelecomOfferAttributes offerAttributes = null;
		String packName = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();

			Query query = session.createQuery("FROM TelecomOfferAttributes WHERE offerId=:offerId");
			// query.setCacheable(true);
			query.setInteger("offerId", offerId);
			long l1 = System.currentTimeMillis();
			logger.info("Start Time for package" + l1);
			List<TelecomOfferAttributes> offerAttributesList = query.list();
			logger.info("Total Time = " + (System.currentTimeMillis() - l1));

			if (offerAttributesList != null && offerAttributesList.size() > 0) {
				offerAttributes = offerAttributesList.get(0);
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

		return offerAttributes;
	}

	public void updateTransaction(Session session, Long loyatyId, Double points, Long Id) throws Exception {
		Query query = null;
		String sql = "";
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		try {
			// sql =
			// "update "+TableInfoDAO.getLoyaltyTransactionTable(loyatyId.toString())+" set rewardPoints=rewardPoints-:rewardPoints where id=:id";
			sql = "UPDATE " + tableInfoDAO.getLoyaltyTransactionTable(loyatyId.toString()) + " SET redeemPoint=redeemPoint+? WHERE id=?";
			query = session.createQuery(sql);
			query.setParameter(0, points);
			query.setParameter(1, Id);
			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			logger.info("Total time to update = " + (System.currentTimeMillis() - l1));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			query = null;
			tableInfoDAO = null;
		}

	}

	public List<Long> outsideList = new ArrayList<Long>();


	public Double getAllPoints(List<Long> allLoyaltyId) {
		double totalPoints = 0.0;
		Session session = null;
		;
		Query query;
		List<Double> list = null;
		Criteria ctr = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			String sql = "";
			for (Long loyaltyId : allLoyaltyId) {
				ctr = session.createCriteria(tableInfoDAO.getLoyaltyProfileTable(loyaltyId.toString()));

				ctr.add(Restrictions.eq("loyaltyID", loyaltyId));
				ctr.setProjection(Projections.property("rewardPoints"));

				/*
				 * sql =
				 * "SELECT rewardPoints FROM "+tableInfoDAO.getLoyaltyProfileTable
				 * (loyaltyId.toString())+" WHERE loyaltyID=?"; query =
				 * session.createQuery(sql); query.setParameter(0, loyaltyId);
				 */
				long l1 = System.currentTimeMillis();
				logger.info("Start time for reward point = " + l1);
				// list = query.list();
				list = ctr.list();
				logger.info("Total time =" + (System.currentTimeMillis() - l1));
				for (Double d : list) {
					// Double d = Double.parseDouble(all[0].toString());
					// logger.info(d!=null?d.toString():"");
					totalPoints = totalPoints + d;
				}
				list = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
			ctr = null;
			tableInfoDAO = null;
		}
		return totalPoints;
	}

	public Long findParent(Long Id) {
		Session session = null;
		Query query = null;
		List<Long> list = null;
		Long all = null;
		Criteria ctr = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();

			// String sql =
			// "SELECT parentLoyaltyId FROM LoyaltyHierarchyDetailsDTO WHERE childLoyaltyId =?";

			ctr = session.createCriteria(LoyaltyHierarchyDetailsDTO.class);

			ctr.add(Restrictions.eq("childLoyaltyId", Id));
			ctr.setProjection(Projections.property("parentLoyaltyId"));

			/*
			 * query = session.createQuery(sql); query.setParameter(0, Id);
			 */
			long l1 = System.currentTimeMillis();
			logger.info("Start time for parent = " + l1);
			// list = query.list();
			list = ctr.list();
			logger.info("Total time =" + (System.currentTimeMillis() - l1));

			if (list.size() > 0) {
				// Id = findParent(dto.getParentLoyaltyId());
				all = list.get(0);
				Id = all;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();

			ctr = null;
		}

		return Id;
	}

	public List<Long> findChild(Long Id) {
		List<Long> allChilds = new ArrayList<Long>();
		List<Long> list = null;
		Session session = null;
		Query query = null;
		Criteria ctr = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();

			ctr = session.createCriteria(LoyaltyHierarchyDetailsDTO.class);

			ctr.add(Restrictions.eq("parentLoyaltyId", Id));
			ctr.setProjection(Projections.property("childLoyaltyId"));

			/*
			 * String sql =
			 * "SELECT childLoyaltyId FROM LoyaltyHierarchyDetailsDTO WHERE parentLoyaltyId = ?"
			 * ; query = session.createQuery(sql); query.setParameter(0, Id);
			 */

			long l1 = System.currentTimeMillis();
			logger.info("Start time for child = " + l1);
			// list = query.list();
			list = ctr.list();
			logger.info("Total time =" + (System.currentTimeMillis() - l1));
			for (Long all : list) {
				allChilds.add(all);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();

			list = null;
			session = null;
			query = null;
			ctr = null;
		}

		return allChilds;
	}

	public void redeemPoints1(GenericDTO genericDTO, LoyaltyProfileTabDTO loyaltyProfileTabDTO, List<OfferMasterTabDto> packageDetails, String subscriberNum, int langID, SubscriberNumberTabDTO subscriberNumberTabDTO,String voucherId) throws CommonException {
		Session session = null;
		LoyaltyTransactionTabDTO transactionDTO = null;
		RedeemPointsDTO redeemPointsDTO = null;
		List<Long> allRelatedIds = null;
		LoyalityCommonTransaction loyalityCommonTransaction=null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		long loyaltyId = 0;
		LoyaltyProfileTabDTO loyaltyDTO = null;
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		boolean flag = false;
		HashMap<String, String> loyaltyTransactionMap=null;
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			redeemPointsDTO = (RedeemPointsDTO) genericDTO.getObj();
			loyaltyId = loyaltyProfileTabDTO.getLoyaltyID();
			Long parentLoyaltyId = findParent(loyaltyId);

			allRelatedIds = new ArrayList<Long>();
			allRelatedIds.add(parentLoyaltyId);
			logger.info(redeemPointsDTO.getTransactionId() + " LIST = >" + allRelatedIds);

				loyaltyDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyId);

				logger.info("Total Points = " + loyaltyDTO.getRewardPoints() + " and Redeem Points = " + packageDetails.get(0).getPoints() + "instanceId = " + packageDetails.get(0).getInterfaceId());
				if (loyaltyDTO.getRewardPoints() < Double.valueOf(packageDetails.get(0).getPoints())) {
					genericDTO.setStatusCode(Cache.getServiceStatusMap().get("INSUF_BAL_PKG_" + redeemPointsDTO.getDefaultLanguage()).getStatusCode());
					throw new CommonException(Cache.getServiceStatusMap().get("INSUF_BAL_PKG_" + redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
				}
				// Update LoyaltyProfile table for Parent Id
				flag = updateLoyaltyProfileTable(session, parentLoyaltyId, Double.valueOf(packageDetails.get(0).getPoints()), redeemPointsDTO, loyaltyDTO);
				// process block
				
				transactionDTO = new LoyaltyTransactionTabDTO();
				loyalityCommonTransaction=new LoyalityCommonTransaction();
				
				loyaltyTransactionMap=new HashMap<String, String>();
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.loyaltyID, String.valueOf(parentLoyaltyId));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.channel,redeemPointsDTO.getChannel());
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.preRewardPoints, String.valueOf(loyaltyDTO.getRewardPoints()));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.curRewardPoints, String.valueOf(loyaltyDTO.getRewardPoints() - packageDetails.get(0).getPoints()));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.packageId, String.valueOf(redeemPointsDTO.getPackageId()));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.statusId, String.valueOf(LoyaltyTransactionStatus.pointRedeemedforActivationPackage));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.subscriberNumber, redeemPointsDTO.getSubscriberNumber());
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.createTime, String.valueOf(new Date()));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.serverId, Cache.cacheMap.get("SERVER_ID").toString());
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.voucherOrderId,String.valueOf(voucherId));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.testNumber, String.valueOf(""));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.reqTransactionID, redeemPointsDTO.getTransactionId());
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.preTierId, String.valueOf(loyaltyDTO.getTierId()));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.curTierId, String.valueOf(loyaltyDTO.getTierId()));
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.area, redeemPointsDTO.getArea());
	    		loyaltyTransactionMap.put(LoyalityTransactionConstants.location, redeemPointsDTO.getLocation());
	    		
	    		transactionDTO=loyalityCommonTransaction.loyaltyTransactionSetter(transactionDTO, loyaltyTransactionMap);
				

				long l1 = System.currentTimeMillis();
				session.save(tableInfoDAO.getLoyaltyTransactionTable(parentLoyaltyId.toString()), transactionDTO);
				logger.info(redeemPointsDTO.getTransactionId() + " Time to insert transaction == == " + (System.currentTimeMillis() - l1));
				
		} catch (Exception e) {
			// logger.error("Exception occured ",e);
			if (session.getTransaction() != null)
				session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			transactionDTO = null;
			redeemPointsDTO = null;
			allRelatedIds = null;
			loyaltyDTO = null;
		}

	}

	private RedeemPointsDTO getAccountNumberDetails(RedeemPointsDTO redeemPointsDTO, LoyaltyProfileTabDTO loyaltyProfileTabDTO, LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO) {
		// TODO Auto-generated method stub

		String missingTag;
		logger.debug("INSIDE METHOD");
		StringBuilder sb = new StringBuilder();
		TableDetailsDAO detailsDAO = new TableDetailsDAO();
		String requestXML = null;

		try {

			sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:fis=\"http://omantel/services/fiservice\">");
			sb.append("<soapenv:Header/>");
			sb.append("<soapenv:Body>");
			sb.append("<fis:getCustomerInformation>");
			sb.append("<OT_EAI_HEADER>");
			sb.append("<MsgFormat>GETCUSTOMERINFORMATION.SERVICE</MsgFormat>");
			sb.append("<MsgVersion>1.0</MsgVersion>");
			sb.append("<RequestorId>LMS</RequestorId>");
			sb.append("<RequestorChannelId>IP</RequestorChannelId>");
			sb.append("<RequestorUserId>wbimbtst</RequestorUserId>");
			sb.append("<RequestorLanguage>E</RequestorLanguage>");
			sb.append("<RequestorSecurityInfo>RequestorSecurityInfo</RequestorSecurityInfo>");
			sb.append("<EaiReference>0</EaiReference>");
			sb.append("<ReturnCode>0</ReturnCode>");
			sb.append("</OT_EAI_HEADER>");
			sb.append("<Request>");
			sb.append("<ReferenceNo>" + redeemPointsDTO.getTransactionId() + "</ReferenceNo>");
			sb.append("<SubscriberNumber>" + redeemPointsDTO.getSubscriberNumber().replaceFirst("968", "") + "</SubscriberNumber>");
			if (loyaltyRegisteredNumberTabDTO.getAccountTypeId() == 14)
				sb.append("<SubscriberType>H</SubscriberType>");
			else if (loyaltyRegisteredNumberTabDTO.getAccountTypeId() == 9)
				sb.append("<SubscriberType>A</SubscriberType>");
			else if (loyaltyRegisteredNumberTabDTO.getAccountTypeId() == 200)
				sb.append("<SubscriberType>T</SubscriberType>");
			else if (loyaltyRegisteredNumberTabDTO.getAccountTypeId() == 201)
				sb.append("<SubscriberType>T</SubscriberType>");
			sb.append("<RequestTime>" + df1.format(new Date()) + "</RequestTime>");
			sb.append("</Request>");
			sb.append("</fis:getCustomerInformation>");
			sb.append("</soapenv:Body>");
			sb.append("</soapenv:Envelope>");

			requestXML = sb.toString();

			redeemPointsDTO = sendRequest(requestXML, redeemPointsDTO);

		} catch (Exception e) {
			e.printStackTrace();

		}
		return redeemPointsDTO;
	}

	public RedeemPointsDTO sendRequest(String xmlString, RedeemPointsDTO redeemPointsDTO) throws Exception {
		URL url = null;
		HttpURLConnection urlConn = null;
		OutputStreamWriter out = null;
		InputStream in = null;
		SAXBuilder builder = null;
		Document document = null;
		Element rootNode = null;
		BufferedReader reader = null;
		ElementFilter filter = null;
		IteratorIterable<Element> it = null;
		Element ele = null;
		StringBuilder sb = new StringBuilder();
		String xml = "";
		String respUrl = Cache.cacheMap.get("ENQUIRY_URL");

		try {
			logger.info("URL=" + Cache.cacheMap.get("ENQUIRY_URL") + "======FIRING XML>>>>>>" + xmlString);

			// logger.info("request="+xmlString);
			url = new URL(respUrl);
			urlConn = (HttpURLConnection) url.openConnection();

			urlConn.setRequestMethod("POST");
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.setConnectTimeout(10000);
			out = new OutputStreamWriter(urlConn.getOutputStream());
			long l1 = System.currentTimeMillis();
			out.write(xmlString);
			out.flush();

			in = urlConn.getInputStream();
			int i;
			while ((i = in.read()) != -1) {
				sb.append((char) i);
			}

			if (sb != null)
				xml = sb.toString();
			logger.info(" Response From URL == " + xml);

			builder = new SAXBuilder();
			document = (Document) builder.build(new StringReader(xml));
			rootNode = document.getRootElement();

			// -----------------------------------------------------------------

			logger.info("Credit ! Time taken  is " + (System.currentTimeMillis() - l1));

			filter = new ElementFilter("ReturnMsg");

			for (it = rootNode.getDescendants(filter); it.hasNext();) {
				ele = it.next();
				logger.info("Return Message coming is" + ele.getValue().trim());
				redeemPointsDTO.setReturnDesc(ele.getValue().trim());
				ele = null;
			}

			filter = new ElementFilter("ReturnCode");
			for (it = rootNode.getDescendants(filter); it.hasNext();) {
				ele = it.next();
				logger.info("Return Code coming is" + ele.getValue().trim());
				redeemPointsDTO.setReturnCode(ele.getValue().trim());

				ele = null;
			}
			logger.info("=====return code coming is" + redeemPointsDTO.getReturnCode());
			if (redeemPointsDTO.getReturnCode().equalsIgnoreCase("0000") || redeemPointsDTO.getReturnCode().equalsIgnoreCase("0")) {
				if (xml != null) {
					String data = xml.substring(xml.indexOf("<data>") + 6, xml.indexOf("</data>"));
					if (data != null) {

						data = data.replace("><", ":");
						String value[] = data.split(":");
						logger.info(data);
						logger.info(value[0] + " " + value[1]);

						String attrValue = value[0].substring(value[0].indexOf("attrValue=") + 11, value[0].length() - 2);
						String attrValue1 = value[1].substring(value[1].indexOf("attrValue=") + 11, value[1].length() - 3);
						logger.info("attribute value1" + attrValue);
						logger.info("attribute value1" + attrValue1);
						redeemPointsDTO.setCustType(attrValue);
						redeemPointsDTO.setCustAccountNumber(attrValue1);
					}
				}
			}
			// -----------------------------------------------------------------

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
					out.close();
					out = null;
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (urlConn != null) {
				try {
					urlConn.disconnect();
					urlConn = null;
				} catch (Exception e) {
				}
			}
			url = null;
			builder = null;
			document = null;
			rootNode = null;

		}
		return redeemPointsDTO;

		// return sb.toString();

	}

	public boolean updateLoyaltyProfileTable(Session session, Long loyaltyId, Double Points, RedeemPointsDTO redeemPointsDTO, LoyaltyProfileTabDTO loyaltyDTO) throws CommonException {
		Query query = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		boolean flag = false;
		try {
			String sql = " UPDATE " + tableInfoDAO.getLoyaltyProfileTable(loyaltyId.toString()) + " SET rewardPoints=rewardPoints-? , counter=?" + " WHERE loyaltyID=? AND counter=?";

			query = session.createQuery(sql);
			query.setParameter(0, Points);
			query.setParameter(1, (loyaltyDTO.getCounter() >= Integer.MAX_VALUE ? 1 : loyaltyDTO.getCounter() + 1));
			query.setParameter(2, loyaltyId);
			query.setParameter(3, loyaltyDTO.getCounter());

			long l1 = System.currentTimeMillis();
			int i = query.executeUpdate();
			logger.info("Time to update profile table == " + (System.currentTimeMillis() - l1));

			if (i > 0)
				flag = true;
			// throw new
			// CommonException(Cache.getServiceStatusMap().get("REDEEM_FAILURE_"+redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
		} finally {
			query = null;
			tableInfoDAO = null;
		}
		return flag;
	}

	public List<LoyaltyTransactionTabDTO> get10TransactionHistory(Session session, List<Long> allRelatedIds, int offset, int limit) {
		List<LoyaltyTransactionTabDTO> list = new ArrayList<LoyaltyTransactionTabDTO>();
		Query query = null;
		List<Object[]> objlist = null;
		LoyaltyTransactionTabDTO dto = null;
		Criteria ctr = null;
		Object allStatus[] = new Object[2];
		allStatus[0] = LoyaltyTransactionStatus.rewardsPointsAdded;
		allStatus[1] = LoyaltyTransactionStatus.rewardsPointsTransferredAdded;
		ProjectionList pList = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		HashMap<String, String> loyaltyTransactionMap=null;
		LoyalityCommonTransaction loyalityCommonTransaction=null;
		
 try{
	 	loyalityCommonTransaction=new LoyalityCommonTransaction();
		loyaltyTransactionMap=new HashMap<String, String>();
			for (Long id : allRelatedIds) {
				// String sql =
				// " SELECT LOYALTY_ID ,REWARD_POINTS,ID,CREATE_DATE FROM "+tableInfoDAO.getLoyaltyTransactionTable(id.toString())
				// +
				// " WHERE LOYALTY_ID=? AND STATUS_ID IN (?,?) AND REWARD_POINTS>0 ORDER BY ID ";
				ctr = session.createCriteria(tableInfoDAO.getLoyaltyTransactionTable(id.toString()));

				pList = Projections.projectionList();
				pList.add(Projections.property("loyaltyID"));
				pList.add(Projections.property("rewardPoints"));
				pList.add(Projections.property("id"));
				pList.add(Projections.property("createTime"));

				ctr.setProjection(pList);

				ctr.add(Restrictions.eq("loyaltyID", id));
				ctr.add(Restrictions.in("statusID", allStatus));
				ctr.add(Restrictions.sqlRestriction("(REWARD_POINTS-REDEEM_POINT)>0"));
				ctr.addOrder(Order.asc("id"));
				ctr.setFirstResult(offset);
				ctr.setMaxResults(limit);

				/*
				 * String sql
				 * ="SELECT loyaltyID,rewardPoints,id,createTime FROM "
				 * +tableInfoDAO.getLoyaltyTransactionTable(id.toString())+
				 * " WHERE loyaltyID=? AND statusID IN (?,?) AND rewardPoints-redeemPoint>0 ORDER BY id"
				 * ;
				 * 
				 * query = session.createQuery(sql); query.setParameter(0, id);
				 * query.setParameter(1,
				 * LoyaltyTransactionStatus.rewardsPointsAdded);
				 * query.setParameter(2,
				 * LoyaltyTransactionStatus.rewardsPointsTransferredAdded);
				 * query.setFirstResult(offset); query.setMaxResults(limit);
				 */
				// System.out.println("SOMESH == check == "+id);
				long l1 = System.currentTimeMillis();
				logger.info("Start time for history = " + l1);
				// objlist = query.list();
				objlist = ctr.list();
				logger.info("Total time = " + (System.currentTimeMillis() - l1));
				for (Object[] all : objlist) {
					dto = new LoyaltyTransactionTabDTO();
					/*dto.setLoyaltyID(Long.parseLong(all[0].toString()));
					dto.setRewardPoints(all[1] != null ? Double.parseDouble(all[1].toString()) : null);
					dto.setId(Long.parseLong(all[2].toString()));
					dto.setCreateTime((Date) all[3]);
					*/
		    		loyaltyTransactionMap.put(LoyalityTransactionConstants.loyaltyID, String.valueOf(Long.parseLong(all[0].toString())));
		    		loyaltyTransactionMap.put(LoyalityTransactionConstants.rewardPoints, String.valueOf(all[1] != null ? Double.parseDouble(all[1].toString()) : null));
		    		loyaltyTransactionMap.put(LoyalityTransactionConstants.id, String.valueOf(Long.parseLong(all[2].toString())));
		    		loyaltyTransactionMap.put(LoyalityTransactionConstants.createTime, String.valueOf((Date) all[3]));
		    		dto=loyalityCommonTransaction.loyaltyTransactionSetter(dto, loyaltyTransactionMap);
					
					list.add(dto);
				}

			}

			Collections.sort(list, new LoyaltyTransactionComparator());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			query = null;
			pList = null;
			ctr = null;
			tableInfoDAO = null;
		}

		// logger.info("Before returning size = "+list.size());
		return list;
	}

	public String updatePackageDetails(Session session, Long loyaltyId, PackageDetails pd, RedeemPointsDTO redeemPointsDTO, boolean isSindabadRedeem) throws CommonException {
		Query query = null;
		boolean flag = false;
		String orderId = null;
		LoyaltyRandomNumber loyaltyRandomNumber = new LoyaltyRandomNumber();
		boolean oldOrNewFlow = false;
		try {

			if (!isSindabadRedeem)
				oldOrNewFlow = loyaltyRandomNumber.getPackIdBoolean(pd.getPackageId());
			else
				oldOrNewFlow = false;

			if (oldOrNewFlow) {

				orderId = insertOrderDetails(session, loyaltyId, pd, redeemPointsDTO, oldOrNewFlow);

			} else if (pd.getQuantity() != null && pd.getQuantity() > 0 && pd.getExpiryDate() != null && pd.getExpiryDate().after(new Date())) {
				query = session.createQuery("UPDATE PackageDetails SET quantity=quantity-1 WHERE packageId=?");
				query.setParameter(0, pd.getPackageId());
				query.executeUpdate();
				if (isSindabadRedeem)
					return " ";

				orderId = insertOrderDetails(session, loyaltyId, pd, redeemPointsDTO, oldOrNewFlow);

				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			throw new CommonException(Cache.getServiceStatusMap().get("REDEEM_FAILURE_" + redeemPointsDTO.getDefaultLanguage()).getStatusDesc());

		} finally {
			query = null;
		}

		return orderId;
	}

	public String insertOrderDetails(Session session, Long loyaltyId, PackageDetails pd, RedeemPointsDTO redeemPointsDTO, boolean oldOrNewFlow) throws CommonException {
		// Query query = null;
		TableInfoDAO dao = new TableInfoDAO();
		LoyaltyRandomNumber orderNumber = new LoyaltyRandomNumber();
		String orderId = null;
		try {

			if (oldOrNewFlow) {
				orderId = orderNumber.getOrderNumberRedeem(redeemPointsDTO);
				if (orderId != null && !orderId.equalsIgnoreCase("")) {
					orderNumber.blockReddemPackage(orderId, redeemPointsDTO);
				} else {
					redeemPointsDTO.setStatusCode(Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_" + redeemPointsDTO.getDefaultLanguage()).getStatusCode());
					redeemPointsDTO.setStatusDesc(Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_" + redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
					throw new CommonException(Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_" + redeemPointsDTO.getDefaultLanguage()).getStatusCode(), Cache.getServiceStatusMap().get("VOUCHER_PROMO_EXHAUST_" + redeemPointsDTO.getDefaultLanguage()).getStatusDesc());

				}
			} else {

				orderId = orderNumber.getOrderNumber(15);
			}

			VoucherOrderDetailsDTO voucherOrdDTO = new VoucherOrderDetailsDTO();
			voucherOrdDTO.setLoyalityID(loyaltyId);
			voucherOrdDTO.setOrderId(orderId);
			voucherOrdDTO.setOrderDate(new Date());
			voucherOrdDTO.setVoucherNumber(pd.getPackageId() + "");
			voucherOrdDTO.setVoucherName(pd.getPackageName());
			voucherOrdDTO.setQuantity(1);
			voucherOrdDTO.setRedeemPoints(pd.getRedeemPoints());
			voucherOrdDTO.setSubscriberNumber(redeemPointsDTO.getSubscriberNumber());
			voucherOrdDTO.setArea(redeemPointsDTO.getArea());
			voucherOrdDTO.setLocation(redeemPointsDTO.getLocation());
			session.save(dao.getVoucherOrderTable(loyaltyId.toString()), voucherOrdDTO);

		} catch (HibernateException e) {
			if (e instanceof ConstraintViolationException) {
				// ConstraintViolationException
				// exception=(ConstraintViolationException)e;
				orderId = insertOrderDetails(session, loyaltyId, pd, redeemPointsDTO, oldOrNewFlow);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(Cache.getServiceStatusMap().get("REDEEM_FAILURE_" + redeemPointsDTO.getDefaultLanguage()).getStatusDesc());
		} finally {
			dao = null;
			orderNumber = null;
		}
		return orderId;
	}

	public void notification(String orderID, Long loyaltyId, PackageDetails pd, String subNum, int langID, String area, String location) // sajith
																																			// ks
																																			// modified
																																			// sts618
																																			// added
																																			// area
																																			// and
																																			// location
	{
		logger.info("--- sending notification ---");
		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();

		LoyaltyProfileTabDTO loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyId);
		boolean voucherFlag = false;

		if (pd.getQuantity() != null && pd.getQuantity() > 0 && pd.getExpiryDate() != null && pd.getExpiryDate().after(new Date()))
			voucherFlag = true;
		else
			voucherFlag = false;

		/*
		 * NotificationPanel panel = new NotificationPanel();
		 * panel.redeemNotification1(orderID, loyaltyProfileTabDTO, pd, voucherFlag,
		 * subNum, langID); // sajith
		 */																						// ks
																									// modified
																									// sts618
																									// added
																									// area
																									// and
																									// location

	}// notification

	private void notification(String orderId, Long loyaltyId, PackageDetails pd, int langID, Set<String> notificationNumberList, String noToSendConfirm, String area, String location, String lineNumber, boolean isSindabadRedeem) {

		TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(loyaltyId);
		loyaltyProfileTabDTO.setContactNumberSet(notificationNumberList);
		boolean voucherFlag = false;

		if (pd.getQuantity() != null && pd.getQuantity() > 0 && pd.getExpiryDate() != null && pd.getExpiryDate().after(new Date()))
			voucherFlag = true;
		else
			voucherFlag = false;
		logger.info("voucherFlag " + voucherFlag + " size " + loyaltyProfileTabDTO.getContactNumberSet().size());
		/*
		 * NotificationPanel panel = new NotificationPanel();
		 * panel.redeemNotificationList(orderId, loyaltyProfileTabDTO, pd, voucherFlag,
		 * langID, noToSendConfirm, area, location, lineNumber, isSindabadRedeem);
		 */
	}

	private void insertRevertLoyaltyDetails(RevertLoyalty revertLoyalty) {
		Session session = null;

		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.save(revertLoyalty);

			session.getTransaction().commit();
		} catch (Exception e) {
			if (session.getTransaction() != null)
				session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
				session = null;
				revertLoyalty = null;
			}
		}
	}

	public boolean isEligibleToRedeem(String loyaltyId, boolean voucherFlag) {
		Session session = null;
		Query query = null;
		List list = null;

		try {
			session = HiberanteUtil.getSessionFactory().openSession();

			if (voucherFlag) {
				query = session.createSQLQuery("SELECT LOYALTY_ID FROM REDEEM_ELIGIBLE WHERE LOYALTY_ID=?");
				query.setParameter(0, Long.parseLong(loyaltyId));
				query.setMaxResults(1);
			} else {
				query = session.createSQLQuery("SELECT SUBSCRIBER_NUMBER FROM REDEEM_ELIGIBLE WHERE SUBSCRIBER_NUMBER=?");
				query.setParameter(0, loyaltyId);
				query.setMaxResults(1);
			}
			list = query.list();
			if (list != null && list.size() > 0)
				return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return false;
	}

	private void callProvisioning(PackageDetails packageDetails, RedeemPointsDTO redeemPointsDTO, LoyaltyRegisteredNumberTabDTO loyaltyRegisteredNumberTabDTO, String callType, int isTestNumber, long parentLoyaltyId, LoyaltyProfileTabDTO loyaltyProfileTabDTO, double totalPoints, String noToSendConfirm, String subsNumber, String requestMDN, String contactMDN, GenericDTO genericDTO, boolean isPaymentBill) {
		if (packageDetails.getQuantity() == null) {
			RERequestHeader reRequestHeader = null;
			RERequestDataSet reRequestDataSet = null;
			ArrayList<ReResponseParameter> responseList = null;
			ReResponseParameter reResponseParam = null;
			boolean sychronizeFlag = false;
			// TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
			/*
			 * loyaltyRegisteredNumberTabDTO =
			 * tableDetailsDAO.getLoyaltyRegisteredNumberDetails(loyaltyId,
			 * subsNumber); String callType =
			 * Cache.getCacheMap().get("CALL_TYPE_"
			 * +loyaltyRegisteredNumberTabDTO
			 * .getAccountTypeId())!=null?Cache.getCacheMap
			 * ().get("CALL_TYPE_"+loyaltyRegisteredNumberTabDTO
			 * .getAccountTypeId()):"1";
			 */logger.info(redeemPointsDTO.getTransactionId() + " Call Type " + callType + " Register Number [" + loyaltyRegisteredNumberTabDTO.getAccountTypeId() + "]" + "pack id =" + packageDetails.getPackageId());

			reRequestHeader = new RERequestHeader();
			reRequestHeader.setRequestId(redeemPointsDTO.getTransactionId());
			reRequestHeader.setMsisdn(redeemPointsDTO.getSubscriberNumber());
			reRequestHeader.setTimeStamp(new Date() + "");
			reRequestHeader.setDataSet(reRequestDataSet);
			// reRequestHeader.setRewardPoint(redeemPoint+"");

			/*
			 * Provisioning can be of two types MO_ROUTER_Provision = 1
			 * LMS_Provision = 2 Call Type defines whether it is prepaid(1)
			 * postpaid(2)
			 */
			// logger.info(callType+" "+Cache.getCacheMap().get("PROVISIONING_MODULE"));
			responseList = new ArrayList<ReResponseParameter>();

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("TIER_ID");
			reResponseParam.setValue(String.valueOf(loyaltyProfileTabDTO.getTierId()));
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("ACC_TYPE_ID");
			reResponseParam.setValue(String.valueOf(loyaltyRegisteredNumberTabDTO.getAccountTypeId()));
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("PACK_ID");
			reResponseParam.setValue(String.valueOf((packageDetails.getPackageId())));
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("NOTIFICATION_NUMBER");
			reResponseParam.setValue(noToSendConfirm);
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("REQUEST_NOTIFICATION_NUMBER");
			reResponseParam.setValue(getModifiedNumber(requestMDN));
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("LINE_NOTIFICATION_NUMBER");
			reResponseParam.setValue(getModifiedNumber(subsNumber));
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("CONTACT_NUMBER");
			reResponseParam.setValue(getModifiedNumber(contactMDN));
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("SUBS_TYPE");
			reResponseParam.setValue(String.valueOf(loyaltyRegisteredNumberTabDTO.getAccountTypeId() == 201 ? 200 : loyaltyRegisteredNumberTabDTO.getAccountTypeId()));
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("LANGUAGE_ID");
			reResponseParam.setValue(redeemPointsDTO.getDefaultLanguage() + "");
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("RedeemPoints");
			reResponseParam.setValue(packageDetails.getRedeemPoints() + "");
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("LOYALTY_ID");
			reResponseParam.setValue(parentLoyaltyId + "");
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("CHANNEL");
			reResponseParam.setValue(redeemPointsDTO.getChannel());
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("TOTAL_POINTS");
			reResponseParam.setValue(String.valueOf(totalPoints));
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("TEST_NUMBER");
			reResponseParam.setValue(isTestNumber + "");
			responseList.add(reResponseParam);
			reResponseParam = null;

			reResponseParam = new ReResponseParameter();
			reResponseParam.setId("DialCode");
			if (packageDetails.getDialCode() != null)
				reResponseParam.setValue(packageDetails.getDialCode() + "");
			else
				reResponseParam.setValue("");
			responseList.add(reResponseParam);
			reResponseParam = null;
			if (isPaymentBill) {
				// Ajith added for BILL Discount
				reRequestHeader.setUrl(Cache.getCacheMap().get("BILL_DISCOUNT"));
				logger.info("CHANNEL COMING IS" + redeemPointsDTO.getChannel());
				logger.info("CHANNEL COMING IS" + Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()));
				if (Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()) != null && Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()).equalsIgnoreCase("SYNC")) {
					sychronizeFlag = true;
					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("ASYNC");
					reResponseParam.setValue("false");
					responseList.add(reResponseParam);
					reResponseParam = null;
				} else {
					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("ACTION_RESP_URL");
					reResponseParam.setValue(Cache.getCacheMap().get("PROVISION_RESP_URL"));
					responseList.add(reResponseParam);
					reResponseParam = null;
				}
				logger.info("FLAG FOR SYNC FLOW IS" + sychronizeFlag);
				reResponseParam = new ReResponseParameter();
				reResponseParam.setId("PackageID");
				reResponseParam.setValue(redeemPointsDTO.getPackageId() + "");
				responseList.add(reResponseParam);
				reResponseParam = null;

				reResponseParam = new ReResponseParameter();
				reResponseParam.setId("RespUrl");
				reResponseParam.setValue(Cache.getCacheMap().get("PROVISION_RESP_URL"));
				responseList.add(reResponseParam);
				reResponseParam = null;

				reResponseParam = new ReResponseParameter();
				reResponseParam.setId("PackageName");
				reResponseParam.setValue(packageDetails.getPackageName());
				responseList.add(reResponseParam);
				reResponseParam = null;

				reResponseParam = new ReResponseParameter();
				reResponseParam.setId("Points");
				reResponseParam.setValue(packageDetails.getRedeemPoints() + "");
				responseList.add(reResponseParam);
				reResponseParam = null;

				reResponseParam = new ReResponseParameter();
				reResponseParam.setId("ACCOUNT_NUMBER");
				reResponseParam.setValue(redeemPointsDTO.getCustAccountNumber());
				responseList.add(reResponseParam);
				reResponseParam = null;

				reResponseParam = new ReResponseParameter();
				reResponseParam.setId("ACCOUNT_TYPE");
				reResponseParam.setValue(redeemPointsDTO.getCustType());
				responseList.add(reResponseParam);
				reResponseParam = null;

				reResponseParam = new ReResponseParameter();
				reResponseParam.setId("CURRENCY_VALUE");
				reResponseParam.setValue(packageDetails.getCurrencyValue() + "");
				responseList.add(reResponseParam);
				reResponseParam = null;

				reResponseParam = new ReResponseParameter();
				reResponseParam.setId("UNIQUE_ID");
				reResponseParam.setValue(redeemPointsDTO.getUniqueId());
				responseList.add(reResponseParam);
				reResponseParam = null;

			} else if (Cache.getCacheMap().get("PROVISIONING_MODULE").equalsIgnoreCase("2")) {
				reRequestHeader.setUrl(Cache.getCacheMap().get("PROVISION_2_" + callType));
				if (callType.equalsIgnoreCase("1")) {
					if (packageDetails.getChargingType() != null && packageDetails.getChargingType().trim().equalsIgnoreCase("1")) {
						reRequestHeader.setUrl(Cache.getCacheMap().get("REDEEM_AIR"));

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("ClientReference");
						reResponseParam.setValue(System.currentTimeMillis() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("ASYNC");
						reResponseParam.setValue("true");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("Amount");
						reResponseParam.setValue(packageDetails.getChargeAmt() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("Units");
						reResponseParam.setValue(packageDetails.getChargeAmt() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("Currency");
						reResponseParam.setValue("OMR");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("ACTION_RESP_URL");
						reResponseParam.setValue(Cache.getCacheMap().get("PROVISION_RESP_URL"));
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("UNIQUE_ID");
						reResponseParam.setValue(redeemPointsDTO.getUniqueId());
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("Points");
						reResponseParam.setValue(packageDetails.getRedeemPoints() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("PackageName");
						reResponseParam.setValue(packageDetails.getPackageName());
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("OfferId");
						reResponseParam.setValue(redeemPointsDTO.getPackageId() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("OfferName");
						reResponseParam.setValue(packageDetails.getOfferName());
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("UseCarryForward");
						reResponseParam.setValue("true");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("ValidityDays");
						reResponseParam.setValue(packageDetails.getValidityDays() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("Units");
						reResponseParam.setValue(packageDetails.getUnits() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

					} else {
						logger.info("CHANNEL COMING IS" + redeemPointsDTO.getChannel());
						logger.info("CHANNEL COMING IS" + Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()));
						reRequestHeader.setUrl(Cache.getCacheMap().get("PROVISION_2_1"));
						if (Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()) != null && Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()).equalsIgnoreCase("SYNC")) {
							sychronizeFlag = true;
							reResponseParam = new ReResponseParameter();
							reResponseParam.setId("ASYNC");
							reResponseParam.setValue("false");
							responseList.add(reResponseParam);
							reResponseParam = null;
						} else {
							reResponseParam = new ReResponseParameter();
							reResponseParam.setId("ACTION_RESP_URL");
							reResponseParam.setValue(Cache.getCacheMap().get("PROVISION_RESP_URL"));
							responseList.add(reResponseParam);
							reResponseParam = null;
						}
						logger.info("FLAG FOR SYNC FLOW IS" + sychronizeFlag);

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("ClientReference");
						reResponseParam.setValue(System.currentTimeMillis() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("UseCarryForward");
						reResponseParam.setValue("true");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("OfferName");
						reResponseParam.setValue(packageDetails.getOfferName() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("ValidityDays");
						reResponseParam.setValue(packageDetails.getValidityDays() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("Units");
						reResponseParam.setValue(packageDetails.getUnits() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("OfferId");
						reResponseParam.setValue(redeemPointsDTO.getPackageId() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("PackageName");
						reResponseParam.setValue(packageDetails.getPackageName());
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("Points");
						reResponseParam.setValue(packageDetails.getRedeemPoints() + "");
						responseList.add(reResponseParam);
						reResponseParam = null;

						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("UNIQUE_ID");
						reResponseParam.setValue(redeemPointsDTO.getUniqueId());
						responseList.add(reResponseParam);
						reResponseParam = null;
					}

				} else {

					logger.info("CHANNEL COMING IS" + redeemPointsDTO.getChannel());
					logger.info("CHANNEL COMING IS" + Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()));
					if (Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()) != null && Cache.channelFlowDetails.get(redeemPointsDTO.getChannel()).equalsIgnoreCase("SYNC")) {
						sychronizeFlag = true;
						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("ASYNC");
						reResponseParam.setValue("false");
						responseList.add(reResponseParam);
						reResponseParam = null;
					} else {
						reResponseParam = new ReResponseParameter();
						reResponseParam.setId("ACTION_RESP_URL");
						reResponseParam.setValue(Cache.getCacheMap().get("PROVISION_RESP_URL"));
						responseList.add(reResponseParam);
						reResponseParam = null;
					}
					logger.info("FLAG FOR SYNC FLOW IS" + sychronizeFlag);
					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("PackageID");
					reResponseParam.setValue(redeemPointsDTO.getPackageId() + "");
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("RespUrl");
					reResponseParam.setValue(Cache.getCacheMap().get("PROVISION_RESP_URL"));
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("AccountNumber");
					reResponseParam.setValue(loyaltyRegisteredNumberTabDTO.getAccountNumber() + "");
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("PackageName");
					reResponseParam.setValue(packageDetails.getPackageName());
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("Points");
					reResponseParam.setValue(packageDetails.getRedeemPoints() + "");
					responseList.add(reResponseParam);
					reResponseParam = null;

					/*
					 * reResponseParam = new ReResponseParameter();
					 * reResponseParam.setId("ACTION_RESP_URL");
					 * reResponseParam.
					 * setValue(Cache.getCacheMap().get("PROVISION_RESP_URL"));
					 * responseList.add(reResponseParam); reResponseParam =null;
					 */

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("UNIQUE_ID");
					reResponseParam.setValue(redeemPointsDTO.getUniqueId());
					responseList.add(reResponseParam);
					reResponseParam = null;
				}
			} else {

				reRequestHeader.setUrl(Cache.getCacheMap().get("PROVISION_1_" + callType));
				logger.info(redeemPointsDTO.getTransactionId() + " Keyword to call " + packageDetails.getKeyword());
				reRequestHeader.setKeyWord(packageDetails.getKeyword());
				responseList = new ArrayList<ReResponseParameter>();
				if (callType.equalsIgnoreCase("1")) {
					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("PackageName");
					reResponseParam.setValue(packageDetails.getPackageName() + "");
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("RedeemPoints");
					reResponseParam.setValue(packageDetails.getRedeemPoints() + "");
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("ACTION_RESP_URL");
					reResponseParam.setValue(Cache.getCacheMap().get("PROVISION_RESP_URL"));
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("AccountNumber");
					reResponseParam.setValue(loyaltyRegisteredNumberTabDTO.getAccountNumber() + "");
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("CHARGE_INDICATOR");
					reResponseParam.setValue(packageDetails.getChargingIndicator());
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("CHARGING_TYPE");
					reResponseParam.setValue(packageDetails.getChargingType());
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					if (packageDetails.getCreditAmt() != null) {
						reResponseParam.setId("CREDIT_AMOUNT");
						reResponseParam.setValue(packageDetails.getCreditAmt() + "");

					} else if (packageDetails.getCreditUnits() != null) {
						reResponseParam.setId("FREE_UNITS");
						reResponseParam.setValue(packageDetails.getCreditAmt() + "");

					} else if (packageDetails.getChargeAmt() != null) {
						reResponseParam.setId("CHARGED_AMOUNT");
						reResponseParam.setValue(packageDetails.getCreditAmt() + "");
					}
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("DEDICATED_ACCOUNTID");
					reResponseParam.setValue(packageDetails.getDedAcntId());
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("CALLING_FLAG");
					reResponseParam.setValue("false");
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("UNIQUE_ID");
					reResponseParam.setValue(redeemPointsDTO.getUniqueId());
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("LOYALTY_ID");
					reResponseParam.setValue(loyaltyRegisteredNumberTabDTO.getLoyaltyID() + "");
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("ACC_TYPE_ID");
					reResponseParam.setValue(loyaltyRegisteredNumberTabDTO.getAccountTypeId() + "");
					responseList.add(reResponseParam);
					reResponseParam = null;

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("NOTIFICATION_NUMBER");
					reResponseParam.setValue(noToSendConfirm);
					responseList.add(reResponseParam);
					reResponseParam = null;
					/*
					 * reResponseParam = new ReResponseParameter();
					 * reResponseParam.setId("Redeem");
					 * reResponseParam.setValue(noToSendConfirm);
					 * responseList.add(reResponseParam); reResponseParam =null;
					 */

					reResponseParam = new ReResponseParameter();
					reResponseParam.setId("REDEMPTION_SUCCESS_CODE");
					reResponseParam.setValue("SC000");
					responseList.add(reResponseParam);
					reResponseParam = null;

				}

			}
			reRequestDataSet = new RERequestDataSet();
			reRequestHeader.setDataSet(reRequestDataSet);

			//reRequestDataSet.setResponseParam(responseList);

			if (sychronizeFlag) {
				ThirdPartyCall tpCall = new ThirdPartyCall();
				tpCall.makeProvisionCall(reRequestHeader, sychronizeFlag, genericDTO);
			} else {
				logger.info("Adding into Provisioning Pool for Transaction Id [" + redeemPointsDTO.getTransactionId() + "] for Redemption for Loyalty ID [" + parentLoyaltyId + "]");

				//ThreadInitiator.provCallPool.addTask(reRequestHeader);
			}
		}
	}

	public String getModifiedNumber(String number) {

		String subscriberCountryCode = Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
		int subscriberSize = Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
		String subscriberNumber = number;
		logger.info("checking the msisdn for valid length" + subscriberNumber + " " + subscriberSize + " " + (subscriberNumber != null && (subscriberNumber.length() == subscriberSize)) + " " + subscriberCountryCode);
		if (subscriberNumber != null && (subscriberNumber.length() == subscriberSize)) {
			subscriberNumber = (subscriberCountryCode != null ? subscriberCountryCode : "968") + "" + subscriberNumber;
		}
		return subscriberNumber;
	}

	public boolean getDataPackageRedemptionCheck(Date lastDate, String subscriberNumber, Integer packageID) throws CommonException {
		boolean flag = false;
		Session session = null;
		String sql = null;
		Query query = null;
		long count = 0;
		Object object = null;
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			sql = "select count(*) from LoyaltyRedeemDTO where subscriberNumber=:subsNumber and pakcageID =:packId and date >=:date ";
			query = session.createQuery(sql);
			query.setParameter("subsNumber", subscriberNumber);
			query.setParameter("packId", packageID);
			query.setParameter("date", lastDate);
			object = query.uniqueResult();
			count = (Long) object;
			logger.info(" Count Value is >>>>>" + count);
			if (count >= Long.parseLong(Cache.getCacheMap().get("DATA_PACK_REDEMPTION_COUNT"))) {

				logger.info("Count Value is More than the configured value ");
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			throw new CommonException("Error Occured While Condition Check for postpaid Data Pack Redumption");
		} finally {
			object = null;
			count = 0;
			sql = null;
			query = null;
			try {
				if (session != null) {
					session.close();
					session = null;
				}
			} catch (Exception e) {
			}
		}

		logger.info(" Flag Returned is >>>>>>>>>>" + flag);
		return flag;
	}

	public static void main(String[] args) {
		BufferedReader br = null;
		List<String> l = new ArrayList<String>();
		StringBuffer reverseLine = null;
		int j = 1;
		FileWriter fw = null;
		Document document = null;
		Element rootNode = null;
		BufferedReader reader = null;
		ElementFilter filter = null;
		IteratorIterable<Element> it = null;
		Element ele = null;
		StringBuilder sb = new StringBuilder();
		String xml = "";
		SAXBuilder builder = null;
		try {

			String resp = "<data><attributeId attrName=\"BillingSystemID\" attrValue=\"A\"/><attributeId attrName=\"CustAccountNumber\" attrValue=\"13383061\"/></data>";

			String data = resp.substring(resp.indexOf("<data>") + 6, resp.indexOf("</data>"));
			data = data.replace("><", ":");
			String value[] = data.split(":");
			// System.out.println(data);
			// System.out.println(value[0]+" "+value[1]);

			String attrValue = value[0].substring(value[0].indexOf("attrValue=") + 11, value[0].length() - 2);
			String attrValue1 = value[1].substring(value[1].indexOf("attrValue=") + 11, value[1].length() - 3);

			System.out.println("val : " + attrValue);

			// -----------------------------------------------------------------
			builder = new SAXBuilder();
			document = (Document) builder.build(new File("D:\\testFile.txt"));
			rootNode = document.getRootElement();

			filter = new ElementFilter("ReturnMsg");

			for (it = rootNode.getDescendants(filter); it.hasNext();) {
				ele = it.next();
				logger.info("Return Message coming is" + ele.getValue().trim());
				ele = null;
			}

			filter = new ElementFilter("ReturnCode");
			for (it = rootNode.getDescendants(filter); it.hasNext();) {
				ele = it.next();
				logger.info("Return Code coming is" + ele.getValue().trim());

				ele = null;
			}

			filter = new ElementFilter("attributeId");

			for (it = rootNode.getDescendants(filter); it.hasNext();) {
				ele = it.next();
				logger.info("Cust Type coming is" + ele.getValue());
				ele = null;
			}
			filter = new ElementFilter("attrValue");

			for (it = rootNode.getDescendants(filter); it.hasNext();) {
				ele = it.next();
				logger.info("Cust Account Number coming is" + ele.getValue().trim());
				ele = null;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public List<OfferMasterTabDto> getInterfaceIdOfferMaster(String offerId)
	{

		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Query query = null;
		List<OfferMasterTabDto> OfferMasterTabDto = null;
		try {
			tx = session.beginTransaction();
				query = session.createQuery("FROM OfferMasterTabDto where offerId=:offerId");
				query.setParameter("offerId", Integer.valueOf(offerId));
				query.setMaxResults(1);
				OfferMasterTabDto = query.list();
				logger.info("List of offerType " + OfferMasterTabDto.toString());
				tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return OfferMasterTabDto;

	}
	
	public OfferMasterTabDto getOfferDetails(int offerID)
			throws Exception {

		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Query query = null;
		List<OfferMasterTabDto> offerMasterTabDtos = null;

		try {
			tx = session.beginTransaction();
			query = session
					.createQuery("FROM OfferMasterTabDto where offerId=:offerID ");
			query.setParameter("offerID", offerID);
			/*query.setParameter("redeemLimit", 1);
			*/offerMasterTabDtos = query.list();
			
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if(session!=null)
			{
			session.close();
			}
		}
		if(offerMasterTabDtos.size()>0)
		return offerMasterTabDtos.get(0);
		else
			return null;

	}
	public Boolean updateOfferDetails(int offerID, int redeemlimit)
			throws Exception {

		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Query query = null;
		Boolean flag=false;
		try {
			tx = session.beginTransaction();
			logger.info("transaction started");
			query = session
					.createQuery("UPDATE OfferMasterTabDto SET redeemLimit=:redeemLimit WHERE offerId=:offerID");
			query.setParameter("offerID", offerID);
			query.setParameter("redeemLimit", redeemlimit-1);
			if(query.executeUpdate()>0){
				tx.commit();
				logger.info("*****COmmited*****");
				flag=true;
			}else{
				flag=false;
			}
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		
			return flag;

	}

	public boolean updateLoyaltyProfileRedeem(
			LoyaltyProfileTabDTO loyaltyProfileTabDTO,  Double points, Double tierPoints,
			Double bonusPoints) {
		logger.info("***** updateLoyaltyProfileRedeem*****");
		Session session=null;
		String hql=null;
		boolean flag=false;
		Transaction transaction=null;
		Query query =null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try{
		

		logger.info(">>>loyalty id>>>"+loyaltyProfileTabDTO.getLoyaltyID()+">>>points>>>"+points);
		session=HiberanteUtil.getSessionFactory().openSession();
		transaction = session.beginTransaction();
		//String profileTable = infoDAO.getLoyaltyProfileDBTable(loyaltyProfileTabDTO.getLoyaltyID()+"");
		hql="UPDATE LOYALTY_PROFILE_ENTITY_0 SET rewardPoints = :rewardPoints, counter=counter+1, tierPoints=:tierPoints,bonusPoints=:bonusPoints WHERE loyaltyID =:loyaltyId AND counter=:counter";
		query=session.createQuery(hql);
		query.setParameter("rewardPoints", (loyaltyProfileTabDTO.getRewardPoints()-points));
		query.setParameter("loyaltyId", loyaltyProfileTabDTO.getLoyaltyID());
		query.setParameter("counter", loyaltyProfileTabDTO.getCounter());
		query.setParameter("tierPoints", (loyaltyProfileTabDTO.getTierPoints()-tierPoints));
		query.setParameter("bonusPoints", (loyaltyProfileTabDTO.getBonusPoints()-bonusPoints));
		logger.info("*****Values Cming*****"+(loyaltyProfileTabDTO.getRewardPoints()-points));
		
		if(query.executeUpdate()>0){
			transaction.commit();
			logger.info("*****COmmited*****");
			flag=true;
		}else{
			flag=false;
		}
		}
		catch(HibernateException he)
		{
			he.printStackTrace();
			flag=false;
			if(transaction!=null){
				transaction.rollback();
			}
			throw he;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			throw e;
		}
		finally{
			if(session!=null && session.isOpen()){
				session.close();
				session=null;
			}
			
		}
		return flag;
	}
	
	
	public boolean updateLoyaltyProfilePoints(
			LoyaltyProfileTabDTO loyaltyProfileTabDTO,  long points, long tierPoints,
			long bonusPoints) {
		logger.info("***** updateLoyaltyProfileRedeem*****");
		Session session=null;
		String hql=null;
		boolean flag=false;
		Transaction transaction=null;
		Query query =null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		try{
		

		logger.info(">>>loyalty id>>>"+loyaltyProfileTabDTO.getLoyaltyID()+">>>points>>>"+points);
		session=HiberanteUtil.getSessionFactory().openSession();
		transaction = session.beginTransaction();
		//String profileTable = infoDAO.getLoyaltyProfileDBTable(loyaltyProfileTabDTO.getLoyaltyID()+"");
		hql="UPDATE LOYALTY_PROFILE_ENTITY_0 SET rewardPoints = :rewardPoints, counter=counter+1, tierPoints=:tierPoints,bonusPoints=:bonusPoints WHERE loyaltyID =:loyaltyId AND counter=:counter";
		query=session.createQuery(hql);
		query.setParameter("rewardPoints", (loyaltyProfileTabDTO.getRewardPoints()+points));
		query.setParameter("loyaltyId", loyaltyProfileTabDTO.getLoyaltyID());
		query.setParameter("counter", loyaltyProfileTabDTO.getCounter());
		query.setParameter("tierPoints", (loyaltyProfileTabDTO.getTierPoints()+tierPoints));
		query.setParameter("bonusPoints", (loyaltyProfileTabDTO.getBonusPoints()+bonusPoints));
		
		if(query.executeUpdate()>0){
			transaction.commit();
			logger.info("*****COmmited*****");
			flag=true;
		}else{
			flag=false;
		}
		}
		catch(HibernateException he)
		{
			he.printStackTrace();
			flag=false;
			if(transaction!=null){
				transaction.rollback();
			}
			throw he;
		}catch(Exception e){
			flag=false;
			e.printStackTrace();
			throw e;
		}
		finally{
			if(session!=null && session.isOpen()){
				session.close();
				session=null;
			}
			
		}
		return flag;
	}
	
	public HashMap<String,String> getOfferAdditionalInformation(OfferMasterTabDto offerMasterTabDto,RedeemPointsDTO redeemPointsDTO) {
		Session session = null;
		Transaction tx = null;
		Query query = null;
		HashMap<String,String> offerAdditionalInfoMap=null;
		try {
			long l1 = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			offerAdditionalInfoMap= new HashMap<String,String>();
			query = session.createQuery("select offerId,paramId, paramValue,categoryId FROM OfferAdditionalInfoDto where offerId=:offerId");
			query.setParameter("offerId",offerMasterTabDto.getOfferId());
			List<Object[]> list = query.list();
			for(Object[] objects:list) {
				if(objects[1].toString()!=null && objects[2].toString()!=null)
				{
					
					offerAdditionalInfoMap.put(objects[1].toString().trim(), objects[2].toString().trim());
				}
			}
			for (Entry<String, String> entry : offerAdditionalInfoMap.entrySet()) {
				logger.info("TransactionId "+redeemPointsDTO.getTransactionId()+" Additional info ParamId : " + entry.getKey() + " ParamValue : " + entry.getValue());
			}
			tx.commit();
			logger.debug("Total time to getVoucherAdditionalInformation = " + (System.currentTimeMillis() - l1));
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
		return offerAdditionalInfoMap;
	}
	
	public RedeemPointsDTO getCustomerProfileInfo(RedeemPointsDTO redeemPointsDTO) throws Exception {

		Session session = HiberanteUtil.getSessionFactory().openSession();
		Transaction tx = null;
		Query query = null;
		List<CustomerProfileTabDTO> customerProfileDto = null;
		try {
			tx = session.beginTransaction();
			query = session.createQuery(" FROM CustomerProfileTabDTO where msisdn=:msisdn");
			query.setParameter("msisdn", redeemPointsDTO.getMoNumber());
			customerProfileDto = query.list();
			logger.debug("List size of customerProfileInfo  " + customerProfileDto.size());
			if(customerProfileDto!=null && customerProfileDto.size()>0)
			{
				redeemPointsDTO.setCustomerProfileTabDTO(customerProfileDto.get(0));
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return redeemPointsDTO;

	}
}

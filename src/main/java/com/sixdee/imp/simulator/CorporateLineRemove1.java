package com.sixdee.imp.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import om.omantel.arbor.inquiryservices.AccountSummaryJB;
import om.omantel.arbor.inquiryservices.SubscriberInfoJB;
import om.omantel.arbor.inquiryservices.SubscriberInfoService;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.util.CRMException;

public class CorporateLineRemove1 {

	Logger logger = Logger.getLogger(CorporateLineRemove1.class);

	Map<Long, Set<String>> loyaltyLineMap = new HashMap<Long, Set<String>>();

	public void lineRemove(String number) {

		Set<String> linesList = null;
		LoyaltyProfileTabDTO crmProfileDTO = null;
		Map<Long, List<SubscriberNumberTabDTO>> resultList = new HashMap<Long, List<SubscriberNumberTabDTO>>();
		try {

			getAllLoyaltyIds(number);
			logger.info(loyaltyLineMap);

			// Loop the loyalty IDs

			for (Long loyaltyId : loyaltyLineMap.keySet()) {

				try {

					logger.info("******************************** " + loyaltyId + "  ***********************************************");
					// Loop the lines for perticular loyalty ID
					linesList = loyaltyLineMap.get(loyaltyId);

					double totalPointstoRemove = 0.0;

					boolean flag = false;
					for (String lineNumber : linesList) {

						crmProfileDTO = new LoyaltyProfileTabDTO();
						crmProfileDTO.setLoyaltyID(loyaltyId);
						//String templateID=getBasicSubscriberInfo(crmProfileDTO, lineNumber, "Tran_" + lineNumber);
						SubscriberNumberTabDTO subDTO = getBasicSubscriberInfo(crmProfileDTO, lineNumber, "Tran_" + lineNumber);
						double points = getPoints(subDTO);
						insertAllLinePoints(loyaltyId, subDTO.getAccountCategory(), subDTO.getSubscriberNumber() + "", points);

						if (subDTO != null && (subDTO.getAccountCategory() != null && (subDTO.getAccountCategory().equalsIgnoreCase("Fixed-SAHL") || subDTO.getAccountCategory().equalsIgnoreCase("Fixed-Service")))) {

						} else {
							flag = true;
							totalPointstoRemove = totalPointstoRemove + points;
							if (resultList.containsKey(loyaltyId)) {
								resultList.get(loyaltyId).add(subDTO);
							} else {
								List<SubscriberNumberTabDTO> l1 = new ArrayList<SubscriberNumberTabDTO>();
								l1.add(subDTO);
								resultList.put(loyaltyId, l1);
							}
						}

					}// Lines Loop

					logger.info("resultList  " + resultList);
					logger.info("totalPointstoRemove  " + totalPointstoRemove);

					Double totalLoyaltyPoints = getAllPoints(loyaltyId);

					if (totalLoyaltyPoints > totalPointstoRemove && flag) {
						insertInToTable(loyaltyId, totalLoyaltyPoints - totalPointstoRemove);
					}

				} catch (Exception e) {
					logger.info("Exception = ", e);
				} finally {
					linesList = null;
					crmProfileDTO = null;
					loyaltyLineMap.get(loyaltyId).clear();
				}

				logger.info("******************************** " + loyaltyId + "  *********************************************** END ");

			}// Loyalty Loop

			RemoveCorporateAccount1 deleteAPI = new RemoveCorporateAccount1();
			List<SubscriberNumberTabDTO> subsList = null;
			
			Set<Long> set = resultList.keySet();
			for(Long lid : set)
			{
				subsList = resultList.get(lid);
				for (SubscriberNumberTabDTO dto : subsList) {
					deleteAPI.deleteFromSubscriberTab(dto);
				}
			}
			
			
		} catch (Exception e) {
			logger.info("", e);
		} finally {
		}

	}// lineRemove

	@SuppressWarnings("unchecked")
	private List<Object[]> getAllLoyaltyIds(String number) {
		Session session = null;
		Query query = null;
		List<Object[]> loyaltyIds = null;

		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			String sql = "SELECT LOYALTY_ID,SUBSCRIBER_NUMBER FROM CORPORATE_LINES ";
			if (number != null)
				sql += "WHERE LOYALTY_ID=" + number;

			query = session.createSQLQuery(sql);
			//query.setMaxResults(500);

			loyaltyIds = query.list();

			if (loyaltyIds != null && loyaltyIds.size() > 0) {
				for (Object[] subscriberNumberTabDTO : loyaltyIds) {

					Long loyaltyId = Long.parseLong(subscriberNumberTabDTO[0] + "");
					String subsNum = subscriberNumberTabDTO[1] + "";
					if (loyaltyLineMap.get(loyaltyId) != null) {
						loyaltyLineMap.get(loyaltyId).add(subsNum);
					} else {
						Set<String> set = new HashSet<String>();
						set.add(subsNum);
						loyaltyLineMap.put(loyaltyId, set);
						set = null;
					}
				}

			}

		} catch (Exception e) {
			logger.error("Exception ", e);
		} finally {
			query = null;
			if (session != null)
				session.close();
		}

		return loyaltyIds;
	}

	private void insertInToTable(long loyaltyId, double points) {
		Session session = null;
		Query query = null;
		Transaction trx = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();

			query = session.createSQLQuery("INSERT INTO CORPORATE_LINES_POINTS (LOYALTY_ID,POINTS) VALUES (?,?)");
			query.setParameter(0, loyaltyId);
			query.setParameter(1, points);

			query.executeUpdate();

			trx.commit();
		} catch (Exception e) {
			if (trx != null)
				trx.rollback();
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
	}

	public SubscriberNumberTabDTO getBasicSubscriberInfo(LoyaltyProfileTabDTO loyaltyProfileTabDTO, String subscriberNumber, String transactionID) throws CRMException {

		SubscriberInfoService infoService = null;
		SubscriberInfoJB subscriberInfoJB = null;
		AccountSummaryJB accountInfo = null;
		List<String> statusList = null;
		//List<String> accountCategoryList=null;
		SubscriberNumberTabDTO subscriberNumberTabDTO = new SubscriberNumberTabDTO();
		subscriberNumberTabDTO.setSubscriberNumber(Long.parseLong(subscriberNumber));
		subscriberNumberTabDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
		try {

			logger.info(transactionID + " : getBasicSubscriberInfo " + Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));

			infoService = new SubscriberInfoService(Cache.getCacheMap().get("CRM_CALL_BASIC_URL"));

			subscriberInfoJB = infoService.getSubscriberInfo("C1_" + transactionID, subscriberNumber);

			logger.info(transactionID + " : Basic Profile Return Code " + subscriberInfoJB.getReturnCode());

			if (subscriberInfoJB.getReturnCode() == null || !subscriberInfoJB.getReturnCode().equalsIgnoreCase("0000") || subscriberInfoJB.getCustAccountNumber() == null || subscriberInfoJB.getCustAccountNumber().trim().equals("")) {
				accountInfo = infoService.getAccountInfo("C2_" + transactionID, subscriberNumber);
				logger.info(transactionID + " : Account Return Code  " + accountInfo.getReturnCode());
				if (accountInfo.getReturnCode() == null || !accountInfo.getReturnCode().equalsIgnoreCase("0000"))
					return subscriberNumberTabDTO;

				loyaltyProfileTabDTO.setAccountNumber(subscriberNumber);
			}

			if (subscriberInfoJB.getReturnCode() != null && subscriberInfoJB.getReturnCode().equalsIgnoreCase("0000") && subscriberInfoJB.getCustAccountNumber() != null && !subscriberInfoJB.getCustAccountNumber().trim().equalsIgnoreCase("")) {
				loyaltyProfileTabDTO.setFirstName(subscriberInfoJB.getCustName());
				loyaltyProfileTabDTO.setAccountNumber(subscriberInfoJB.getCustAccountNumber());
				accountInfo = infoService.getAccountInfo("C2_" + transactionID, subscriberInfoJB.getCustAccountNumber());
			}

			if (accountInfo == null)
				return subscriberNumberTabDTO;

			logger.info(transactionID + " : Account Return Code  " + accountInfo.getReturnCode());
			if (accountInfo.getReturnCode() != null && accountInfo.getReturnCode().equalsIgnoreCase("0000")) {
				logger.info(transactionID + " : Cust Number " + accountInfo.getCustName());
				logger.info(transactionID + " : subscriber status  " + accountInfo.getCustStatus());
				logger.info(transactionID + " :  HVC Flag  " + accountInfo.getHvcFlag());
				logger.info(transactionID + " :  AcctCategory  " + accountInfo.getAcctCategory());
				logger.info(transactionID + " :  Line List " + accountInfo.getSiList());
				logger.info(transactionID + " :  Cust ID List " + accountInfo.getCustIdList());

				loyaltyProfileTabDTO.setFirstName(accountInfo.getCustName());

				/*if (accountInfo.getCustStatus() == null || !accountInfo.getCustStatus().trim().equalsIgnoreCase("ACTIVE")) {
					logger.info(" Subscriber Number : " + subscriberNumber + " is not active status ");
					return subscriberNumberTabDTO;
				}
*/
				if (Cache.getCacheMap().get("STATUS") != null) {
					if (Cache.getCacheMap().get("STATUS").contains(","))
						statusList = Arrays.asList(Cache.getCacheMap().get("STATUS").split(","));
					else {
						statusList = new ArrayList<String>();
						statusList.add(Cache.getCacheMap().get("STATUS"));
					}
				}

				logger.info(transactionID + " : Configre Status List " + statusList);

				/*if(accountInfo.getCustStatus()==null||!statusList.contains(accountInfo.getCustStatus().trim()))
				 {
				 logger.info(transactionID+" : Subscriber Number : "+subscriberNumber+ " is not in configured Status List "+statusList);
				 return NotificationTokens.createLoyalty_loyalty_StausType_id;
				 }*/

				/*List<String> hvcFlagList=null;
				 if(Cache.getCacheMap().get("HVC_FLAG")!=null)
				 {
				 if(Cache.getCacheMap().get("HVC_FLAG").contains(","))
				 hvcFlagList=Arrays.asList(Cache.getCacheMap().get("HVC_FLAG").split(","));
				 else
				 {
				 hvcFlagList=new ArrayList<String>();
				 hvcFlagList.add(Cache.getCacheMap().get("HVC_FLAG"));
				 }
				 }
				 
				 logger.info("Configre HVC Flag "+hvcFlagList);
				 
				 if(accountInfo.getHvcFlag()==null||!hvcFlagList.contains(accountInfo.getHvcFlag().trim()))
				 {
				 logger.info(" Subscriber Number : "+subscriberNumber+ " is not in configured HVC Flag List "+hvcFlagList);
				 return;
				 }*/

				/*if(Cache.getCacheMap().get("ACCOUNT_CATEGORY")!=null)
				 {
				 if(Cache.getCacheMap().get("ACCOUNT_CATEGORY").contains(","))
				 accountCategoryList=Arrays.asList(Cache.getCacheMap().get("ACCOUNT_CATEGORY").split(","));
				 else
				 {
				 accountCategoryList=new ArrayList<String>();
				 accountCategoryList.add(Cache.getCacheMap().get("ACCOUNT_CATEGORY"));
				 }
				 }*/

				logger.info(transactionID + ": Configured Account Category " + Cache.accountCategoryList);

				loyaltyProfileTabDTO.setDefaultLanguage(accountInfo.getLanguageCode());
				loyaltyProfileTabDTO.setCategory(accountInfo.getAcctCategory());
				loyaltyProfileTabDTO.setGender(accountInfo.getCustGender());

				/*if(Cache.accountCategoryList==null || accountInfo.getAcctCategory()==null || !Cache.accountCategoryList.contains(accountInfo.getAcctCategory().trim()))
				 {
				 logger.info(transactionID+" : Subscriber Number : "+subscriberNumber+ " is not in configured Account Category List "+Cache.accountCategoryList);
				 return NotificationTokens.createLoyalty_loyalty_AccCategory_id;
				 }*/

				subscriberNumberTabDTO.setAccountCategory(accountInfo.getAcctCategory());
				subscriberNumberTabDTO.setAccountNumber(loyaltyProfileTabDTO.getAccountNumber());
				//	subscriberNumberTabDTO.setSubscriberNumber(subscriberNumber)
				subscriberNumberTabDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
				//subscriberNumberTabDTO.setAccountTypeId(accountTypeId)

				/*							if(Cache.getCacheMap().get("CUST_ID_TYPE")!=null)
				 {
				 if(Cache.getCacheMap().get("CUST_ID_TYPE").contains(","))
				 custIDTypeList=Arrays.asList(Cache.getCacheMap().get("CUST_ID_TYPE").split(","));
				 else
				 {
				 custIDTypeList=new ArrayList<String>();
				 custIDTypeList.add(Cache.getCacheMap().get("CUST_ID_TYPE"));
				 }
				 }
				 
				 logger.info(transactionID+" : Configre Cust ID List "+custIDTypeList);
				 
				 List<CustIdJB> custIdInfos= accountInfo.getCustIdList();
				 List<NationalNumberTabDTO> list=new ArrayList<NationalNumberTabDTO>();
				 
				 if(custIdInfos!=null&&custIDTypeList!=null)
				 {
				 for(CustIdJB custIdInfo:custIdInfos)
				 {
				 logger.info(transactionID+" :  Cust ID  "+custIdInfo.getCustID());
				 logger.info(transactionID+" :  Cust ID Type  :"+custIdInfo.getCustIdType()+":");
				 
				 if(custIDTypeList.contains(custIdInfo.getCustIdType().trim()))
				 {
				 logger.info(transactionID+" : Consider "+custIdInfo.getCustIdType());
				 NationalNumberTabDTO nationalNumberTabDTO=new NationalNumberTabDTO();
				 String nationalId=custIdInfo.getCustID().trim();
				 nationalId=nationalId.replaceFirst("^0+(?!$)", "");
				 
				 nationalNumberTabDTO.setNationalNumber(nationalId);
				 nationalNumberTabDTO.setIdType(custIdInfo.getCustIdType().trim());
				 
				 if(!list.contains(nationalNumberTabDTO))
				 list.add(nationalNumberTabDTO);
				 }
				 logger.info(transactionID+" :  FInal List "+list);
				 
				 }
				 }
				 
				 loyaltyProfileTabDTO.setCustIdList(list);
				 list=null;
				 custIdInfos=null;
				 
				 
				 
				 if(loyaltyProfileTabDTO.getCustIdList()==null||loyaltyProfileTabDTO.getCustIdList().size()<=0)
				 {
				 return NotificationTokens.createLoyalty_loyalty_CustType_id;
				 }
				 */

			}

			/*GetBasicSubscriberInfoRequest basicSubscriberInfoRequest=new GetBasicSubscriberInfoRequest();
			 basicSubscriberInfoRequest.setReferenceNum(new CommonUtil().getTransaction());
			 basicSubscriberInfoRequest.setMsisdn(subscriberNumber);
			 
			 InquiryServiceMsgSetSOAP_HTTP_Service inquiryServiceMsgSetSOAP_HTTP_Service=new InquiryServiceMsgSetSOAP_HTTP_ServiceLocator();
			 
			 InquiryServiceMsgSetPortType msgSetPortType= inquiryServiceMsgSetSOAP_HTTP_Service.getSOAP_HTTP_Port(new URL(Cache.getCacheMap().get("CRM_CALL_BASIC_URL")));
			 
			 
			 _XML_XML1_Request request=new _XML_XML1_Request();
			 
			 request.setGetBasicSubscriberInfoRequest(basicSubscriberInfoRequest);
			 
			 _XML_XML1_OM_EAI_MESSAGE_REQUEST _xml_xml1_om_eai_message_request=new _XML_XML1_OM_EAI_MESSAGE_REQUEST();
			 
			 _XML_XML1_OM_EAI_HEADER _xml_xml1_om_eai_header=new _XML_XML1_OM_EAI_HEADER();
			 
			 _xml_xml1_om_eai_header.setMsgFormat("GETBASICSUBSCRIBERINFO.SERVICE");
			 _xml_xml1_om_eai_header.setMsgVersion("0000");
			 _xml_xml1_om_eai_header.setRequestorId("OM");
			 _xml_xml1_om_eai_header.setRequestorChannelId("IP"); // Our IP
			 _xml_xml1_om_eai_header.setRequestorUserId("wbimbtst");
			 _xml_xml1_om_eai_header.setRequestorLanguage("E");
			 _xml_xml1_om_eai_header.setRequestorSecurityInfo("");
			 _xml_xml1_om_eai_header.setEaiReference("0");
			 _xml_xml1_om_eai_header.setReturnCode("");
			 
			 _xml_xml1_om_eai_message_request.setOM_EAI_HEADER(_xml_xml1_om_eai_header);
			 
			 

			 _xml_xml1_om_eai_message_request.setRequest(request);
			 
			 
			 _XML_XML1_OM_EAI_MESSAGE_REPLY _xml_xml1_om_eai_message_reply=msgSetPortType.inquiryService(_xml_xml1_om_eai_message_request);
			 
			 GetBasicSubscriberInfoResponse basicSubscriberInfoResponse= _xml_xml1_om_eai_message_reply.getReply().getGetBasicSubscriberInfoResponse();
			 
			 if(basicSubscriberInfoResponse.getReturnCode()!=null&&basicSubscriberInfoResponse.getReturnCode().equalsIgnoreCase("0000"))
			 {
			 loyaltyProfileTabDTO.setFirstName(basicSubscriberInfoResponse.getCustName());
			 loyaltyProfileTabDTO.setAccountNumber(basicSubscriberInfoResponse.getCustAccountNumber());
			 basicSubscriberInfoResponse.getCustCompany();
			 }
			 */

		} catch (Exception e) {
			logger.info(transactionID + " : ", e);
			throw new CRMException("CRM_1000", e.getLocalizedMessage());
		} finally {
			infoService = null;
			subscriberInfoJB = null;
			accountInfo = null;
			statusList = null;
			//accountCategoryList=null;
		}

		//	return retrunNotificationTemplate;
		return subscriberNumberTabDTO;

	}//getBasicSubscriberInfo

	private double getPoints(SubscriberNumberTabDTO subscriberNumberTabDTO) {
		Session session = null;
		TableInfoDAO dao = new TableInfoDAO();
		Criteria ctr = null;
		Double points = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			ctr = session.createCriteria(dao.getSubscriberNumberTable(subscriberNumberTabDTO.getSubscriberNumber() + ""));
			ctr.add(Restrictions.eq("loyaltyID", subscriberNumberTabDTO.getLoyaltyID()));
			ctr.add(Restrictions.eq("subscriberNumber", subscriberNumberTabDTO.getSubscriberNumber()));

			ctr.setProjection(Projections.property("points"));

			points = (Double) ctr.uniqueResult();

		} catch (Exception e) {
			logger.error("Exception while getting points", e);
		} finally {
			if (session != null)
				session.close();
		}

		return points;
	}

	public Double getAllPoints(Long LoyaltyId) {
		Session session = null;
		;
		Double Points = null;
		Criteria ctr = null;
		TableInfoDAO tableInfoDAO = new TableInfoDAO();
		try {
			session = HiberanteUtil.getSessionFactory().openSession();

			ctr = session.createCriteria(tableInfoDAO.getLoyaltyProfileTable(LoyaltyId.toString()));

			ctr.add(Restrictions.eq("loyaltyID", LoyaltyId));
			ctr.setProjection(Projections.property("rewardPoints"));
			Points = (Double) ctr.uniqueResult();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
			ctr = null;
			tableInfoDAO = null;
		}
		return Points;
	}

	public void insertAllLinePoints(Long loyaltyId, String category, String subscriberNum, double points) {
		Session session = null;
		Query query = null;
		try {
			session = HiberanteUtil.getSessionFactory().openSession();
			session.beginTransaction();
			query = session.createSQLQuery("INSERT INTO CORPORATE_LOYALTY_AUDIT(LOYALTY_ID,LINE,CATEGORY_NAME,POINTS) VALUES (?,?,?,?)");

			query.setParameter(0, loyaltyId);
			query.setParameter(1, subscriberNum);
			query.setParameter(2, category);
			query.setParameter(3, points);
			query.executeUpdate();
			session.getTransaction().commit();

		} catch (Exception e) {
			session.getTransaction().rollback();
			logger.error("Exception while insertion loyalty id =" + loyaltyId + " and subs number = " + subscriberNum);
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
	}

}
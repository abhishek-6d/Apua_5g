package com.sixdee.imp.dao;

/**
 * 
 * @author Paramesh
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
 * <td>April 24,2013 04:37:12 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.NotificationModule.NotificationTokens;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.ConfigureParameterDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRedeemDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.TierAndBonusPointDetailsDTO;
import com.sixdee.imp.dto.TransferPointsDTO;
import com.sixdee.imp.service.ReServices.DAO.TierAndBonusCalculationDAO;
import com.sixdee.lms.dto.CDRInformationDTO;
import com.sixdee.lms.util.selections.CDRCommandID;

public class TransferPointsDAO {

	Logger logger=Logger.getLogger(TransferPointsDAO.class);
	
	
	public boolean transferPoints(GenericDTO genericDTO)
	{
		TransferPointsDTO pointsDTO=(TransferPointsDTO)genericDTO.getObj();
		TableDetailsDAO tableDetailsDAO=new TableDetailsDAO();
		Session session=null;
		Transaction transaction=null;
		TableInfoDAO infoDAO=null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO=null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO1=null;
		CommonUtil commonUtil=null;
		LanguageDAO languageDAO=null;
		LoyaltyRedeemDTO loyaltyRedeemDTO=null;
		CDRInformationDTO cdrInformationDTO =null;
		String fromLoyaltyLanguage="1";
		String toLoyaltyLanguage=Cache.defaultLanguageID;
		String logFile = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		TierAndBonusCalculationDAO tierAndBonusCalculationDAO = new TierAndBonusCalculationDAO();
		TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO = null;
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			/*languageDAO=new LanguageDAO();
			toLoyaltyLanguage=languageDAO.getLanguageID(pointsDTO.getDestinationSubscriberNumber());
			*/
			commonUtil=new CommonUtil();
			infoDAO=new TableInfoDAO();
			session=HiberanteUtil.getSessionFactory().openSession();
			logFile = "" + pointsDTO.getTransactionId() + "|" + dateFormat.format(new Date()) + "|" + "5" + "|" + pointsDTO.getChannel() + "|" ;

			int loopCounter=0;
			while(loopCounter<10)
			{
				pointsDTO.setFromLoyaltyId(getLoyaltyId(pointsDTO.getSubscriberNumber()));
				pointsDTO.setToLoyaltyId(getLoyaltyId(pointsDTO.getDestinationSubscriberNumber()));
				
				logFile += pointsDTO.getFromLoyaltyId() + "|" + pointsDTO.getSubscriberNumber() + "|" ;
				if(pointsDTO.getFromLoyaltyId()==null)
				{
					throw new CommonException(Cache.getServiceStatusMap().get("SUB_INVALID_"+fromLoyaltyLanguage).getStatusCode(),commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_"+fromLoyaltyLanguage).getStatusDesc(),new String[]{NotificationTokens.subscriberNumber}, new String[]{pointsDTO.getSubscriberNumber()}));
				}
				
				if(pointsDTO.getToLoyaltyId()==null)
				{
					throw new CommonException(Cache.getServiceStatusMap().get("TRAN_POINTS_DEST_SUB_INVALID_"+fromLoyaltyLanguage).getStatusCode(),commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("TRAN_POINTS_DEST_SUB_INVALID_"+fromLoyaltyLanguage).getStatusDesc(),new String[]{NotificationTokens.subscriberNumber}, new String[]{pointsDTO.getDestinationSubscriberNumber()}));
				}
				
		
				transaction=session.beginTransaction();
			    
			    loyaltyProfileTabDTO=tableDetailsDAO.getLoyaltyProfileDetails(pointsDTO.getFromLoyaltyId());
			    loyaltyProfileTabDTO1=tableDetailsDAO.getLoyaltyProfileDetails(pointsDTO.getToLoyaltyId());
			    
			    if(loyaltyProfileTabDTO==null||loyaltyProfileTabDTO1==null)
				{
					logger.info("Invalid Loyalty IDs");
					throw new CommonException(Cache.getServiceStatusMap().get("SUB_INVALID_"+fromLoyaltyLanguage).getStatusCode(),commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("SUB_INVALID_"+fromLoyaltyLanguage).getStatusDesc(),new String[]{NotificationTokens.subscriberNumber}, new String[]{(pointsDTO.getFromLoyaltyId()==null?pointsDTO.getSubscriberNumber():pointsDTO.getDestinationSubscriberNumber())}));
				}
			    
			    /*fromLoyaltyLanguage=(loyaltyProfileTabDTO.getDefaultLanguage()!=null?loyaltyProfileTabDTO.getDefaultLanguage():fromLoyaltyLanguage);
			    toLoyaltyLanguage=(loyaltyProfileTabDTO1.getDefaultLanguage()!=null?loyaltyProfileTabDTO1.getDefaultLanguage():toLoyaltyLanguage);*/
			    
				
				if(pointsDTO.getFromLoyaltyId().equals(pointsDTO.getToLoyaltyId()))
				{
					
					throw new CommonException(Cache.getServiceStatusMap().get("TRAN_POINT_SAME_ACC_"+fromLoyaltyLanguage).getStatusCode(),Cache.getServiceStatusMap().get("TRAN_POINT_SAME_ACC_"+fromLoyaltyLanguage).getStatusDesc());
				}
			    
			    
				
				
				
				if(pointsDTO.getChannel().equalsIgnoreCase("SMS"))
				{
					if(!loyaltyProfileTabDTO.getPin().equals(pointsDTO.getPin().toString()))
					{
						logger.info("Subscriber Number Authentication Failed Subscriber Number : "+pointsDTO.getSubscriberNumber());
						throw new CommonException(Cache.getServiceStatusMap().get("SUB_AUTH_FAIL_"+fromLoyaltyLanguage).getStatusCode(),commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("SUB_AUTH_FAIL_"+fromLoyaltyLanguage).getStatusDesc(),new String[]{NotificationTokens.subscriberNumber}, new String[]{pointsDTO.getSubscriberNumber()}));
					}
				}
				
				
				if(loyaltyProfileTabDTO.getStatusID()!=1||loyaltyProfileTabDTO1.getStatusID()!=1)
				{
					logger.info("Loyalty IDs are not active states ");
					//logger.info("Loyalty ID : "+loyaltyProfileTabDTO.getLoyaltyID()+"  Status : "+Cache.getStatusMap().get(loyaltyProfileTabDTO.getStatusID()) );
					//logger.info("Loyalty ID : "+loyaltyProfileTabDTO1.getLoyaltyID()+"  Status : "+Cache.getStatusMap().get(loyaltyProfileTabDTO1.getStatusID()) );
					if(loyaltyProfileTabDTO.getStatusID()!=1)
					{
					   throw new CommonException(Cache.getServiceStatusMap().get("TRAN_POINT_LOYALTY_STATE_"+fromLoyaltyLanguage).getStatusCode(),
							  Cache.getServiceStatusMap().get("TRAN_POINT_LOYALTY_STATE_"+fromLoyaltyLanguage).getStatusDesc());
					   //throw new CommonException("Loyalty ID : "+loyaltyProfileTabDTO.getLoyaltyID()+"  Status : "+Cache.getStatusMap().get(loyaltyProfileTabDTO.getStatusID())+" InActive state");
					}
					if(loyaltyProfileTabDTO1.getStatusID()!=1)
						throw new CommonException(Cache.getServiceStatusMap().get("TRAN_POINT_LOYALTY_STATE_"+fromLoyaltyLanguage).getStatusCode(),
								  Cache.getServiceStatusMap().get("TRAN_POINT_LOYALTY_STATE_"+fromLoyaltyLanguage).getStatusDesc());
						  	}
			    
			    
			    System.out.println(loopCounter+"                        "+loyaltyProfileTabDTO.getRewardPoints()+"  "+loyaltyProfileTabDTO.getCounter());
			    System.out.println(loopCounter+"                        "+loyaltyProfileTabDTO1.getRewardPoints()+"  "+loyaltyProfileTabDTO1.getCounter());
			    
				if(loyaltyProfileTabDTO.getRewardPoints()>=pointsDTO.getTransferPoints())
				{
							 
					
					if(updateLoyaltyProfile(loyaltyProfileTabDTO,"-"+pointsDTO.getTransferPoints())){
						if(updateLoyaltyProfile(loyaltyProfileTabDTO1,pointsDTO.getTransferPoints()+"")){
							LoyaltyTransactionTabDTO loyaltyTransactionTabDTO=new LoyaltyTransactionTabDTO();
						loyaltyTransactionTabDTO.setSubscriberNumber(""+pointsDTO.getSubscriberNumber());
						loyaltyTransactionTabDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
						//loyaltyTransactionTabDTO.setDestLoyaltyID(loyaltyProfileTabDTO1.getLoyaltyID());
						loyaltyTransactionTabDTO.setPreRewardPoints(loyaltyProfileTabDTO.getRewardPoints());
						loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyProfileTabDTO.getRewardPoints()-pointsDTO.getTransferPoints());
						loyaltyTransactionTabDTO.setRewardPoints(pointsDTO.getTransferPoints().doubleValue());
						loyaltyTransactionTabDTO.setPointType(0);
						//loyaltyProfileTabDTO.setRewardPoints(loyaltyProfileTabDTO.getRewardPoints()-pointsDTO.getTransferPoints());
						loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.rewardsPointsTransferred);
						loyaltyTransactionTabDTO.setStatusDesc("Reward points transfered");
						
						tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);
						
						//new RegisterAcctDAO().insertLoyaltyTransactionDetails(session,loyaltyTransactionTabDTO);
						
						loyaltyTransactionTabDTO=new LoyaltyTransactionTabDTO();
						loyaltyTransactionTabDTO.setSubscriberNumber(""+pointsDTO.getDestinationSubscriberNumber());
						loyaltyTransactionTabDTO.setLoyaltyID(loyaltyProfileTabDTO1.getLoyaltyID());
						loyaltyTransactionTabDTO.setPreRewardPoints(loyaltyProfileTabDTO1.getRewardPoints());
						loyaltyTransactionTabDTO.setCurRewardPoints(loyaltyProfileTabDTO1.getRewardPoints()+pointsDTO.getTransferPoints());
						loyaltyTransactionTabDTO.setRewardPoints(pointsDTO.getTransferPoints().doubleValue());
						//loyaltyProfileTabDTO1.setRewardPoints(loyaltyProfileTabDTO1.getRewardPoints()+pointsDTO.getTransferPoints());
						loyaltyTransactionTabDTO.setPointType(0);
						loyaltyTransactionTabDTO.setStatusID(LoyaltyTransactionStatus.rewardsPointsTransferredAdded);
						loyaltyTransactionTabDTO.setStatusDesc("Points added ");
						
						tierAndBonusCalculationDAO.inserTransaction(loyaltyTransactionTabDTO);
						
						Calendar c = Calendar.getInstance();
					    ConfigureParameterDTO configParmeterDTO = Cache.configParameterMap.get("TIER_POINTS_VALIDITY_DAYS");
				        if(configParmeterDTO!=null && configParmeterDTO.getParameterType()!=null && !configParmeterDTO.getParameterValue().equalsIgnoreCase("")) 
				        	c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(configParmeterDTO.getParameterValue()));  // number of days to add
				       Date expiryDate = c.getTime();
				      String   exp = sdf.format(expiryDate);
				       logger.info(">>>exp>>"+exp);
				       logger.info(">>>exp>>"+exp);
				       expiryDate = sdf.parse(exp);
				        String expiryQuarter = ((c.get(Calendar.MONTH)/3)+1)+""+c.get(Calendar.YEAR)+"";
				       logger.info("EXPIRY date and quarter :: "+expiryDate+" :: "+expiryQuarter);		 
						
				       
				       tierAndBonusPointDetailsDTO = tierAndBonusCalculationDAO.checkInTableFortierAndBonusPointDetails(expiryDate,pointsDTO.getDestinationSubscriberNumber(),loyaltyProfileTabDTO1.getLoyaltyID());
						
						if(tierAndBonusPointDetailsDTO!=null){
							
								tierAndBonusPointDetailsDTO.setTierCreateDate(new Date());
								tierAndBonusPointDetailsDTO.setTierPoints(tierAndBonusPointDetailsDTO.getTierPoints()+pointsDTO.getTransferPoints().doubleValue());
								tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTierPoints()+tierAndBonusPointDetailsDTO.getBonusPoints());
							
							tierAndBonusCalculationDAO.updateTierAndBonusDEtails(tierAndBonusPointDetailsDTO);
								
						}else{
						tierAndBonusPointDetailsDTO = new TierAndBonusPointDetailsDTO();
						tierAndBonusPointDetailsDTO.setMsisdn(pointsDTO.getDestinationSubscriberNumber());
						tierAndBonusPointDetailsDTO.setLoyaltyId(loyaltyProfileTabDTO1.getLoyaltyID()+"");
						tierAndBonusPointDetailsDTO.setTierCreateDate(new Date());
						tierAndBonusPointDetailsDTO.setTierPoints(pointsDTO.getTransferPoints().doubleValue());
						  
				       
						tierAndBonusPointDetailsDTO.setExpiryDate(expiryDate);
						tierAndBonusPointDetailsDTO.setExpiryQuarter(expiryQuarter);
						tierAndBonusPointDetailsDTO.setTotalPoints(tierAndBonusPointDetailsDTO.getTierPoints()+tierAndBonusPointDetailsDTO.getBonusPoints());
						tierAndBonusCalculationDAO.insetTierAndBonusDetails(tierAndBonusPointDetailsDTO);
						}
						//new RegisterAcctDAO().insertLoyaltyTransactionDetails(session,loyaltyTransactionTabDTO);
						break;
						}
						
					}
							
							//	insertLoyaltyRedeemDetails(session,pointsDTO,loyaltyProfileTabDTO);
								
							
				}else{
					logger.info("Loyalty ID :"+pointsDTO.getFromLoyaltyId()+" does not have sufficient points to trasnfer. Points :"+loyaltyProfileTabDTO.getRewardPoints()+" Request Transfer points :"+pointsDTO.getTransferPoints());
					
					throw new CommonException(Cache.getServiceStatusMap().get("TRAN_PONITS_INSUF_POINTS_"+fromLoyaltyLanguage).getStatusCode(),commonUtil.getStatusDescription(Cache.getServiceStatusMap().get("TRAN_PONITS_INSUF_POINTS_"+fromLoyaltyLanguage).getStatusDesc(),new String[]{NotificationTokens.subscriberNumber}, new String[]{pointsDTO.getSubscriberNumber()+""}));
					
					//throw new CommonException("Loyalty ID :"+pointsDTO.getFromLoyaltyId()+" does not have sufficient points to trasnfer ");
				}
				
				 loopCounter++;
				 
			}//while
			
			if(loopCounter>=10)
			{
				logger.info("System is busy.");
				throw new CommonException(Cache.getServiceStatusMap().get("TRAN_POINT_FAIL_"+fromLoyaltyLanguage).getStatusCode(),Cache.getServiceStatusMap().get("TRAN_POINT_FAIL_"+fromLoyaltyLanguage).getStatusDesc());
			}
			
		/*	if(loyaltyProfileTabDTO!=null){
				loyaltyProfileTabDTO.setDefaultLanguage(fromLoyaltyLanguage);
			    new NotificationPanel().transferPointsNotification(loyaltyProfileTabDTO,pointsDTO.getSubscriberNumber(),loyaltyProfileTabDTO1.getLoyaltyID()+"",pointsDTO.getTransferPoints()+"",NotificationTokens.pointsTransfer_success_id);
			}
			
			if(loyaltyProfileTabDTO1!=null){
				loyaltyProfileTabDTO1.setDefaultLanguage(toLoyaltyLanguage);
			    new NotificationPanel().transferPointsNotification(loyaltyProfileTabDTO1,pointsDTO.getDestinationSubscriberNumber(),loyaltyProfileTabDTO.getLoyaltyID()+"",pointsDTO.getTransferPoints()+"",NotificationTokens.pointsTransferDest_success_id);
			}
			
			*/
			genericDTO.setStatusCode(Cache.getServiceStatusMap().get("TRAN_POINTS_SUCCESS_"+fromLoyaltyLanguage).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("TRAN_POINTS_SUCCESS_"+fromLoyaltyLanguage).getStatusDesc());
			
		}catch (Exception e) {
			
			logger.info("",e);
			
			if(transaction!=null)
				transaction.rollback();
			
			if(e instanceof CommonException){
				CommonException commonException=(CommonException)e;
				genericDTO.setStatusCode(commonException.getStatusCode());
				genericDTO.setStatus(commonException.getMessage());
			}else{
				
				genericDTO.setStatusCode(Cache.getServiceStatusMap().get("TRAN_POINT_FAIL_"+fromLoyaltyLanguage).getStatusCode());
				genericDTO.setStatus(Cache.getServiceStatusMap().get("TRAN_POINT_FAIL_"+fromLoyaltyLanguage).getStatusDesc());
			}
			return false;
		}	
		 finally{
			 cdrInformationDTO=new CDRInformationDTO();
		        
		        cdrInformationDTO.setTransactionId(pointsDTO.getTransactionId());
		      //  logger.info(">>>"+cdrInformationDTO.getTransactionId());
		        //cdrInformationDTO.setTimeStamp(pointsDTO.getTimestamp());
		        cdrInformationDTO.setCommandId(CDRCommandID.TransferPoints.getId());
		        cdrInformationDTO.setChannelID(pointsDTO.getChannel());
		        cdrInformationDTO.setLoyaltyId(pointsDTO.getFromLoyaltyId()+"");
		        cdrInformationDTO.setSubscriberNumber(pointsDTO.getSubscriberNumber()+"");
		       // cdrInformationDTO.setAccountNumber(accountNumber);
		        //cdrInformationDTO.setSubscriberType(subscriberType);
		        cdrInformationDTO.setStatusCode(genericDTO.getStatusCode());
		        cdrInformationDTO.setStatusDescription(genericDTO.getStatus());
		        cdrInformationDTO.setTierPoints(loyaltyProfileTabDTO.getTierPoints());
		        cdrInformationDTO.setBonusPoints(loyaltyProfileTabDTO.getBonusPoints());
		        //cdrInformationDTO.setPreviousTier(loyaltyProfileTabDTO.get);
		        cdrInformationDTO.setCurrentTier(loyaltyProfileTabDTO.getTierId());
		      //  cdrInformationDTO.setCategoryId(Integer.parseInt(loyaltyProfileTabDTO.getCategory()));
		       // cdrInformationDTO.setCategoryDesc(loyaltyProfileTabDTO.getCategory());
		        //cdrInformationDTO.setOfferId(offerId);
		        //cdrInformationDTO.setMerchantId(merchantId);
		        //cdrInformationDTO.setVoucherNumber(voucherNumber);
		        cdrInformationDTO.setDestSubscriberNumber(pointsDTO.getDestinationSubscriberNumber());
		        cdrInformationDTO.setDestLoyaltyId(pointsDTO.getToLoyaltyId()+"");
		       // cdrInformationDTO.setField1(field1);
		        
		       logger.warn(cdrInformationDTO.toString());
		       
		       cdrInformationDTO=new CDRInformationDTO();
		        
		        cdrInformationDTO.setTransactionId("T"+pointsDTO.getTransactionId());
		      //  logger.info(">>>"+cdrInformationDTO.getTransactionId());
		        //cdrInformationDTO.setTimeStamp(pointsDTO.getTimestamp());
		        cdrInformationDTO.setCommandId(CDRCommandID.PointAccumulation.getId());
		        cdrInformationDTO.setChannelID(pointsDTO.getChannel());
		        cdrInformationDTO.setLoyaltyId(pointsDTO.getToLoyaltyId()+"");
		        cdrInformationDTO.setSubscriberNumber(pointsDTO.getDestinationSubscriberNumber()+"");
		       // cdrInformationDTO.setAccountNumber(accountNumber);
		        //cdrInformationDTO.setSubscriberType(subscriberType);
		        cdrInformationDTO.setStatusCode(genericDTO.getStatusCode());
		        cdrInformationDTO.setStatusDescription(genericDTO.getStatus());
		        cdrInformationDTO.setTierPoints(loyaltyProfileTabDTO1.getTierPoints());
		        cdrInformationDTO.setBonusPoints(loyaltyProfileTabDTO1.getBonusPoints());
		        //cdrInformationDTO.setPreviousTier(loyaltyProfileTabDTO.get);
		        cdrInformationDTO.setCurrentTier(loyaltyProfileTabDTO1.getTierId());
		      //  cdrInformationDTO.setCategoryId(Integer.parseInt(loyaltyProfileTabDTO.getCategory()));
		       // cdrInformationDTO.setCategoryDesc(loyaltyProfileTabDTO.getCategory());
		        //cdrInformationDTO.setOfferId(offerId);
		        //cdrInformationDTO.setMerchantId(merchantId);
		        //cdrInformationDTO.setVoucherNumber(voucherNumber);
		        //cdrInformationDTO.setDestSubscriberNumber(pointsDTO.getDestinationSubscriberNumber());
		        //cdrInformationDTO.setDestLoyaltyId(pointsDTO.getToLoyaltyId()+"");
		       // cdrInformationDTO.setField1(field1);
			logger.warn(cdrInformationDTO.toString());
			if(session!=null)
				session.close();
			session=null;
			commonUtil=null;
			infoDAO=null;
			loyaltyProfileTabDTO=null;
			loyaltyProfileTabDTO1=null;
		}
		
		 return true;
			
		 
	}//transferPointsStep2
	
	public boolean updateLoyaltyProfile(
			LoyaltyProfileTabDTO loyaltyProfileTabDTO, String points) throws Exception{
		logger.info("***** updateLoyaltyProfile*****");
		Session session=null;
		String hql=null;
		boolean flag=false;
		Transaction transaction=null;
		Query query =null;
		//TableInfoDAO infoDAO = new TableInfoDAO();
		try{
			logger.info(">>>loyalty id>>>"+loyaltyProfileTabDTO.getLoyaltyID()+">>>points>>>"+points);
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			//String profileTable = infoDAO.getLoyaltyProfileDBTable(loyaltyProfileTabDTO.getLoyaltyID()+"");
			hql="UPDATE LOYALTY_PROFILE_ENTITY_0 SET rewardPoints = :rewardPoints, counter=counter+1, tierPoints=:tierPoints WHERE loyaltyID =:loyaltyId AND counter=:counter";
			query=session.createQuery(hql);
			query.setParameter("rewardPoints", loyaltyProfileTabDTO.getRewardPoints()+Double.parseDouble(points));
			query.setParameter("loyaltyId", loyaltyProfileTabDTO.getLoyaltyID());
			query.setParameter("counter", loyaltyProfileTabDTO.getCounter());
			query.setParameter("tierPoints", loyaltyProfileTabDTO.getTierPoints()+Double.parseDouble(points));
			
			if(query.executeUpdate()>0){
				transaction.commit();
				flag=true;
			}else{
				flag=false;
			}
		
		}
		catch(HibernateException he)
		{
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
	
	private boolean ValidationOnPointsTransferedPerDay(
			LoyaltyProfileTabDTO loyaltyProfileTabDTO, Long transferPoints) {
		Session session = null;
	    Transaction transaction =null;
	    TableInfoDAO tableInfoDAO = new TableInfoDAO();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
	    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	    ArrayList<LoyaltyTransactionTabDTO> transactionList = null;
	    Double totalPoints =Double.parseDouble(transferPoints+"");
	    boolean flag =false;
	    try{
	    	session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			Date now = new  Date();
		   
		    String StartDate = df.format(now)+"00:00:00";
		    Date sd= sdf.parse(StartDate);
		    String  endDate =sdf.format(now);
			Date ed =sdf.parse(endDate);
			
			Criteria criteria=session.createCriteria(tableInfoDAO.getLoyaltyTransactionTable(loyaltyProfileTabDTO.getLoyaltyID()+""));
	    	criteria.add(Restrictions.eq("loyaltyID", loyaltyProfileTabDTO.getLoyaltyID()));
	    	criteria.add(Restrictions.eq("statusID", LoyaltyTransactionStatus.rewardsPointsTransferred));
	    	criteria.add(Restrictions.ge("createTime",sd));
			criteria.add(Restrictions.le("createTime",ed));
			
			transactionList = (ArrayList<LoyaltyTransactionTabDTO>) criteria.list();
			logger.info(">>list size>>>"+transactionList.size());
			if(transactionList!=null && transactionList.size()>0){
				for(int k=0;k<=transactionList.size();k++){
					LoyaltyTransactionTabDTO trnsDto = transactionList.get(k);
					totalPoints =totalPoints+trnsDto.getRewardPoints();
				}
			}
			if(totalPoints<=Double.parseDouble(Cache.getCacheMap().get("TRANSACTION_LIMIT_PER_DAY")) && transactionList.size()+1<=5){
				flag=true;
			}else{
				flag=false;
				logger.info(">>list size>>>"+transactionList.size());
				logger.info(">>>>total points transfered on current day >>>"+totalPoints);
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    logger.info(">>>>total points transfered on current day >>>"+totalPoints);
		return false;
	}

	private Double getPointsTransferedCurrentMonth(
			LoyaltyProfileTabDTO loyaltyProfileTabDTO) {
		// TODO Auto-generated method stub
		Session session = null;
	    Transaction transaction =null;
	    TableInfoDAO tableInfoDAO = new TableInfoDAO();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
	    ArrayList<LoyaltyTransactionTabDTO> transactionList = null;
	    Double totalPoints =0.0;
	    try{
	    	session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			Date now = new  Date();
		    int	i=now.getMonth();
		    int year =now.getYear();
		    String StartDate = "01-"+i+"-"+year+" "+"00:00:00";
		    Date sd= sdf.parse(StartDate);
		    String  endDate =sdf.format(now);
			Date ed =sdf.parse(endDate);
			
			Criteria criteria=session.createCriteria(tableInfoDAO.getLoyaltyTransactionTable(loyaltyProfileTabDTO.getLoyaltyID()+""));
	    	criteria.add(Restrictions.eq("loyaltyID", loyaltyProfileTabDTO.getLoyaltyID()));
	    	criteria.add(Restrictions.eq("statusID", LoyaltyTransactionStatus.rewardsPointsTransferred));
	    	criteria.add(Restrictions.ge("createTime",sd));
			criteria.add(Restrictions.le("createTime",ed));
			
			transactionList = (ArrayList<LoyaltyTransactionTabDTO>) criteria.list();
			logger.info(">>list size>>>"+transactionList.size());
			if(transactionList!=null && transactionList.size()>0){
				for(int k=0;k<=transactionList.size();k++){
					LoyaltyTransactionTabDTO trnsDto = transactionList.get(k);
					totalPoints =totalPoints+trnsDto.getRewardPoints();
				}
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    logger.info(">>>>total points transfered on current month from table>>>"+totalPoints);
		return totalPoints;
	}

	private void insertLoyaltyRedeemDetails(Session session,TransferPointsDTO pointsDTO, LoyaltyProfileTabDTO loyaltyProfileTabDTO) {

		logger.info("Inserting Record into Loyalty Redeemed Table...");
		LoyaltyRedeemDTO loyaltyRedeemDTO = new LoyaltyRedeemDTO();
		loyaltyRedeemDTO.setLoyaltyID(loyaltyProfileTabDTO.getLoyaltyID());
		loyaltyRedeemDTO.setDate(new Date());
		loyaltyRedeemDTO.setPakcageID(-1);
		loyaltyRedeemDTO.setRedeemPoint(pointsDTO.getTransferPoints());
		loyaltyRedeemDTO.setSubscriberNumber(pointsDTO.getSubscriberNumber());
		loyaltyRedeemDTO.setStatusID(6);
		session.save(loyaltyRedeemDTO);
	}

	public Long getLoyaltyId(String subscriberNumber)
	{
		
		TableDetailsDAO tableDetailsDAO=null;
		Long loyaltyId=null;
		SubscriberNumberTabDTO subscriberNumberTabDTO;
		AccountNumberTabDTO accountNumberTabDTO;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO;
		try{
			tableDetailsDAO=new TableDetailsDAO();
			if(subscriberNumber==null)
				return null;
			logger.info("inside getLoyaltyId");
			/*util=new CommonUtil();
			
			if(util.isItChar(subscriberNumber))
			{
				ADSLTabDTO tabDTO=tableDetailsDAO.getADSLDetails(subscriberNumber);
				if(tabDTO==null)
				{
					logger.info("Request ADSL Number is not found ADLS Number :"+subscriberNumber);
				}else{
					logger.info("Request ADSL Number is found ADLS Number :"+tabDTO.getADSLNumber()+"  Loyalty ID : "+tabDTO.getLoyaltyID());
					loyaltyId=tabDTO.getLoyaltyID();
					return loyaltyId;
				}
			}else{*/
				
				subscriberNumberTabDTO=tableDetailsDAO.getSubscriberNumberDetails(Long.parseLong(subscriberNumber.trim()));
				if(subscriberNumberTabDTO!=null)
				{
					logger.info("Request Subscriber Number is  found Subscriber Number :"+subscriberNumber+"  Loyaty ID"+subscriberNumberTabDTO.getLoyaltyID());
					loyaltyId=subscriberNumberTabDTO.getLoyaltyID();
					return loyaltyId;
				}
				
				accountNumberTabDTO=tableDetailsDAO.getAccountNumberDetails(subscriberNumber.trim());
				if(accountNumberTabDTO!=null)
				{
					logger.info("Request Subscriber Number is  found Subscriber Number :"+subscriberNumber+"  Loyaty ID"+accountNumberTabDTO.getLoyaltyID());
					loyaltyId=accountNumberTabDTO.getLoyaltyID();
					return loyaltyId;
				}
				
				loyaltyProfileTabDTO=tableDetailsDAO.getLoyaltyProfileDetails(Long.parseLong(subscriberNumber.trim()));
				if(loyaltyProfileTabDTO!=null)
				{
					logger.info("Request Subscriber Number is  found Subscriber Number :"+subscriberNumber+"  Loyaty ID"+loyaltyProfileTabDTO.getLoyaltyID());
					loyaltyId=loyaltyProfileTabDTO.getLoyaltyID();
					return loyaltyId;
				}
			//}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			tableDetailsDAO=null;
			accountNumberTabDTO=null;
			loyaltyProfileTabDTO=null;
			subscriberNumberTabDTO=null;
		}
		return loyaltyId;
	}//getLoyaltyId
	
}

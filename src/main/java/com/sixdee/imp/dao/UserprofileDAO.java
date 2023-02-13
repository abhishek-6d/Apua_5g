package com.sixdee.imp.dao;

/**
 * 
 * @author Geevan
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
 * <td>May 11,2013 08:47:22 AM</td>
 * <td>Geevan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.TierAndBonusPointDetailsDTO;
import com.sixdee.imp.dto.TierInfoDTO;
import com.sixdee.imp.dto.UserIdentifierDTO;
import com.sixdee.imp.dto.UserLoginInfoDTO;
import com.sixdee.imp.dto.UserprofileDTO;
import com.sixdee.util.ConnectionPool1;
import com.sixdee.util.ConnectionPool5;

public class UserprofileDAO {
	static Logger logger=Logger.getLogger(UserprofileDAO.class);
	
	public static DateFormat dateformat=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public static DateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd");
	
	
	public boolean getSubscriberNumberDetails(String subscriberNumber) {
		SubscriberNumberTabDTO subscriberNumberTabDTO = null;
		UserprofileDTO userprofileDTO = null;
		Session session = null;
		boolean flag=false;
		// Transaction trx = null;
		TableInfoDAO infoDAO = new TableInfoDAO();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		try {
			long start = System.currentTimeMillis();

			session = HiberanteUtil.getSessionFactory().openSession();
			// trx = session.beginTransaction();

			String tableName = infoDAO.getSubscriberNumberTable(subscriberNumber + "");

			String sql = " FROM " + tableName + " WHERE subscriberNumber=?";


			Query query = session.createQuery(sql);
			query.setParameter(0, Long.parseLong(subscriberNumber));

			List<SubscriberNumberTabDTO> list = query.list();
			userprofileDTO=new UserprofileDTO();
			if (list.size() > 0) {
				subscriberNumberTabDTO = list.get(0);
				if(subscriberNumberTabDTO.getAccountTypeId()!=null){
				if(subscriberNumberTabDTO.getAccountTypeId().equals(9)){
					flag=true;	
				}else if(subscriberNumberTabDTO.getAccountTypeId().equals(14)){
					String dbDate=dateformat1.format(subscriberNumberTabDTO.getPointsUpdatedDate());
					String calenderDate=dateformat1.format(cal.getTime());
					
					int dateInteger=dateformat1.parse(dbDate).compareTo(dateformat1.parse(calenderDate));
					if(dateInteger>=0){
						flag=true;	
					}
					else
						flag=false;	
				}
				else
					flag=false;		
				
			}else
				flag=false;
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

		return flag;

	}// getSubscriberNumberDetails
public static UserprofileDTO getAllAccountDetails(GenericDTO genericDTO)throws CommonException, ParseException {

	//UserprofileDTO userprofiledto=(UserprofileDTO)genericDTO.getObj();
	
	TableDetailsDAO tabDAO=new TableDetailsDAO();
	 SubscriberNumberTabDTO subscriberNumDTO=new SubscriberNumberTabDTO();
	 LoyaltyProfileTabDTO loyaltyProfileDTO=new LoyaltyProfileTabDTO();
	 AccountNumberTabDTO accntNumDTO=new AccountNumberTabDTO();
	 CommonUtil commonUtil=new CommonUtil();
	UserprofileDTO userprofileDTO = (UserprofileDTO) genericDTO.getObj();

	logger.info("UserProfile DAO");
	
if(userprofileDTO.getSubscriberNumber()!=null)	{
	

  logger.info(userprofileDTO.getSubscriberNumber());
  
  
  
  if(commonUtil.isItChar(userprofileDTO.getSubscriberNumber()))
	{	  
	// ADSL Number
	   logger.info("ADSL NO ENTERED>>>>>>>>>>>>");
		ADSLTabDTO tabDTO=tabDAO.getADSLDetails(userprofileDTO.getSubscriberNumber());
		if(tabDTO!=null)
			
		
			 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(tabDTO.getLoyaltyID());	
			
	}
  
 
  
  else // subscriber
  { 
  
	Long subscriberNum=Long.parseLong(userprofileDTO.getSubscriberNumber());
	logger.info("SUB>>>>>>"+subscriberNum);
	
	 if( subscriberNum!=null && subscriberNum>0)
	 {
		 subscriberNumDTO=tabDAO.getSubscriberNumberDetails(subscriberNum,null);
		 
		
		 if(subscriberNumDTO!=null)
			
			 
			 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(subscriberNumDTO.getLoyaltyID());
		
		 
		 
		 else 
		 {
			 accntNumDTO=tabDAO.getAccountNumberDetails(subscriberNum+"");	 
			 if(accntNumDTO!=null)
				 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(accntNumDTO.getLoyaltyID()); 
			 else
			
			 loyaltyProfileDTO=tabDAO.getLoyaltyProfileDetails(subscriberNum);
			 System.out.println("loyalty profile DTO"+loyaltyProfileDTO);
			// com.sixdee.imp.common.config.Cache.getTierInfoMap().get(loyaltyProfileDTO.getTierId());
		
	
			 // Tier info Map
			 
			 
			/* Map<Integer,TierInfoDTO> TierInfoMap;
			 TierInfoDTO tierinfo=new TierInfoDTO();
			 //TierInfoMap=(Map<Integer, TierInfoDTO>) com.sixdee.imp.common.config.Cache.getTierInfoMap().get(loyaltyProfileDTO.getTierId());
			
			 tierinfo= (TierInfoDTO) com.sixdee.imp.common.config.Cache.getTierInfoMap().get(loyaltyProfileDTO.getTierId());
			
			 System.out.println("DTO SIZE"+com.sixdee.imp.common.config.Cache.getTierInfoMap().get(loyaltyProfileDTO.getTierId()));
			 
			 // (com.sixdee.imp.common.config.Cache.getTierInfoMap().get(i))
			 //Iterator<TierInfoDTO> values= TierInfoMap.keySet().iterator();
			 
			 for(int i=0;i<tierinfo.size();i++)
			 {
				 TierInfoDTO dto=TierInfoMap.get(i);
				 System.out.println("TierName"+dto.getTierName());
				 System.out.println("MaxValue"+dto.getMaxValue());
				 System.out.println("MinValue"+dto.getMinValue());
				 System.out.println("RewardPoints"+dto.getRewardPoints());
				 System.out.println("StatusPoint"+dto.getStatusPoints());
				 }
			  
			 logger.info("TierInfo Details>>>>>") ;
			 logger.info("Tier"+tierinfo.getTierId()) ; 
			 
			logger.info("TierName"+tierinfo.getTierName()) ;
			userprofileDTO.setTierName(tierinfo.getTierName());
			 
			
			logger.info("MaxValue"+tierinfo.getMaxValue()) ;
			logger.info("MinValue"+tierinfo.getMinValue()) ;
			
			logger.info("RewardPoint"+tierinfo.getRewardPoints()) ;
			userprofileDTO.setRewardPoints(tierinfo.getRewardPoints());
			 
			logger.info("StatusPoint"+tierinfo.getStatusPoints()) ;
			userprofileDTO.setStatusPoints(tierinfo.getStatusPoints());
			*/
			
			
		 }
		 
		 if(loyaltyProfileDTO.equals(null))		    			    					
			 logger.info("No details found in any TABLES...!!!");		    					  
 
 
 	
		 
	 }
	 

	 
	
  }
  if(loyaltyProfileDTO!=null) {
		 System.out.println("Details >>>>>>>>>>>>");
		// logger.info("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
		/* System.out.println("FNAME"+loyaltyProfileDTO.getFirstName());
		 System.out.println("LNAME"+loyaltyProfileDTO.getLastName());
		 System.out.println("LoyalityID"+loyaltyProfileDTO.getLoyaltyID());
		 System.out.println("EMAILID"+loyaltyProfileDTO.getEmailID());*/
		 
		 logger.info("FirstName>>"+loyaltyProfileDTO.getFirstName());
		 userprofileDTO.setFirstName(loyaltyProfileDTO.getFirstName());
		 
		 logger.info("LastName>>"+loyaltyProfileDTO.getLastName());
		 userprofileDTO.setLastName(loyaltyProfileDTO.getLastName());
		 
		 logger.info("LoyalityId>>"+loyaltyProfileDTO.getLoyaltyID());
		 userprofileDTO.setLoyaltyID(loyaltyProfileDTO.getLoyaltyID());
		 
		 logger.info("EmailId>>"+loyaltyProfileDTO.getEmailID());
		 userprofileDTO.setEmailID(loyaltyProfileDTO.getEmailID());
		 
		/* 
		 logger.info("AccountNo>>"+loyaltyProfileDTO.getAccountNumber());
		 userprofileDTO.setAccountNumber(loyaltyProfileDTO.getAccountNumber());
		 */
		 logger.info("ContactNo>>"+loyaltyProfileDTO.getContactNumber());
		 userprofileDTO.setContactNumber(loyaltyProfileDTO.getContactNumber());
		 
		 
		/* logger.info("Dob>>"+loyaltyProfileDTO.getDateOfBirth());
		 userprofileDTO.setDateOfBirth(loyaltyProfileDTO.getDateOfBirth());
		 
		 */
		 logger.info("Address>>"+loyaltyProfileDTO.getAddress());
		 userprofileDTO.setAddress(loyaltyProfileDTO.getAddress());
		 
		 
		 logger.info("Category>>"+loyaltyProfileDTO.getCategory());
		 userprofileDTO.setCategory(loyaltyProfileDTO.getCategory());
		 
		 
		 logger.info("Occupation>>"+loyaltyProfileDTO.getOccupation());
		 userprofileDTO.setOccupation(loyaltyProfileDTO.getOccupation());
		 
		 
		 logger.info("Industry>>"+loyaltyProfileDTO.getIndustry());
		 userprofileDTO.setIndustry(loyaltyProfileDTO.getIndustry());
		 
		 logger.info("NationalId>>"+loyaltyProfileDTO.getCustID());
		 userprofileDTO.setNationalID(loyaltyProfileDTO.getCustID());
		 
		 
		 
		 logger.info("StatusID>>"+loyaltyProfileDTO.getStatusID());
		 userprofileDTO.setStatusID(loyaltyProfileDTO.getStatusID());
		 
		 
		 
		// logger.info("StatusUpdatedate>>"+loyaltyProfileDTO.getStatusUpdatedDate());
		 logger.info("StatusUpdatedate>>"+(loyaltyProfileDTO.getStatusUpdatedDate()));
		
		// userprofileDTO.setStatusUpdatedDate((loyaltyProfileDTO.getStatusUpdatedDate()));
		 
		 logger.info("tier>>"+loyaltyProfileDTO.getTierId());
		 userprofileDTO.setTierId(loyaltyProfileDTO.getTierId());
		 
		 logger.info("tierUpdateDate>>"+dateformat.format(loyaltyProfileDTO.getTierUpdatedDate()));
		// userprofileDTO.setTierUpdatedDate((loyaltyProfileDTO.getTierUpdatedDate()));
		 
		
		 
		 
	//	 logger.info("tierName>>>>"+userprofileDTO.getTierName());
	//	 logger.info("StatusPoint>>>>"+userprofileDTO.getStatusPoints());
	//	 logger.info("RewardPoint>>>>"+userprofileDTO.getRewardPoints());
		  }	
 /* Map<Integer,String> statusMap;
  StatusDTO statusdto=new StatusDTO();
  
  statusdto=com.sixdee.imp.common.config.Cache.getStatusMap().get(loyaltyProfileDTO.getStatusID());
  */
  
  
  
	 Map<Integer,TierInfoDTO> TierInfoMap;
	 TierInfoDTO tierinfo=new TierInfoDTO();
	 //TierInfoMap=(Map<Integer, TierInfoDTO>) com.sixdee.imp.common.config.Cache.getTierInfoMap().get(loyaltyProfileDTO.getTierId());
	
	if (loyaltyProfileDTO!=null)
	 
	 tierinfo= (TierInfoDTO) com.sixdee.imp.common.config.Cache.getTierInfoMap().get(loyaltyProfileDTO.getTierId());
	
	// System.out.println("DTO SIZE"+com.sixdee.imp.common.config.Cache.getTierInfoMap().get(loyaltyProfileDTO.getTierId()));
	 
	 // (com.sixdee.imp.common.config.Cache.getTierInfoMap().get(i))
	 //Iterator<TierInfoDTO> values= TierInfoMap.keySet().iterator();
	 
	 /*for(int i=0;i<tierinfo.size();i++)
	 {
		 TierInfoDTO dto=TierInfoMap.get(i);
		 System.out.println("TierName"+dto.getTierName());
		 System.out.println("MaxValue"+dto.getMaxValue());
		 System.out.println("MinValue"+dto.getMinValue());
		 System.out.println("RewardPoints"+dto.getRewardPoints());
		 System.out.println("StatusPoint"+dto.getStatusPoints());
		 }
	  */
	if(tierinfo!=null) {
	 
	 logger.info("TierInfo Details>>>>>") ;
	 logger.info("Tier"+tierinfo.getTierId()) ; 
	 
	logger.info("TierName"+tierinfo.getTierName()) ;
	userprofileDTO.setTierName(tierinfo.getTierName());
	 
	} 
  
  
  
	
}
	return userprofileDTO;
	}

public UserIdentifierDTO getUserIdentifier(
Long subscriberNumber, Long loyaltyID) {
    UserIdentifierDTO userIdentifierDTO = null;
    Session session = null;
    TableInfoDAO infoDAO = new TableInfoDAO();
    try {
      // long start = System.currentTimeMillis();
      session = HiberanteUtil.getSessionFactory().openSession();

      String tableName = infoDAO.getUserIdentifierTable(loyaltyID + "");

      String sql = " FROM " + tableName + " WHERE ";

      if (subscriberNumber != null)
        sql += " subscriberNumber=? ";

      if (subscriberNumber != null && loyaltyID != null && loyaltyID > 0)
        sql += " AND loyaltyID=? ";
      else if (loyaltyID != null && loyaltyID > 0)
        sql += "loyaltyID=? ";

      Query query = session.createQuery(sql);
      if (subscriberNumber != null)
        query.setParameter(0, subscriberNumber);
      if (subscriberNumber != null && loyaltyID != null && loyaltyID > 0)
        query.setParameter(1, loyaltyID);
      else if (loyaltyID != null && loyaltyID > 0)
        query.setParameter(0, loyaltyID);

      List<UserIdentifierDTO> list = query.list();

      if (list.size() > 0) {
        userIdentifierDTO = list.get(0);
        logger.info("in dao " + list.get(0).toString());

      }

    } catch (Exception e) {
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

    return userIdentifierDTO;

  }

  public UserLoginInfoDTO getAuthInfo() {
    UserLoginInfoDTO userLoginInfoDTO = null;
    Session session = null;
    try {
      // long start = System.currentTimeMillis();
      session = HiberanteUtil.getSessionFactory().openSession();
      String tableName = "UserLoginInfoDTO";
      session.beginTransaction();
      String sql = " FROM " + tableName + " WHERE IS_USED = 'FALSE' and rownum < 2";
      Query query = session.createQuery(sql);
      List<UserLoginInfoDTO> list = query.list();

      if (list.size() > 0) {
        userLoginInfoDTO = list.get(0);
      }
    } catch (Exception e) {
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

    return userLoginInfoDTO;

  }

  public void updateLoginInfo(UserLoginInfoDTO userLoginInfoDTO) {

    Session session = null;
    try {
      session = HiberanteUtil.getSessionFactory().openSession();
      session.beginTransaction();
      /*
       * String tableName = "USER_LOGIN_INFO"; String sql =
       * " UPDATE  UserLoginInfoDTO SET IS_USED =?,COUNTER=? " + " WHERE COUNTER=?" + "AND ID = ?";
       * 
       * Query query = session .createSQLQuery(sql) .setString(0, "TRUE") .setParameter( 1,
       * (userLoginInfoDTO.getCounter() >= Integer.MAX_VALUE ? 1 : userLoginInfoDTO .getCounter() +
       * 1)).setParameter(2, userLoginInfoDTO.getCounter()) .setParameter(3,
       * userLoginInfoDTO.getId());
       * 
       * query.executeUpdate();
       */

      Query query =
          session.createQuery("UPDATE  UserLoginInfoDTO SET IS_USED =?,COUNTER=? "
              + " WHERE COUNTER=? AND ID = ?");
      query
          .setString(0, "TRUE")
          .setParameter(
              1,
              (userLoginInfoDTO.getCounter() >= Integer.MAX_VALUE ? 1 : userLoginInfoDTO
                  .getCounter() + 1)).setParameter(2, userLoginInfoDTO.getCounter())
          .setParameter(3, userLoginInfoDTO.getId());
      query.executeUpdate();

      session.getTransaction().commit();

    } catch (Exception e) {
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
  }

  public Boolean insertIdentifierInfo(Long loyaltyId, UserLoginInfoDTO userLoginInfoDTO) {
    Boolean insertData = false;
    Session session = null;
    try {
      TableInfoDAO infoDAO = new TableInfoDAO();

      session = HiberanteUtil.getSessionFactory().openSession();
      session.beginTransaction();
      /*
       * String tableName = infoDAO.getUserDetailsTable(loyaltyId + ""); String sql = "INSERT INTO "
       * + tableName + " (LOYALTY_ID,USER_NAME,Password,counter,CREATION_DATE) values(?,?,?,?,?)";
       * Query query = session.createSQLQuery(sql); query.setParameter(0, loyaltyId);
       * query.setParameter(1, userLoginInfoDTO.getUserName()); query.setParameter(2,
       * userLoginInfoDTO.getPassWord()); query.setParameter(3, 0); query.setDate(4, new Date());
       * query.executeUpdate();
       */
      UserIdentifierDTO userIdentifierDTO = new UserIdentifierDTO();
      userIdentifierDTO.setLoyaltyID(loyaltyId);
      userIdentifierDTO.setUserName(userLoginInfoDTO.getUserName());
      userIdentifierDTO.setPassWord(userLoginInfoDTO.getPassWord());
      userIdentifierDTO.setCounter(0L);
      userIdentifierDTO.setCreateDate(new Date());
      session.save(infoDAO.getUserIdentifierTable(loyaltyId + ""), userIdentifierDTO);
      session.getTransaction().commit();
      insertData = true;
    } catch (Exception e) {
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

    return insertData;
  }
  
  
  public ArrayList<String> getTravellerAppWhiteListSubscribers()
  {
  	ArrayList<String> travellerAppWhiteList=new ArrayList<String>();
  	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = null;
	String subscriberNumber="";
  	try{
  		sql = "SELECT SUBSCRIBER_NUMBER from TRAVELLER_APP_WHITELIST";
		conn = ConnectionPool1.getConnection();
		pstmt = conn.prepareStatement(sql);
		rs=pstmt.executeQuery();
		while(rs.next())
		{
			subscriberNumber=rs.getString(1);
			travellerAppWhiteList.add(subscriberNumber);
		}
  		
  	}catch (Exception e) {
  		e.printStackTrace();
  		
  	}finally{
  		sql = null;
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
		}
		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
		}
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
		}
  		
  	}
  	
  	return travellerAppWhiteList;
  }//getTravellerAppWhiteListSubscribers()
  
  public String GetRewardPointsInComingQuarter(Long loyaltyid){
	  Session session = null;
	  Query query = null;
	  List<BigDecimal> list = null;
	  String expiryrewardpoints=null;
	  Calendar now = Calendar.getInstance();
	    now.add(Calendar.MONTH, 1);
	    now.add(Calendar.YEAR, -2);
	    int n = now.get(Calendar.MONTH);
	    String b = now.get(Calendar.YEAR)+"";
	    Date myDate= new Date();
		int quarter = (myDate.getMonth() / 3) + 1;
	    String v1=null,v2=null,v3=null;
	    switch (quarter){
	    case 1:
	    	v1=b+"0"+1+"";
            v2=b+"0"+2+"";
            v3=b+"0"+3+"";
            break;
	    case 2:
	    	v1=b+"0"+4+"";
            v2=b+"0"+5+"";
            v3=b+"0"+6+"";
        break;
	    case 3:
	    	v1=b+"0"+7+"";
            v2=b+"0"+8+"";
            v3=b+"0"+9+"";
        break;
	    case 4:
	    	v1=b+10+"";
	        v2=b+11+"";
	        v3=b+12+"";
	        break;
	     default:logger.info("");
	    }
	    session = HiberanteUtil.getSessionFactory().openSession();
		   query = session.createSQLQuery("SELECT SUM(REWARD_POINTS) FROM  LOYALTY_TRAN_SUMMARY_MONTHLY "+
		     " WHERE PARTITION_INDEX in ('"+v1+"','"+v2+"','"+v3+"') AND REWARD_POINTS>0 AND LOYALTY_ID = ? ");
		   query.setParameter(0, loyaltyid);
		   list= query.list();
		   for(BigDecimal all : list)
		   {
			   if(all!=null)
				   expiryrewardpoints = all.toString();
		    

		   }
		   logger.info("Reward points>>> "+expiryrewardpoints);
		 return expiryrewardpoints;  
	   }
		public Date getFirstDayOfQuarter(Date date) {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
			 Calendar cal = Calendar.getInstance();
			    cal.setTime(date);
			    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)/3 * 3);
			    cal.set(Calendar.DAY_OF_MONTH, 1);
			    Date d = cal.getTime();
			    String dat =df.format(d);
			    try {
					d= df.parse(dat);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return d;
		}
		public Date getLastdayOfQuarter(Date date) {
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
			 Calendar cal = Calendar.getInstance();
			    cal.setTime(date);
			    cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)/3 * 3 + 2);
			    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			    Date d = cal.getTime();
			    String dat =df.format(d);
			    try {
					d= df.parse(dat);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return d;
		}
		
		public double getPointsToBeExpired(Date startDate, Date endDate,
				Long loyaltyID, Long subscriberNumber) {
			Session session = null;
			Transaction trx = null;
			double expirypoints = 0.0;
			try {
				logger.info("Inside getPointsToBeExpired >>>>loyalityId>>>>"+loyaltyID+">>>>msisdn>>>"+subscriberNumber);
				session=HiberanteUtil.getSessionFactory().openSession();
				trx = session.beginTransaction();
				Criteria criteria=session.createCriteria(TierAndBonusPointDetailsDTO.class);
				criteria.add(Restrictions.eq("loyaltyId",loyaltyID+""));
				criteria.add(Restrictions.eq("msisdn",subscriberNumber+""));
				criteria.add(Restrictions.ge("expiryDate",startDate));
				criteria.add(Restrictions.le("expiryDate",endDate));
				
				List<TierAndBonusPointDetailsDTO> list =criteria.list();
				logger.info(">>>>list size>>>>>"+list.size());
				if(list.size()>0){
					for(TierAndBonusPointDetailsDTO tierAndBonusPointDetailsDTO:list){
						expirypoints = expirypoints+ tierAndBonusPointDetailsDTO.getTotalPoints();
					}
				}
				logger.info(">>expiry points returned >>>"+expirypoints);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
				throw e;
			} finally {
				if (session != null && session.isOpen())
					session.close();
				session = null;
			}
			return expirypoints;
		}
	  
}
	








	
	
	
	
	
	
	
	
	
	// TODO Auto-generated method stub
	

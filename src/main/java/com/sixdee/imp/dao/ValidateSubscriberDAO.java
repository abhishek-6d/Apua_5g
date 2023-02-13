package com.sixdee.imp.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.ActionServiceDetailsDTO;
import com.sixdee.imp.dto.SubscriberProfileDTO;
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
 * <td>May 08,2013 02:27:12 PM</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */




public class ValidateSubscriberDAO {

	private Logger logger=Logger.getLogger(ValidateSubscriberDAO.class);
	private SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	List<SubscriberProfileDTO> list=null;
	
	int counter=0;
	
	public void checkSubscriberProfile(GenericDTO genericDTO)throws CommonException
	{
		SubscriberProfileDTO subscriberProfileDTO=(SubscriberProfileDTO)genericDTO.getObj();
		SubscriberProfileDTO existSubscriberProfile=null;
		ActionServiceDetailsDTO actionServiceDetailsDTO=null;
		SubscriberProfileDTO parentProfileDTO=null;
		TableDetailsDAO tableDetailsDAO=null;
		
		String logFile="";
		long firstLong=System.currentTimeMillis();
		try{
			
			tableDetailsDAO=new TableDetailsDAO();
		
			logFile="'"+subscriberProfileDTO.getTransactionId()+"'|'"+dateFormat.format(new Date())+"'|"+subscriberProfileDTO.getSubscriberNumber()+"|'"+subscriberProfileDTO.getServiceName()+"'";
			//long start=System.currentTimeMillis();
			list=getSubscriberProfileDetails(genericDTO);
			//long end=System.currentTimeMillis();
			//System.out.println("getting subscriberinfo ---------->"+(end-start)+"   "+list.size());
			
			for(SubscriberProfileDTO detailsDTO:list)
			{
				logger.info("    hhhhhhhhh "+detailsDTO.getServiceId()+"     "+detailsDTO.getServiceName()+"   "+detailsDTO.getDailyCount());
			}
			
			if(list==null||list.size()==0)
			{
				logger.info("Subscriber Not Eligible. Subscriber Number : "+subscriberProfileDTO.getSubscriberNumber());
				throw new CommonException("Subscriber Not Eligible. Subscriber Number : "+subscriberProfileDTO.getSubscriberNumber());
			}
			
			//start=System.currentTimeMillis();
			actionServiceDetailsDTO=tableDetailsDAO.getServiceDetails(subscriberProfileDTO.getServiceName());
			//end=System.currentTimeMillis();
			//System.out.println("getting Service ---------->"+(end-start));
			
			if(actionServiceDetailsDTO==null)
			{
				logger.info("Service Details not found for Service Name : "+subscriberProfileDTO.getServiceName());
				throw new CommonException("Service Details not found for Service Name : "+subscriberProfileDTO.getServiceName());
			}
			
			boolean isServiceExist=false;
			
			
			
			//start=System.currentTimeMillis();
			// checking requested service name available or not
			for(SubscriberProfileDTO profileDTO:list)
			{
				//if(profileDTO.getServiceName().trim().equalsIgnoreCase(subscriberProfileDTO.getServiceName().trim()))
				if(profileDTO.getServiceId()==actionServiceDetailsDTO.getServiceID())
				{
					existSubscriberProfile=profileDTO;
					isServiceExist=true;
				}
				
				// Parent Exist
				if(actionServiceDetailsDTO.getParentID()!=null&&profileDTO.getServiceId()==actionServiceDetailsDTO.getParentID())
				{
					parentProfileDTO=profileDTO;
				}else if(actionServiceDetailsDTO.getParentID()==null&&profileDTO.getServiceId()==actionServiceDetailsDTO.getServiceID())
				{
					parentProfileDTO=profileDTO;
					
				}
				
			}
			 
			
			//end=System.currentTimeMillis();
			//System.out.println("Finding Servce ---------->"+(end-start));
			
			
			//start=System.currentTimeMillis();
			if(!isValidation(parentProfileDTO,subscriberProfileDTO.getServiceName(),actionServiceDetailsDTO))
			{
				logger.info("Subscriber Not Eligible. Subscriber Number : "+subscriberProfileDTO.getSubscriberNumber());
				throw new CommonException("Subscriber Not Eligible. Subscriber Number : "+subscriberProfileDTO.getSubscriberNumber());
			}
			//end=System.currentTimeMillis();
			
			//System.out.println("Validation ---------->"+(end-start));
			
			if(isServiceExist)
			{
				/*
				 * Requested Service Name is exist
				 * Check the control group and check the counts
				 */
				//start=System.currentTimeMillis();
				updateSubscriberProfileDetails(existSubscriberProfile,actionServiceDetailsDTO,parentProfileDTO);
				//end=System.currentTimeMillis();
				//System.out.println("Updation ---------->"+(end-start));
				
			}else{
				/*
				 * Requested Service Name does not exist
				 * Check the control group and check the counts
				 */
				//start=System.currentTimeMillis();
				insertSubscriberProfileDetails(subscriberProfileDTO,actionServiceDetailsDTO,parentProfileDTO);
				//end=System.currentTimeMillis();
				//System.out.println("Insertion ---------->"+(end-start));
			}
			logFile+="|'SUCCESS'";
		}catch (CommonException e) {

			// If duplicate insertion for subscriber and service name
			if(e.getErrorCode()==9090)
			{
				
				counter++;
				
				if(counter<3)
				{
				  logFile=null;
				  checkSubscriberProfile(genericDTO);
				}else{
					logFile+="|'Duplicate Insertion Execpton'";
				}
				
			}else{
				logFile+="|'"+e.getLocalizedMessage()+"'";
				e.printStackTrace();
				throw e;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logFile+="|'"+e.getLocalizedMessage()+"'";
			throw new CommonException("Exception");
		}finally{
			// Writting CDR File
			if(logFile!=null)
			{
			    logger.warn(logFile+"---"+(System.currentTimeMillis()-firstLong));
			    logFile=null;
			}
			parentProfileDTO=null;
			tableDetailsDAO=null;
			list=null;
			actionServiceDetailsDTO=null;
			existSubscriberProfile=null;
		}
		
		
	}//checkSubscriberProfile
	
	public void updateSubscriberProfileDetails(SubscriberProfileDTO profileDTO,ActionServiceDetailsDTO actionServiceDetailsDTO,SubscriberProfileDTO parentProfileDTO)throws CommonException
	{
		Session session=null;
		Transaction transaction=null;
		try{
			
			
			int loopCount=1;
			
			session=HiberanteUtil.getSessionFactory().openSession();
			
			while(loopCount<=10)
			{
			
				transaction =session.beginTransaction();
				
				if(loopCount>1)
				{
					profileDTO=getSubscriberProfileDetails(profileDTO);
					//if(profileDTO.getServiceId()!=parentProfileDTO.getServiceId())
					if(parentProfileDTO.getStatus().equalsIgnoreCase("R"))
					    parentProfileDTO=getSubscriberProfileDetails(parentProfileDTO);
				}
				
				
				Calendar calendar=Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY,0);
				calendar.set(Calendar.MINUTE,0);
				calendar.set(Calendar.SECOND,0);
				calendar.set(Calendar.MILLISECOND,0);
				
				if(parentProfileDTO.getStatus().equalsIgnoreCase("R"))
				{
					boolean flag=false;
					
					String status=profileDTO.getServiceId()==parentProfileDTO.getServiceId()?",STATUS='A'":"";
					
					switch (actionServiceDetailsDTO.getActionType()) {
					case 1:
							if(updateSubscriberProfileDetails(session,profileDTO,"DAILY_COUNT=1"+status,"DAILY_DATE")>0)
								flag=true;
							break;
						
					case 2:
							if(updateSubscriberProfileDetails(session,profileDTO,"WEEKLY_COUNT=1"+status,"WEEKLY_DATE")>0)
								flag=true;
							break;
						
					case 3:
							if(updateSubscriberProfileDetails(session,profileDTO,"MONTHLY_COUNT=1"+status,"MONTHLY_DATE")>0)
								flag=true;
							break;

					default:
						break;
					}
					
					if(flag)
					{
						if(!status.equalsIgnoreCase("")||updateSubscriberProfileStatus(session, parentProfileDTO)>0)
						{
						   transaction.commit();
						   break;
						}else{
							transaction.rollback();
						}
						
					}else{
						transaction.rollback();
					}
					
					
				}else if(actionServiceDetailsDTO.getActionType()==1)
				{
					//	DAILY Check
					if(profileDTO.getDailyDate()!=null){
						profileDTO.getDailyDate().setHours(0);
						profileDTO.getDailyDate().setMinutes(0);
						profileDTO.getDailyDate().setSeconds(0);
					}
					
					
					
					if(profileDTO.getDailyDate()!=null&&profileDTO.getDailyDate().compareTo(calendar.getTime())==0)
					{
						//	record exist for today date,check the counts and update
						if(profileDTO.getDailyCount()>=actionServiceDetailsDTO.getNoOfTimes())
						{
							// Daily Count has  crossed
							logger.info("Daily Count exceeded for the subscriber : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
							throw new CommonException("Daily Count exceeded for the subscriber : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
						}else{
							// Daily Count available
							if(updateSubscriberProfileDetails(session,profileDTO,"DAILY_COUNT=DAILY_COUNT+1")>0)
							{
								transaction.commit();
								break;
							}else{
								transaction.rollback();
							}
						}
						
						
					}else if(profileDTO.getDailyDate()==null||actionServiceDetailsDTO.getRepeat().trim().equalsIgnoreCase("Y")){
						// If service is repeat option,make date  to today and count 1
						if(updateSubscriberProfileDetails(session,profileDTO,"DAILY_COUNT=1","DAILY_DATE")>0)
						{
							transaction.commit();
							break;
						}else{
							transaction.rollback();
						}
						
					}else{
						// Service Does not have repeat
						logger.info("Daily Quota is not repeat for  Subscriber Number : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
						throw new CommonException("Subscriber Not Eligible. Subscriber Number : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
					}
					//	DAILY Check		
				}else if(actionServiceDetailsDTO.getActionType()==2)
				{
					//	 Weekly Check
					if(profileDTO.getWeeklyDate()!=null)
					{
						profileDTO.getWeeklyDate().setHours(0);
						profileDTO.getWeeklyDate().setMinutes(0);
						profileDTO.getWeeklyDate().setSeconds(0);
					}
					
					long weeks=-1;
					
					if(profileDTO.getWeeklyDate()!=null)
					   weeks=(calendar.getTime().getTime()-profileDTO.getWeeklyDate().getTime())/(1000*60*60*24);
					
					if(0<=weeks&&weeks<=6)
					{
						//	record exist for weekly date,check the counts and update
						if(profileDTO.getWeeklyCount()>=actionServiceDetailsDTO.getNoOfTimes())
						{
							// Weekly Count has  crossed
							logger.info("Weekly Count exceeded for the subscriber : "+profileDTO.getSubscriberNumber()+" and Service Name "+profileDTO.getServiceName());
							throw new CommonException("Weekly Count exceeded for the subscriber : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
						}else{
							// Weekly Count available
							if(updateSubscriberProfileDetails(session,profileDTO,"WEEKLY_COUNT=WEEKLY_COUNT+1")>0)
							{
								transaction.commit();
								break;
							}else{
								transaction.rollback();
							}
						}
						
					}else if(profileDTO.getWeeklyDate()==null||actionServiceDetailsDTO.getRepeat().trim().equalsIgnoreCase("Y")){
						// If service is repeat option,make date change to today and count 1
						if(updateSubscriberProfileDetails(session,profileDTO,"WEEKLY_COUNT=1","WEEKLY_DATE")>0)
						{
							transaction.commit();
							break;
						}else{
							transaction.rollback();
						}
						
					}else{
						// Service Does not have repeat
						logger.info("Weekly Quota is not repeat for  Subscriber Number : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
						throw new CommonException("Subscriber Not Eligible. Subscriber Number : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
					}
					//	Weekly Check	
				}else if(actionServiceDetailsDTO.getActionType()==3)
				{
					// Monthly Check
					if(profileDTO.getMonthlyDate()!=null)
					{
						profileDTO.getMonthlyDate().setHours(0);
						profileDTO.getMonthlyDate().setMinutes(0);
						profileDTO.getMonthlyDate().setSeconds(0);
					}
					
					long monthly=-1;
					if(profileDTO.getMonthlyDate()!=null)
					  monthly=(calendar.getTime().getTime()-profileDTO.getMonthlyDate().getTime())/(1000*60*60*24);

					if(0<=monthly&&monthly<=30)
					{
						//	record exist for weekly date,check the counts and update
						if(profileDTO.getMonthlyCount()>=actionServiceDetailsDTO.getNoOfTimes())
						{
							// Weekly Count has  crossed
							logger.info("Monthly Count exceeded for the subscriber : "+profileDTO.getSubscriberNumber()+" and Service Name "+profileDTO.getServiceName());
							throw new CommonException("Monthly Count exceeded for the subscriber : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
						}else{
							// Weekly Count available
							if(updateSubscriberProfileDetails(session,profileDTO,"MONTHLY_COUNT=MONTHLY_COUNT+1")>0)
							{
								transaction.commit();
								break;
							}else{
								transaction.rollback();
							}
						}
						
					}else if(profileDTO.getMonthlyDate()==null||actionServiceDetailsDTO.getRepeat().trim().equalsIgnoreCase("Y")){
						// If service is repeat option,make date change to today and count 1
						if(updateSubscriberProfileDetails(session,profileDTO,"MONTHLY_COUNT=1","MONTHLY_DATE")>0)
						{
							transaction.commit();
							break;
						}else{
							transaction.rollback();
						}
						
					}else{
						// Service Does not have repeat
						logger.info("Monthly Quota is not repeat for  Subscriber Number : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
						throw new CommonException("Subscriber Not Eligible. Subscriber Number : "+profileDTO.getSubscriberNumber()+" and Service Name : "+profileDTO.getServiceName());
					}
					
				}//Monthly Check
				
				
				
				
				loopCount++;
				
			}//while loop	
			
			
			if(loopCount>10)
			{
				logger.info("System is Busy");
				throw new CommonException("System is Busy");
			}
			
				
		}catch (CommonException e) {
			if(transaction!=null&&transaction.isActive())
				transaction.rollback();
			
			throw e;
		}
		catch (Exception e) {
			
			if(transaction!=null&&transaction.isActive())
				transaction.rollback();
			
			e.printStackTrace();
			throw new CommonException("Exception");
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
			
		
	}//updateSubscriberProfileDetails
	
	
	public int updateSubscriberProfileDetails(Session session,SubscriberProfileDTO subscriberProfileDTO,String countColumn)throws Exception
	{
		int result=-1;
		try{
			
			String tableName=new TableInfoDAO().getSubscriberProfileDBTable(subscriberProfileDTO.getSubscriberNumber()+"");
			
			String sql="UPDATE "+tableName+" SET "+countColumn+", COUNTER=? " +
					   " WHERE SUBSCRIBER_NUMBER=? AND SERVICE_ID=? AND COUNTER=? ";
			
			Query query=session.createSQLQuery(sql)
						.setParameter(0,(subscriberProfileDTO.getCounter()>Integer.MAX_VALUE?1:subscriberProfileDTO.getCounter()+1))
						.setParameter(1,subscriberProfileDTO.getSubscriberNumber())
						.setParameter(2,subscriberProfileDTO.getServiceId())
						.setParameter(3,subscriberProfileDTO.getCounter());
			
			result=query.executeUpdate();
			
		}catch (Exception e) {
			throw e;
		}
		
		return result;
		
	}//updateSubscriberProfileDetails
	
	
	public int updateSubscriberProfileDetails(Session session,SubscriberProfileDTO subscriberProfileDTO,String countColumn,String typeColumn)throws Exception
	{
		int result=-1;
		try{
			
			String tableName=new TableInfoDAO().getSubscriberProfileDBTable(subscriberProfileDTO.getSubscriberNumber()+"");
			
			String sql="UPDATE "+tableName+" SET "+countColumn+","+typeColumn+"=?, COUNTER=? " +
			   			" WHERE SUBSCRIBER_NUMBER=? AND SERVICE_ID=? AND COUNTER=?  ";
			
			Query query=session.createSQLQuery(sql)
						.setParameter(0,new Date())
						.setParameter(1,(subscriberProfileDTO.getCounter()>Integer.MAX_VALUE?1:subscriberProfileDTO.getCounter()+1))
						.setParameter(2,subscriberProfileDTO.getSubscriberNumber())
						.setParameter(3,subscriberProfileDTO.getServiceId())
						.setParameter(4,subscriberProfileDTO.getCounter());
			
			result=query.executeUpdate();
			
		}catch (Exception e) {
			throw e;
		}
		
		return result;
		
	}//updateSubscriberProfileDetails
	
	public int updateSubscriberProfileStatus(Session session,SubscriberProfileDTO parentProfileDTO)throws Exception
	{
		int result=-1;
		try{
			
			String tableName=new TableInfoDAO().getSubscriberProfileDBTable(parentProfileDTO.getSubscriberNumber()+"");
			
			String sql="UPDATE "+tableName+" SET STATUS=?, COUNTER=? " +
			   			" WHERE SUBSCRIBER_NUMBER=? AND SERVICE_ID=? AND COUNTER=?  ";
			
			Query query=session.createSQLQuery(sql)
						.setParameter(0,"A")
						.setParameter(1,(parentProfileDTO.getCounter()>Integer.MAX_VALUE?1:parentProfileDTO.getCounter()+1))
						.setParameter(2,parentProfileDTO.getSubscriberNumber())
						.setParameter(3,parentProfileDTO.getServiceId())
						.setParameter(4,parentProfileDTO.getCounter());
			
			result=query.executeUpdate();
			
		}catch (Exception e) {
			throw e;
		}
		
		return result;
		
	}//updateSubscriberProfileDetails
	
	
	public void insertSubscriberProfileDetails(SubscriberProfileDTO subscriberProfileDTO,ActionServiceDetailsDTO actionServiceDetailsDTO,SubscriberProfileDTO parentProfileDTO)throws CommonException
	{
		
		Session session=null;
		Transaction transaction=null;
		try{
			
			subscriberProfileDTO.setServiceId(actionServiceDetailsDTO.getServiceID());
			subscriberProfileDTO.setServiceName(actionServiceDetailsDTO.getServiceName());
			
			if(actionServiceDetailsDTO.getActionType()==1)
			{
				subscriberProfileDTO.setDailyDate(new Date());
				subscriberProfileDTO.setDailyCount(1);
			}else if(actionServiceDetailsDTO.getActionType()==2)
			{
				subscriberProfileDTO.setWeeklyDate(new Date());
				subscriberProfileDTO.setWeeklyCount(1);
			}else if(actionServiceDetailsDTO.getActionType()==3)
			{
				subscriberProfileDTO.setMonthlyDate(new Date());
				subscriberProfileDTO.setMonthlyCount(1);
			}
			
			subscriberProfileDTO.setStartDate(new Date());
			
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			String tableName=new TableInfoDAO().getSubscriberProfileTable(subscriberProfileDTO.getSubscriberNumber()+"");
			session.save(tableName,subscriberProfileDTO);
			
				if(parentProfileDTO.getStatus().equalsIgnoreCase("R")?updateSubscriberProfileStatus(session, parentProfileDTO)>0:true)
					transaction.commit();
				else
					transaction.rollback();
				
		}
		catch (Exception e) {
			if(transaction!=null)
				transaction.rollback();
			e.printStackTrace();
			
			if(e instanceof ConstraintViolationException)
			{
				ConstraintViolationException exception=(ConstraintViolationException)e;
				
				logger.info("Duplicate Insertion happened for "+subscriberProfileDTO.getSubscriberNumber()+" : "+subscriberProfileDTO.getServiceName());
				throw new CommonException(9090,"Duplicate Insertion of "+subscriberProfileDTO.getSubscriberNumber()+" and "+subscriberProfileDTO.getServiceName());
			}
			
			if(e instanceof CommonException)
				throw (CommonException)e;
			else
				throw new CommonException("Exception");
			
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		
		
		
	}//insertSubscriberProfileDetails
	
	
	
	public List<SubscriberProfileDTO> getSubscriberProfileDetails(GenericDTO genericDTO)throws Exception
	{
		Session session=null;
		SubscriberProfileDTO profileDTO=(SubscriberProfileDTO)genericDTO.getObj();
		List<SubscriberProfileDTO> subscriberProfileList=null;
		Transaction transaction=null;
		try{
			subscriberProfileList=new ArrayList<SubscriberProfileDTO>();
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String tableName=new TableInfoDAO().getSubscriberProfileDBTable(profileDTO.getSubscriberNumber()+"");
			
			String sql=" SELECT SUBSCRIBER_NUMBER,SERVICE_ID,SERVICE_NAME,DAILY_DATE,DAILY_COUNT,WEEKLY_DATE,WEEKLY_COUNT,MONTHLY_DATE,MONTHLY_COUNT," +
			   		   " START_DATE,END_DATE,COUNTER,STATUS FROM "+tableName+" WHERE SUBSCRIBER_NUMBER=?";
			
			//logger.info(sql+"   "+profileDTO.getSubscriberNumber());
			
			Query query=session.createSQLQuery(sql)
						.setParameter(0,profileDTO.getSubscriberNumber());
			
		
			List<Object[]> list=query.list();
			
			SubscriberProfileDTO tabDTO=null;
			
			for(Object[] obj:list)
			{
				tabDTO=new SubscriberProfileDTO();
				
				tabDTO=new SubscriberProfileDTO();
				tabDTO.setSubscriberNumber(Long.parseLong(obj[0].toString()));
				tabDTO.setServiceId(Integer.parseInt(obj[1].toString()));
				tabDTO.setServiceName((String)obj[2]);
				tabDTO.setDailyDate((Date)obj[3]);
				tabDTO.setDailyCount(Integer.parseInt(obj[4].toString()));
				tabDTO.setWeeklyDate((Date)obj[5]);
				tabDTO.setWeeklyCount(Integer.parseInt(obj[6].toString()));
				tabDTO.setMonthlyDate((Date)obj[7]);
				tabDTO.setMonthlyCount(Integer.parseInt(obj[8].toString()));
				tabDTO.setStartDate((Date)obj[9]);
				tabDTO.setEndDate((Date)obj[10]);
				tabDTO.setCounter(Long.parseLong(obj[11].toString()));
				tabDTO.setStatus((String)obj[12]);
				
				subscriberProfileList.add(tabDTO);
				
			}	
			
			
			//transaction.commit();
			
			
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		
		return subscriberProfileList;
	}//getSubscriberProfileDetails
	
	
	public SubscriberProfileDTO getSubscriberProfileDetails(SubscriberProfileDTO subscriberProfileDTO)
	{
		Session session=null;
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			
			String tableName=new TableInfoDAO().getSubscriberProfileDBTable(subscriberProfileDTO.getSubscriberNumber()+"");
			
			String sql=" SELECT SUBSCRIBER_NUMBER,SERVICE_ID,SERVICE_NAME,DAILY_DATE,DAILY_COUNT,WEEKLY_DATE,WEEKLY_COUNT,MONTHLY_DATE,MONTHLY_COUNT," +
			   		   " START_DATE,END_DATE,COUNTER,STATUS FROM "+tableName+" WHERE SUBSCRIBER_NUMBER=? AND SERVICE_ID=? ";
			
			logger.info(sql+"   "+subscriberProfileDTO.getSubscriberNumber()+"   "+subscriberProfileDTO.getServiceId());
			
			Query query=session.createSQLQuery(sql)
						.setParameter(0,subscriberProfileDTO.getSubscriberNumber())
						.setParameter(1,subscriberProfileDTO.getServiceId());
			
		
			List<Object[]> list=query.list();
			
			
			for(Object[] obj:list)
			{
				
				subscriberProfileDTO=new SubscriberProfileDTO();
				subscriberProfileDTO.setSubscriberNumber(Long.parseLong(obj[0].toString()));
				subscriberProfileDTO.setServiceId(Integer.parseInt(obj[1].toString()));
				subscriberProfileDTO.setServiceName((String)obj[2]);
				subscriberProfileDTO.setDailyDate((Date)obj[3]);
				subscriberProfileDTO.setDailyCount(Integer.parseInt(obj[4].toString()));
				subscriberProfileDTO.setWeeklyDate((Date)obj[5]);
				subscriberProfileDTO.setWeeklyCount(Integer.parseInt(obj[6].toString()));
				subscriberProfileDTO.setMonthlyDate((Date)obj[7]);
				subscriberProfileDTO.setMonthlyCount(Integer.parseInt(obj[8].toString()));
				subscriberProfileDTO.setStartDate((Date)obj[9]);
				subscriberProfileDTO.setEndDate((Date)obj[10]);
				subscriberProfileDTO.setCounter(Long.parseLong(obj[11].toString()));
				subscriberProfileDTO.setStatus((String)obj[12]);
				
			}	
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		
		return subscriberProfileDTO;
	}//getSubscriberProfileDetails
	
	
	public boolean isValidation(SubscriberProfileDTO parentProfileDTO,String serviceName,ActionServiceDetailsDTO actionServiceDetailsDTO)throws CommonException
	{
		boolean flag=false;
		try{
			Calendar currentDate=Calendar.getInstance();
			
			
			
			if(actionServiceDetailsDTO.getStartDate()==null || actionServiceDetailsDTO.getStartDate().after(currentDate.getTime()))
			{
				logger.info("Service Name : "+actionServiceDetailsDTO.getServiceName()+" start from "+actionServiceDetailsDTO.getStartDate());
				throw new CommonException("Service Name : "+actionServiceDetailsDTO.getServiceName()+" start from "+actionServiceDetailsDTO.getStartDate());
			}
			
			if(actionServiceDetailsDTO.getEndDate()==null || actionServiceDetailsDTO.getEndDate().before(currentDate.getTime()))
			{
				logger.info("Service Expired for Service ID : "+actionServiceDetailsDTO.getServiceID()+"   Service Name : "+actionServiceDetailsDTO.getServiceName()+" on "+actionServiceDetailsDTO.getEndDate());
				throw new CommonException("Service Expired for Service Name : "+actionServiceDetailsDTO.getServiceName()+" on "+actionServiceDetailsDTO.getEndDate());
			}
			
			
		/*	if(actionServiceDetailsDTO.getNoOfTimes()<=0)
			{
				logger.info("Service Name : "+actionServiceDetailsDTO.getServiceName()+" Count is zero ");
				throw new CommonException("Service Name : "+actionServiceDetailsDTO.getServiceName()+" Count is zero ");
			}
			*/
			// checking whether this service has any validation,if all validation fields are null,ie no validation
			if(actionServiceDetailsDTO.getValidity()==null&&actionServiceDetailsDTO.getStartTime()==null&&actionServiceDetailsDTO.getEndTime()==null)
			{
				flag=true;
				return flag;
			}
			
			
			/*SubscriberProfileDTO parentProfileDTO=null;
			
			for(SubscriberProfileDTO subscriberProfileDTO:list)
			{
				// Parent Exist
				if(actionServiceDetailsDTO.getParentID()!=null&&subscriberProfileDTO.getServiceId()==actionServiceDetailsDTO.getParentID())
				{
					parentProfileDTO=subscriberProfileDTO;
				}else if(actionServiceDetailsDTO.getParentID()==null&&subscriberProfileDTO.getServiceId()==actionServiceDetailsDTO.getServiceID())
				{
					parentProfileDTO=subscriberProfileDTO;
				}
				
			}*/
			
			
			if(parentProfileDTO==null)
			{
				logger.info("Service Name : "+actionServiceDetailsDTO.getServiceName()+" "+(actionServiceDetailsDTO.getParentID()!=null?"Parent":"")+" does not exist ");
				throw new CommonException("Service Name : "+actionServiceDetailsDTO.getServiceName()+"  "+(actionServiceDetailsDTO.getParentID()!=null?"Parent":"")+" does not exist ");
			}
			
			
			logger.info("Valide for "+actionServiceDetailsDTO.getValidity()+" "+actionServiceDetailsDTO.getValidityType()+" From "+actionServiceDetailsDTO.getStartTime()+"  To  "+actionServiceDetailsDTO.getEndTime()+" Every Day "+actionServiceDetailsDTO.getEveryDay());
			
			
			
			Calendar validityDate=null;
			Calendar fromDate=null;			
			Calendar toDate=null;
			
			if(actionServiceDetailsDTO.getStartTime()!=null)
			{
				fromDate=Calendar.getInstance();
				fromDate.setTime(parentProfileDTO.getStartDate());
				fromDate.set(Calendar.HOUR_OF_DAY,Integer.parseInt(actionServiceDetailsDTO.getStartTime().split(":")[0]));
				fromDate.set(Calendar.MINUTE,Integer.parseInt(actionServiceDetailsDTO.getStartTime().split(":")[1]));
			}
			
			if(actionServiceDetailsDTO.getEndTime()!=null)
			{
				toDate=Calendar.getInstance();
				toDate.setTime(parentProfileDTO.getStartDate());
				toDate.set(Calendar.HOUR_OF_DAY,Integer.parseInt(actionServiceDetailsDTO.getEndTime().split(":")[0]));
				toDate.set(Calendar.MINUTE,Integer.parseInt(actionServiceDetailsDTO.getEndTime().split(":")[1]));
				
			}
			
			if(actionServiceDetailsDTO.getValidity()!=null&&actionServiceDetailsDTO.getValidityType()!=null)
			{
				if(actionServiceDetailsDTO.getValidityType().trim().equalsIgnoreCase("HOURS"))
				{
					validityDate=Calendar.getInstance();
					validityDate.setTime(parentProfileDTO.getStartDate());
					validityDate.add(Calendar.HOUR_OF_DAY,actionServiceDetailsDTO.getValidity());
				}
				else if(actionServiceDetailsDTO.getValidityType().trim().equalsIgnoreCase("DAYS"))
				{
					validityDate=Calendar.getInstance();
					validityDate.setTime(parentProfileDTO.getStartDate());
					validityDate.add(Calendar.DATE,actionServiceDetailsDTO.getValidity());
					
					if(toDate!=null)
					{
						validityDate.set(Calendar.HOUR_OF_DAY,toDate.get(Calendar.HOUR_OF_DAY));
						validityDate.set(Calendar.MINUTE,toDate.get(Calendar.MINUTE));
					}
					
					
					if(actionServiceDetailsDTO.getEveryDay().trim().equalsIgnoreCase("Y"))
					{
					   	if(currentDate.getTime().before(validityDate.getTime()))
					   	{
					   		if(fromDate!=null)
					   			fromDate.set(Calendar.DATE, currentDate.get(Calendar.DATE));
					   		if(toDate!=null)
								toDate.set(Calendar.DATE, currentDate.get(Calendar.DATE));
					   	}
					}else{
						if(toDate!=null)
							toDate.add(Calendar.DATE,actionServiceDetailsDTO.getValidity());
					}
				}
					
			}
			
		/*	logger.info("Current time "+dateFormat.format(currentDate.getTime()));
			logger.info("Parent Start Date "+dateFormat.format(parentProfileDTO.getStartDate()));
			if(validityDate!=null)
			  logger.info("Validity Date "+dateFormat.format(validityDate.getTime()));
			if(fromDate!=null)
				logger.info(" From Date "+dateFormat.format(fromDate.getTime()));
			if(toDate!=null)
				logger.info("To date "+dateFormat.format(toDate.getTime()));*/
			
			if(validityDate!=null)
			{
				if(currentDate.getTime().before(validityDate.getTime()))
					flag=true;
				
				if(fromDate!=null&&toDate==null)
					flag=flag&&checkStartTime(fromDate.getTime());
				else if(fromDate==null&&toDate!=null)
					flag=flag&&checkEndTime(toDate.getTime());
				else if(fromDate!=null&&toDate!=null)
					flag=flag&&checkStart_EndDates(fromDate.getTime(),toDate.getTime());
				
			}else{
				
				if(fromDate!=null&&toDate==null)
					flag=checkStartTime(fromDate.getTime());
				else if(fromDate==null&&toDate!=null)
					flag=checkEndTime(toDate.getTime());
				else if(fromDate!=null&&toDate!=null)
					flag=checkStart_EndDates(fromDate.getTime(),toDate.getTime());
			}
			
			
			
		}catch (CommonException e) {
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CommonException("Exception");
		}
		return flag;
	}//validation
	
	
	public boolean checkStartTime(Date startTime)
	{
		
		return new Date().after(startTime);
		
	}//checkStartTime
	
	public boolean checkEndTime(Date endTime)
	{
		
		return new Date().before(endTime);
		
	}//checkEndTime
	
	
	public boolean checkStart_EndDates(Date startTime,Date endTime)
	{
		return checkStartTime(startTime)&&checkEndTime(endTime);
		
	}//checkStart_EndDates

	

}

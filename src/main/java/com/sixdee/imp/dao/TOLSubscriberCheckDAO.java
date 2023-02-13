package com.sixdee.imp.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.TOLSubscriberCheckDTO;
import com.sixdee.imp.dto.TOLSubscriberDTO;

public class TOLSubscriberCheckDAO {
	
	private Logger logger=Logger.getLogger(TOLSubscriberCheckDAO.class);

	public boolean checkTOL(TOLSubscriberCheckDTO subscriberCheckDTO)
	{
		boolean flag=false;
		int retryCounter=1;
		TOLSubscriberDTO subscriberDTO=null;
		try{
			
			
			while(retryCounter<3)
			{
				retryCounter++;
				subscriberDTO=selectTOLSubscriberInfo(subscriberCheckDTO);
				
				if(subscriberDTO!=null)
				{ 
					if(subscriberCheckDTO.getActionType().equalsIgnoreCase("CR"))
					{
						if(subscriberDTO.getCredit()==subscriberDTO.getDebit())
						{
							if(subscriberDTO.getDebitStatus().trim().equalsIgnoreCase("S"))
							{
								int status = updateTOLSubscriberInfo(subscriberCheckDTO.getRequestId(),"UPDATE TOL_SUBSCRIBER_CHECK SET CREDIT=1,CREDIT_STATUS='P',COUNTER=COUNTER+1 WHERE SCHEDULE_ID='"+subscriberCheckDTO.getScheduleID()+"' and MSISDN='"+subscriberCheckDTO.getSubscriberNumber()+"' and COUNTER="+subscriberDTO.getCounter());
								if(status==0)
									continue;
								else
								{
									flag = true;
									break;
								}
								
							}
							else if (subscriberDTO.getCreditStatus().trim().equalsIgnoreCase("F"))
							{
								
								int status = updateTOLSubscriberInfo(subscriberCheckDTO.getRequestId(),"UPDATE TOL_SUBSCRIBER_CHECK SET CREDIT=1,CREDIT_STATUS='P',COUNTER=COUNTER+1 WHERE SCHEDULE_ID='"+subscriberCheckDTO.getScheduleID()+"' and MSISDN='"+subscriberCheckDTO.getSubscriberNumber()+"' and COUNTER="+subscriberDTO.getCounter());
								if(status==0)
									continue;
								else
								{
									flag = true;
									break;
								}
							}
							else
							{
								flag = false;
								break;
							}
						}
						else
						{
							flag = false;
							break;
						}
						 
					}else
					{
						if(subscriberDTO.getCredit()>subscriberDTO.getDebit())
						{
							if(subscriberDTO.getCreditStatus().trim().equalsIgnoreCase("S"))
							{
								
								int status = updateTOLSubscriberInfo(subscriberCheckDTO.getRequestId(),"UPDATE TOL_SUBSCRIBER_CHECK SET DEBIT=1,DEBIT_STATUS='P',COUNTER=COUNTER+1 WHERE SCHEDULE_ID='"+subscriberCheckDTO.getScheduleID()+"' and MSISDN='"+subscriberCheckDTO.getSubscriberNumber()+"' and COUNTER="+subscriberDTO.getCounter());
								if(status==0)
									continue;
								else
								{
									flag = true;
									break;
								}
								
							}
							else if (subscriberDTO.getDebitStatus().trim().equalsIgnoreCase("F"))
							{
								flag = true;
								int status = updateTOLSubscriberInfo(subscriberCheckDTO.getRequestId(),"UPDATE TOL_SUBSCRIBER_CHECK SET DEBIT=1,DEBIT_STATUS='P',COUNTER=COUNTER+1 WHERE SCHEDULE_ID='"+subscriberCheckDTO.getScheduleID()+"' and MSISDN='"+subscriberCheckDTO.getSubscriberNumber()+"' and COUNTER="+subscriberDTO.getCounter());
								if(status==0)
									continue;
								else
								{
									flag = true;
									break;
								}
							}
							else
							{
								flag = false;
								break;
							}
						} 
						else
						{
							flag = false;
							break;
						}
					}
					
				}else{
					
					if(insertTOLSubscriberInfo(subscriberCheckDTO))
					{
						flag=true;
						break;
					}else{
						 
						continue;
					}
				}
				
			}
			
			
			
			
		}catch (Exception e) {
			logger.info(subscriberCheckDTO.getRequestId()+" : ",e);
			
		}finally{
			insertTransaction(subscriberCheckDTO,subscriberDTO,flag);
			subscriberDTO=null;
		}

		logger.info(subscriberCheckDTO.getRequestId()+" : Final "+subscriberCheckDTO+" Flag "+flag);
		
		//insertTransaction("INSERT INTO TOL_SUBS_CHECK_TRAN (SCHEDULE_ID,MSISDN,ACTION,STATUS,CREDIT,DEBIT,CREDIT_STATUS,DEBIT_STATUS) VALUES ('"+subscriberCheckDTO.getScheduleID()+"','"+subscriberCheckDTO.getSubscriberNumber()+"','"+subscriberCheckDTO.getActionType()+"','"+(flag?"SUCCESS":"FAILURE")+"')");
		
		
		return flag;
	}//checkTOL
	
	
	private boolean insertTOLSubscriberInfo(TOLSubscriberCheckDTO checkDTO) throws Exception
	{
		boolean flag=false;
		Session session=null;
		Transaction transaction=null;
		Query query=null;
		try{
		
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			String sql="insert into TOL_SUBSCRIBER_CHECK (ID,SCHEDULE_ID,MSISDN,CREDIT,DEBIT,CREDIT_STATUS,DEBIT_STATUS) values" +
					   " (TOL_CHECK_SEQ.NEXTVAL,?,?,?,?,?,?)";
			query=session.createSQLQuery(sql);
			query.setParameter(0,checkDTO.getScheduleID());
			query.setParameter(1,checkDTO.getSubscriberNumber());
			if(checkDTO.getActionType().equalsIgnoreCase("CR"))
			{	
				query.setParameter(2,1);
				query.setParameter(3,0);
				query.setParameter(4,"P");
				query.setParameter(5,"S");
			}else{
				query.setParameter(2,1);
				query.setParameter(3,1);
				query.setParameter(4,"S");
				query.setParameter(5,"P");
			}
			

			logger.info(checkDTO.getRequestId()+" : insert "+checkDTO);
			
			logger.info(checkDTO.getRequestId()+" : Insert Count "+query.executeUpdate());
			
			transaction.commit();
			flag=true;
			
		}catch (Exception e) {
			logger.info(checkDTO.getRequestId()+" ",e);
			if(transaction!=null)
				transaction.rollback();
			throw e;
		}finally{
			if(session!=null)
				session.close();
			session=null;
			transaction=null;
			query=null;
				
		}
		return flag;
	}
	
	private int updateTOLSubscriberInfo(String transactionID,String sql) throws Exception
	{
		int updateCount=0;
		Session session=null;
		Transaction transaction=null;
		try{
		
			//String sql="UPDATE TOL_SUBSCRIBER_CHECK SET PROCESSED_DATE=?,COUNTER=? where SCHEDULE_ID=? and MSISDN=? and TYPE_OF_ACTION=? AND COUNTER=? " ;
			session=HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			
			logger.info(transactionID+" : "+sql);
			Query query=session.createSQLQuery(sql);
			
			/*query.setParameter(0,new Date());
			query.setParameter(1,(checkDTO.getCounter()>=Integer.MAX_VALUE?1:checkDTO.getCounter()+1));
			query.setParameter(2,checkDTO.getScheduleID());
			query.setParameter(3,checkDTO.getSubscriberNumber());
			query.setParameter(4,checkDTO.getActionType().toUpperCase());
			query.setParameter(5,checkDTO.getCounter());*/
			
			
			updateCount=query.executeUpdate();
			logger.info(transactionID+" : Update Count "+updateCount);
			
			if(updateCount>0)
				transaction.commit();
			else
				transaction.rollback();
			
		}catch (Exception e) {
			logger.info(transactionID+" ",e);
			if(transaction!=null)
				transaction.rollback();
			throw e;
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			transaction=null;
		}
		
		return updateCount;
	}
	
	
	private TOLSubscriberDTO selectTOLSubscriberInfo(TOLSubscriberCheckDTO checkDTO) throws Exception
	{
		Session session=null;
		List<Object[]> list=null;
		TOLSubscriberDTO subscriberDTO=null;
		try{
		
			session=HiberanteUtil.getSessionFactory().openSession();
			
			/*String sql="select PROCESSED_DATE,COUNTER FROM " +
					   " TOL_SUBSCRIBER_CHECK WHERE SCHEDULE_ID=? and MSISDN=? and TYPE_OF_ACTION=? " ;*/
			
			String sql="select CREDIT,DEBIT,CREDIT_STATUS,DEBIT_STATUS,COUNTER FROM " +
			   " TOL_SUBSCRIBER_CHECK WHERE SCHEDULE_ID=? and MSISDN=? " ;
			
			Query query=session.createSQLQuery(sql);
			query.setParameter(0,checkDTO.getScheduleID());
			query.setParameter(1,checkDTO.getSubscriberNumber());
			
			list=query.list();
			
			if(list!=null&&list.size()>0)
			{
				Object[] objects=list.get(0);
				subscriberDTO=new TOLSubscriberDTO();
				subscriberDTO.setSubscriberNumber(checkDTO.getSubscriberNumber());
				subscriberDTO.setScheduleID(checkDTO.getScheduleID());
				subscriberDTO.setCredit(Integer.parseInt(objects[0].toString()));
				subscriberDTO.setDebit(Integer.parseInt(objects[1].toString()));
				subscriberDTO.setCreditStatus(objects[2].toString());
				subscriberDTO.setDebitStatus(objects[3].toString());
				subscriberDTO.setCounter(Integer.parseInt(objects[4].toString()));
				
				logger.info(checkDTO.getRequestId()+" : SELECT : "+subscriberDTO);
			}
			
		}catch (Exception e) {
			logger.info("",e);
			throw e;
		}finally{
			if(session!=null)
				session.close();
			session=null;
			list=null;
		}
		
		return subscriberDTO;
	}
	
	
	public boolean checkTOLOld(TOLSubscriberCheckDTO subscriberCheckDTO)
	{
		boolean flag=false;
		Session session=null;
		Transaction transaction=null;
		int retryCounter=10;
		
		try{/*
			
			session=HiberanteUtil.getSessionFactory().openSession();
			
			while(retryCounter>0)
			{
				retryCounter--;
				
				subscriberCheckDTO=selectTOLSubscriberInfo(subscriberCheckDTO, session);
				
				if(subscriberCheckDTO.getDate()!=null)
				{
					Date date=new Date();
					int diffsec=(int)(date.getTime()-subscriberCheckDTO.getDate().getTime())/(1000);
					logger.info(subscriberCheckDTO.getRequestId()+" : Diff in Sec "+diffsec+" Interval is "+(subscriberCheckDTO.getInterval()*60));
					if(diffsec>(subscriberCheckDTO.getInterval()*60))
					{
						//transaction=session.beginTransaction();
						int updateCount=updateTOLSubscriberInfo(subscriberCheckDTO, session);
						if(updateCount>0)
						{
							flag=true;
							//transaction.commit();
							break;
							
						}else{
							//transaction.rollback();
							continue;
						}
					}else{
						flag=false;
						break;
					}
					
				}else{
					
					transaction=session.beginTransaction();
					subscriberCheckDTO.setDate(new Date());
					subscriberCheckDTO.setCounter(1);
					if(insertTOLSubscriberInfo(subscriberCheckDTO, session))
					{
						flag=true;
						transaction.commit();
						break;
					}else{
						transaction.rollback();
						continue;
					}
				}
				
			}
			
			
			
			
		*/}catch (Exception e) {
			logger.info(subscriberCheckDTO.getRequestId()+" : ",e);
			if(transaction!=null)
				transaction.rollback();
			
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			transaction=null;
		}

		logger.info(subscriberCheckDTO.getRequestId()+" : Final "+subscriberCheckDTO+" Flag "+flag);
		
		return flag;
	}//checkTOL
	
	public void insertTransaction(TOLSubscriberCheckDTO checkDTO,TOLSubscriberDTO subscriberDTO,boolean flag)
	{
		Session session=null;
		Transaction transaction=null;
		Query query = null;
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			String sql = "INSERT INTO TOL_SUBS_CHECK_TRAN (SCHEDULE_ID,MSISDN,ACTION,STATUS,CREDIT,DEBIT,CREDIT_STATUS,DEBIT_STATUS) VALUES (?,?,?,?,?,?,?,?)";
			query = session.createSQLQuery(sql);
			query.setParameter(0, checkDTO.getScheduleID());
			query.setParameter(1, checkDTO.getSubscriberNumber());
			query.setParameter(2, checkDTO.getActionType());
			query.setParameter(3, flag?"SUCCESS":"FAILURE");
			
			if(subscriberDTO!=null)
			{
				query.setParameter(4, subscriberDTO.getCredit());
				query.setParameter(5, subscriberDTO.getDebit());
				query.setParameter(6, subscriberDTO.getCreditStatus());
				query.setParameter(7, subscriberDTO.getDebitStatus());
			}
			else
			{
			if(checkDTO.getActionType().equalsIgnoreCase("CR"))
			{	
				query.setParameter(4,1);
				query.setParameter(5,0);
				query.setParameter(6,"P");
				query.setParameter(7,"S");
			}else{
				query.setParameter(4,1);
				query.setParameter(5,1);
				query.setParameter(6,"S");
				query.setParameter(7,"P");
			}
			}
			
/*			query.setParameter(4, subscriberDTO.getCredit());
			query.setParameter(5, subscriberDTO.getDebit());
			query.setParameter(6, subscriberDTO.getCreditStatus());
			query.setParameter(7, subscriberDTO.getDebitStatus());
*/	
			
			query.executeUpdate();
			
			transaction.commit();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			if(transaction!=null)
				transaction.rollback();
				transaction = null;
		}
		finally
		{
			if(session!=null)
			{
				try
				{
					session.close();
					session=null;
				}
				catch (Exception e) {
				}
				query=null;
			}
		}
	}
	
	
}

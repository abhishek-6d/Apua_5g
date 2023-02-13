/**
 * 
 */
package com.sixdee.lms.bo;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.RequestParseXML;
import com.sixdee.imp.utill.Response;
import com.sixdee.lms.dto.OnlineTriggerTableDTO;
import com.sixdee.lms.dto.persistent.ExtNotificationDTO;
import com.sixdee.lms.thirdPartyCall.CallThirdParty;
import com.sixdee.lms.util.constant.SystemConstants;



/**
 * @author rahul.kr
 *
 */
public class InitiateRECallBO {
	
	private static final Logger logger = Logger.getLogger("InitateRECallBO");
	
	
	

	public Response createRequestXML(ExtNotificationDTO extNotificationDTO,Request request) {
		
		ArrayList<OnlineTriggerTableDTO> onlineTableDTOList = null;
		OnlineTriggerTableDTO onlineDTO = null;
		String reqXml = null;
		CallThirdParty callThirdParty = null;
		Response response = null;
		try
		{
			logger.info(" TriggerDAO ... inside  createRequestXML ");
			if(Cache.getTriggerTableMap() !=null){
				logger.info("Online Trigger table map "+Cache.getTriggerTableMap()+ " Trigger detaisl "+extNotificationDTO.getTriggerId());
				onlineTableDTOList=Cache.getTriggerTableMap().get(extNotificationDTO.getTriggerId());
			if(onlineTableDTOList!=null){
				logger.info("Size of List:"+onlineTableDTOList.size());
				//insertTriggerTable(onlineDTO,onlineTrigDTO);
				
				for(int i=0;i<onlineTableDTOList.size();i++)
					{
					onlineDTO=onlineTableDTOList.get(i);
					logger.info("RULE ID:"+onlineDTO.getRuleId()+" :: "+"PLAN ID:"+onlineDTO.getPlanId());
				
					//insertTriggerTable(onlineDTO,onlineTrigDTO);
					request.getDataSet().getParameter1().add(new Param(SystemConstants.TRIGGER_NAME,extNotificationDTO.getTrigger()));
					request.getDataSet().getParameter1().add(new Param(SystemConstants.RE_SERVICE_ID,onlineDTO.getTriggerId()+""));
					request.getDataSet().getParameter1().add(new Param("ID","1"));

					request.getDataSet().getParameter1().add(new Param(SystemConstants.RE_SCHEULE_ID,onlineDTO.getRuleId()+""));
					//request.getDataSet().getParameter1().add(new Param(SystemConstants.TRIGGER_NAME,onlineDTO.getTriggerName()));
					
					reqXml=RequestParseXML.getRequest().toXML(request);
				
					logger.info("Request XML to RE:"+reqXml);
					callThirdParty = new CallThirdParty();
					String respXML=callThirdParty.makeThirdPartyCall(reqXml, onlineDTO.getRuleURL(), 5000);
					logger.info("Response from ThirdParty :"+respXML);
					response =  (Response) RequestParseXML.responseXstream().fromXML(respXML);
					onlineDTO=null;
				
					}
				}
			}
			
			else {
				logger.info("error -------->the size of the list onlineTableDTOList is NULL ");
				//insertTriggerTableForNullRuleId(onlineTrigDTO);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			
		}
		return response;
	}

	/*private void insertTriggerTableForNullRuleId1(OnlineTriggerTableDTO onlineTrigDTO) {

		Session session = null;
		Transaction tx = null;
		TriggerDetailsTableDTO triggerDetailsTableDTO ;  
		
		try {
			//long start = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			triggerDetailsTableDTO = new TriggerDetailsTableDTO(); 
			String entityName = getTriggerTable(onlineTrigDTO);
			triggerDetailsTableDTO.setAmount(onlineTrigDTO.getAmount());
			triggerDetailsTableDTO.setMsisdn(onlineTrigDTO.getMsisdn());
			triggerDetailsTableDTO.setProductKey(onlineTrigDTO.getProductOrderKey());
			triggerDetailsTableDTO.setProductOrderKey(onlineTrigDTO.getProductOrderKey());
			triggerDetailsTableDTO.setStatus(onlineTrigDTO.getStatusDesc());
			triggerDetailsTableDTO.setTriggerID(onlineTrigDTO.getTriggerID());
			triggerDetailsTableDTO.setTriggerName(Cache.getTriggerIDMap().get(onlineTrigDTO.getTriggerID()));
			
			session.save(entityName,triggerDetailsTableDTO);
			logger.info("Values Inserted in ["+entityName+"] - TriggerID:["+triggerDetailsTableDTO.getTriggerID()+"],msisdn["+onlineTrigDTO.getMsisdn()+"],amount:["+onlineTrigDTO.getAmount()+"]");
			tx.commit();
			
			//long finaltime = System.currentTimeMillis() - start;
			//logger.info("Time taken for Inserting into triggerDetailsTableDTO is  :" + finaltime);
			
		}                            
		catch (HibernateException e) {
			e.printStackTrace();
			logger.error("Hibernate Exception occured ", e);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here =  =>", e);
			logger.info("exception in triggerDetailsTableDTO");
		} finally {
			
			triggerDetailsTableDTO=null;
			onlineTrigDTO=null;
			
			if (session != null)
				session.close();
				session=null;
		}
		
	}
	
	private void insertTriggerTableForNullRuleId(OnlineTriggerDTO onlineTrigDTO) {
		
		Session session=null;
		Transaction transaction=null;
		String tableName =null;
		String sql=null;
		for (int i = 1; i <= 2;) {
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction=session.beginTransaction();
			tableName = getTriggerDBTable(onlineTrigDTO);
			sql="insert into "+tableName+" PARTITION (PART"+mnthFrmt.format(myDate)+dateFrmt.format(myDate)+") (TRIGGER_ID, TRIGGER_NAME, AMOUNT, PRODUCT_KEY, PRODUCT_ORDER_KEY, STATUS, MSISDN, CREATE_DATE, PART_INDEX, ID)" +
					   " values ('"+onlineTrigDTO.getTriggerID()+"','"+Cache.getTriggerIDMap().get(onlineTrigDTO.getTriggerID())+"','"+onlineTrigDTO.getAmount()+"','"+onlineTrigDTO.getProductKey()+"'," +
					   		"'"+onlineTrigDTO.getProductOrderKey()+"','"+onlineTrigDTO.getStatusDesc()+"','"+onlineTrigDTO.getMsisdn()+"',TO_DATE('" +dateFormat.format(new Date())+"','DD-MM-YYYY'),'" +mnthFrmt.format(myDate)+dateFrmt.format(myDate)+"'," +
					   				"TRIGGER_DETAIL_SEQ_"+onlineTrigDTO.getCallingNumber().substring(onlineTrigDTO.getCallingNumber().length()-1)+".nextVal)";
			int x=session.createSQLQuery(sql).executeUpdate();
			logger.info("Inserted:"+x);
			transaction.commit();
			session.close();
			session = null;
			logger.info("Transaction completed:");
			i = 4;
		}
		catch (Exception e) {
			logger.error("Exception Details: TRIGGER NAME["+Cache.getTriggerIDMap().get(onlineTrigDTO.getTriggerID())+"],TRIGGER_ID["+onlineTrigDTO.getTriggerID()+"],MSISDN["+onlineTrigDTO.getMsisdn()+"],AMOUNT["+onlineTrigDTO.getAmount()+"]");
			logger.info("",e);
			if(transaction!=null) {
				try {
				transaction.rollback();
				} catch(Exception ex) {
					logger.error("Transaction was interrupted."+ex);
					i++;
				}
			}
		}
		finally{
				if(session!=null) {
				try{
					session.close();
					session=null;
				} catch (Exception ex) {
					logger.error("Session Close Exception" +ex);
				  }
				}
				transaction=null;
				tableName=null;
				sql=null;
			}
		}
	}

	private void insertTriggerTable1(OnlineTriggerTableDTO onlineDTO, OnlineTriggerDTO onlineTrigDTO) {
		

		Session session = null;
		Transaction tx = null;
		TriggerDetailsTableDTO triggerDetailsTableDTO ;  
		
		try {
			//long start = System.currentTimeMillis();
			session = HiberanteUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			triggerDetailsTableDTO = new TriggerDetailsTableDTO(); 
			String entityName = getTriggerTable(onlineTrigDTO);
			triggerDetailsTableDTO.setAmount(onlineTrigDTO.getAmount());
			triggerDetailsTableDTO.setMsisdn(onlineTrigDTO.getMsisdn());
			triggerDetailsTableDTO.setMarketingPlanID(onlineDTO.getPlanId()+"");
			triggerDetailsTableDTO.setProductKey(onlineTrigDTO.getProductKey());
			triggerDetailsTableDTO.setProductOrderKey(onlineTrigDTO.getProductOrderKey());
			triggerDetailsTableDTO.setScheduleID(onlineDTO.getRuleId()+"");
			triggerDetailsTableDTO.setStatus(onlineTrigDTO.getStatusDesc());
			triggerDetailsTableDTO.setTriggerID(onlineTrigDTO.getTriggerID());
			triggerDetailsTableDTO.setTriggerName(Cache.getTriggerIDMap().get(onlineTrigDTO.getTriggerID()));
			
			
			session.save(entityName,triggerDetailsTableDTO);
			logger.info("Values Inserted in ["+entityName+"] - TriggerID:["+triggerDetailsTableDTO.getTriggerID()+"],msisdn["+onlineTrigDTO.getMsisdn()+"],amount:["+onlineTrigDTO.getAmount()+"]ScheduleID:["+onlineDTO.getRuleId()+"]");
			tx.commit();
			
			//long finaltime = System.currentTimeMillis() - start;
			//logger.info("Time taken for Inserting into triggerDetailsTableDTO is  :" + finaltime);
			
		}                            
		catch (HibernateException e) {
			e.printStackTrace();
			logger.error("Hibernate Exception occured ", e);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception here =  =>", e);
			logger.info("exception in triggerDetailsTableDTO");
		} finally {
			
			triggerDetailsTableDTO=null;
			onlineDTO=null;
			onlineTrigDTO=null;
			
			if (session != null)
				session.close();
				session=null;
		}
		
	}
	
	
	//

	private synchronized void insertTriggerTable(OnlineTriggerTableDTO onlineDTO, OnlineTriggerDTO onlineTrigDTO) {
		
	Session session=null;
	Transaction transaction=null;
	String tableName =null;
	String sql=null;
	for (int i = 1; i <= 2;) { 
		//'"+onlineDTO.getRuleId()+"','"+onlineDTO.getPlanId()+"'
	try{
		session = HiberanteUtil.getSessionFactory().openSession();
		transaction=session.beginTransaction();
		tableName = getTriggerDBTable(onlineTrigDTO);
		
		sql="insert into "+tableName+" PARTITION (PART"+mnthFrmt.format(myDate)+dateFrmt.format(myDate)+") (TRIGGER_ID, TRIGGER_NAME, AMOUNT, PRODUCT_KEY, PRODUCT_ORDER_KEY, SCHEDULE_ID, MARKETINGPLAN_ID, STATUS, MSISDN, CREATE_DATE, PART_INDEX, ID)" +
				   " values ('"+onlineTrigDTO.getTriggerID()+"','"+Cache.getTriggerIDMap().get(onlineTrigDTO.getTriggerID())+"','"+onlineTrigDTO.getAmount()+"','"+onlineTrigDTO.getProductKey()+"'," +
				   		"'"+onlineTrigDTO.getProductOrderKey()+"','','','"+onlineTrigDTO.getStatusDesc()+"','"+onlineTrigDTO.getMsisdn()+ "',TO_DATE('" +dateFormat.format(new Date())+"','DD-MM-YYYY'),'" +mnthFrmt.format(myDate)+dateFrmt.format(myDate)+"'," +
				   				"TRIGGER_DETAIL_SEQ_"+onlineTrigDTO.getCallingNumber().substring(onlineTrigDTO.getCallingNumber().length()-1)+".nextVal)";
		int x=session.createSQLQuery(sql).executeUpdate();
		logger.info("Inserted:"+x);
		transaction.commit();
		logger.info("Transaction completed:");
		i = 4;
	}catch (Exception e) {
		logger.error("Exception Details: TRIGGER NAME["+Cache.getTriggerIDMap().get(onlineTrigDTO.getTriggerID())+"],TRIGGER_ID["+onlineTrigDTO.getTriggerID()+"],MSISDN["+onlineTrigDTO.getMsisdn()+"],AMOUNT["+onlineTrigDTO.getAmount()+"],RULE_ID[],PLAN_ID[]");
		e.printStackTrace();
		
		try {
			i++;
			if (session.isOpen() && transaction != null) {
				transaction.rollback();
			}
			if (session != null) {
				session.close();
			}
		} catch (Exception localException1) {
			logger.error("Transaction was interrupted.");
			e.printStackTrace();
		}
	}
	finally{
			if(session!=null) {
			try {
				session.close();
				session=null;
			} catch(Exception e) {
				logger.error("Session Close Exception");
			}
		  }
			transaction=null;
			tableName=null;
			sql=null;
		}
	 }
	}
	

	//

	private String getTriggerTable(OnlineTriggerDTO onlineTrigDTO) {

		String msisdn = onlineTrigDTO.getCallingNumber();
		String tablePrefix="TRIGGER_DETAILS_ENTITY";
		int index=Integer.parseInt(Cache.getCacheMap().get("TRIGGER_TABLE_SUFFIX_LENGTH"));
		
		String tableName="_0";
		if(index>0)
		 tableName="_"+msisdn.substring(msisdn.length()-index);
		
		return tablePrefix+tableName;
	}
	
	public String getTriggerDBTable(OnlineTriggerDTO onlineTrigDTO)
	{
		String msisdn = onlineTrigDTO.getCallingNumber();
		String tablePrefix="TRIGGER_DETAILS";
		int index=Integer.parseInt(Cache.getCacheMap().get("TRIGGER_TABLE_SUFFIX_LENGTH"));
		String tableName="_0";
		if(index>0)
		  tableName="_"+msisdn.substring(msisdn.length()-index);
		
		return tablePrefix+tableName;
		
	}
		*/
}

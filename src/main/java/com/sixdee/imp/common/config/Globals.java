package com.sixdee.imp.common.config;

import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.sixdee.imp.common.dao.LoyaltyInfoDAO;
import com.sixdee.imp.common.dao.ServiceDAO;
import com.sixdee.imp.common.dao.VoucherInfoDAO;
import com.sixdee.imp.common.dto.INServiceDetailsDTO;
import com.sixdee.imp.common.dto.Request;
import com.sixdee.imp.common.dto.SecurityTokenDto;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.HiberanteUtilRule;
import com.sixdee.imp.common.util.LogUtil;
import com.sixdee.imp.dto.AccountTypeWisePackDTO;
import com.sixdee.imp.dto.ActionServiceDetailsDTO;
import com.sixdee.imp.dto.CategoryDetailsDTO;
import com.sixdee.imp.dto.ConfigureParameterDTO;
import com.sixdee.imp.dto.EmailInfoDTO;
import com.sixdee.imp.dto.InstantRewardConfigDTO;
import com.sixdee.imp.dto.InterfaceMasterTabDto;
import com.sixdee.imp.dto.LanguageDTO;
import com.sixdee.imp.dto.RewardPointsCategoryDTO;
import com.sixdee.imp.dto.ServiceStatusDTO;
import com.sixdee.imp.dto.TOLSubscriberCheckDTO;
import com.sixdee.imp.dto.TierInfoDTO;
import com.sixdee.imp.dto.featureMappingDTO;
import com.sixdee.imp.dto.hbmdto.AccountTypeConfigDTO;
import com.sixdee.imp.service.httpcall.dto.DataSet;
import com.sixdee.imp.service.httpcall.dto.Parameters;
import com.sixdee.imp.service.httpcall.dto.SubscriberDataSet;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequest;
import com.sixdee.imp.service.httpcall.dto.SubscriberRequestParam;
import com.sixdee.lms.dto.OnlineTriggerTableDTO;
import com.sixdee.lms.dto.persistent.ExtNotificationDTO;
import com.sixdee.lms.dto.persistent.TriggerDetailsDTO;
import com.sixdee.ussd.dto.TierLanguageMappingDTO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
@Component
public class Globals 
{
	private static Logger log = Logger.getLogger(Globals.class);
	public static final String	SYSTEM_PROPERTIES_FILE	= "System.properties";
	
	@PostConstruct
	public static void reloadServiceDetails()
	{
		loadServiceDetailsMap();
		
		
	}//reloadServiceDetails
	@PostConstruct
	public static void reload()
	{
		cacheMap();
	}
	
	@PostConstruct
	public static void cacheMap()
	{
		
		log.info("======CacheMap========");
		loadProperties();
		//loadLanguageDetails();
		loadConfigParameters();
		//loadAccountCategories();
		//loadDBMap();
		loadStatusDetails();
		loadCacheDetails();
		//loadMORouterXstream();  // creating xstream object for morouter
		//loadSubscriberProfileXstream(); // creating xstream object for subsciber profile
		//loadTOLXstream();// creating xstream object for Trust of Lifetime
		//loadCommonReqXstream(); // Creating Xstrem object for all request which are coming from RE
		//loadCommonResXstream(); // Creating Xstrem object for all response which are coming from RE
		//loadGetLanguageXstream(); 
		loadRewardPointsCategory();
		loadRewardPointsDetails();
		loadTierInfoDetails();
		loadTierLanguageInfoDetails();
		//loadServiceDetailsMap();
		//loadParentServiceDetails();
		loadAccountTypeMap();
		loadServiceStatusDetails();
		//loadInstantRewardBuketConfig();
//		new InstantRewardsDAO().fillBucket();
		loadChannelDetails();
		loadOfferTypeDetails();
		loadTelecomOfferCategoryMap();
		loadAccountCategoryMap();
		//loadEmailInfo();
		//loadINServiceDetails();
		//loadLoyaltyRegPackDetails();
		//loadOfferDetails();
		//loadFeatureClasses();
		loadOnlineTriggers();
		loadInterfaceDetails();
		loadSecurityTokenDetails();
		loadReTriggers();
		loadExtTriggerMaster();
	}
	
	
	private static void loadExtTriggerMaster() {
		log.info("*****loadExtTriggerMaster****");
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		HashMap<String, ExtNotificationDTO> map = null;
		try {
			map = new HashMap<String, ExtNotificationDTO>();
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(ExtNotificationDTO.class);
			List<ExtNotificationDTO> list = criteria.list();
			for (ExtNotificationDTO extNotificationDTO : list) {
				
				map.put(extNotificationDTO.getKeyword(), extNotificationDTO);
			}
			transaction.commit();
			Cache.setExtTriggerMasterMap(map);
			log.info("loadExtTriggerMaster :"+map);
		} catch (HibernateException h) {
			if (transaction != null) {
				transaction.rollback();
			}

		} catch (Exception e) {
			log.error("Exception in loadExtTriggerMaster " + e);
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	private static void loadReTriggers() {
		log.info("*****loadReTriggers****");
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		HashMap<String, TriggerDetailsDTO> map = null;
		try {
			map = new HashMap<String, TriggerDetailsDTO>();
			session = HiberanteUtilRule.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(TriggerDetailsDTO.class);
			List<TriggerDetailsDTO> list = criteria.list();
			for (TriggerDetailsDTO triggerDetailsDTO : list) {
				
				map.put(triggerDetailsDTO.getTriggerName(), triggerDetailsDTO);
			}
			transaction.commit();
			Cache.setReTriggersMap(map);
			log.info("loadReTriggers ::"+map);
		} catch (HibernateException h) {
			if (transaction != null) {
				transaction.rollback();
			}

		} catch (Exception e) {
			log.error("Exception in loadReTriggers " + e);
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	private static void loadAccountCategories() {
		HashMap<String,String> accountCategories = new HashMap<String, String>();
		List<AccountTypeConfigDTO> accountTypeDTOList = null;
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			criteria = session.createCriteria(AccountTypeConfigDTO.class);
			accountTypeDTOList = criteria.list();
			for(AccountTypeConfigDTO accountTypeConfigDTO : accountTypeDTOList){
				accountCategories.put(accountTypeConfigDTO.getAccountType().toUpperCase(),accountTypeConfigDTO.getAccountTypeId()+"");
			}
			transaction.commit();
			log.info("Account categories map "+accountCategories);
			Cache.accountConfigMap = accountCategories;
		}catch (HibernateException h) {
			if (transaction != null) {
				transaction.rollback();
			}

		} catch (Exception e) {
			log.error("Exception in getBlClass " + e);
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}

	
	private static void loadOnlineTriggers() {
		log.info("*****getBlClass****");
		ArrayList<OnlineTriggerTableDTO> onlineTableDTOList = null;
		Session session = null;
		Criteria criteria = null;
		HashMap<Integer, ArrayList<OnlineTriggerTableDTO>> map = null;
		try {
			map = new HashMap<Integer, ArrayList<OnlineTriggerTableDTO>>();
			session = HiberanteUtilRule.getSessionFactory().openSession();
			criteria = session.createCriteria(OnlineTriggerTableDTO.class);
			List<OnlineTriggerTableDTO> list = criteria.list();
			for (OnlineTriggerTableDTO featureMappingDTO : list) {
				try{
					if((onlineTableDTOList=map.get(featureMappingDTO.getTriggerId()))==null){
						onlineTableDTOList = new ArrayList<>();
					}
					}catch (Exception e) {
						onlineTableDTOList = new ArrayList<>();
					}
				onlineTableDTOList.add(featureMappingDTO);
				map.put(featureMappingDTO.getTriggerId(), onlineTableDTOList);
			}
			Cache.setTriggerTableMap(map);
			
		} catch (HibernateException h) {
			log.error(h);
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */

		} catch (Exception e) {
			log.error("Exception in getBlClass " + e);
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}
	
	

	private static void loadInterfaceDetails() {
		log.info("*****getBlClass****");
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		HashMap<Integer, String> map = null;
		try {
			map = new HashMap<Integer, String>();
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="FROM InterfaceMasterTabDto";
			
			Query query=session.createQuery(sql);
			
			 
			List<InterfaceMasterTabDto> list=query.list();
			
			for(InterfaceMasterTabDto interfaceMasterTabDto:list)
			{
				
				if(interfaceMasterTabDto.getInterfaceId()!=0 && interfaceMasterTabDto.getClassName()!=null)
					map.put(interfaceMasterTabDto.getInterfaceId(), interfaceMasterTabDto.getClassName());
			}
			
			Cache.setInterfaceDetailsMap(map);
			
			//transaction.commit();
			log.info(">>>>>>>>>>>>>>>>>>>>>>Interface Details"+map);
			
			
			
			
			
		} catch (HibernateException h) {
			/*
			 * if (transaction != null) { transaction.rollback(); }
			 */

		} catch (Exception e) {
			log.error("Exception in getBlClass " + e);
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
				session = null;
			}
		}
	}

	public static void loadFeatureClasses(){
		log.info("*****getBlClass****");
		Session session =null;
		Transaction transaction =null;
		Criteria criteria =null;
		HashMap<String,String> map = null;
	try{
		map = new HashMap<String, String>();
		session = HiberanteUtil.getSessionFactory().openSession();
		transaction = session.beginTransaction();
		criteria = session.createCriteria(featureMappingDTO.class);
		List<featureMappingDTO> list=criteria.list();
		for(featureMappingDTO featureMappingDTO :list){
			map.put(featureMappingDTO.getFeatureId(), featureMappingDTO.getBlClassPath());
		}
		transaction.commit();
		Cache.setFeatureDeetailsMap(map);
	}catch(HibernateException h){
		if(transaction!=null){
			transaction.rollback();
		}
		
	}catch (Exception e){
		log.error("Exception in getBlClass "+e);
		e.printStackTrace();
	}finally{
		if(session !=null && session.isOpen()){
			session.close();
			session=null;
		}
	}
	}
	
	private static void loadEmailInfo()
	{
		log.info("Load Email Info ");
		Session session = null;
		Criteria ctr = null;
		Map<Integer, EmailInfoDTO> emails = new HashMap<Integer, EmailInfoDTO>();
		List<EmailInfoDTO> list = null;
		
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			ctr = session.createCriteria(EmailInfoDTO.class);
			
			list = ctr.list();
			
			for(EmailInfoDTO dto : list)
				emails.put(dto.getSno(), dto);
			
		}
		catch (Exception e) 
		{
			log.info("Exception in load Email ");
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
			
			list = null;
			Cache.emails = emails;
		}
		
	}
	
	private static void loadInstantRewardBuketConfig() {
		log.info("Loading instant rewards");
		Session session= null;
		Transaction transaction = null;
		Criteria criteria = null;
		Map<String,InstantRewardConfigDTO> instantPacketsConfig = null;
	    
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			criteria = session.createCriteria(InstantRewardConfigDTO.class);
			instantPacketsConfig = Collections.synchronizedMap(new HashMap<String, InstantRewardConfigDTO>());
			for(InstantRewardConfigDTO instantRewardConfigDTO : (List<InstantRewardConfigDTO>)criteria.list()){
				instantPacketsConfig.put(instantRewardConfigDTO.getId()+"", instantRewardConfigDTO);
			}
			transaction.commit();
		}catch(Exception e){
			log.error("Exception in loading packet Details");
		}finally{
			log.info("Instant Rewards "+instantPacketsConfig);
			Cache.instantPacketsConfig = instantPacketsConfig;
			if(session!=null)
				session.close();
			session = null;
		}
		
	}

	private static void loadINServiceDetails() {
		log.info("Loading In service Details");
		Session session= null;
		Transaction transaction = null;
		Criteria criteria = null;
		Map<Integer,INServiceDetailsDTO> inServiceConfig = null;
	    
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			criteria = session.createCriteria(INServiceDetailsDTO.class);
			inServiceConfig = Collections.synchronizedMap(new HashMap<Integer, INServiceDetailsDTO>());
			for(INServiceDetailsDTO inServiceDTO : (List<INServiceDetailsDTO>)criteria.list()){
				inServiceConfig.put(inServiceDTO.getServiceId(),inServiceDTO);
			}
			transaction.commit();
		}catch(Exception e){
			log.error("Exception in loading packet Details");
		}finally{
			log.info("IN Service "+inServiceConfig);
			Cache.inServiceDetailsMap = inServiceConfig;
			
			if(session!=null)
				session.close();
			session = null;
		}
		
	}
	
	
	private static void loadOfferDetails() {
		log.info("Loading In service Details");
		Session session= null;
		Transaction transaction = null;
		Criteria criteria = null;
		Map<String,String> inServiceConfig = null;
	    
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			
			criteria = session.createCriteria(InstantRewardConfigDTO.class);
			inServiceConfig = Collections.synchronizedMap(new HashMap<String, String>());
			for(InstantRewardConfigDTO inServiceDTO : (List<InstantRewardConfigDTO>)criteria.list()){
				inServiceConfig.put(inServiceDTO.getPackName(),inServiceDTO.getOfferSynonym());
			}
			transaction.commit();
		}catch(Exception e){
			log.error("Exception in loading packet Details");
		}finally{
			log.info("IN Service "+inServiceConfig);
			Cache.instantOfferMapping = inServiceConfig;
			Cache.instantOfferCount = inServiceConfig.size();
			if(session!=null)
				session.close();
			session = null;
		}
		
	}

	private static void loadParentServiceDetails() {
		Set<String> keySet = null;
		ArrayList<Integer> parentMap = null;
		ActionServiceDetailsDTO actionServiceDetailsDTO = null;
		keySet = Cache.serviceDetailsMap.keySet();
		parentMap = new ArrayList<Integer>();
		for(String key : keySet){
			actionServiceDetailsDTO = Cache.serviceDetailsMap.get(key);
			if(actionServiceDetailsDTO.isParent()){
				parentMap.add(actionServiceDetailsDTO.getServiceID());
			}
		}
	}
	
	
	public static void loadCommonReqXstream() {
		log.info("loading xstream for common request ...");
		XStream xStream = new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						cdata =true;
					}
					
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
		xStream.alias("Request", com.sixdee.imp.service.httpcall.dto.Request.class);
		xStream.aliasField("requestId", com.sixdee.imp.service.httpcall.dto.Request.class, "requestId");
		xStream.aliasField("msisdn", com.sixdee.imp.service.httpcall.dto.Request.class, "msisdn");
		xStream.aliasField("keyword", com.sixdee.imp.service.httpcall.dto.Request.class, "keyword");
		xStream.aliasField("featureId", com.sixdee.imp.service.httpcall.dto.Request.class, "featureId");
		xStream.aliasField("timeStamp", com.sixdee.imp.service.httpcall.dto.Request.class, "timeStamp");
		xStream.aliasField("dataSet", com.sixdee.imp.service.httpcall.dto.Request.class, "dataSet");
		
		xStream.addImplicitCollection(DataSet.class, "params");
		
		xStream.alias("param",Parameters.class);
		xStream.aliasField("id", Parameters.class, "id");
		xStream.aliasField("value", Parameters.class, "value");		
		Cache.coomonReqXstream = xStream;
		
	}//loadCommonReqXstream
	
	public static void loadCommonResXstream() {
		log.info("loading xstream for common response ...");
		XStream xStream = new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						cdata =true;
					}
					
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
		xStream.alias("Response", com.sixdee.imp.service.httpcall.dto.Response.class);
		xStream.aliasField("ClientTxnId", com.sixdee.imp.service.httpcall.dto.Response.class, "requestId");
		xStream.aliasField("Msisdn", com.sixdee.imp.service.httpcall.dto.Response.class, "msisdn");
		xStream.aliasField("Timestamp", com.sixdee.imp.service.httpcall.dto.Response.class, "timeStamp");
		xStream.aliasField("RespCode", com.sixdee.imp.service.httpcall.dto.Response.class, "status");
		xStream.aliasField("RespDesc", com.sixdee.imp.service.httpcall.dto.Response.class, "statusDesc");
		
		xStream.aliasField("dataSet", com.sixdee.imp.service.httpcall.dto.Response.class, "dataSet");
		
		xStream.addImplicitCollection(DataSet.class, "params");
		
		xStream.alias("param",Parameters.class);
		xStream.aliasField("id", Parameters.class, "id");
		xStream.aliasField("value", Parameters.class, "value");		
		Cache.coomonResXstream = xStream;
		
	}//loadCommonResXstream


	public static void loadSubscriberProfileXstream() {
		log.info("loading xstream for subscriber profile...");
		XStream xStream = new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						cdata =true;
					}
					
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
		xStream.alias("Request", SubscriberRequest.class);
		xStream.aliasField("requestId", SubscriberRequest.class, "requestId");
		xStream.aliasField("msisdn", SubscriberRequest.class, "msisdn");
		xStream.aliasField("keyword", SubscriberRequest.class, "keyword");
		xStream.aliasField("featureId", SubscriberRequest.class, "featureId");
		xStream.aliasField("timeStamp", SubscriberRequest.class, "timeStamp");
		xStream.aliasField("dataSet", SubscriberRequest.class, "dataSet");
		xStream.addImplicitCollection(SubscriberDataSet.class, "paramlist");
		xStream.alias("param", SubscriberRequestParam.class);
		xStream.aliasField("id", SubscriberRequestParam.class, "id");
		xStream.aliasField("value", SubscriberRequestParam.class, "value");		
		Cache.subscriberProfileXstream = xStream;
		
	}
	
	public static void loadTOLXstream() {
		log.info("loading xstream for TOL parsing...");
		XStream xStream = new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						cdata =true;
					}
					
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
		xStream.alias("Request", TOLSubscriberCheckDTO.class);
		xStream.aliasField("requestId", TOLSubscriberCheckDTO.class, "requestId");
		xStream.aliasField("msisdn", TOLSubscriberCheckDTO.class, "subscriberNumber");
		xStream.aliasField("keyword", TOLSubscriberCheckDTO.class, "keyword");
		xStream.aliasField("featureId", TOLSubscriberCheckDTO.class, "featureId");
		xStream.aliasField("timeStamp", TOLSubscriberCheckDTO.class, "timeStamp");
		xStream.aliasField("dataSet", TOLSubscriberCheckDTO.class, "dataSet");
		xStream.addImplicitCollection(SubscriberDataSet.class, "paramlist");
		xStream.alias("param", SubscriberRequestParam.class);
		xStream.aliasField("id", SubscriberRequestParam.class, "id");
		xStream.aliasField("value", SubscriberRequestParam.class, "value");		
		Cache.TOLXstream = xStream;
		
	}

	

	private static void loadCacheDetails() {
		try{
			//service = new ServiceDAO();
			Cache.appMap = new ServiceDAO().getServiceInfo();
			Cache.loyaltyStatusMap = new LoyaltyInfoDAO().getLoyaltyStatusInfo();
			Cache.voucherStatusMap = new VoucherInfoDAO().getVoucherStatusInfo();
		}catch (Exception e) {
			log.error("Exception occured ",e);
		}
	}

	public static void loadProperties()
	{
		InputStream in = null;
		Properties properties = null;
		Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());
		Enumeration enumeration = null;
		List<String> accountCategoryList=null;
		try
		{
			in = Globals.class.getClassLoader().getResourceAsStream(SYSTEM_PROPERTIES_FILE);
			properties = new Properties();
			properties.load(in);
			
			enumeration = properties.keys();
			
			while (enumeration.hasMoreElements()) 
			{
				String key = (String) enumeration.nextElement();
				
				if(properties.getProperty(key)!=null)
				{
					log.info("key = "+key+" &  value = "+(String)properties.getProperty(key));
					map.put(key.trim(), (String)properties.getProperty(key).trim());
				}
				else
				{
					log.info("key = "+key +" & value = NOT FOUND");
					log.info("Exit ");
					throw new Exception();
				}
				
			}
			Cache.setCacheMap(map);
			
			// For account category
			if(Cache.getCacheMap().get("ACCOUNT_CATEGORY")!=null)
			{
			  	if(Cache.getCacheMap().get("ACCOUNT_CATEGORY").contains(","))
			  		accountCategoryList=Arrays.asList(Cache.getCacheMap().get("ACCOUNT_CATEGORY").split(","));
			  	else
			  	{
			  		accountCategoryList=new ArrayList<String>();
			  		accountCategoryList.add(Cache.getCacheMap().get("ACCOUNT_CATEGORY"));
			  	}
			}
			
			Cache.accountCategoryList=accountCategoryList;
			
			accountCategoryList = null;
			
//			 For account category service Wise
			if(Cache.getCacheMap().get("ACCOUNT_CATEGORY_SERVICE")!=null)
			{
			  	if(Cache.getCacheMap().get("ACCOUNT_CATEGORY_SERVICE").contains(","))
			  		accountCategoryList=Arrays.asList(Cache.getCacheMap().get("ACCOUNT_CATEGORY_SERVICE").split(","));
			  	else
			  	{
			  		accountCategoryList=new ArrayList<String>();
			  		accountCategoryList.add(Cache.getCacheMap().get("ACCOUNT_CATEGORY_SERVICE"));
			  	}
			}
			
			Cache.accountCategoryServiceList = accountCategoryList;
			
		}
		catch (Exception e) 
		{
			log.info("Error occured while loading system property",e);
			e.printStackTrace();
			System.exit(1);
		}
		finally
		{
			try
			{
				if(in!=null)
				{
					in.close();
					in=null;
				}
			}
			catch (Exception e) {
			}
			properties = null;
			map = null;
			enumeration =null;
			accountCategoryList=null;
			
		}
	}//loadProperties
	
	public static void loadServiceDetailsMap()
	{
		Session session=null;
		//Transaction transaction=null;
		Map<String,ActionServiceDetailsDTO> map=new HashMap<String, ActionServiceDetailsDTO>();
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="SELECT TAB.serviceID,TAB.serviceName,TAB.endDate,TAB.parentID from ActionServiceDetailsDTO TAB";
			Query query=session.createQuery(sql);
			List<Object[]> list=query.list();
			for(Object[] objects:list)
			{
				ActionServiceDetailsDTO actionServiceDetailsDTO=new ActionServiceDetailsDTO();
				actionServiceDetailsDTO.setServiceID(Integer.parseInt(objects[0].toString()));
				actionServiceDetailsDTO.setServiceName(objects[1].toString().toUpperCase());
				actionServiceDetailsDTO.setEndDate((Date)objects[2]);
				actionServiceDetailsDTO.setParent(objects[3]==null?true:false);
				map.put(actionServiceDetailsDTO.getServiceName(),actionServiceDetailsDTO);
			}
			
			log.info("Service Details  "+map);
			Cache.serviceDetailsMap=map;
			//transaction.commit();
			
			
		}catch (Exception e) {
			e.printStackTrace();
			/*
			 * if(transaction!=null) transaction.rollback();
			 */
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		
		
	}//loadServiceDetailsMap
	
	public static void loadAccountTypeMap()
	{
		Session session=null;
		//Transaction transaction=null;
		Map<String,String> map=new HashMap<String,String>();
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="SELECT TYPE_ID,LANGUAGE_ID,TYPE_NAME from ACCOUNT_TYPE";
			
			Query query=session.createSQLQuery(sql);
			List<Object[]> list=query.list();
			for(Object[] obj:list)
			{
				map.put(obj[0].toString()+"_"+obj[1].toString(),""+obj[2].toString());
				map.put(obj[2].toString(),obj[0].toString());
			}
			
			/*Query query=session.createSQLQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List<Map<String,Object>> list=query.list();
			
			log.info("##########################################ACCOUNT TYPE MAP#########################");
			for(Map<String,Object> obj:list)
			{
				log.info("obj.get(TYPE_ID)"+obj.get("TYPE_ID"));
				log.info("obj.get(LANGUAGE_ID"+obj.get("LANGUAGE_ID"));
				log.info("obj.get(TYPE_NAME"+obj.get("TYPE_NAME"));
				 map.put(obj.get("TYPE_ID").toString()+"_"+obj.get("LANGUAGE_ID"),""+obj.get("TYPE_NAME"));
				 map.put(obj.get("TYPE_NAME").toString(),obj.get("TYPE_ID").toString());
			}*/
			
			log.info("Account Types  "+map);
			Cache.accountTypeMap=map;
			//transaction.commit();
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
			/*
			 * if(transaction!=null) transaction.rollback();
			 */
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		map=null;
		
		
	}//loadServiceDetailsMap
	
	
	public static void loadLanguageDetails()
	{
		Session session=null;
		//Transaction transaction=null;
		Map<Integer,LanguageDTO> map=Collections.synchronizedMap(new HashMap<Integer, LanguageDTO>());
	
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="SELECT LANGUAGE_ID,LANGUAGE_NAME,LANGUAGE_SYNM from SUBSCRIBER_LANGUAGE_DETAILS";
			Query query=session.createSQLQuery(sql);
			List<Object[]> list=query.list();
			LanguageDTO languageDTO=null;
			for(Object[] objects:list)
			{
				languageDTO=new LanguageDTO();
				languageDTO.setLanguageID(Integer.parseInt(objects[0].toString()));
				languageDTO.setLanguageName(objects[1].toString());
				languageDTO.setLanguageSynonym(objects[2].toString());
				
				map.put(languageDTO.getLanguageID(),languageDTO);
				
			}
			
			log.info("Language  "+map);
			Cache.languageDetailsMap=map;
			//transaction.commit();
			
			
		}catch (Exception e) {
			e.printStackTrace();
			
			/*
			 * if(transaction!=null) transaction.rollback();
			 */
		}finally{
			if(session!=null)
				session.close();
			session=null;
		}
		
		
	}//loadLanguageDetails
	
	public static void loadStatusDetails()
	{
		
		Session session=null;
		//Transaction transaction=null;
		Map<String,String> statusDetails=Collections.synchronizedMap(new HashMap<String, String>());
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="SELECT STATUS_ID,LANGUAGE_ID,STATUS_DESC from STATUS_CODES ";
			
			Query query=session.createSQLQuery(sql);//.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			
			List<Map<String,Object>> list=query.list();
			
			for(Map<String,Object> obj:list)
			{
		      statusDetails.put(obj.get("STATUS_ID").toString()+"_"+obj.get("LANGUAGE_ID"),""+obj.get("STATUS_DESC"));
			}
			
			Cache.setStatusMap(statusDetails);
			
			log.info("Status Details  : "+statusDetails);
			
			//transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			statusDetails=null;
		}
		
	}//loadStatusDetails
	
	
	
	public static void loadConfigParameters()
	{
		
		Session session=null;
		//Transaction transaction=null;
		Map<String,ConfigureParameterDTO> configParameterMap=Collections.synchronizedMap(new HashMap<String, ConfigureParameterDTO>());
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			String sql="FROM ConfigureParameterDTO";
			
			Query query=session.createQuery(sql);
			
			 
			List<ConfigureParameterDTO> list=query.list();
			
			for(ConfigureParameterDTO parameterDTO:list)
			{
				configParameterMap.put(parameterDTO.getParameterName().trim(),parameterDTO);
			}
			
			Cache.setConfigParameterMap(configParameterMap);
			
			//transaction.commit();
			log.info("Config param map "+configParameterMap);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			configParameterMap=null;
		}
		
	}//loadConfigParameters
	
	
	public static void loadMORouterXstream()
	{
		log.info("loading xstream for mo router ..");
		XStream xStream = new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					boolean cdata = false;

					public void startNode(String name, Class clazz) {
						super.startNode(name, clazz);
						
					}
					
					protected void writeText(QuickWriter writer, String text) {
						if (cdata) {
							writer.write("<![CDATA[");
							writer.write(text);
							writer.write("]]>");
						} else {
							writer.write(text);
						}
					}
				};
			}
		});
		
		xStream.alias("Request", Request.class);
		xStream.aliasField("requestId", Request.class, "requestId");
		xStream.aliasField("msisdn", Request.class, "msisdn");
		xStream.aliasField("keyword", Request.class, "keyword");
		xStream.aliasField("featureId", Request.class, "featureId");
		xStream.aliasField("timeStamp", Request.class, "timeStamp");
		
		xStream.addImplicitCollection(Request.class, "dataSet");
		
		xStream.alias("param", SubscriberRequestParam.class);
		xStream.aliasField("id", SubscriberRequestParam.class, "id");
		xStream.aliasField("value", SubscriberRequestParam.class, "value");
		
		Cache.moRouterXstream = xStream;
		
	}
	
	public static void loadGetLanguageXstream()
	{
		log.info("loading xstream for GetLanguage ..");
		XStream xStream = new XStream();	
		xStream.alias("Request", Request.class);	
		xStream.aliasField("timeStamp", Request.class, "timeStamp");
		xStream.aliasField("msisdn", Request.class, "msisdn");
		Cache.GetlanguageXstream = xStream;		
	}
	
	
	public static void loadRewardPointsCategory()
	{
		
		Session session=null;
		//Transaction transaction=null;
		Map<Integer,RewardPointsCategoryDTO> rewardPointsMap=Collections.synchronizedMap(new HashMap<Integer, RewardPointsCategoryDTO>());
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="SELECT CATEGORY_ID,CATEGORY_NAME,CATEGORY_DESC,UNITS_CALCULATION from REWARD_POINTS_CATEGORY ";
			
			Query query=session.createSQLQuery(sql);
			
			List<Object[]> list=query.list();
			
			
			for(Object[] obj:list)
			{
				RewardPointsCategoryDTO categoryDTO=new RewardPointsCategoryDTO();
				categoryDTO.setCategoryID(Integer.parseInt(obj[0].toString()));
				categoryDTO.setCategoryName((String)obj[1]);
				categoryDTO.setCategoryDesc((String)obj[2]);
				categoryDTO.setUnitsCalculation(obj[3].toString().toUpperCase());
				
				rewardPointsMap.put(categoryDTO.getCategoryID(),categoryDTO);
				
			}
			
			Cache.rewardPointsCategoryMap=rewardPointsMap;
			
			log.info("RewardPoints Category Details  : "+rewardPointsMap);
			
			//transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			rewardPointsMap=null;
		}
		
	}//loadTierInfoDetails
	
	
	public static void loadRewardPointsDetails()
	{
		
		Session session=null;
		//Transaction transaction=null;
		// MAP<TierID,<CategoryID,List<DTO>>>
		Map<Integer,Map<Integer,List<CategoryDetailsDTO>>> tierCategoryDetailsMap=Collections.synchronizedMap(new HashMap<Integer, Map<Integer,List<CategoryDetailsDTO>>>());
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="SELECT TIER_ID,CATEGORY_ID,MIN_VALUE,MAX_VALUE,REWARD_POINTS,STATUS_POINTS from TIER_REWARD_POINTS_DETAILS ";
			
			Query query=session.createSQLQuery(sql);
			
			List<Object[]> list=query.list();
			
			
			for(Object[] obj:list)
			{
				CategoryDetailsDTO detailsDTO=new CategoryDetailsDTO();
				detailsDTO.setTierID(Integer.parseInt(obj[0].toString()));
				detailsDTO.setCategoryID(Integer.parseInt(obj[1].toString()));
				detailsDTO.setMinValue((obj[2]==null?null:Double.parseDouble(obj[2]+"")));
				detailsDTO.setMaxValue((obj[3]==null?null:Double.parseDouble(obj[3]+"")));
				detailsDTO.setRewardPoints((obj[4]==null?null:Double.parseDouble(obj[4]+"")));
				detailsDTO.setStatusPoints((obj[5]==null?null:Double.parseDouble(obj[5]+"")));
				
				
				if(tierCategoryDetailsMap.get(detailsDTO.getTierID())!=null)
				{
					
					if(tierCategoryDetailsMap.get(detailsDTO.getTierID()).get(detailsDTO.getCategoryID())!=null)
					{
						tierCategoryDetailsMap.get(detailsDTO.getTierID()).get(detailsDTO.getCategoryID()).add(detailsDTO);
					}else{
						List<CategoryDetailsDTO> list2=new ArrayList<CategoryDetailsDTO>();
						list2.add(detailsDTO);
						tierCategoryDetailsMap.get(detailsDTO.getTierID()).put(detailsDTO.getCategoryID(),list2);
					}
					
				}else{
					List<CategoryDetailsDTO> list2=new ArrayList<CategoryDetailsDTO>();
					list2.add(detailsDTO);
				
					
					Map<Integer,List<CategoryDetailsDTO>> map=new HashMap<Integer, List<CategoryDetailsDTO>>();
					map.put(detailsDTO.getCategoryID(),list2);
					
				
					tierCategoryDetailsMap.put(detailsDTO.getTierID(),map);
					
				}
				
			}
			
			Cache.tierCategoryDetailsMap=tierCategoryDetailsMap;
			
			Iterator<Integer> iterator=tierCategoryDetailsMap.keySet().iterator();
			while(iterator.hasNext())
			{
				int tierId=iterator.next();
				log.info("Tier ID :"+tierId+"   "+tierCategoryDetailsMap.get(tierId));
				
			}
			
			
			//transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			tierCategoryDetailsMap=null;
		}
		
	}//loadTierInfoDetails
	
	
	public static void loadTierInfoDetails()
	{
		
		Session session=null;
		//Transaction transaction=null;
		Map<Integer,TierInfoDTO> tierInfoDetails=Collections.synchronizedMap(new HashMap<Integer, TierInfoDTO>());
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="FROM TierInfoDTO";
			
			Query query=session.createQuery(sql);
			
			List<TierInfoDTO> list=query.list();
			
			for(TierInfoDTO tierInfoDTO:list)
			{
				tierInfoDetails.put(tierInfoDTO.getTierId(),tierInfoDTO);
			}
			
			Cache.setTierInfoMap(tierInfoDetails);
			
			log.info("Tier Info Details  : "+tierInfoDetails);
			
			//transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			tierInfoDetails=null;
		}
		
	}//loadTierInfoDetails
	
	
	public static void loadTierLanguageInfoDetails()
	{
		
		Session session=null;
		//Transaction transaction=null;
		Map<String,String> tierInfoDetails=Collections.synchronizedMap(new HashMap<String,String>());
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="FROM TierLanguageMappingDTO";
			
			Query query=session.createQuery(sql);
			
			List<TierLanguageMappingDTO> list=query.list();
			
			for(TierLanguageMappingDTO tierInfoDTO:list)
			{
				tierInfoDetails.put(tierInfoDTO.getTierId()+"_"+tierInfoDTO.getLangId(),tierInfoDTO.getTierName());
			}
			
			Cache.tierLanguageInfoMap=tierInfoDetails;
			
			log.info("Tier Language Info Details  : "+tierInfoDetails);
			
			//transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			tierInfoDetails=null;
		}
		
	}//loadTierLanguageInfoDetails
	
	
	public static void loadChannelDetails()
	{

		Session session = null;
		Map<String, String> channelDetails = Collections.synchronizedMap(new HashMap<String, String>());

		Map<String, String> channelFlowDetails = Collections.synchronizedMap(new HashMap<String, String>());

		Map<String, String> channelDescriptionFlowDetails = Collections.synchronizedMap(new HashMap<String, String>());
		try {

			session = HiberanteUtil.getSessionFactory().openSession();

			String sql = "SELECT CHANNEL_ID,CHANNEL_NAME,FLOW_TYPE FROM LMS_CNFG_CHANNEL_DETAILS";

			Query query = session.createSQLQuery(sql);

			List<Object[]> list = query.list();

			for (Object[] obj : list) {
				channelDetails.put(obj[0].toString(), obj[1].toString().toUpperCase());
				channelDetails.put(obj[1].toString().toUpperCase(), obj[0].toString());
				if (obj[2].toString() != null && !obj[2].toString().equalsIgnoreCase("")) {
					channelFlowDetails.put(obj[0].toString(), obj[2].toString().toUpperCase());
					channelFlowDetails.put(obj[1].toString().toUpperCase(), obj[2].toString().toUpperCase());
					channelDescriptionFlowDetails.put(obj[0].toString().toUpperCase(), obj[1].toString().toUpperCase());

				}
			}
			Cache.channelDetails = channelDetails;
			Cache.channelFlowDetails = channelFlowDetails;
			Cache.channelDescDetails = channelDescriptionFlowDetails;

			log.info("Channel Details  : " + channelDetails);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			channelDetails = null;
			channelFlowDetails = null;
			channelDescriptionFlowDetails = null;
		}

	}//loadChannelDetails
	
	public static void loadOfferTypeDetails()
	{

		Session session = null;
		Map<String, String> loadOfferTypeDetails = Collections.synchronizedMap(new HashMap<String, String>());
		try {

			session = HiberanteUtil.getSessionFactory().openSession();

			String sql = "SELECT OFFERTYPEID,OFFERTYPE FROM LMS_CNFG_OFFER_TYPE";

			Query query = session.createSQLQuery(sql);

			List<Object[]> list = query.list();

			for (Object[] obj : list) {
				loadOfferTypeDetails.put(obj[0].toString(), obj[1].toString().toUpperCase());
			}
			Cache.offerCategoryMapInfo = loadOfferTypeDetails;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			loadOfferTypeDetails=null;
		}

	}
	
	
	public static void loadTelecomOfferCategoryMap()
	{

		Session session = null;
		Map<String, String> telecomOfferCategoryMap = Collections.synchronizedMap(new HashMap<String, String>());
		
		Map<String, String> telecomOfferTypeIdMap = Collections.synchronizedMap(new HashMap<String, String>());
		try {

			session = HiberanteUtil.getSessionFactory().openSession();

			String sql = "SELECT CAT_ID,CAT_NAME,TYPE_ID FROM LMS_CNFG_TELE_OFFER_CATEGORY";

			Query query = session.createSQLQuery(sql);

			List<Object[]> list = query.list();

			for (Object[] obj : list) {
				telecomOfferCategoryMap.put(obj[0].toString(), obj[1].toString().toUpperCase());
				telecomOfferTypeIdMap.put(obj[0].toString(), obj[2].toString());
			}
			Cache.TelecomofferCategoryMapInfo = telecomOfferCategoryMap;
			Cache.TelecomofferTypeIdMapInfo=telecomOfferTypeIdMap;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
				session = null;
			}
			telecomOfferCategoryMap = null;
			telecomOfferTypeIdMap = null;
		}

	}
	
	public static void loadAccountCategoryMap()
	{
		Map<String, String> accountCategoryTypeMap = new HashMap<String, String>();
		try
		{
			accountCategoryTypeMap.put("Residential", "1");
			accountCategoryTypeMap.put("Business", "2");
			Cache.accountCategoryType=accountCategoryTypeMap;
		}catch(Exception e)
		{
			e.printStackTrace();
			log.info("Exception "+e.getMessage());
		}finally
		{
			accountCategoryTypeMap=null;
		}
	}
	
	
	public static void loadLoyaltyRegPackDetails()
	{
		
		Session session=null;
		Map<Integer,AccountTypeWisePackDTO> loyaltyPackDetails=Collections.synchronizedMap(new HashMap<Integer, AccountTypeWisePackDTO>());
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			
			String sql="SELECT ACCOUNT_TYPE,PACK_ID,PACK_NAME FROM LOYALTY_REG_PACK";
			
			Query query=session.createSQLQuery(sql);
			
			List<Object[]> list=query.list();
			
			for(Object[] obj:list)
			{
				if(obj[0]!=null&&obj[1]!=null&&!obj[1].toString().trim().equals(""))
				{
				  AccountTypeWisePackDTO packDTO=new AccountTypeWisePackDTO(Integer.parseInt(obj[0].toString()),obj[1].toString(),obj[2].toString());	
				  loyaltyPackDetails.put(packDTO.getAccountType(),packDTO);
				  packDTO=null;
				}
			}
			
			Cache.loyaltyRegPackMap=loyaltyPackDetails;
			
			log.info("Loyalty Registraton Pack Details  : "+loyaltyPackDetails);
			
			
		}catch (Exception e) {
			log.info("",e);
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			loyaltyPackDetails=null;
		}
		
	}//loadChannelDetails
	
	
	public static void loadServiceStatusDetails()
	{
		
		Session session=null;
		//Transaction transaction=null;
		Map<String,ServiceStatusDTO> serviceStatusMap=Collections.synchronizedMap(new HashMap<String, ServiceStatusDTO>());
		try{
			
			session=HiberanteUtil.getSessionFactory().openSession();
			//transaction=session.beginTransaction();
			
			String sql="SELECT ID,STATUS_CODE,STATUS_DESC FROM MODULE_SERVICE_REPLY_MESSAGE";
			
			Query query=session.createSQLQuery(sql);
			
			List<Object[]> list=query.list();
			
			ServiceStatusDTO serviceStatusDTO=null;
			
			for(Object[] strObj:list)
			{
				serviceStatusDTO=new ServiceStatusDTO();
				serviceStatusDTO.setId((String)strObj[0]);
				serviceStatusDTO.setStatusCode((String)strObj[1]);
				serviceStatusDTO.setStatusDesc((String)strObj[2]);
				
				serviceStatusMap.put(serviceStatusDTO.getId(),serviceStatusDTO);
			}
			
			Cache.setServiceStatusMap(serviceStatusMap);
			
			log.info("Service Status Details  : "+serviceStatusMap);
			
			//transaction.commit();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session!=null)
			{
				session.close();
				session=null;
			}
			serviceStatusMap=null;
		}
		
	}//loadServiceStatusDetails
	
	 @SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
		private static void loadSecurityTokenDetails() {
			log.info("*****loadSecurityTokenDetails****");
			Session session = null;
			HashMap<String, SecurityTokenDto> securityTokenDetailsMap = null;
			try {
				securityTokenDetailsMap = new 	HashMap<String, SecurityTokenDto>();
				session = HiberanteUtil.getSessionFactory().openSession();
				String sql = "FROM SecurityTokenDto";
				Query query = session.createQuery(sql);
				List<SecurityTokenDto> list = query.list();
				if (list != null && list.size() > 0) {
					for (SecurityTokenDto securityTokenDto : list) {
					securityTokenDetailsMap.put(securityTokenDto.getSecurityToken(), securityTokenDto);
					}
				}
				Cache.setSecurityTokenDetailsMap(securityTokenDetailsMap);
				log.info("Security Details ::" + securityTokenDetailsMap);
			}catch(Exception e) {
				log.error("Exception in loadSecurityTokenDetails " + e);
				e.printStackTrace();
			}finally {
				if (session != null && session.isOpen()) {
					session.close();
					session = null;
				}
			}
			
		}
	 
	public static void reloadCache()
	{
		
		ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
		
		// this interval we have to take from System property
		exe.scheduleAtFixedRate(new Reloading(),12*60*60*1000 , 12*60*60*1000, TimeUnit.MILLISECONDS);
	}
	
	public static void generateLog()
	{
		ScheduledExecutorService exe = Executors.newSingleThreadScheduledExecutor();
		
		// this interval we have to take from System property
		exe.scheduleAtFixedRate(new LogReloading(),Integer.parseInt(Cache.getCacheMap().get("CDR_REQUEST_INTERVAL"))*60*1000 , Integer.parseInt(Cache.getCacheMap().get("CDR_REQUEST_INTERVAL"))*60*1000, TimeUnit.MILLISECONDS);
	}
}
class LogReloading implements Runnable
{
	Logger log = Logger.getLogger(LogReloading.class);
	public void run() {
		log.info("generating log now --------------------------------------------");
		LogUtil.generateLog();
		
	}
}



class Reloading implements Runnable
{
	Logger log = Logger.getLogger(Reloading.class);
 	
	public void run() 
	{
		log.info("Reloading cache START.. .. >>>>>");
		Globals.reload();
		log.info("Reloading cache  OVER.. .. >>>>>");
	}
}

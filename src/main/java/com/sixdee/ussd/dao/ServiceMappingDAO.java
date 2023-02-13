package com.sixdee.ussd.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.ussd.dto.ServiceRequestDTO;

public class ServiceMappingDAO {
	private static final Logger logger = Logger.getLogger(ServiceMappingDAO.class);

	public HashMap<String, ServiceRequestDTO> getServices() {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		HashMap<String, ServiceRequestDTO> serviceMap = new HashMap<String, ServiceRequestDTO>();
		HashMap<String, String> serviceNameMap = new HashMap<String, String>();
		
		//ServiceRequestDTO serviceRequestDTO = null;
		try{
			//logger.info("=====================================================");
			session = HiberanteUtil.getSessionFactory().openSession();
			//logger.info("=====================================================");
			criteria = session.createCriteria(ServiceRequestDTO.class);
		//	logger.info("=====================================================");
			for(ServiceRequestDTO serviceRequestDTO : (List<ServiceRequestDTO>)criteria.list()){
				serviceMap.put(serviceRequestDTO.getServiceId()+"", serviceRequestDTO);
				
			}
			//logger.info("=====================================================");
		}catch (HibernateException e) {
			logger.error("Exception in reloading",e);
		}catch (Exception e) {
			logger.error("Exception in reloading",e);
		}
		finally{
			if(session != null)
				session.close();
		}
		return serviceMap;
	}

	
	
}

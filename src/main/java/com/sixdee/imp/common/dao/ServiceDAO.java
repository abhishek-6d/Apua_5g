/**
 * 
 */
package com.sixdee.imp.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.ServiceMappingDTO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class ServiceDAO {
	private static final Logger logger = Logger.getLogger(ServiceDAO.class);

	public HashMap<Integer, ServiceMappingDTO> getServiceInfo(){
		Session session = null;
		//Transaction transaction = null;
		Criteria criteria = null;
		HashMap<Integer, ServiceMappingDTO> serviceMap = new HashMap<Integer, ServiceMappingDTO>();
		//ServiceMappingDTO serviceMappingDTO = null;
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(ServiceMappingDTO.class);
		//	criteria.setProjection(Projections.projectionList().add(Projections.property("serviceId")).add(Projections.property("serviceName")));
			for(ServiceMappingDTO serviceDetails : (List<ServiceMappingDTO>) criteria.list()){
				serviceMap.put(serviceDetails.getServiceId(), serviceDetails);
			}
			//transaction.commit();
		}catch (HibernateException e) {
			/*
			 * if(transaction != null) transaction.rollback();
			 * logger.error("Exception occured ",e);
			 */
			logger.error(e);
		}catch (Exception e) {
			logger.error("Exception occured ",e);
		}finally{
			if(session != null){
				session.close();
				session =null;
			}
		}
		return serviceMap;
	}
}

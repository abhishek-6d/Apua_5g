package com.sixdee.ussd.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.ussd.dto.KeywordMappingDTO;

public class KeywordMappingDAO {

	private static final Logger logger = Logger.getLogger(KeywordMappingDAO.class);
	public List<Object> getKeyWords() {
		Session session = null;
		Transaction transaction = null;
		Criteria criteria = null;
		HashMap<String, KeywordMappingDTO> keyMap = new HashMap<String, KeywordMappingDTO>();
		HashMap<Integer,ArrayList<String>> childMap = new HashMap<Integer, ArrayList<String>>();
		//ServiceRequestDTO serviceRequestDTO = null;
		List<Object> objList = new ArrayList<Object>();
		try{
			session = HiberanteUtil.getSessionFactory().openSession();
			//transaction = session.beginTransaction();
			criteria = session.createCriteria(KeywordMappingDTO.class);
			for(KeywordMappingDTO keywordMappingDTO : (List<KeywordMappingDTO>)criteria.list()){
				logger.debug(keywordMappingDTO.getId()+" "+keywordMappingDTO.getServiceId()+" "+keywordMappingDTO.getKeyWord()+" "+keywordMappingDTO.getParentService());
				
				keyMap.put(keywordMappingDTO.getKeyWord().toUpperCase().trim()+"_"+keywordMappingDTO.getLangId(), keywordMappingDTO);
				//logger.debug(keywordMappingDTO.getParentService());
				//logger.debug(keywordMappingDTO.getKeyWord());
				if(childMap.get(keywordMappingDTO.getParentService())==null){
					ArrayList<String> list = new ArrayList<String>();
					if(keywordMappingDTO.getParentService()!=0){
						list.add(keywordMappingDTO.getKeyWord());
					}
					childMap.put(keywordMappingDTO.getParentService(), list);
				}else{
					childMap.get(keywordMappingDTO.getParentService()).add(keywordMappingDTO.getKeyWord().toUpperCase());
				}
			}
			objList.add(keyMap);
			objList.add(childMap);
		}catch (HibernateException e) {
			logger.error("Exception in loading cache",e);
		}catch (Exception e) {
			logger.error("Exception in loading cache",e);
			
		}
		finally{
			if(session != null)
				session.close();
		}
		return objList;
	}

	
	
	

}

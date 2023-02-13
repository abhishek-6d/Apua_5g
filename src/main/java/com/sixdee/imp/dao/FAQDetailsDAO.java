package com.sixdee.imp.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.FAQDetailsDTO;
import com.sixdee.imp.dto.FAQQADetailsDTO;

public class FAQDetailsDAO {

	Logger logger = Logger.getLogger(FAQDetailsDAO.class);

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	public void GetFAQDetails(GenericDTO genericDTO) {
		logger.info("----------- UpdateVoucherDetails -----------");

		Session session = null;
		Transaction transaction = null;
		Query query = null;
		FAQDetailsDTO faqDetails = (FAQDetailsDTO)genericDTO.getObj();
		FAQQADetailsDTO faqQADetailsDTO = null;
		String sql = "";
		List<Object[]> list = null;
		List faqList = new ArrayList();
		try {

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			sql = "SELECT QUESTION,ANSWER FROM FAQ_DETAILS ";
			logger.info(sql);
			query = session.createSQLQuery(sql);

			list = query.list();

			for (Object[] all : list) {

				faqQADetailsDTO = new FAQQADetailsDTO();
				String question = all[0].toString();
				String answer = all[1].toString();
				faqQADetailsDTO.setQuestion(question);
				faqQADetailsDTO.setAnswer(answer);
				faqList.add(faqQADetailsDTO);
				faqDetails.setFAQList(faqList);

			}

			//query.executeUpdate();
			transaction.commit();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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

}

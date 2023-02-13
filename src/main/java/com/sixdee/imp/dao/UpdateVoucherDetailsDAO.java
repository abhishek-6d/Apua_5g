package com.sixdee.imp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.common.util.LoyaltyTransactionStatus;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.AccountNumberTabDTO;
import com.sixdee.imp.dto.UpdateVoucherDetailsDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.LoyaltyRegisteredNumberTabDTO;
import com.sixdee.imp.dto.LoyaltyTransactionTabDTO;
import com.sixdee.imp.dto.NationalNumberTabDTO;
import com.sixdee.imp.dto.SubscriberNumberTabDTO;
import com.sixdee.imp.dto.ADSLTabDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;

public class UpdateVoucherDetailsDAO {

	Logger logger = Logger.getLogger(UpdateVoucherDetailsDAO.class);

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	public boolean UpdateVoucherDetails(GenericDTO genericDTO,Long LoyaltyID) {
		logger.info("----------- UpdateVoucherDetails -----------");
		UpdateVoucherDetailsDTO updateVoucherDetailsDTO = (UpdateVoucherDetailsDTO) genericDTO
				.getObj();
		Session session = null;
		Transaction transaction = null;
		boolean flag = false;
		Query query = null;
		int rows = 0;
		try {

			List<String> OrderList = updateVoucherDetailsDTO.getOrderList();
			int Status = updateVoucherDetailsDTO.getStatus();

			session = HiberanteUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			String OrderId = null;
			String sql = "";
			logger.info("--------list size --- "+OrderList.size());
			for (int i = 0; i < OrderList.size(); i++) {
				OrderId = OrderList.get(i);
				sql = "UPDATE VOUCHER_ORDER_0 SET ORDER_STATUS = ? WHERE LOYALTY_ID = ? and  ORDER_ID = ? ";
				logger.info(sql);
				query = session.createSQLQuery(sql);
				logger.info("--- LoyaltyID ---" + LoyaltyID);
				logger.info("--- OrderId ---" + OrderId);
				logger.info("--- Status ---" + Status);
				query.setParameter(0, Status);
				query.setParameter(1, LoyaltyID);
				query.setParameter(2, OrderId);
				rows = rows+query.executeUpdate();
				
			}
			logger.info("------ rows ----------"+rows);
			if (rows == OrderList.size()) {
				transaction.commit();
				flag = true;
				
			} else {
				transaction.rollback();
				flag = false;
			}
			
			

		} catch (Exception e) {
			// TODO: handle exception
			//genericDTO.setStatusCode("SC0002");
			e.printStackTrace();
			transaction.rollback();
			flag = false;
		} finally {
			try {
				if (session != null) {
					session.close();
					session = null;
				}

			} catch (Exception e) {
			}
		}

		return flag;
	}
	
	
	public Long GetLoyaltyId(GenericDTO genericDTO) throws CommonException
	{	
		UpdateVoucherDetailsDTO updateVoucherDetailsDTO = (UpdateVoucherDetailsDTO) genericDTO
		.getObj();
		ADSLTabDTO adslDTO = null;
		SubscriberNumberTabDTO subsDTO = null;
		AccountNumberTabDTO accountNumberDTO = null;
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		Long LoyaltyID = null;
		try
		{
			TableDetailsDAO tableDetailsDAO = new TableDetailsDAO();
			String subscriberNumber = updateVoucherDetailsDTO.getSubscriberNo();
			// Check if Subscriber number is digits or characters.
			CommonUtil util = new CommonUtil();
			if (subscriberNumber != null && util.isItChar(subscriberNumber)) {
				adslDTO = tableDetailsDAO.getADSLDetails(subscriberNumber);
				if (adslDTO != null) {
					LoyaltyID = adslDTO.getLoyaltyID();
				}

			} else {
				subsDTO = tableDetailsDAO.getSubscriberNumberDetails(Long
						.parseLong(subscriberNumber));
				if (subsDTO != null) {
					LoyaltyID = subsDTO.getLoyaltyID();
				} else {
					accountNumberDTO = tableDetailsDAO
							.getAccountNumberDetails(subscriberNumber);
					if (accountNumberDTO != null) {
						LoyaltyID = accountNumberDTO.getLoyaltyID();
					} else {
						loyaltyProfileTabDTO = tableDetailsDAO
								.getLoyaltyProfile(Long
										.parseLong(subscriberNumber));
						if (loyaltyProfileTabDTO != null) {
							LoyaltyID = loyaltyProfileTabDTO.getLoyaltyID();
						}
					}
				}

			}
			if (LoyaltyID == null) {
				genericDTO.setStatusCode("SC0001");
				genericDTO.setStatus("Subscriber is not registered with LMS");
				throw new CommonException(
						"Subscriber is not registered with LMS");
			}
			
		}
		catch (CommonException e) {
			e.printStackTrace();
			throw new CommonException(e.getMessage());
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return LoyaltyID;
		
	}

}

package com.sixdee.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.sixdee.imp.dto.TransactionResponseDTO;


public interface TransactionManagementService {
	public List<String> getTransactionDetails(String requestId, String language, String channel,
			String phoneNumber,HttpServletResponse servletResponse, String startDate, String endDate, Integer start, Integer limit, String transactionType,
			String loyaltyId);
}

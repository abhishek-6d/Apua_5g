package com.sixdee.service;

import java.util.Map;

import com.sixdee.imp.dto.LoyaltyResponseDTO;


public interface GeneralManagementService {
	
	public LoyaltyResponseDTO serviceManagement(String phoneNumber, Map<String , String> headers);
}

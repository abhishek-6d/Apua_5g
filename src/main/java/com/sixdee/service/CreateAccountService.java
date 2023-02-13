package com.sixdee.service;

import java.util.Map;

import com.sixdee.imp.dto.ResponsesDTO;
import com.sixdee.imp.dto.UserResponseDTO;
import com.sixdee.imp.service.serviceDTO.resp.AccountLineDTO;
import com.sixdee.imp.service.serviceDTO.resp.UserDTO;

public interface CreateAccountService {
	public ResponsesDTO createAccount(String phoneNumber, Map<String , String> headers);
	//public AccountLineDTO viewAccounts(String phoneNumber, Map<String , String> headers);
	public ResponsesDTO deleteLoyaltyAccount(String phoneNumber, Map<String , String> headers);
	public UserResponseDTO getUserProfile(String phoneNumber, Map<String , String> headers);
}

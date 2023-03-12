package com.sixdee.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sixdee.imp.dto.LoyaltyResponseDTO;
import com.sixdee.imp.dto.RequestPoint;


public interface PointEarnService {
	//public LoyaltyResponseDTO adjustPoint(String phoneNumber, Map<String , String> headers,RequestPoint requestPoint);

	public LoyaltyResponseDTO adjustPoints(String phoneNumber, Map<String, String> headers,
		 RequestPoint requestPoint,HttpServletResponse servletResponse);
}

package com.sixdee.imp.bo;

import com.google.gson.Gson;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.RewardPointsDTO;



public class JsonParser {
public static void main(String[] args) {
	Data [] datas=new Data[3];
	
	RewardPointsDTO rewardPointsDTO=new RewardPointsDTO();
	rewardPointsDTO.setTransactionID("43554545469");
	rewardPointsDTO.setTimestamp("20171221182423");
	rewardPointsDTO.setChannel("SMS");
	rewardPointsDTO.setSubscriberNumber("8904655351");
	rewardPointsDTO.setRewardPointsCategory(1);
	rewardPointsDTO.setVolume(100.00);
	//rewardPointsDTO.setRewardPoints(100.00);
	//rewardPointsDTO.setStatusPoints(100.00);
	rewardPointsDTO.setStatusPointsOnly(false);
	Data data=new Data();
	data.setName("ProductSubscribed");
	data.setValue("501");
	datas[0]=data;
	
	Data data1=new Data();
	data1.setName("Description");
	data1.setValue("test");
	datas[1]=data1;
	
	Data data2=new Data();
	data2.setName("TestTag");
	data2.setValue("testValue");
	datas[2]=data2;
	
	rewardPointsDTO.setData(datas);
	Gson gson = new Gson();
	String jsonInString = gson.toJson(rewardPointsDTO);
	System.out.println("json string"+jsonInString);
	
}
}

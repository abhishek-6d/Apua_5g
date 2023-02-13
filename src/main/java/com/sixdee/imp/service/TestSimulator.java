package com.sixdee.imp.service;

import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;
import com.sixdee.imp.service.serviceDTO.resp.ResponseDTO;

public class TestSimulator {
	
	public static void main(String arg[])
	{
		ResponseDTO res= new ResponseDTO();
		Data data[]= new Data[2];
		Data data1 = new Data();
		data1.setName("ASA");
		data1.setValue("true");
		data[0]=data1;
		
		Data data2 = new Data();
		
		data2.setName("MSISDN");
		data2.setValue("993593505");
		data[1]=data2;
		
		
		
	AccountManagement am= new AccountManagement();
	AccountDTO accountDto = new AccountDTO();
	accountDto.setMoNumber("343434");
	accountDto.setData(data);
	
	
	Data data12[] = accountDto.getData();
	System.out.println("Data length"+data12.length);
	if (data != null && data12.length > 0) {
		for (int i = 0; i < data12.length; i++) {
			if (data12[i].getName() != null && data12[i].getValue() != null)
			{
				System.out.println(">> Name "+data[i].getName());
				System.out.println(">> Value "+data[i].getValue());
			}
		}
	}
	accountDto.setTransactionId("3535335");
	accountDto.setTimestamp("3535335999");
	System.out.println("AccountDtoCall");
	//res=am.getCustomerProfile(accountDto);
	}

}

/*package com.sixdee.lms.dto.FetchProfile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GenerateJson {
	
	public static void main(String arg[]) throws JsonProcessingException
	{
		FetchProfile fetchProfile=null;
		BillingSystem billingSystem=null;
		Request request=null;
		Profile profile=null;
		Service service=null;
		Account account=null;
		
		request=new Request(); 
		account = new Account();
		service= new Service();
		profile=new Profile();
		
		fetchProfile= new FetchProfile();
		billingSystem = new BillingSystem();
		
		
		account.setShow_itemized_preferences("");
		account.setAccount_id("");
		service.setService_id("756347565");
		profile.setProfile_id("");
		request.setAccount(account);
		request.setProfile(profile);
		request.setService(service);
		billingSystem.setRequest(request);
		billingSystem.setRequest_id("11201869451979");
		billingSystem.setAction("FetchProfile");
		billingSystem.setRequest_timestamp("06034628032017");
		fetchProfile.setBillingSystem(billingSystem);
		
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonInString = mapper.writeValueAsString(fetchProfile);
		System.out.println(jsonInString);
		
		
				
	}

}
*/
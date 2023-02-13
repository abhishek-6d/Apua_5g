/**
 * 
 */
package com.sixdee.lms.util;

import java.util.List;

import com.sixdee.imp.dto.CustomerProfileTabDTO;
import com.sixdee.lms.dao.profileInformationDAO.CustomerProfileInformaionDAO;
import com.sixdee.lms.util.selections.CustomerProfileQueryType;

/**
 * @author rahul.kr
 *
 */
public class ProfileQueryUtil {

	
	public List<CustomerProfileTabDTO> getCustomerProfileDetails(String requestId,String key,CustomerProfileQueryType customerProfileQueryType){
		CustomerProfileInformaionDAO customerProfileInformaionDAO = null;
		List<CustomerProfileTabDTO> customerProfileList = null;
		try{
			customerProfileInformaionDAO = new CustomerProfileInformaionDAO();
			switch(customerProfileQueryType){
				case Msisdn :  customerProfileList = customerProfileInformaionDAO.getCustomerProfileInfoBasedOnMsisdn(requestId," msisdn=:key " , key);
					break;
				case AccountNumber : customerProfileList = customerProfileInformaionDAO.getCustomerProfileInfoBasedOnMsisdn(requestId," accountNo=:key ",key);
						break;
				case CRN : customerProfileList = customerProfileInformaionDAO.getCustomerProfileInfoBasedOnMsisdn(requestId," crn=:key ",key);
						break;
			}
		}finally{
			
		}
		return customerProfileList;
	}
}

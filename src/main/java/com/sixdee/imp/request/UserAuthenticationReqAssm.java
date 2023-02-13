package com.sixdee.imp.request;

/**
 * 
 * @author Nithin Kunjappan
 * @version 1.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%"><b>Date</b></td>
 * <td width="20%"><b>Author</b></td>
 * </tr>
 * <tr>
 * <td>April 24,2013 05:54:40 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.dto.UserAuthenticationDTO;
import com.sixdee.imp.service.serviceDTO.req.AuthenticateDTO;



public class UserAuthenticationReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> UserAuthenticationReqAssm :: Method ==> buildAssembleGUIReq ");

		AuthenticateDTO authenticateDTO = (AuthenticateDTO)genericDTO.getObj();
		UserAuthenticationDTO userAuthenticationDTO = null;
		try{			
			userAuthenticationDTO=new UserAuthenticationDTO();
			//userAuthenticationDTO=(UserAuthenticationDTO)genericDTO.getObj();
			userAuthenticationDTO.setSubscriberNumber(authenticateDTO.getSubscriberNumber());
			userAuthenticationDTO.setPin(""+authenticateDTO.getPin());
			userAuthenticationDTO.setTransactionId(authenticateDTO.getTransactionId());
			userAuthenticationDTO.setChannel(authenticateDTO.getChannel());
			userAuthenticationDTO.setLanguageID(authenticateDTO.getLanguageID());
			
			logger.info("SUBSCRIBER NUMBER::>>"+userAuthenticationDTO.getSubscriberNumber());
			//logger.info("PIN::>>"+userAuthenticationDTO.getPin());
			logger.info("Language ID ::>>"+userAuthenticationDTO.getLanguageID());
			
			if(!isADSL(""+userAuthenticationDTO.getSubscriberNumber()))
			{
			userAuthenticationDTO.setADSLNumber(true);
			}
			
			logger.info("ADSL BEAN VALUE::>>"+userAuthenticationDTO.isADSLNumber());
			
			userAuthenticationDTO.setAuthenticate(true);
			
			genericDTO.setObj(userAuthenticationDTO);	

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(userAuthenticationDTO);
			userAuthenticationDTO = null;
		}

		return genericDTO;
	}
	
	private boolean isADSL(String s) {
		boolean result=true;
		char[] c = s.toCharArray();		
	      for(int i=0; i < s.length(); i++)
	      {
	          if (Character.isDigit(c[i]))
	          {}
	          else
	          {
	        	  result=false; 
	          }
	     }
	     return result;
	}	

}

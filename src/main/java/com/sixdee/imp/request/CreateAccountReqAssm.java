package com.sixdee.imp.request;
/**
 * 
 * @author Paramesh
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
 * <td>APR 15, 2013</td>
 * <td>Paramesh</td>
 * </tr>
 * </table>
 * </p>
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.CreateAccountDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.AccountDTO;

public class CreateAccountReqAssm extends ReqAssmCommon 
{
	 
	/**
	 * This method is called from the framework class in Request Assembler Layer. Converts request Object to Value Object
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	Logger logger=Logger.getLogger(CreateAccountReqAssm.class);
	

	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {

		
		boolean selectAllAcounts=false;
		CreateAccountDTO createAccountDTO=null;
		try {
			createAccountDTO=new CreateAccountDTO();
			logger.info("Cache config map "+Cache.getConfigParameterMap());
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			Object object=genericDTO.getObj();
			if(object instanceof AccountDTO)
			{
				AccountDTO accountDTO=(AccountDTO)object;
				
				createAccountDTO.setTransactionId(accountDTO.getTransactionId());
				createAccountDTO.setTimestamp(accountDTO.getTimestamp());
				
				List<String> registerNumber=null;
				if(accountDTO.getRegisterNumbers()!=null&&accountDTO.getRegisterNumbers().length>0)
				{
					List<String> regNum=Arrays.asList(accountDTO.getRegisterNumbers());
					registerNumber=new ArrayList<String>(regNum);
				} 
				logger.info("Before Check REQ : Mo Number Numbers "+accountDTO.getMoNumber() +" subscriberCountryCode "+subscriberCountryCode +" subscriberSize "+subscriberSize);
				if(accountDTO.getMoNumber()!=null&&accountDTO.getMoNumber().length()==subscriberSize)
					accountDTO.setMoNumber(subscriberCountryCode+accountDTO.getMoNumber());
				
				logger.info("After check REQ : Mo Number Numbers "+accountDTO.getMoNumber() +" subscriberCountryCode "+subscriberCountryCode +" subscriberSize "+subscriberSize);
				if(registerNumber!=null)
				{
					ListIterator<String>  iterator=registerNumber.listIterator();
					while(iterator.hasNext())
					{
						String value=iterator.next();
						if(value==null||value.trim().equalsIgnoreCase(""))
							continue;
						
						value=value.trim();
						
						if(value.length()==subscriberSize)
						{
							value=subscriberCountryCode+value;
							iterator.set(value);
						}
						
						if(value.equalsIgnoreCase("ALL"))
						{
							selectAllAcounts=true;
							iterator.remove();
						}
					}
					
				}else{
					registerNumber=new ArrayList<String>();
				}
				
				
				if(accountDTO.getMoNumber()!=null&&!accountDTO.getMoNumber().trim().equalsIgnoreCase(""))
				  registerNumber.add(0,accountDTO.getMoNumber());
				
				
				registerNumber=new ArrayList<String>(new LinkedHashSet<String>(registerNumber));
				
				logger.info("REQ : Select All Number Flag "+selectAllAcounts);
				logger.info("REQ : Mo Number Numbers "+accountDTO.getMoNumber());
				logger.info("REQ : Register Numbers "+registerNumber);
				logger.info("Language ID "+accountDTO.getLanguageID());
				
				if(accountDTO.getData()!=null)
				{
					List<Data> list=new ArrayList<Data>(Arrays.asList(accountDTO.getData()));
					
					createAccountDTO.setDataSet(list);
					
					
					for(int i=0;i<list.size();i++){
						if (list.get(i).getName()!=null && list.get(i).getName().equalsIgnoreCase("PrimaryNumber")) {
							if(list.get(i).getValue().equalsIgnoreCase("TRUE")){
								createAccountDTO.setPrimaryNumber(true);
							}
						}
						}
				}
				logger.info("PRIMARY FLAG CUMING IS::"+createAccountDTO.isPrimaryNumber());
				// Checking
				
				
				createAccountDTO.setCreate(true);
				createAccountDTO.setAllAccounts(selectAllAcounts);
				
				if(accountDTO.getMoNumber()!=null)
				  createAccountDTO.setMoNumber(Long.parseLong(accountDTO.getMoNumber()));
				
				createAccountDTO.setChannel(Cache.channelDetails.get(accountDTO.getChannel().toUpperCase()));
				
				createAccountDTO.setRegisterNumbers(registerNumber);
				createAccountDTO.setLanguageID(accountDTO.getLanguageID());
			}else{
				createAccountDTO=(CreateAccountDTO)genericDTO.getObj();
			}
			
			
		}catch (Exception e) {
				e.printStackTrace();
		}finally {
			genericDTO.setObj(createAccountDTO);
		}
		return genericDTO;
	
	}
	
	
}

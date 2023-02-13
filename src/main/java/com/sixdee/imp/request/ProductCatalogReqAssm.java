package com.sixdee.imp.request;

/**
 * 
 * @author S@j!th
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
 * <td>October 30,2015 11:55:15 AM</td>
 * <td>S@j!th</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.ProductCatalogDTO;
import com.sixdee.imp.service.serviceDTO.req.PackageDTO;



public class ProductCatalogReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ProductCatalogReqAssm :: Method ==> buildAssembleGUIReq ");
		ProductCatalogDTO productCatalogDTO = null;
		PackageDTO packageDTO = null;
		int defaultLangId=1;
		try{
			productCatalogDTO=new ProductCatalogDTO();
			packageDTO = (PackageDTO)genericDTO.getObj();
			
			int length = Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue().toString());
			
			if(packageDTO.getSubscriberNumber()!=null && packageDTO.getSubscriberNumber().length()==length)
				packageDTO.setSubscriberNumber(Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue()+packageDTO.getSubscriberNumber());

			
			String subsNumber = packageDTO.getSubscriberNumber();
			
			if(packageDTO.getLanguageId()==null || packageDTO.getLanguageId().trim().equalsIgnoreCase("") ){
				productCatalogDTO.setDefaultLanguageId(defaultLangId);
			}else{
			productCatalogDTO.setDefaultLanguageId(Integer.parseInt(packageDTO.getLanguageId()));
			}
			
			/*if(subsNumber.startsWith(subscriberCountryCode))
				subsNumber = subsNumber.substring(subscriberCountryCode.length());*/
			logger.info("Subscriber number is = "+subsNumber);
			logger.info("Subscriber language is = "+productCatalogDTO.getDefaultLanguageId());
			
			CommonUtil util = new CommonUtil();
			if(subsNumber!=null && util.isItChar(subsNumber))
				productCatalogDTO.setAdsl(true);
			
			productCatalogDTO.setTransactionId(packageDTO.getTranscationId());
			productCatalogDTO.setSubscriberNumber(subsNumber);
			if(packageDTO.getChannel()!=null)
				productCatalogDTO.setChannel(packageDTO.getChannel().toUpperCase());
			
			if(productCatalogDTO.getChannel()!=null)
				productCatalogDTO.setGetPackage(true);
				
			

		

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(productCatalogDTO);
			productCatalogDTO = null;
		}

		return genericDTO;
	}

}

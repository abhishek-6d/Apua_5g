package com.sixdee.imp.request;

/**
 * 
 * @author Somesh
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
 * <td>May 10,2013 12:35:06 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.GetPackagesDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.PackageDTO;
import com.sixdee.imp.util.CommonServiceConstants;

public class GetPackagesReqAssm extends ReqAssmCommon {
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {

		logger.info(" Class ==> GetPackagesReqAssm :: Method ==> buildAssembleGUIReq ");
		GetPackagesDTO getPackagesDTO = null;
		PackageDTO packageDTO = null;
		CommonUtil commonUtil=null;
		try {
			getPackagesDTO = new GetPackagesDTO();
			packageDTO = (PackageDTO) genericDTO.getObj();
			commonUtil=new CommonUtil();
			String subsNumber = packageDTO.getSubscriberNumber();

			getPackagesDTO.setLanguageId(Integer.parseInt(packageDTO.getLanguageId()));
			getPackagesDTO.setTimestamp(packageDTO.getTimestamp());
			getPackagesDTO.setTransactionId(packageDTO.getTranscationId());
			getPackagesDTO.setSubscriberNumber(subsNumber);
			
			getPackagesDTO.setSubscriberNumber(commonUtil.discardCountryCodeIfExists(getPackagesDTO.getSubscriberNumber()));
			
			/*if (subsNumber.length() > 10 && subsNumber.startsWith("1")) {
				subsNumber = subsNumber.replaceFirst("1", "");
				getPackagesDTO.setSubscriberNumber(subsNumber);
			}else
			{
				getPackagesDTO.setSubscriberNumber(subsNumber);
			}
			*/
			if (packageDTO.getChannel() != null)
			{
				getPackagesDTO.setChannel(packageDTO.getChannel().toUpperCase());
			if(packageDTO.getChannel().equalsIgnoreCase("SSM"))
			{
				packageDTO.setOfferType("5");
				logger.info("OfferType set as SSM");
			}
			}

			if(packageDTO.getOfferType()!=null)
			getPackagesDTO.setOfferType(packageDTO.getOfferType());

			String offerId = getExtraParamValue(CommonServiceConstants.offerId, packageDTO);
			if (offerId != null)
				getPackagesDTO.setOfferId(offerId);

			String isFirstlevel = getExtraParamValue(CommonServiceConstants.isFirstLevel, packageDTO);
			if (isFirstlevel != null)
				getPackagesDTO.setIsFirstLevel(isFirstlevel);

		} catch (Exception e) {
			logger.info("Exception "+e.getMessage());
			e.printStackTrace();
		} finally {
			genericDTO.setObj(getPackagesDTO);
			getPackagesDTO = null;
			commonUtil=null;
		}

		return genericDTO;
	}

	public String getExtraParamValue(String param,PackageDTO packageDTO)
	{
		String paramValue = null;
		Data data[] = packageDTO.getData();
		if (data != null && data.length > 0) {
			for (int i = 0; i < data.length; i++) {
				if (data[i].getName() != null && data[i].getValue() != null)
					if (data[i].getName().equalsIgnoreCase(param)) {
						paramValue = data[i].getValue();
						break;
					}
			}
		}
		return paramValue;
	}

}

	package com.sixdee.imp.request;

/**
 * 
 * @author Athul Gopal
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
 * <td>April 15,2015 06:58:41 PM</td>
 * <td>Athul Gopal</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.dto.MerchantDiscountDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantDetailsDTO;



@SuppressWarnings("deprecation")
public class MerchantDiscountReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> MerchantDiscountReqAssm :: Method ==> buildAssembleGUIReq ");
		MerchantDiscountDTO merchantDiscountDTO = null;
		try{
			merchantDiscountDTO=new MerchantDiscountDTO();
			
			MerchantDetailsDTO detailsDTO=(MerchantDetailsDTO)genericDTO.getObj();
			
			merchantDiscountDTO.setTransactionId(detailsDTO.getTransactionId());
			merchantDiscountDTO.setChannel(detailsDTO.getChannel());
			merchantDiscountDTO.setData(detailsDTO.getData());
			merchantDiscountDTO.setMerchantId(detailsDTO.getMerchantId());
			merchantDiscountDTO.setPin(detailsDTO.getPin());
			merchantDiscountDTO.setLanguageID(detailsDTO.getLanguageID());
			merchantDiscountDTO.setSubscriberNumber(detailsDTO.getSubscriberNumber());
			merchantDiscountDTO.setTimestamp(detailsDTO.getTimestamp());
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(merchantDiscountDTO);
			merchantDiscountDTO = null;
		}

		return genericDTO;
	}

}

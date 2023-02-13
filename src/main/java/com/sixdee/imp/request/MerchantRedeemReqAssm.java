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
 * <td>April 21,2015 11:42:09 AM</td>
 * <td>Athul Gopal</td>
 * </tr>
 * </table>
 * </p>
 */




import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.dto.MerchantRedeemDTO;
import com.sixdee.imp.service.serviceDTO.req.MerchantRedeemReqDTO;



public class MerchantRedeemReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> MerchantRedeemReqAssm :: Method ==> buildAssembleGUIReq ");
		MerchantRedeemDTO merchantRedeemDTO = null;
		try{
			merchantRedeemDTO=new MerchantRedeemDTO();
			MerchantRedeemReqDTO dto=(MerchantRedeemReqDTO) genericDTO.getObj();
			
			merchantRedeemDTO.setChannel(dto.getChannel());
			merchantRedeemDTO.setDiscount(dto.getDiscount());
			merchantRedeemDTO.setLanguageID(dto.getLanguageID());
			merchantRedeemDTO.setMerchantId(dto.getMerchantId());
			merchantRedeemDTO.setPin(dto.getPin());
			merchantRedeemDTO.setSubscriberNumber(dto.getSubscriberNumber());
			merchantRedeemDTO.setTierId(dto.getTierId()+"");
			merchantRedeemDTO.setTimestamp(dto.getTimestamp());
			merchantRedeemDTO.setTransactionId(dto.getTransactionId());
			merchantRedeemDTO.setBranchId(dto.getBranchId());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(merchantRedeemDTO);
			merchantRedeemDTO = null;
		}

		return genericDTO;
	}

}

package com.sixdee.imp.validator.format;
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
 * <td>May 15,2013 12:24:17 PM</td>
 * <td>Somesh</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.dto.RedeemPointsDTO;
import com.sixdee.imp.dto.UserprofileDTO;

public class RedeemPointsFrmtValidator extends FrmtValidatorCommon{
	
	/**
	 * This method is called from the framework class in Format Validator Layer.
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		logger.debug("Method =>buildValidateGUIReq()");
		RedeemPointsDTO redeemPointsDTO = null;
		try{
			redeemPointsDTO = (RedeemPointsDTO) genericDTO.getObj();
			if((redeemPointsDTO.getTransactionId()==null || redeemPointsDTO.getTransactionId().equalsIgnoreCase("")) ){
				throw new CommonException("Transaction id is missing in request");
			}
			if((redeemPointsDTO.getTimestamp()==null || redeemPointsDTO.getTimestamp().equalsIgnoreCase("")) ){
				throw new CommonException("TimeStamp id is missing in request");
			}
			if((redeemPointsDTO.getChannel()==null || redeemPointsDTO.getChannel().equalsIgnoreCase("")) ){
				throw new CommonException("Channel  is missing in request");
			}
			if((redeemPointsDTO.getPackageId()==null || redeemPointsDTO.getPackageId().equalsIgnoreCase("")) ){
				throw new CommonException("Package id is missing in request");
			}
			
		}catch(CommonException ce){
			redeemPointsDTO.setStatusCode("SC0001");
			redeemPointsDTO.setStatusDesc(ce.getMessage());
			genericDTO.setStatus(ce.getMessage());
			logger.info(">>>>mandatory parameter missing>>>>");
			throw ce;
		}
		genericDTO.setObj(redeemPointsDTO);
		return genericDTO;
	}


}

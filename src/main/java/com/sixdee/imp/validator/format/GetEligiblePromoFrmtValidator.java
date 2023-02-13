package com.sixdee.imp.validator.format;
/**
 * 
 * @author Rahul K K
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
 * <td>April 24,2013 05:44:52 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorCommon;
import com.sixdee.imp.util.Validations;
import com.sixdee.imp.vo.GetEligiblePromoVO;

public class GetEligiblePromoFrmtValidator extends FrmtValidatorCommon {

	public GenericDTO buildValidateReq(GenericDTO genericDTO) throws CommonException {
		GetEligiblePromoVO getEligiblePromoVO = null;
		String subsNumber = null;
		Validations validate = null; 
		try{
			getEligiblePromoVO = (GetEligiblePromoVO) genericDTO.getObj();
			validate = new Validations();
			
			if(getEligiblePromoVO.getTransactionId()==null || getEligiblePromoVO.getTransactionId().trim().equals("")){
				throw new CommonException("Transaction Id not recieved");
			}
			if(getEligiblePromoVO.getTimestamp()==null || getEligiblePromoVO.getTimestamp().trim().equals("") || !(validate.validateTimeStamp(getEligiblePromoVO.getTimestamp().trim(),"ddMMyyyyHHmmSS")) ){
				throw new CommonException("TimeStamp not recieved or empty or in wrong format "+getEligiblePromoVO.getTimestamp()!=null?getEligiblePromoVO.getTimestamp():"");
			}
			if(getEligiblePromoVO.getChannel()==null || getEligiblePromoVO.getChannel().trim().equals("")){
				throw new CommonException("Channel Id not recieved or empty");
			}
			subsNumber = getEligiblePromoVO.getSubsNumber();
			if(subsNumber==null || subsNumber.trim().equals("")){
				throw new CommonException("Subsriber Number recieved is null or empty");
			}
			
			if(!(validate.validateSubsNumber(subsNumber))){
				throw new CommonException("Subscriber Number ["+subsNumber+"] does not meet validation " +
						"criterias ");
			}
			
		}catch (CommonException e) {
			logger.error("Exception occured ",e);
			genericDTO.setStatus("SC0003");
			throw e;
		}finally{
			
			validate = null;
			subsNumber = null;
		}
		return genericDTO;
	}
	


}

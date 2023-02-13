package com.sixdee.imp.validator.format;
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
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.validator.format.FrmtValidatorGUICommon;
import com.sixdee.imp.dto.CreateAccountDTO;
import com.sixdee.imp.dto.UserprofileDTO;

public class CreateAccountFrmtValidator extends FrmtValidatorGUICommon 
{
	public GenericDTO buildValidateGUIReq(GenericDTO genericDTO) throws CommonException 
	{
		CreateAccountDTO createAccountDTO=null;
		try {
			createAccountDTO = (CreateAccountDTO) genericDTO.getObj();
			if((createAccountDTO.getTransactionId()==null || createAccountDTO.getTransactionId().equalsIgnoreCase("")) ){
				throw new CommonException("Transaction id is missing in request");
			}
			if((createAccountDTO.getTimestamp()==null || createAccountDTO.getTimestamp().equalsIgnoreCase("")) ){
				throw new CommonException("TimeStamp id is missing in request");
			}
			if((createAccountDTO.getChannel()==null || createAccountDTO.getChannel().equalsIgnoreCase("")) ){
				throw new CommonException("Channel id is missing in request");
			}
			
			
		}catch(CommonException ce){
			createAccountDTO.setStatusCode("SC0001");
			createAccountDTO.setStatusDesc(ce.getMessage());
			genericDTO.setStatus(ce.getMessage());
			genericDTO.setStatusCode("SC0001");
			logger.info(">>>>mandatory parameter missing>>>>");
			throw ce;
		}
		genericDTO.setObj(createAccountDTO);
		return genericDTO;
	}

	 
}

/**
 * 
 */
package com.sixdee.imp.response;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCommon;
import com.sixdee.imp.service.UpdateAccountInfoResponseDTO;
import com.sixdee.imp.vo.ProfileInformationUpdateVO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>April 24, 2013</td>
 * <td>Rahul K K</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class UpdateSubscriberInfoRespAssm extends RespAssmCommon {

	private static final Logger logger = Logger.getLogger(UpdateSubscriberInfoRespAssm.class);
	/* (non-Javadoc)
	 * @see com.sixdee.fw.response.RespAssmInfCommon#buildAssembleResp(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException {
		// TODO Auto-generated method stub
	//	UpdateSubscriberInfoRespAssm updateSubscriberInfoRespAssm = null;
		UpdateAccountInfoResponseDTO updateAccountInfoResponseDTO = null;
		ProfileInformationUpdateVO profileInformationUpdateVO = null;
		try{
			profileInformationUpdateVO = (ProfileInformationUpdateVO) genericDTO.getObj();
			logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateVO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateVO.getMsisdn()+"] Request in Resp-assembler layer");
		
			updateAccountInfoResponseDTO = new UpdateAccountInfoResponseDTO();
			updateAccountInfoResponseDTO.setStatusCode(profileInformationUpdateVO.getStatusCode());
			updateAccountInfoResponseDTO.setStatusDescription(profileInformationUpdateVO.getStatusDesc());
			updateAccountInfoResponseDTO.setTranscationId(profileInformationUpdateVO.getTransactionId());
			updateAccountInfoResponseDTO.setTimestamp(profileInformationUpdateVO.getTimestamp());
			//updateAccountInfoResponseDTO.set
			
		}catch(Exception e){
			logger.error("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+profileInformationUpdateVO.getTransactionId()+"] "
					+ "MSISDN :- ["+profileInformationUpdateVO.getMsisdn()+"] Exception",e);
		
		}finally{
			genericDTO.setObj(updateAccountInfoResponseDTO);
		}
		return genericDTO;
	}

}

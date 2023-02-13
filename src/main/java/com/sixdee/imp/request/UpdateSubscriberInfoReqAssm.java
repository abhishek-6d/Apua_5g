/**
 * 
 */
package com.sixdee.imp.request;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.response.UpdateSubscriberInfoRespAssm;
import com.sixdee.imp.service.serviceDTO.req.UpdateAccountInfoRequestDTO;
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
public class UpdateSubscriberInfoReqAssm extends ReqAssmCommon {


	private static final Logger logger = Logger.getLogger(UpdateSubscriberInfoRespAssm.class);
	/* (non-Javadoc)
	 * @see com.sixdee.fw.response.RespAssmInfCommon#buildAssembleResp(com.sixdee.fw.dto.GenericDTO)
	 */
	@Override
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		// TODO Auto-generated method stub
		ProfileInformationUpdateVO profileInformationUpdateVO = null;
		UpdateAccountInfoRequestDTO updateAccountInfoRequestDTO = null;
		try{
			updateAccountInfoRequestDTO = (UpdateAccountInfoRequestDTO) genericDTO.getObj();
			logger.info("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+updateAccountInfoRequestDTO.getTransactionId()+"] "
					+ "MSISDN :- ["+updateAccountInfoRequestDTO.getSubscriberNumber()+"] Request in assembler layer");
			profileInformationUpdateVO = new ProfileInformationUpdateVO();
			profileInformationUpdateVO.setTransactionId(updateAccountInfoRequestDTO.getTransactionId());
			profileInformationUpdateVO.setTimestamp(updateAccountInfoRequestDTO.getTimestamp());
			profileInformationUpdateVO.setChannel(updateAccountInfoRequestDTO.getChannel());
			profileInformationUpdateVO.setMsisdn(updateAccountInfoRequestDTO.getSubscriberNumber());
			profileInformationUpdateVO.setNewNationalId(updateAccountInfoRequestDTO.getNewId());
			profileInformationUpdateVO.setNewNationalIdType(updateAccountInfoRequestDTO.getNewIdType());
			profileInformationUpdateVO.setOldNationalId(updateAccountInfoRequestDTO.getOldId());
			profileInformationUpdateVO.setOldNationalIdType(updateAccountInfoRequestDTO.getOldIdType());
			profileInformationUpdateVO.setStatusCode("SC0000");
			profileInformationUpdateVO.setStatusDesc("Success");
		}catch(Exception e){
			logger.error("SERVICE :- UpdateSubscriberAccount - Transaction ID :- ["+updateAccountInfoRequestDTO.getTransactionId()+"] "
					+ "MSISDN :- ["+updateAccountInfoRequestDTO.getSubscriberNumber()+"] Request in assembler layer");
			if(profileInformationUpdateVO==null){
				profileInformationUpdateVO = new ProfileInformationUpdateVO();
				profileInformationUpdateVO.setStatusCode("SC1000");
				profileInformationUpdateVO.setStatusDesc("Request Failed");
			}	
			throw new CommonException(e.getMessage());
		}finally{
			genericDTO.setObj(profileInformationUpdateVO);
		}
		return genericDTO;
	}


}

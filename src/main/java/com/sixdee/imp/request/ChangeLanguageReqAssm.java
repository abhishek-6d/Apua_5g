package com.sixdee.imp.request;

/**
 * 
 * @author Nithin Kunjappan
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
 * <td>September 05,2013 07:34:59 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.arch.Globals;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.dto.ChangeLanguageDTO;
import com.sixdee.imp.vo.ChangeLanguageVO;



public class ChangeLanguageReqAssm extends ReqAssmGUICommon {
	
	
	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> ChangeLanguageReqAssm :: Method ==> buildAssembleGUIReq ");
		ChangeLanguageVO changeLanguageVO = null;
		try{
			changeLanguageVO=new ChangeLanguageVO();
			
			com.sixdee.imp.service.serviceDTO.req.SubscriberLanguageDTO langDTO=(com.sixdee.imp.service.serviceDTO.req.SubscriberLanguageDTO)genericDTO.getObj();
			changeLanguageVO.setSubscriberNumber(langDTO.getMoNumber());
			changeLanguageVO.setChannel(langDTO.getChannel());
			changeLanguageVO.setOldLanguageID(Integer.parseInt(langDTO.getLanguageID()));
			changeLanguageVO.setNewLanguageID(Integer.parseInt(langDTO.getChangeLanguageID()));
			changeLanguageVO.setLanguageID(langDTO.getLanguageID());
			
			logger.info("Subscriber Number"+changeLanguageVO.getSubscriberNumber());
			logger.info("Channel"+changeLanguageVO.getChannel());
			logger.info("Old Language"+changeLanguageVO.getOldLanguageID());
			logger.info("New Language"+changeLanguageVO.getNewLanguageID());
			logger.info("Language ID"+changeLanguageVO.getLanguageID());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(changeLanguageVO);
			changeLanguageVO = null;
		}

		return genericDTO;
	}

}

package com.sixdee.imp.bo;

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
 * <td>September 05,2013 07:34:58 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.List;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dao.ChangeLanguageDAO;
import com.sixdee.imp.dto.ChangeLanguageDTO;
import com.sixdee.imp.vo.ChangeLanguageVO;

public class ChangeLanguageBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => ChangeLanguageBL :: Method => buildProcess()");
		
		ChangeLanguageDAO  changeLanguageDAO;
		ChangeLanguageVO changeLanguageVO = (ChangeLanguageVO) genericDTO.getObj();

		try{
			changeLanguageDAO=new ChangeLanguageDAO();	
			if(changeLanguageDAO.updateLanguageID(changeLanguageVO))
			{
				changeLanguageVO.setStatusCode(Cache.getServiceStatusMap().get("LANG_SUCCESS_"+changeLanguageVO.getLanguageID()).getStatusCode());
				changeLanguageVO.setStatusDesc(Cache.getServiceStatusMap().get("LANG_SUCCESS_"+changeLanguageVO.getLanguageID()).getStatusDesc());
				logger.info("Language Updated Successfully...");
			}
			else
			{
				changeLanguageVO.setStatusCode(Cache.getServiceStatusMap().get("LANG_NT_EXIST_"+changeLanguageVO.getLanguageID()).getStatusCode());
				changeLanguageVO.setStatusDesc(Cache.getServiceStatusMap().get("LANG_NT_EXIST_"+changeLanguageVO.getLanguageID()).getStatusDesc());
				logger.info("Record doesnt Exists...");
			}
			}
		catch(Exception e)
		{
		changeLanguageVO.setStatusCode(Cache.getServiceStatusMap().get("LANG_FAILURE_"+changeLanguageVO.getLanguageID()).getStatusCode());
		changeLanguageVO.setStatusDesc(Cache.getServiceStatusMap().get("LANG_FAILURE_"+changeLanguageVO.getLanguageID()).getStatusDesc());
		logger.info("Failure ...Error Occured");
		logger.error(e.getMessage());
		e.printStackTrace();
		}
			return genericDTO;
		}
}

package com.sixdee.imp.bo;

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
 * <td>May 27,2013 01:17:50 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Globals;
import com.sixdee.imp.dao.FailureActionDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.FailureActionDTO;

public class FailureActionBO extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private TableInfoDAO tableInfoDAO = new TableInfoDAO();
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => FailureActionBO :: Method => buildProcess()");
		
		FailureActionDTO failureActionDTO = (FailureActionDTO) genericDTO.getObj();
		try{
			processFailureRequest(failureActionDTO);
			genericDTO.setStatusCode("0");
		}catch (CommonException e) {
			logger.error("Exception occured ",e);
			genericDTO.setStatusCode("1");
			genericDTO.setStatus("FAILURE");
		}finally{
			
		}
		
					
			return genericDTO;
		}

	private void processFailureRequest(FailureActionDTO failureActionDTO) throws CommonException {
		FailureActionDAO  failureActionDAO;
		String tableName = null;
		int serviceId = 0;
		try{
			tableName = tableInfoDAO.getSubscriberProfileDBTable(failureActionDTO.getSubscriberNumber());
			failureActionDAO = new FailureActionDAO();
			serviceId=new TableDetailsDAO().getServiceDetails(failureActionDTO.getServiceName()).getServiceID();
			
			failureActionDAO.updateFailureSubscriber(tableName,serviceId,failureActionDTO);
		}catch (CommonException e) {
			throw e;
		}finally{
			failureActionDAO = null;
		}
	}
	
	public static void main(String[] args) {
		FailureActionBO failureActionBO = new FailureActionBO();
	//	Cache.getCacheMap();
		Globals.cacheMap();
		FailureActionDTO failureActionDTO = new FailureActionDTO();
		failureActionDTO.setTransactionId("1232131232");
		failureActionDTO.setServiceName("bonus");
		failureActionDTO.setSubscriberNumber("9902390347");
		GenericDTO genericDTO = new GenericDTO();
		genericDTO.setObj(failureActionDTO);
		try {
			failureActionBO.buildProcess(genericDTO);
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

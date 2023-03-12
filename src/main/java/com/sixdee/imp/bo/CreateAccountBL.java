package com.sixdee.imp.bo;

import java.util.ArrayList;
import java.util.HashMap;

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
import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TransferPointsDAO;
import com.sixdee.imp.dto.CreateAccountDTO;
import com.sixdee.imp.utill.DataSet;
import com.sixdee.imp.utill.Param;
import com.sixdee.imp.utill.Request;
import com.sixdee.imp.utill.Response;
import com.sixdee.lms.bo.CreateLoyaltyAccountBO;
import com.sixdee.lms.util.constant.SystemConstants;

public class CreateAccountBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {
		logger.info("Class => CreateAccountBL :: Method => buildProcess()");
		CreateAccountDTO createAccountDTO = null;
		TransferPointsDAO transferPointsDAO = null;
		Long loyaltyID = null;
		Response response = null;
		CreateLoyaltyAccountBO createLoyaltyAccountBO;
		TableDetailsDAO tableDetailsDAO;
		boolean isBlacklisted=false;
		Long t1=System.currentTimeMillis();
		try {
			tableDetailsDAO=new TableDetailsDAO();
			createLoyaltyAccountBO=new CreateLoyaltyAccountBO();
			createAccountDTO = (CreateAccountDTO) genericDTO.getObj();
			transferPointsDAO = new TransferPointsDAO();
			loyaltyID = transferPointsDAO.getLoyaltyId(createAccountDTO.getMoNumber() + "");
			logger.info("Transction id " + createAccountDTO.getTransactionId() + "Loyalty id" + loyaltyID);
			if (loyaltyID == null) {
				isBlacklisted=tableDetailsDAO.getBlackListDetails(createAccountDTO.getMoNumber() + "",1);
				if (!isBlacklisted) {
					response = createLoyaltyAccountBO.createAccount(createAccountDTO.getTransactionId(),createAccountDTO.getMoNumber() + "", createAccountDTO.getChannel());
				logger.info("Transction id " + createAccountDTO.getTransactionId() + " Response code create Account "+ response.getRespCode());
				if (response != null && response.getRespCode().equalsIgnoreCase("SC0000")) {
					createAccountDTO.setStatusCode(response.getRespCode());
					createAccountDTO.setStatusDesc(response.getRespDesc());
					accountNotification(createAccountDTO);
				}  else if (response != null && response.getDataSet() != null) {
						for (int i = 0; i < response.getDataSet().getParameter1().size(); i++) {
							if (response.getDataSet().getParameter1().get(i).getId()
									.equalsIgnoreCase("BLACKLIST_CHECK")) {
								if (response.getDataSet().getParameter1().get(i).getValue()
										.equalsIgnoreCase("true")) {
									this.logger.info("Blacllist User");
									createAccountDTO.setStatusCode((Cache.getServiceStatusMap()
											.get("LOYALTY_BLACLIST_" + createAccountDTO.getLanguageID()))
													.getStatusCode());
									createAccountDTO.setStatusDesc((Cache.getServiceStatusMap()
											.get("LOYALTY_BLACLIST_" + createAccountDTO.getLanguageID()))
													.getStatusDesc());
									break;
								}
								this.logger.info("LoyaltyFailure");
								createAccountDTO.setStatusCode((Cache.getServiceStatusMap().get("LOYALTY_FAILURE_" + createAccountDTO.getLanguageID())).getStatusCode());
								createAccountDTO.setStatusDesc((Cache.getServiceStatusMap().get("LOYALTY_FAILURE_" + createAccountDTO.getLanguageID())).getStatusDesc());
								break;
							}
						}
					} else if (response != null && response.getRespCode() != null && response.getRespDesc() != null) {
								createAccountDTO.setStatusCode(response.getRespCode());
								createAccountDTO.setStatusDesc(response.getRespDesc());
							}
			}else {
				createAccountDTO.setStatusCode((Cache.getServiceStatusMap().get("LOYALTY_BLACLIST_" + createAccountDTO.getLanguageID())).getStatusCode());
				createAccountDTO.setStatusDesc((Cache.getServiceStatusMap().get("LOYALTY_BLACLIST_" + createAccountDTO.getLanguageID())).getStatusDesc());
			} }else
			    	 {
			    		 createAccountDTO.setStatusCode(Cache.getServiceStatusMap().get("SUB_ALREADY_EXISTS_"+createAccountDTO.getLanguageID()).getStatusCode());
			    		 createAccountDTO.setStatusDesc(Cache.getServiceStatusMap().get("SUB_ALREADY_EXISTS_"+createAccountDTO.getLanguageID()).getStatusDesc());
			    		 logger.info("TransactionId"+createAccountDTO.getTransactionId()+" subscriber Already Registered");
			    	 }
				 genericDTO.setObj(createAccountDTO);
				 
				 logger.info("TransactionId"+createAccountDTO.getTransactionId()+" status code "+createAccountDTO.getStatusCode()+"TotalTime:"+(t1-System.currentTimeMillis()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				 transferPointsDAO = null;
				 loyaltyID = null;
				 response = null;
				 createAccountDTO=null;
				
			}
			return genericDTO;
		}

	private Request getRuleRequest(CreateAccountDTO createAccountDTO) {
		Request request = new Request();
		request.setRequestId(createAccountDTO.getTransactionId());
		request.setTimeStamp(createAccountDTO.getTimestamp());
		request.setKeyWord("CreateAccount");
		if (createAccountDTO.getRegisterNumbers() != null) {
			String registerNumber = null;
			if (createAccountDTO.getRegisterNumbers() != null) {
				registerNumber = String.valueOf(createAccountDTO.getRegisterNumbers().get(0));
				System.out.println("moNumber length " + registerNumber.length());
				if (registerNumber.length() > 10 && registerNumber.startsWith("1")) {
					registerNumber = registerNumber.replaceFirst("1", "");
				}
			}
			request.setMsisdn(registerNumber);
		} else {
			String moNumber = String.valueOf(createAccountDTO.getMoNumber());
			logger.info("moNumber length " + moNumber.length());
			if (moNumber.length() > 10 && moNumber.startsWith("1")) {
				moNumber = moNumber.replaceFirst("1", "");
				createAccountDTO.setMoNumber(Long.valueOf(moNumber));
			}
			request.setMsisdn(moNumber);
		}
		DataSet dataSet = new DataSet();
		ArrayList<Param> parameter1 = new ArrayList<>();
		parameter1.add(new Param(SystemConstants.MSISDN,createAccountDTO.getMoNumber()+""));
		parameter1.add(new Param(SystemConstants.CHANNEL,createAccountDTO.getChannel()+""));
		dataSet.setParameter1(parameter1);
		request.setDataSet(dataSet);
		return request;
	}
	
	public void accountNotification(CreateAccountDTO createAccountDTO)
	{
		
		String keyWord=null;
		CommonUtil commonUtil = null;
		try {
			commonUtil = new CommonUtil();
			HashMap<String, String> map = new HashMap<>();
			map.put(SystemConstants.MSISDN, createAccountDTO.getMoNumber() + "");
			map.put(SystemConstants.MSG_CAUSE, "CREATE_ACCOUNT_SUCCESS");
			keyWord=(Cache.getConfigParameterMap().get("KEY_WORD")).getParameterValue();
			logger.info("keyWord:"+keyWord);
			commonUtil.generateNotifyRequest(createAccountDTO.getTransactionId(),keyWord ,createAccountDTO.getMoNumber() + "", map);
		} catch (Exception e) {
			logger.info("Exception " + e.getMessage());
		}finally
		{
			commonUtil=null;
		}
	}
	}
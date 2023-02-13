package com.sixdee.imp.request;

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
 * <td>May 06,2013 06:00:07 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.dao.LanguageDAO;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dto.GetTransactionDetailsDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.TransactionDTO;
import com.sixdee.imp.vo.GetTransactionDetailsVO;

public class GetTransactionDetailsReqAssm extends ReqAssmCommon {

	private static final Logger logger = Logger
			.getLogger(GetTransactionDetailsDTO.class);

	@Override
	public GenericDTO buildAssembleReq(GenericDTO genericDTO)
			throws CommonException {

		logger.info(" Class ==> GetTransactionDetailsReqAssm :: Method ==> buildAssembleReq ");
		GetTransactionDetailsDTO getTransactionDetailsDTO = null;
		TransactionDTO transactionDetailsDTO = null;
		String subsNumber = null;
		LanguageDAO languageDAO = null;

		try {
			getTransactionDetailsDTO = new GetTransactionDetailsDTO();

			transactionDetailsDTO = (TransactionDTO) genericDTO.getObj();
			subsNumber = transactionDetailsDTO.getSubscriberNumber();
			genericDTO.setTransactionId(transactionDetailsDTO
					.getTransactionId());
			getTransactionDetailsDTO.setTransactionId(transactionDetailsDTO
					.getTransactionId());
			genericDTO.setTimestamp(transactionDetailsDTO.getTimestamp());
			getTransactionDetailsDTO.setTimestamp(transactionDetailsDTO
					.getTimestamp());
			getTransactionDetailsDTO.setMsisdn(transactionDetailsDTO
					.getMoNumber());
			getTransactionDetailsDTO.setPin(transactionDetailsDTO.getPin());
			getTransactionDetailsDTO.setSubscriberNumber(subsNumber);
			getTransactionDetailsDTO.setChannel(transactionDetailsDTO
					.getChannel() + "");
			getTransactionDetailsDTO.setFromDate(transactionDetailsDTO
					.getFromDate());
			getTransactionDetailsDTO.setEndDate(transactionDetailsDTO
					.getToDate());
			getTransactionDetailsDTO.setNoOfMonths(transactionDetailsDTO
					.getNoOfMonths());
			getTransactionDetailsDTO
					.setNoOfLastTransactions(transactionDetailsDTO
							.getNoOfLastTransaction());
			getTransactionDetailsDTO.setOffset(transactionDetailsDTO
					.getOffSet());
			getTransactionDetailsDTO.setLimit(transactionDetailsDTO.getLimit());
			getTransactionDetailsDTO.setLangId(transactionDetailsDTO.getLanguageID());
			if (transactionDetailsDTO.getData() != null) {

				List<Data> list = new ArrayList<Data>(
						Arrays.asList(transactionDetailsDTO.getData()));

				for (int i = 0; i < list.size(); i++) {

					if (list.get(i) != null
							&& list.get(i).getName() != null
							&& list.get(i).getName()
									.equalsIgnoreCase("STATUS_ID")) {
						/*getTransactionDetailsDTO.setStatusId(Integer
								.parseInt(list.get(i).getValue()));*/
						getTransactionDetailsDTO.setStatusId(list.get(i).getValue());
					}
				}
			}

			CommonUtil commonUtil = new CommonUtil();
			if(getTransactionDetailsDTO.getSubscriberNumber()!=null)
				getTransactionDetailsDTO.setSubscriberNumber(commonUtil.discardCountryCodeIfExists(getTransactionDetailsDTO.getSubscriberNumber()));
			
			if(getTransactionDetailsDTO.getMsisdn()!=null)
				getTransactionDetailsDTO.setMsisdn(commonUtil.discardCountryCodeIfExists(getTransactionDetailsDTO.getMsisdn()));
			
			genericDTO.setStatusCode("SC0000");
		} catch (Exception e) {
			genericDTO.setStatusCode("SC0001");
			genericDTO.setStatus(Cache
					.getServiceStatusMap()
					.get("GET_TRANS_COMMON_FAIL_"
							+ getTransactionDetailsDTO.getLangId())
					.getStatusDesc());

			logger.error("Exception occured on request assembling ["
					+ genericDTO.getTransactionId() + "]");
			throw new CommonException();
		} finally {
			logger.info(">>sub num>>"+getTransactionDetailsDTO.getSubscriberNumber());
			genericDTO.setObj(getTransactionDetailsDTO);
			getTransactionDetailsDTO = null;
			transactionDetailsDTO = null;
		}

		return genericDTO;
	}
}

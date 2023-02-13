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
 * <td>May 30,2013 08:26:31 PM</td>
 * <td>Rahul K K</td>
 * </tr>
 * </table>
 * </p>
 */



import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.parser.InstantRequestDTO;
import com.sixdee.imp.vo.InstantRewardsVO;



public class InstantRewardsReqAssm extends ReqAssmCommon {
	
	
	private Logger logger = Logger.getLogger(InstantRewardsReqAssm.class);
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> InstantRewardsReqAssm :: Method ==> buildAssembleGUIReq ");
		InstantRequestDTO instantRequestDTO = null;
		InstantRewardsVO instantRewardsVO = null;
		String xml = null;
		try{/*
			instantRewardsVO=new InstantRewardsVO();
			xml = (String) genericDTO.getObj();
			instantRequestDTO = (InstantRequestDTO)InstantRewardsParser.getInstantXStreamInstance().fromXML(xml);
			instantRewardsVO.setTransactionId(instantRequestDTO.getRequestId());
			instantRewardsVO.setTimestamp(instantRequestDTO.getTimeStamp());
			instantRewardsVO.setbSubscriberNumber(instantRequestDTO.getMsisdn());
			int  i = 0;
			for(SubscriberRequestParam p : instantRequestDTO.getDataSet().getParam()){
				if(p.getId().equalsIgnoreCase("A_PARTY")){
					instantRewardsVO.setbSubscriberNumber(p.getValue());
				}else if(p.getId().equalsIgnoreCase("OFFER_ID")){
					instantRewardsVO.setOfferId(p.getValue());
				}else  if(p.getId().equalsIgnoreCase("B_PARTY")){
					instantRewardsVO.setbSubscriberNumber(p.getValue());
				}
				if(i==3){
					break;
				}
				if(i<2){
					throw new CommonException("Either two of A_Party/B_Party/OFFER_ID is missing");
				}
			}
			
			
		*/} catch (Exception e) {
			logger.error("Exception occured ",e);
			throw new CommonException(e.getMessage());
		} finally {
		//	genericDTO.setObj(instantRewardsVO);
			instantRewardsVO = null;
		}

		return genericDTO;
	}

}

package com.sixdee.imp.response;

/**
 * 
 * @author Somesh Soni
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
 * <td>September 13,2013 12:46:40 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCommon;
import com.sixdee.imp.dto.SubsHistoryDTO;
import com.sixdee.imp.service.serviceDTO.resp.SubscriberHistoryDTO;
import com.sixdee.imp.service.serviceDTO.resp.SubscriberHistoryInfoDTO;

public class SubsHistoryRespAssm extends RespAssmCommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private DateFormat df = new SimpleDateFormat("ddmmyyyyHHMMSS");
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		
		try
		{
			SubsHistoryDTO dto = (SubsHistoryDTO)genericDTO.getObj();
			SubscriberHistoryInfoDTO infoDTO = new SubscriberHistoryInfoDTO();
			infoDTO.setTranscationId(dto.getTransactionId());
			infoDTO.setTimestamp(df.format(new Date()));
			infoDTO.setStatusCode(genericDTO.getStatusCode());
			infoDTO.setStatusDescription(genericDTO.getStatus());
			List<SubscriberHistoryDTO> subscriberList = dto.getSubscriberList();
			if(subscriberList!=null && subscriberList.size()>0)
			{
				SubscriberHistoryDTO[] all = new SubscriberHistoryDTO[subscriberList.size()];
				int i=0;
				for(SubscriberHistoryDTO subsdto : subscriberList)
				{
					all[i++]=subsdto;
				}
				infoDTO.setSubscriberHistory(all);
			}
			genericDTO.setObj(infoDTO);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		
		return genericDTO;
	}
}

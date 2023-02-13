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
 * <td>September 04,2013 07:46:17 PM</td>
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
import com.sixdee.imp.dto.EligibleSubscriberDTO;
import com.sixdee.imp.service.serviceDTO.resp.EligibleSubscriberDetails;
import com.sixdee.imp.service.serviceDTO.resp.EligibleSubscriberInfoDTO;

public class EligibleSubscriberRespAssm extends RespAssmCommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private DateFormat df = new SimpleDateFormat("ddmmyyyyHHMMSS");
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException 
	{
		logger.info("Method => buildAssembleGUIResp()");
		try
		{
			EligibleSubscriberDTO dto = (EligibleSubscriberDTO)genericDTO.getObj();
			List<EligibleSubscriberDetails> list = dto.getEligibleSubscriberList();
			
			EligibleSubscriberInfoDTO infoDTO = new EligibleSubscriberInfoDTO();
			infoDTO.setStatusCode(genericDTO.getStatusCode());
			infoDTO.setStatusDescription(genericDTO.getStatus());
			infoDTO.setTranscationId(dto.getTransactionId());
			infoDTO.setTimestamp(df.format(new Date()));
			if(list!=null && list.size()>0)
			{
				EligibleSubscriberDetails[] all = new EligibleSubscriberDetails[list.size()];
				int i=0;
				for(EligibleSubscriberDetails details:list)
				{
					all[i++]=details;
				}
				infoDTO.setEligibleSubscriberDetails(all);
			}
			genericDTO.setObj(infoDTO);
		
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception in EligibleSubscriberRespAssm ");
		}
		

		
		return genericDTO;
	}
}

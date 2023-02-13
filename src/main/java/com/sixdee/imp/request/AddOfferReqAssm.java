package com.sixdee.imp.request;

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
 * <td>September 04,2013 12:57:23 PM</td>
 * <td>Somesh Soni</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmCommon;
import com.sixdee.imp.dto.AddOfferDTO;



public class AddOfferReqAssm extends ReqAssmCommon {
	
	
	
	public GenericDTO buildAssembleReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> AddOfferReqAssm :: Method ==> buildAssembleGUIReq ");
		AddOfferDTO addOfferDTO = null;
		try{
			com.sixdee.imp.service.serviceDTO.req.AddOfferDTO req = (com.sixdee.imp.service.serviceDTO.req.AddOfferDTO)genericDTO.getObj();
			addOfferDTO=new AddOfferDTO();
			
			addOfferDTO.setChannel(req.getChannel());
			addOfferDTO.setMsisdn(req.getMsisdn());
			addOfferDTO.setOfferId(req.getOfferId());
			addOfferDTO.setOperation(req.getOperation());
			
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(addOfferDTO);
			addOfferDTO = null;
		}

		return genericDTO;
	}

}

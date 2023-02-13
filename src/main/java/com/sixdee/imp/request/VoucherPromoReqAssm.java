package com.sixdee.imp.request;

/**
 * 
 * @author @jith
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
 * <td>June 25,2015 11:18:20 AM</td>
 * <td>@jith</td>
 * </tr>
 * </table>
 * </p>
 */



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.request.ReqAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.VoucherPromoTranverseDTO;
import com.sixdee.imp.service.serviceDTO.common.Data;
import com.sixdee.imp.service.serviceDTO.req.MerchantRedemptionDTO;



public class VoucherPromoReqAssm extends ReqAssmGUICommon {
	

	
	public GenericDTO buildAssembleGUIReq(GenericDTO genericDTO) throws CommonException {
		
		logger.info(" Class ==> VoucherPromoReqAssm :: Method ==> buildAssembleGUIReq ");
		logger.info("Reached inside VoucherPromoReqAssm");
		VoucherPromoTranverseDTO voucherPromoDTO = null;
		try{
			
			
			voucherPromoDTO=new VoucherPromoTranverseDTO();
			
			
			MerchantRedemptionDTO merchantRedemptionDTO=(MerchantRedemptionDTO)genericDTO.getObj();
			
			String subscriberCountryCode=Cache.getConfigParameterMap().get("SUBSCRIBER_COUNTRY_CODE").getParameterValue();
			Integer subscriberSize=Integer.parseInt(Cache.getConfigParameterMap().get("SUBSCRIBER_NUMBER_LENGTH").getParameterValue());
			
			voucherPromoDTO.setTranscationId(merchantRedemptionDTO.getTransactionId());
			if(merchantRedemptionDTO.getMerchantId()!=null && !merchantRedemptionDTO.getMerchantId().equalsIgnoreCase(""))
				voucherPromoDTO.setMerchandID(merchantRedemptionDTO.getMerchantId());
			else
				voucherPromoDTO.setMerchandID(merchantRedemptionDTO.getMerchantId());
			
		//	voucherPromoDTO.setVoucherFlag(merchantRedemptionDTO.isVoucherFlag());
			if(merchantRedemptionDTO.getSubscriberNumber()!=null && merchantRedemptionDTO.getSubscriberNumber().length()==subscriberSize)
				voucherPromoDTO.setSubscriberNo(subscriberCountryCode+merchantRedemptionDTO.getSubscriberNumber());
			else
				voucherPromoDTO.setSubscriberNo(merchantRedemptionDTO.getSubscriberNumber());
			if(merchantRedemptionDTO.getChannel()!=null && !merchantRedemptionDTO.getChannel().equalsIgnoreCase(""))
			voucherPromoDTO.setChannel(merchantRedemptionDTO.getChannel());
			
			if(merchantRedemptionDTO.getLanguageId()!=null && !merchantRedemptionDTO.getLanguageId().equalsIgnoreCase(""))
				voucherPromoDTO.setLanguageID(merchantRedemptionDTO.getLanguageId());
			voucherPromoDTO.setVoucherFlag(true);
			
			 if (voucherPromoDTO.getMerchandID().equalsIgnoreCase((String)Cache.getCacheMap().get("DOD_MERCHANT_ID"))) {
			        voucherPromoDTO.setDealOfDay(Boolean.valueOf(true).booleanValue());
			      }
			      if (Cache.getCacheMap().get("WEEK_DAY_MERCHANT_ID") != null) {
			        String[] weekDayMerchnatID = ((String)Cache.getCacheMap().get("WEEK_DAY_MERCHANT_ID")).split(",");
			        for (String s : weekDayMerchnatID) {
			          if (voucherPromoDTO.getMerchandID().equalsIgnoreCase(s))
			            voucherPromoDTO.setDealOfWeek(Boolean.valueOf(true).booleanValue());
			        }
			      }
			      this.logger.info("IS deal of the week--------->" + voucherPromoDTO.isDealOfWeek());
			      this.logger.info("request reaches before null check");
			      if (merchantRedemptionDTO.getData() != null)
			      {
			        this.logger.info(" Data array in reqassm is not null");
			        this.logger.info("size of the array merchantRedemptionDTO.getData()---->" + merchantRedemptionDTO.getData().length);
			        List<Data> list = new ArrayList<Data>(Arrays.asList(merchantRedemptionDTO.getData()));
			        for (int i = 0; i < list.size(); i++) {
			          if (((Data)list.get(i)).getName().equalsIgnoreCase("PROMO_CODE")) {
			            this.logger.info("Found PROMO_CODE tag ");
			            if (((Data)list.get(i)).getValue().equalsIgnoreCase("FALSE")) {
			              this.logger.info("PROMO_CODE tag is false");
			              voucherPromoDTO.setVoucherFlag(false);
			            }
			          }
			          if (((Data)list.get(i)).getName().equalsIgnoreCase("LANGUAGE_ID")) {
			            voucherPromoDTO.setLanguageID(((Data)list.get(i)).getValue());
			            this.logger.info("LANGUAGE ID CAME" + voucherPromoDTO.getLanguageID());
			          }
			          if ((!((Data)list.get(i)).getName().equalsIgnoreCase("PackageRedemption")) || 
			            (!((Data)list.get(i)).getValue().equalsIgnoreCase("true"))) continue;
			          this.logger.info("PackageRedemption tag is true");
			          voucherPromoDTO.setPackageRedemption(true);
			        }

			      }
			
			voucherPromoDTO.setTranscationId(merchantRedemptionDTO.getTransactionId());
			voucherPromoDTO.setTimestamp(merchantRedemptionDTO.getTimestamp());
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			genericDTO.setObj(voucherPromoDTO);
			voucherPromoDTO = null;
		}

		return genericDTO;
	}

}

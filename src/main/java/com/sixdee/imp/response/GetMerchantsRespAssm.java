package com.sixdee.imp.response;

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
 * <td>June 29,2015 12:25:33 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;

import com.sixdee.fw.response.RespAssmGUICommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.GetMerchantsDTO;
import com.sixdee.imp.dto.PackageCategory;
import com.sixdee.imp.dto.PackageDetails;
import com.sixdee.imp.service.serviceDTO.resp.MerchantInfoDTO;
import com.sixdee.imp.service.serviceDTO.resp.MerchantPackageDetailsDTO;

public class GetMerchantsRespAssm extends RespAssmGUICommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	
	private DateFormat df = new SimpleDateFormat("ddmmyyyyHHMMSS");
	private int defaultLanguage;
	
	public GenericDTO buildAssembleGUIResp(GenericDTO genericDTO) throws CommonException {
		logger.info("Method => buildAssembleGUIResp()");
		
		GetMerchantsDTO dto =null;
		try
			{
				dto = (GetMerchantsDTO)genericDTO.getObj();
				defaultLanguage = dto.getLanguageId();
				HashMap<Integer,PackageCategory> categoryMap =  dto.getMerchantMap();
				long l1 = System.currentTimeMillis();
				MerchantInfoDTO merchantInfoDTO=null;
				if(categoryMap!=null && categoryMap.size()>0)
					merchantInfoDTO = createDTOArray(categoryMap,dto.getChannel());
				else
					merchantInfoDTO = new MerchantInfoDTO();
				long l2 = System.currentTimeMillis();
				merchantInfoDTO.setTranscationId(dto.getTransactionId());
				merchantInfoDTO.setTimestamp(df.format(new Date()));
				
				genericDTO.setObj(merchantInfoDTO);
			}catch (Exception e) {
				e.printStackTrace();
				genericDTO.setObj(new MerchantInfoDTO());
			}

		return genericDTO;
	}
	
	public MerchantInfoDTO createDTOArray(HashMap<Integer, PackageCategory> categoryMap,String channel)
	{
		
		MerchantInfoDTO merchantInfo = new MerchantInfoDTO();
		MerchantPackageDetailsDTO merchantPackageDetailsDTO=null;
		MerchantPackageDetailsDTO packageDtlsArray[] = null;
		PackageCategory packageCategory = null;
		MerchantPackageDetailsDTO outerDTO[] = null;
		List<MerchantPackageDetailsDTO> outerDTO1 = new ArrayList<MerchantPackageDetailsDTO>();
	try{
		
		Iterator<Integer> iter=categoryMap.keySet().iterator();
		while(iter.hasNext())
		{
		Integer categoryId = (Integer) iter.next();
		packageCategory = categoryMap.get(categoryId);
		merchantPackageDetailsDTO = new MerchantPackageDetailsDTO();
		merchantPackageDetailsDTO.setCategory(channel.equalsIgnoreCase("USSD")?packageCategory.getCategorySynonm():packageCategory.getCategoryName());
		merchantPackageDetailsDTO.setTypeId(packageCategory.getTypeId());
		merchantPackageDetailsDTO.setTypeName(Cache.accountTypeMap.get(packageCategory.getTypeId()+"_"+defaultLanguage));
		merchantPackageDetailsDTO.setCategoryID(categoryId);
		logger.info("PACKAGE CATEGORY:"+merchantPackageDetailsDTO.getCategory());
		packageDtlsArray = createArray(packageCategory, channel ,categoryId);
		
		if(packageDtlsArray !=null && packageDtlsArray.length>0)
		{
			logger.info("ARRAY LENGTH:"+packageDtlsArray.length);
			merchantPackageDetailsDTO.setSubPackageDetails(packageDtlsArray);
			outerDTO1.add(merchantPackageDetailsDTO);
		}
		}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		outerDTO = new MerchantPackageDetailsDTO[outerDTO1.size()];
		merchantInfo.setPackages(outerDTO1.toArray(outerDTO));
		return merchantInfo;
		}
	
	
	public MerchantPackageDetailsDTO[] createArray(PackageCategory packageCategory,String channel,Integer categoryID)
	{
		Object all[] = null;
		PackageDetails packageDetails = null;
		MerchantPackageDetailsDTO packageDtlsArray[] =null;
		try
		{
		Set<PackageDetails> sets = packageCategory.getPackages();
		all = sets.toArray();
		List<MerchantPackageDetailsDTO> list = null;
		if(all.length>0)
		{
			list = new ArrayList<MerchantPackageDetailsDTO>();
			for(Object o : all)
			{
				packageDetails = (PackageDetails)o;
				if(packageDetails.getLanguageId()==defaultLanguage)
					if((packageDetails.getQuantity()==null || packageDetails.getQuantity()>0)&&(packageDetails.getExpiryDate()==null || packageDetails.getExpiryDate().after(new Date())))
					{
						MerchantPackageDetailsDTO p = new MerchantPackageDetailsDTO();
						p.setCategoryID(categoryID);
						p.setPackageID(packageDetails.getPackageId());
						p.setPackageName(channel.equalsIgnoreCase("USSD") ? (packageDetails.getPackageSynonm() + "(" + packageDetails.getRedeemPoints() + ")") : packageDetails.getPackageName());
						p.setRedeemPoints(packageDetails.getRedeemPoints());
						p.setInfo(packageDetails.getPackageDesc());
						list.add(p);
					}
			}
		}
		if(list!=null)
		{
			packageDtlsArray = new MerchantPackageDetailsDTO[list.size()];
			packageDtlsArray = list.toArray(packageDtlsArray);
		}
		if(packageDtlsArray!=null)
			Arrays.sort(packageDtlsArray);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	return packageDtlsArray;
	}
}

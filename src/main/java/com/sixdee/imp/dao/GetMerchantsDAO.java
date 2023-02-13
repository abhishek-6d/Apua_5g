package com.sixdee.imp.dao;

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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.GetMerchantsDTO;
import com.sixdee.imp.dto.PackageCategory;
import com.sixdee.imp.dto.PackageDetails;

public class GetMerchantsDAO {
	
	private static Logger logger = Logger.getLogger(GetPackagesDAO.class);
	public HashMap<Integer, PackageCategory> getMerchantMap(GenericDTO genericDTO) throws CommonException
	{
		GetMerchantsDTO merchantDTO = (GetMerchantsDTO)genericDTO.getObj();
		
		Session session = null;
		Transaction txn=null;
		List<PackageCategory> list = null;
		Set<PackageDetails> set = null;
		Set<PackageDetails> finalset = null;
		PackageCategory finalCategory=null;
		HashMap<Integer, PackageCategory> categoryMap = new HashMap<Integer, PackageCategory>();
		
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			txn=session.beginTransaction();
			Criteria ctr = session.createCriteria(PackageCategory.class);
			ctr.add(Restrictions.eq("languageId", merchantDTO.getLanguageId()));
			ctr.add(Restrictions.or(Restrictions.eq("isPackage", "Y"),Restrictions.eq("isPackage", "y")));			
			ctr.addOrder(Order.asc("categoryId"));
			list = ctr.list();
			logger.info("merchantDTO ID:"+merchantDTO.getMerchantId());
			for(PackageCategory category:list)
			{
				if(category.getIsPackage().equalsIgnoreCase("Y") && category.getParentId()!=null)
				{
					ctr = session.createCriteria(PackageDetails.class);
					ctr.add(Restrictions.eq("categoryId", category.getCategoryId()));
					ctr.add(Restrictions.eq("languageId", category.getLanguageId()));
					if(merchantDTO.getMerchantId()!=0){
						ctr.add(Restrictions.eq("merchantId", merchantDTO.getMerchantId()));
					}
					ArrayList<PackageDetails> al = (ArrayList<PackageDetails>) ctr.list();
					set = new LinkedHashSet<PackageDetails>();
					for(PackageDetails packageDetails : al){
						set.add(packageDetails);
					}
					category.setPackages(set);
					logger.info("Size of set is = "+ set.size());

					finalCategory=new PackageCategory();
					finalset=new HashSet<PackageDetails>();
					for(PackageDetails d : set)
					{
						
						if(d.getMerchantId()!=null)
						{
							logger.info("Merchant ID :"+merchantDTO.getMerchantId());
							logger.info("Category ID:"+category.getCategoryId());
							logger.info("Category Name:"+category.getCategoryName());
							logger.debug("Package Name = "+d.getPackageName()+" Package Desc = "+d.getPackageDesc()+" Points:"+d.getRedeemPoints()+" Quantity:"+d.getQuantity());
							finalCategory.setCategoryId(category.getCategoryId());
							finalCategory.setCategoryName(category.getCategoryName());
							finalCategory.setCategorySynonm(category.getCategorySynonm());
							finalCategory.setTypeId(category.getTypeId());
							finalCategory.setCategoryDesc(category.getCategoryDesc());
						
							if(merchantDTO.getMerchantId()==0 || merchantDTO.getMerchantId().equals("0"))
							{
								logger.info("All Merchants");
								finalset.add(d);
								finalCategory.setPackages(finalset);
								categoryMap.put(category.getCategoryId(), finalCategory);
							}
							else if(d.getMerchantId().equals(merchantDTO.getMerchantId()))
							{
								logger.info("Particular Merchant");
								finalset.add(d);
								finalCategory.setPackages(finalset);
								categoryMap.put(category.getCategoryId(), finalCategory);
							}
						}
					}
				}
			}
			txn.commit();
				
			session.close();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_MERCHANT_PACK_ERR_"+merchantDTO.getLanguageId()).getStatusCode());
			genericDTO.setStatus(Cache.getServiceStatusMap().get("GET_MERCHANT_PACK_ERR_"+merchantDTO.getLanguageId()).getStatusDesc());
			logger.info("Exception while getting packages...");
			throw new CommonException(Cache.getServiceStatusMap().get("GET_MERCHANT_PACK_ERR_"+merchantDTO.getLanguageId()).getStatusDesc());
		}
		finally
		{
			try
			{
				if(session!=null)
					session.close();
			}
			catch (Exception e) {
			}
			set = null;
			list = null;
		}
		logger.info(merchantDTO.getTransactionId()+" :MAP size == "+ categoryMap.size());
		return categoryMap;
	}


}

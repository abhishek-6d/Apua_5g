package com.sixdee.imp.dao;

/**
 * 
 * @author S@j!th
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
 * <td>October 30,2015 11:55:15 AM</td>
 * <td>S@j!th</td>
 * </tr>
 * </table>
 * </p>
 */



import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dto.ProductCatalogDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.HiberanteUtil;
import com.sixdee.imp.dto.ProductCatalogDTO;
import com.sixdee.imp.dto.PackageCategory;
import com.sixdee.imp.dto.PackageDetails;

public class ProductCatalogDAO {
	private static Logger logger = Logger.getLogger(ProductCatalogDAO.class);
	public HashMap<String, PackageCategory> getPackageCategory(GenericDTO genericDTO) throws CommonException
	{
		ProductCatalogDTO dto = (ProductCatalogDTO)genericDTO.getObj();
		logger.info("In ProductCatalogDAO: getPackageCategory()");
		Session session = null;
		List<PackageCategory> list = null;
		Set<PackageDetails> set = null;
		HashMap<String, PackageCategory> categoryMap = new HashMap<String, PackageCategory>();
		HashMap<String, List<Integer>>  categoryMapWthParentID = new HashMap<String, List<Integer>>();
		List<Integer> pkgCategory = null;
		String typeId = "";
		ArrayList<Integer> typeList = new  ArrayList<Integer>();
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			//dto.setDefaultLanguageId(11);
			Criteria ctr = session.createCriteria(PackageCategory.class);
			ctr.add(Restrictions.eq("languageId", dto.getDefaultLanguageId()));
			//ctr.setProjection(Projections.projectionList().add(Projections.groupProperty("typeId")));
			ctr.addOrder(Order.asc("categoryId"));
			ctr.addOrder(Order.asc("parentId"));
			Query query = session.createQuery("from PackageCategory as pkgCat  where pkgCat.languageId=:id  order by pkgCat.categoryId,pkgCat.parentId");
			
	//		Query query = session.createQuery("from PackageCategory as pkgCat left join fetch pkgCat.packages pkg where pkgCat.languageId=:id  order by pkgCat.categoryId,pkgCat.parentId");
			
//			query.setInteger("id", dto.getDefaultLanguageId());
	//		query.setInteger("pkid", dto.getDefaultLanguageId());
			query.setInteger("id", dto.getDefaultLanguageId());
			
			list = query.list();
			//		
			//list = ctr.list();
		//	list = query.list();
			
			for(PackageCategory category:list)
			{
				logger.info("Category Description :: "+category.getCategoryDesc()+" Category Type "+category.getTypeId()+" ParentId "+category.getParentId()+" isPackage "+category.getIsPackage()+" Child "+category.getPackages());
				//logger.info(category.getTypeId());
				//logger.info("parent id = "+category.getParentId());
			//	logger.info("Exception above");
				if(category.getIsPackage().equalsIgnoreCase("Y"))
				{
				//Commented by Rahul and added below code
				//Purpose and scenario
				//When case of redemption merchant came, where we need to have single id for vouchers irrespective of pre/post 
				//	
					
				/*	set = category.getPackages();
					logger.info("Size of set is = "+ set.size());
					for(PackageDetails d : set)
						logger.debug("package name = "+d.getPackageName()+" package desc = "+d.getPackageDesc()+" redeem point ="+d.getRedeemPoints()+"language id = "+d.getLanguageId());
				*/
					set = new LinkedHashSet<PackageDetails>();
					ctr = session.createCriteria(PackageDetails.class);
					
					logger.info("getting catogoryid");
					ctr.add(Restrictions.eq("categoryId", category.getCategoryId()));
					//logger.info("Exception while getting languageid");
					ctr.add(Restrictions.eq("languageId", category.getLanguageId()));
					logger.info("getting everything into a list");
					ArrayList<PackageDetails> al = (ArrayList<PackageDetails>) ctr.list();
					logger.info("Added into list succesfully");
					for(PackageDetails packageDetails : al){
						logger.info("adding into set");
						set.add(packageDetails);
						logger.info("Added everything into a set");
					}
					category.setPackages(set);
					logger.info("Size of set is = "+ set.size());
					for(PackageDetails d : set)
						logger.debug("package name = "+d.getPackageName()+" package desc = "+d.getPackageDesc()+" redeem point ="+d.getRedeemPoints()+"language id = "+d.getLanguageId());
					
				}
				if(category.getParentId()!=null){
					typeId="_"+category.getTypeId();
				}else{
					typeId="";
				}
				if(!categoryMapWthParentID.containsKey(category.getParentId()+typeId))
				{
					pkgCategory = new ArrayList<Integer>();
					pkgCategory.add(category.getCategoryId());
					categoryMapWthParentID.put(category.getParentId()+typeId, pkgCategory);
				}
				else
				{
					pkgCategory = categoryMapWthParentID.get(category.getParentId()+typeId);
					if(!pkgCategory.contains(category.getCategoryId()))
						pkgCategory.add(category.getCategoryId());
				}
				
				categoryMap.put(category.getCategoryId()+typeId, category);
				
			}
				
//			System.out.println(categoryMap.keySet()+" somesh ");
//			System.out.println(categoryMapWthParentID.keySet()+" somesh ");
			
			dto.setCategoryMapWthParentID(categoryMapWthParentID);
			logger.debug(categoryMapWthParentID+"\n"+categoryMap);
			
			session.close();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			genericDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_PKG_ERR_"+dto.getDefaultLanguageId()).getStatusCode());
			
			logger.info("Exception while getting package category...");
			throw new CommonException(Cache.getServiceStatusMap().get("GET_PKG_ERR_"+dto.getDefaultLanguageId()).getStatusDesc());
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
			categoryMapWthParentID = null;
		}
		logger.debug("and before returning == "+ categoryMap.size());
		return categoryMap;
	}
	
	public Set<PackageDetails> getVouchers()
	{
		Session session = null;
		Transaction trx = null;
		Query query = null;
		List<Object[]> list = null;
		Set<PackageDetails> set = new HashSet<PackageDetails>();
		try
		{
			session = HiberanteUtil.getSessionFactory().openSession();
			trx = session.beginTransaction();
			
			String sql = "SELECT VD.ID,VD.VOUCHER_DESC,REDEEM_POINTS FROM VOUCHER_DETAILS VD WHERE EXPIRY_DATE>? AND QUANTITY>0";
			query = session.createSQLQuery(sql);
			query.setParameter(0, new Date());
			list = query.list();
			PackageDetails pd = null;
			for(Object[] o : list)
			{
				/*System.out.println(o[0].toString());
				System.out.println(o[1].toString());
				System.out.println(o[2].toString());*/
				pd = new PackageDetails();
				pd.setPackageId(Integer.parseInt(o[0].toString()));
				pd.setPackageName(o[1].toString());
				pd.setPackageSynonm(o[1].toString());
				pd.setRedeemPoints(Integer.parseInt(o[2].toString()));
				set.add(pd);
				
			}
			
			trx.commit();
			
		}
		catch (Exception e)
		{	
			trx.rollback();
			e.printStackTrace();
		}
		finally
		{
			if(session!=null)
				session.close();
		}
		
		return set;
	}
	
}

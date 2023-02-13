package com.sixdee.imp.response;

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



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.fw.response.RespAssmCommon;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.ProductCatalogDTO;
import com.sixdee.imp.dto.PackageCategory;
import com.sixdee.imp.dto.PackageDetails;
import com.sixdee.imp.service.serviceDTO.resp.PackageDetailsDTO;
import com.sixdee.imp.service.serviceDTO.resp.PackageInfoDTO;

public class ProductCatalogRespAssm extends RespAssmCommon {
	/**
	 * This method is called from the framework class in Response Assembler
	 * Layer. Coverts Value Object to Model
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	private DateFormat df = new SimpleDateFormat("ddmmyyyyHHMMSS");
	private int defaultLanguage;
	public GenericDTO buildAssembleResp(GenericDTO genericDTO) throws CommonException
	{
		logger.info("Method => buildAssembleGUIResp()");
		
		ProductCatalogDTO dto =null;
		try
		{
			dto = (ProductCatalogDTO)genericDTO.getObj();
			defaultLanguage = dto.getDefaultLanguageId();
			HashMap<String,PackageCategory> categoryMap =  dto.getCategoryMap();
			
			long l1 = System.currentTimeMillis();
			PackageInfoDTO infoDTO=null;
			//System.out.println("category map = size == "+categoryMap.size());
			if(categoryMap!=null && categoryMap.size()>0)
				infoDTO = createArrayOfDTO(categoryMap,dto.getChannel(),dto.getCategoryMapWthParentID());
			else
				infoDTO = new PackageInfoDTO();
			long l2 = System.currentTimeMillis();
			System.out.println("TOTAL TIME = "+(l2-l1));
			infoDTO.setTranscationId(dto.getTransactionId());
			infoDTO.setTimestamp(df.format(new Date()));
			
			logger.debug("size of info= "+infoDTO.getPackages().length);
			
			//PackageDetailsDTO all[] = infoDTO.getPackages();
			
			//printpkg(all);
			
			
			
			genericDTO.setObj(infoDTO);
		}catch (Exception e) {
			e.printStackTrace();
			genericDTO.setObj(new PackageInfoDTO());
		}

		
		return genericDTO;
	}
	
	public void printpkg(PackageDetailsDTO all[])
	{
		if(all!=null)
		for(PackageDetailsDTO dd : all)
		{
			logger.debug(dd.getCategory());
			logger.debug(dd.getPackageID());
			logger.debug(dd.getPackageName());
			logger.debug("Point=="+dd.getRedeemPoints());
			logger.debug(dd.getInfo());
			logger.debug(dd.getTypeName());
			logger.debug(dd.getTypeId());
			logger.debug("array= "+dd.getSubPackageDetails());
			printpkg(dd.getSubPackageDetails());
		}
	}
	
	public PackageInfoDTO createArrayOfDTO(HashMap<String, PackageCategory> categoryMap,String channel,HashMap<String, List<Integer>>  categoryMapWthParentID)
	{
		//get all root elements
		List<Integer> allParents = getChilds(categoryMap, null+"",categoryMapWthParentID);
		if(allParents==null)
			return null;
		
		logger.debug("WE HAVE "+allParents+" MAIN CATEGORYS");
		PackageInfoDTO packageInfo = new PackageInfoDTO();
		PackageDetailsDTO outerDTO[] = null;//new PackageDetailsDTO[allParents.size()];
		List<PackageDetailsDTO> outerDTO1 = new ArrayList<PackageDetailsDTO>();
		//packageInfo.setPackages(outerDTO);
		int i = 0;
		
		PackageCategory packageCategory = null;
		PackageDetailsDTO packageDetailsDTO = null;
		PackageDetailsDTO packageDtlsArray[] = null;
		ListIterator<Integer> iter = allParents.listIterator();
		while (iter.hasNext()) 
		{
			Integer categoryId = (Integer) iter.next();
			packageCategory = categoryMap.get(categoryId+"");
			packageDetailsDTO = new PackageDetailsDTO();
			packageDetailsDTO.setCategory(channel.equalsIgnoreCase("USSD")?packageCategory.getCategorySynonm():packageCategory.getCategoryName());
			packageDetailsDTO.setTypeId(packageCategory.getTypeId());
			packageDetailsDTO.setTypeName(Cache.accountTypeMap.get(packageCategory.getTypeId()+"_"+defaultLanguage));
			
			boolean flag = false;
			List<Integer> allChilds = getChilds(categoryMap, packageCategory.getCategoryId()+"_"+packageCategory.getTypeId(),categoryMapWthParentID);
			if(allChilds!=null && allChilds.size()>0)
			{
				flag = true;
				packageDtlsArray = createArray(categoryMap, allChilds,channel,categoryMapWthParentID,packageCategory.getTypeId()+"");
				//packageDetailsDTO.setSubPackageDetails(packageDtlsArray);
			}
			/*else
			{
				if(packageCategory.getIsPackage().equalsIgnoreCase("Y"))
				{
					allChilds = new ArrayList<Integer>();
					//allChilds.add(9999);
					allChilds.add(packageCategory.getCategoryId());
					packageDtlsArray = createArray(categoryMap, allChilds, channel, categoryMapWthParentID);
				}
			}*/
			
			if(packageCategory.getIsPackage().equalsIgnoreCase("Y"))
			{
				allChilds = new ArrayList<Integer>();
				//allChilds.add(9999);
				allChilds.add(packageCategory.getCategoryId());
				packageDtlsArray = createArray(categoryMap, allChilds, channel, categoryMapWthParentID,packageCategory.getTypeId()+"");
				//packageDetailsDTO = packageDtlsArray[0];
			}
			if(flag)
			{
				if(packageDtlsArray !=null && packageDtlsArray.length>0)
				{
					packageDetailsDTO.setSubPackageDetails(packageDtlsArray);
				outerDTO1.add(packageDetailsDTO);
				}
			}
			else
			{
				if(packageDtlsArray !=null && packageDtlsArray.length>0)
				{
					packageDetailsDTO = packageDtlsArray[0];
				outerDTO1.add(packageDetailsDTO);
				}
			}
			
			
			
			
			
			
			logger.debug("before setting into outer DTO "+(packageDetailsDTO!=null?packageDetailsDTO.getCategory():"--"));//+" and length of aaray "+(packageDetailsDTO!=null?packageDetailsDTO.getSubPackageDetails().length:"--"));
			//outerDTO[i++] = packageDetailsDTO; 
			
		}
		
		outerDTO = new PackageDetailsDTO[outerDTO1.size()];
		packageInfo.setPackages(outerDTO1.toArray(outerDTO));
		return packageInfo;
		
	}
	
	public List<Integer> getChilds(HashMap<String, PackageCategory> categoryMap, String parent,HashMap<String, List<Integer>>  categoryMapWthParentID)
	{
		logger.debug("finding childs for parent "+parent+ " "+categoryMapWthParentID.keySet());
		
		List<Integer> childsList = categoryMapWthParentID.get(parent);
		
		
/*		List<Integer> childsList = new ArrayList<Integer>();
		Iterator<Integer> it = categoryMap.keySet().iterator();
		if(parent == null)
		{
			while(it.hasNext())
			{
				Integer id = it.next();
				if(categoryMap.get(id).getParentId()== parent)
					childsList.add(id);
			}
		}
		else
		{
			while(it.hasNext())
			{
				Integer id = it.next();
				if(categoryMap.get(id).getParentId() !=null && categoryMap.get(id).getParentId().equals(parent))
					childsList.add(id);
			}
		}
*/		
		logger.debug("And childs == "+childsList);
		return childsList;
	}
	
	public PackageDetailsDTO[] createArray(HashMap<String, PackageCategory> categoryMap,List<Integer> childList,String channel ,HashMap<String, List<Integer>>  categoryMapWthParentID,String typeId)
	{
		PackageDetailsDTO allpkg1[] = null;
		List<PackageDetailsDTO> allpkgs = null;
		PackageDetails packageDetails = null;
		int i = 0 ;
		if(childList!=null && childList.size()>0)
		{
			allpkgs = new ArrayList<PackageDetailsDTO>();//new PackageDetailsDTO[childList.size()];
			PackageCategory packageCategory = null;
			PackageDetailsDTO dto = null;
			PackageDetailsDTO packageDtlsArray[] =null;
			Object all[] = null;
			for(Integer id : childList)
			{
				packageCategory = categoryMap.get(id+"_"+typeId);
				dto = new PackageDetailsDTO();
				dto.setCategory(channel.equalsIgnoreCase("USSD")?packageCategory.getCategorySynonm():packageCategory.getCategoryName());
				dto.setTypeId(packageCategory.getTypeId());
				dto.setTypeName(Cache.accountTypeMap.get(packageCategory.getTypeId()+"_"+defaultLanguage));
				//logger.debug(packageCategory.getIsPackage());
				if(packageCategory.getIsPackage().equalsIgnoreCase("Y"))
				{
					Set<PackageDetails> sets = packageCategory.getPackages();
					all = sets.toArray();
					List<PackageDetailsDTO> list = null;
					if(all.length>0)
					{
						
						list = new ArrayList<PackageDetailsDTO>();
						//int j = 0 ;
						for(Object o : all)
						{
							packageDetails = (PackageDetails)o;
							//logger.debug(packageDetails.getLanguageId()+"=="+defaultLanguage);
							if(packageDetails.getLanguageId()==defaultLanguage)
								if((packageDetails.getQuantity()==null || packageDetails.getQuantity()>0)&&(packageDetails.getExpiryDate()==null || packageDetails.getExpiryDate().after(new Date())))
								{
									// logger.debug(packageDetails.getPackageSynonm());
									PackageDetailsDTO p = new PackageDetailsDTO();
									p.setPackageID(packageDetails.getPackageId()+"");
									p.setPackageName(channel.equalsIgnoreCase("USSD") ? (packageDetails.getPackageSynonm() + "(" + packageDetails.getRedeemPoints() + ")") : packageDetails.getPackageName());
									p.setRedeemPoints(packageDetails.getRedeemPoints());
									p.setInfo(packageDetails.getPackageDesc());
									list.add(p);
									// packageDtlsArray[j++] = p;
									//p=null;
								}
							
							
						}
					}
					if(list!=null)
					{
						packageDtlsArray = new PackageDetailsDTO[list.size()];
						packageDtlsArray = list.toArray(packageDtlsArray);
					}
					if(packageDtlsArray!=null)
						Arrays.sort(packageDtlsArray);
					dto.setSubPackageDetails(packageDtlsArray);
					if(packageDtlsArray!=null &&packageDtlsArray.length>0)
						allpkgs.add(dto);//allpkgs[i++] = dto;
					
					all = null;
					sets = null;
					dto = null;
					packageDtlsArray = null;
				}
				else
				{
					List<Integer> allChiList =getChilds(categoryMap, packageCategory.getCategoryId()+"_"+packageCategory.getTypeId(),categoryMapWthParentID);
					PackageDetailsDTO innerpkg[] = null;
					if(allChiList!=null && allChiList.size()>0)
					{
						innerpkg = createArray(categoryMap,allChiList,channel,categoryMapWthParentID,packageCategory.getTypeId()+""); 
						dto.setSubPackageDetails(innerpkg);
					}
					if(innerpkg!=null && innerpkg.length>0)
						allpkgs.add(dto);//allpkgs[i++] = dto;
					dto = null;
				}
			}
		}
		else
			return null;
		
		allpkg1 = new PackageDetailsDTO[allpkgs.size()];
		return allpkgs.toArray(allpkg1);
	}
	
	
}

package com.sixdee.imp.bo;

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

import java.util.HashMap;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.dao.ProductCatalogDAO;
import com.sixdee.imp.dto.PackageCategory;
import com.sixdee.imp.dto.ProductCatalogDTO;

public class ProductCatalogBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => ProductCatalogBL :: Method => buildProcess()");
		
		ProductCatalogDAO  productCatalogDAO= new ProductCatalogDAO();
		ProductCatalogDTO productCatalogDTO = (ProductCatalogDTO) genericDTO.getObj();

		try{
			HashMap<String, PackageCategory> categoryMap = productCatalogDAO.getPackageCategory(genericDTO);
			logger.debug("size of list == "+categoryMap.size());
			
			productCatalogDTO.setCategoryMap(categoryMap);
			
			
			logger.info("WITH PARENT ==> "+productCatalogDTO.getCategoryMapWthParentID());
			
			genericDTO.setStatus("SUCCESS");
			genericDTO.setStatusCode("SC0000");
			
			
		}catch (CommonException e) 
		{
			e.printStackTrace();
			genericDTO.setStatus(e.getMessage());
		}
		catch (Exception e) 
		{
			logger.info("Error",e);
		}
					
			return genericDTO;
		}
}

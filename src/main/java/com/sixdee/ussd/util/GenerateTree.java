/**
 * 
 */
package com.sixdee.ussd.util;

import java.util.HashMap;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.sixdee.ussd.dto.PackageTree;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.AreaLocationInfoDTO;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.PackageDetailsDTO;

/**
 * @author Rahul K K
 * @version 1.0.0
 * 
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%">Date</td>
 *          <td width="20%">Author</td>
 *          <td>Description</td>
 *          </tr>
 *          <tr>
 *          <td>April 24, 2013</td>
 *          <td>Rahul K K</td>
 *          <td>Created this class</td>
 *          </tr>
 *          </table>
 *          </p>
 */
public class GenerateTree {

	private static final Logger logger = Logger.getLogger(GenerateTree.class);
	HashMap<Integer, LinkedList<PackageTree>> packageMap = new HashMap<Integer, LinkedList<PackageTree>>();
	HashMap<Integer,HashMap<String, String[]>> locationMap=null;;
	
	private int maxParent = 1;
	private int id = 1;
	private LinkedList<Integer> parentList = new LinkedList<Integer>();

	public GenerateTree() {
		parentList.add(1);
	}
	public void reset(){
		packageMap = null;

		maxParent = 1;
		id = 1;
		parentList = null;
	}

	public HashMap<Integer, LinkedList<PackageTree>> formPackageTree(
			int parentId, PackageDetailsDTO packageDetailsDTO, int level,
			boolean isRoot) {
		HashMap<String, String[]> areaMap=new HashMap<String, String[]>();
		AreaLocationInfoDTO[] areaLocationInfoDTO=packageDetailsDTO.getAreaLocations();
		//AreaLocationInfoDTO areaLocationInfoDTO=new AreaLocationInfoDTO();		
		boolean isVistited = false;
		PackageTree packageTree = new PackageTree();
		packageTree.setId(id++);
		packageTree
				.setCategoryId(packageDetailsDTO.getCategory() != null ? packageDetailsDTO
						.getCategory() : packageDetailsDTO.getPackageName());
		packageTree.setPackageId(packageDetailsDTO.getPackageID());
		packageTree.setInfo(packageDetailsDTO.getInfo());
		packageTree.setPackageName(packageDetailsDTO.getPackageName());
		packageTree.setParentId("" + parentId);
		packageTree.setTypeId(packageDetailsDTO.getTypeId());
		packageTree.setTypeName(packageTree.getTypeName());	
		if(areaLocationInfoDTO!=null && areaLocationInfoDTO.length>0)
		{
			if(locationMap==null){
				locationMap = new HashMap<Integer, HashMap<String,String[]>>();
			}

			for (AreaLocationInfoDTO a : areaLocationInfoDTO) {
				if(a!=null)
					areaMap.put(a.getArea(), a.getLocation());
			}
			locationMap.put(packageDetailsDTO.getPackageID(), areaMap);
			packageTree.setLocationMap(locationMap);
		}
		
		if(packageDetailsDTO.getCategory()==null || packageDetailsDTO.getCategory().trim().equals("")){
			//logger.debug("Package Category is null "+packageDetailsDTO.getCategory()+ "So treating as root "+packageDetailsDTO.getPackageName());
			packageTree.setLeafNode(true);
		}
		// logger.debug("Level "+(level));
		logger.debug("Level " + level + " parent[" + packageTree.getParentId()
				+ "] category [" + packageDetailsDTO.getCategory()
				+ "] packageId [" + packageDetailsDTO.getPackageID() + "]"
				+ " Info [" + packageDetailsDTO.getInfo() + "] packageName ["
				+ packageDetailsDTO.getPackageName() + "] Type ["+packageDetailsDTO.getTypeId()+"] TypeName["+packageDetailsDTO.getTypeName()+"] ["+packageDetailsDTO.getSubPackageDetails()+"]");
		if (packageMap.get(level) != null){
			//logger.info("Package ["+packageTree.getCategoryId()+"] and isroot ["+packageTree.isLeafNode()+"]");
			packageMap.get(level).add(packageTree);
		}
		else {
			LinkedList<PackageTree> pTreeList = new LinkedList<PackageTree>();
			pTreeList.add(packageTree);
			//logger.info("PackageTree ["+packageTree.isLeafNode()+"]");
			packageMap.put(level, pTreeList);
			// logger.debug(packageMap);
		}
		if (packageDetailsDTO.getSubPackageDetails() != null) {

			// logger.debug("Pack "+packageDetailsDTO+" "+packageDetailsDTO.getSubPackageDetails());
			for (PackageDetailsDTO packDTO : packageDetailsDTO
					.getSubPackageDetails())
				if (packDTO != null) {
					if (!isVistited) {
						level++;
						// parentId++;
						if (parentId >= maxParent) {
							parentId++;
							maxParent = parentId;
						} else {
							parentId = maxParent + 1;
							maxParent = parentId;
						}
					}
					/*
					 * parentId = parentList.remove(0); parentId+=1;
					 * parentList.add(parentId); logger.debug(parentList);
					 */

					// logger.debug("Level "+(level));
					// logger.debug(packDTO.getSubPackageDetails()!=null?packDTO.getSubPackageDetails()[0].getPackageID():"******"
					// );
					// logger.debug(packDTO+" "+level);
					isVistited = true;

					packageMap = formPackageTree(packageTree.getId(), packDTO,
							level, false);
					logger.debug("Level completed");
				} else {
					if (packageDetailsDTO.getSubPackageDetails() != null)
						parentId -= 1;
					logger.debug("----------");

				}
		} else {
			logger.debug("Leaf Node reached " + parentId);
			level = level - 1;
			parentId -= 1;
			packageTree.setLeafNode(true);
			logger.debug("Level decrease " + parentId);

		}
		return packageMap;
	}

	public HashMap<Integer, LinkedList<PackageTree>> createTree(
			PackageDetailsDTO[] packageDetailsDTOList, boolean b) {
	/*	GenertePackageTree generatePackageTree = new GenertePackageTree();
		generatePackageTree.generateTree(packageDetailsDTOList);
	*/	int parentId = 1;
		for (PackageDetailsDTO packageDTO : packageDetailsDTOList) {
			
			if (packageDTO != null) {
				
				packageMap = formPackageTree(0, packageDTO, 1, false);
				
				parentId = maxParent + 1;
				logger.debug(parentId + " ******* Level decreased by 1"
						+ packageMap);
			} else {
				// logger.debug(packageMap);
				parentId -= 1;
				logger.debug("Leaf Node");
			}
		}
		parentId = 1;
		// logger.debug(packageMap);
		return packageMap;

	}
	public HashMap<Integer, HashMap<String, String[]>> getLocationMap() {
		return locationMap;
	}

	public static void main(String[] args) {
		PackageDetailsDTO packageDetailsDTO = new PackageDetailsDTO();
		System.out.println(packageDetailsDTO.getSubPackageDetails());
	}
}

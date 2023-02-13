/**
 * 
 */
package com.sixdee.ussd.serviceCall;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import com.sixdee.fw.exception.CommonException;
import com.sixdee.ussd.dto.PackageTree;
import com.sixdee.ussd.dto.ServiceRequestDTO;
import com.sixdee.ussd.dto.parser.ussdRequest.Parameters;
import com.sixdee.ussd.dto.parser.ussdRequest.Service;
import com.sixdee.ussd.dto.parser.ussdRequest.ServiceList;
import com.sixdee.ussd.dto.parser.ussdRequest.UssdRequestDTO;
import com.sixdee.ussd.dto.parser.ussdResponse.UssdResponseDTO;
import com.sixdee.ussd.util.AppCache;
import com.sixdee.ussd.util.GenerateTree;
import com.sixdee.ussd.util.PackageComparator;
import com.sixdee.ussd.util.inter.StatusCodes;
import com.sixdee.ussd.util.inter.WSCallInter;
import com.sixdee.ussd.webserviceHandler.PointManagementStub;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.GetPackages;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.PackageDTO;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.PackageDetailsDTO;
import com.sixdee.ussd.webserviceHandler.PointManagementStub.PackageInfoDTO;
import com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.UserDTO;

/**
 * 
 *          <p>
 *          <b><u>Development History</u></b><br>
 *          <table border="1" width="100%">
 *          <tr>
 *          <td width="15%">Date</td>PAV
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
public class GetPackTypes implements WSCallInter {

	private static final Logger logger = Logger.getLogger(GetPackTypes.class);
	
	public UssdResponseDTO callService(ServiceRequestDTO serviceRequestDTO,
			UssdRequestDTO ussdRequestDTO, String response)
			throws CommonException {
		boolean isRoot = false;
		boolean isLeaf = false;
		UssdResponseDTO ussdResponseDTO = null;
		PointManagementStub pointManagementStub = null;
		GetPackages getPackages = null;
		PackageDTO packageDto = null;
		PackageInfoDTO packageInfoDTO = null;
		PackageDetailsDTO[] packageDetailsDTOList = null;
		ArrayList<Service> recoList = null;
		Service reccomendation = null;
		ServiceList reccomendations = null;
		Service headerInfo = null;
		String parent = null;
		HashMap<Integer, LinkedList<PackageTree>> packageMap = null;
		HashMap<String, String> ussdMap = null; 
		String type = null;
		String tempType = null;
		boolean isNoEntry = false;
		String leafService = null;
		String nextService = null;
		int level = 0;
		boolean isLangBasedPacks = false;
		String langBasedPacks = null;
		int langId = ussdRequestDTO.getLangId();
		String pathInfo = "";
		String traverseInfo= "";
		boolean isBackMenu = false;
		StringTokenizer stk = null;
		boolean isFirstTime = true;	String emptyString = "";
		String pathDelimiter= "$";
		String isPreDefinedType = null;
		boolean isLocation = false;
		String packageId = null;
		boolean isLocationInRed = false;
		String areaInfo = null;
		boolean isAreaBased = false;
		//String emptyString = "";
		HashMap<Integer, HashMap<String,String[]>> locationMap = null;
		try {
			ussdMap = (HashMap<String, String>) AppCache.util.get("USSD_MANAGER");
			
			langBasedPacks = ussdMap.get("IS_LANG_BASED_PACKAGES");
			isLangBasedPacks = Boolean.parseBoolean(langBasedPacks!=null?langBasedPacks:"false");
			logger.info("Transaction id ["+ussdRequestDTO.getTransactionId()+"] Calling GetPacksAPI for ["+serviceRequestDTO.getUrl()+"] Response ["+response+"] For Language Based api ["+isLangBasedPacks+"]");
			
			ussdResponseDTO = new UssdResponseDTO();
//			logger.info(AppCache.util.get("PACKAGE_TREE_1")+ " \n "+AppCache.util.get("PACKAGE_TREE_11"));

			if(isLangBasedPacks){
				packageMap = (HashMap<Integer, LinkedList<PackageTree>>) AppCache.util
					.get("PACKAGE_TREE_"+ussdRequestDTO.getLangId());
				locationMap = (HashMap<Integer, HashMap<String, String[]>>) AppCache.util
						.get("LOCATION_INFO_"+ussdRequestDTO.getLangId());
			}
			else{
				packageMap = (HashMap<Integer, LinkedList<PackageTree>>) AppCache.util
				.get("PACKAGE_TREE");
			locationMap = (HashMap<Integer, HashMap<String, String[]>>) AppCache.util.get("LOCATION_INFO");
			}
			if(packageMap==null){
				//Call GetPackage Service of LMS
				//GetPackageTree getPackageTree = new GetPackageTree();
				ArrayList<Object> objList =fetchPackageFromLMS(ussdRequestDTO,serviceRequestDTO,isLangBasedPacks); 
				packageMap = (HashMap<Integer, LinkedList<PackageTree>>) objList.get(0);
				locationMap = (HashMap<Integer, HashMap<String, String[]>>) objList.get(1);
				logger.debug("Location Map "+locationMap);
			}
			
			int j = 0;
		/*	String nextService = ((HashMap<Integer, ArrayList<String>>) AppCache.util
					.get("CHILD_RELATION")).get(serviceRequestDTO
					.getKeyWordId()) != null ? ((HashMap<Integer, ArrayList<String>>) AppCache.util
					.get("CHILD_RELATION")).get(
					serviceRequestDTO.getKeyWordId()).get(0) : null;
			logger.info("Next Service to be called ["+nextService+"] For Keyword id ["+serviceRequestDTO.getKeyWordId()+"]");
			
		*/	
			nextService = serviceRequestDTO.getKeyword();
			recoList = new ArrayList<Service>();
			// for(PackageDetailsDTO packageDetailDTO : packageDetailsDTOList){
			/*
			 * logger.debug(packageDetailDTO.getCategery()+" "+packageDetailDTO.
			 * getPackageID()+" "+packageDetailDTO.getInfo()
			 * +" "+packageDetailDTO
			 * .getPackageName()+" "+packageDetailDTO.getRedeemPoints());
			 */// packageMap = (HashMap<Integer, LinkedList<PackageTree>>)
				// AppCache.util.get("PACKAGE_TREE");
				// logger.debug("PackageMap");

			ArrayList<Parameters> paramList = ussdRequestDTO.getServiceList()
					.getServices().get(0).getParamList();

			if (paramList != null) {
				int i = 0;
				for (Parameters p : ussdRequestDTO.getServiceList()
						.getServices().get(0).getParamList()) {
					if (p != null && p.getId().equalsIgnoreCase("parent")) {
						parent = p.getValue();
						logger.debug("Got parent " + parent);
					//	pathInfo=parent;
					}
					
					if (p.getId().equalsIgnoreCase("type")) {
						type = p.getValue();
						//pathInfo+="_"+type;
					//	if (logger.isDebugEnabled())
							logger.debug("Got Type " + type);
							tempType = type;
					}	if (p.getId().equalsIgnoreCase("PACK_ID")) {
						level = Integer.parseInt(p.getValue())+1;
					//	pathInfo+="_"+(level-1);
					//	if (logger.isDebugEnabled())
							logger.debug("Got level " + level);
							i++;
					}
					if(p.getId().equalsIgnoreCase("Pin")){
						traverseInfo = p.getValue();
						if((traverseInfo.startsWith("IB")))
							traverseInfo = traverseInfo.replace("IB", "");
						else
							isBackMenu = true;
					}if(p.getId().equalsIgnoreCase("RedPackage")){
						packageId =p.getValue();
						isLocationInRed = true;
						continue;
					}if(p.getId().equalsIgnoreCase("Area")){
						//location = parameter.getValue();
						logger.debug(p.getValue());
						areaInfo = p.getValue();
						logger.debug("Area information "+areaInfo);
						isAreaBased = true;
					}
					if (i == 4)
						break;
				}
			}
			
			if (!isBackMenu) {
				if (type != null) {
					//tempType = type;
					pathInfo = type;
				}
				if (parent != null) {
					pathInfo += "%" + parent;
				}
				if (level != 0) {
					pathInfo += "%" + (level - 1);
				}
			}else{
				/*
				 * Back Option has been used by user, Pin tag will be having traverse path seperated by $
				 * T1 - Type
				 * T2 - Parent
				 * T3 - level
				 */
				type = null;
				parent = null;
				//level = 1;

				int len = 0;
				if(( len=traverseInfo.length())>4){
					if(traverseInfo.charAt(len-1)=='$'){
						traverseInfo = traverseInfo.substring(0,len-1);
					}
				}
				logger.debug("Recieved Back Request "+type+" Parent "+parent+" Level "+level+" TraverseInfo "+traverseInfo);
				stk = new StringTokenizer(traverseInfo,"$");
				int levelCount = stk.countTokens()-1;
				String lastMenu = null;
				if(levelCount==0)
					lastMenu = traverseInfo.substring(traverseInfo.lastIndexOf("$")+1,traverseInfo.length());
				else{
					int c = 1;
					while(stk.hasMoreTokens()){
						if(c==levelCount)
							lastMenu = stk.nextToken();
						else
							stk.nextToken();
						c++;
					}
				}
				stk = null;
				level  = 1;
				logger.debug(traverseInfo+" LAST MENU "+lastMenu);
				stk  = new StringTokenizer(lastMenu,"%");
				int tokens = stk.countTokens();
				int tokenCount = 1;
				while(stk.hasMoreTokens()){
					if(tokenCount==1)
						type=stk.nextToken();
					else if(tokenCount==2)
						parent = stk.nextToken();
					else if(tokenCount==3){
						level = Integer.parseInt(stk.nextToken());
						/*if(levelCount==3){
							
						}*/
						break;
					}
					tokenCount++;
				}
				
				int l1 = level;
				try{
					String l = traverseInfo.substring(traverseInfo.length()-1);
					
					level  = Integer.parseInt(l);
				}catch(Exception n){
					logger.error(n.getMessage());
					level = l1;
				}
				if(traverseInfo.contains("$"))
					traverseInfo = traverseInfo.substring(0,traverseInfo.lastIndexOf("$"));
				int length = traverseInfo.length();
				if(traverseInfo.length()>4){
					if(traverseInfo.charAt(length-1)=='$')
						traverseInfo = traverseInfo.substring(0,length-1);
				}
				//logger.info(isBackMenu);
				logger.info("Recieved Back Request Type : "+type+" Parent : "+parent+" Level : "+level+" TraverseInfo "+traverseInfo+" PathInfo "+pathInfo);
			}
			if(parent == null){
				type="9";
				for(PackageTree packTree:packageMap.get(1)){
					logger.info(type+" "+packTree.getTypeId());
					if(packTree.getTypeId()==Integer.parseInt(type)){
						parent = packTree.getId()+"";
						logger.info("Identified Parent "+parent);
						isRoot = true;
						level = serviceRequestDTO.getLevel();
					}
				}
			}
			
			
			logger.debug("Level [" + level
					+ "] packages \n "
					+ packageMap.get(level));
		
	/*		if (level == 2 && parent != null) {
				for (PackageTree packTree : packageMap.get(level)) {
					if(type.equalsIgnoreCase(packTree.getTypeId()+"")){
						parent = packTree.getId()+"";
						
							logger.debug("Id found for type ["+type+"] :- ["+parent+"] ["+packTree.getTypeId()+"]");
						break;
					}
				}
			}*/
			if(!isLocationInRed){
			if(parent == null || packageMap.get(level)==null){
				isNoEntry = false;
			}else{
			LinkedList<PackageTree> packList = packageMap.get(level);
			Collections.sort(packList,new PackageComparator());
			for (PackageTree packTree : packList) {
				if(logger.isDebugEnabled())
					logger.debug("Pack Identified " + packTree.getCategoryId() + " "
						+ packTree.getInfo() + " " + packTree.getPackageId()
						+ " " + packTree.getPackageName() + " "
						+ packTree.getRedeemPoints() + " Is LeafNode  "+packTree.isLeafNode()+" "
						+ packTree.getParentId()+" equals ["+packTree.getParentId().equals(parent)+"] Parent From Request["+parent+"]");

				if (parent == null || packTree.getParentId().equals(parent)) {

					if (level == 1
							&& packTree.getCategoryId() != null) {
						isNoEntry = true;
						reccomendation = new Service();
						reccomendation.setMenuIndex(""+(++j));
						reccomendation.setMessageText(packTree.getCategoryId());
						reccomendation.setNextService(packTree.getCategoryId());
						ArrayList<Parameters> parameters = new ArrayList<Parameters>();
						Parameters param = new Parameters();
						param.setId("Parent");
						if(!packTree.isLeafNode()){
							param.setValue(packTree.getId() + "");
						}
						else
							param.setValue(packTree.getPackageId()+"");
						parameters.add(param);
						Parameters param1 = new Parameters();
						param1.setId("LanguageId");
						param1.setValue(ussdRequestDTO.getLangId()+"");
						parameters.add(param1);
						Parameters param2 = new Parameters();
						param2.setId("TYPE");
						if(isBackMenu)
							param2.setValue(!isRoot?type:response);
						else
							param2.setValue(tempType);
						parameters.add(param2);
						Parameters param3 = new Parameters();
						param3.setId("PACK_ID");
			//			logger.info(level);
						param3.setValue((level)+"");
						parameters.add(param3);
						reccomendation.setParamList(parameters);
						recoList.add(reccomendation);

					} else {
						isNoEntry = true;

						 reccomendation = new Service();
						reccomendation.setMessageText(packTree
								.getCategoryId());
						if(packTree.isLeafNode()) {
							isLeaf = true;
							if (packTree.getLocationMap() == null) {
								 leafService = ((HashMap<String, String>) AppCache.util
										.get("USSD_MANAGER"))
										.get("REDEMPTION_SERVICE");
								if (leafService != null
										&& !(leafService.trim()
												.equals("")))
									nextService = leafService;
							} else {
								nextService = serviceRequestDTO
										.getKeyword();
								isLocation = true;
							}
							if (isFirstTime) {
								GetUserProfileCall getUserProfileCall = new GetUserProfileCall();
								UserDTO userDTO = getUserProfileCall
										.getUserDetails(
												ussdRequestDTO,
												((HashMap<String, ServiceRequestDTO>) AppCache.util
														.get("SERVICE_CONFIG"))
														.get("8")
														.getUrl());
								headerInfo = new Service();
								headerInfo
										.setMessageText(((HashMap<String, String>) AppCache.util
												.get("USSD_MANAGER")).get("POINTS_HEADER_"
												+ ussdRequestDTO
														.getLangId())
												+ userDTO
														.getRewardPoints());
								headerInfo.setDefaultOption(1);
								headerInfo.setNextService("1");
								recoList.add(headerInfo);
								isFirstTime = false;
							}

						} else {
							nextService = serviceRequestDTO
									.getKeyword();

						}
						reccomendation.setMenuIndex("" + (++j));

						reccomendation.setNextService(nextService);

						ArrayList<Parameters> parameters = new ArrayList<Parameters>();
						Parameters param = new Parameters();

						param.setId("Parent");
						param.setValue(packTree.getPackageId() != 0 ? packTree
								.getPackageId() + ""
								: packTree.getId() + "");
						parameters.add(param);
						Parameters param1 = new Parameters();
						param1.setId("LanguageId");
						param1.setValue(ussdRequestDTO.getLangId() + "");
						parameters.add(param1);
						Parameters param2 = new Parameters();
						param2.setId("TYPE");
						if (!isBackMenu)
							param2.setValue(!isRoot ? type : response
									+ "_" + ussdRequestDTO.getLangId());
						else
							param2.setValue(tempType);
						parameters.add(param2);
						Parameters param3 = new Parameters();
						param3.setId("PACK_ID");
						param3.setValue((level) + "");

						Parameters param4 = new Parameters();
						param4.setId("Pin");
						param4.setValue((!(traverseInfo.trim()
								.equals("")) ? ("IB"
								+ traverseInfo.trim() + "$" + pathInfo)
								: "IB" + pathInfo));
						// param4.setValue(traverseInfo);
						logger.info("Level " + level + " "
								+ param.getValue());

						parameters.add(param3);
						parameters.add(param4);

						if (isLocation) {
							Parameters param5 = new Parameters();
							param3.setId("RedPackage");
							param3.setValue((packTree.getPackageId())
									+ "");
						}
						reccomendation.setParamList(parameters);

						recoList.add(reccomendation);
					}
				} else {
					logger.debug(packTree.getParentId());
				}
			}
			}
			}else{
				isNoEntry = true;
				logger.debug(locationMap.keySet()+" "+packageId+" "+isAreaBased);
				HashMap<String, String[]> locationDetails = locationMap.get(Integer.parseInt(packageId));
				if (!isAreaBased) {
					headerInfo = new Service();
					headerInfo
							.setMessageText(((HashMap<String, String>) AppCache.util
									.get("USSD_MANAGER")).get("AREA_BASED_HEADER_"
									+ ussdRequestDTO
											.getLangId()));
					headerInfo.setDefaultOption(1);
					headerInfo.setNextService("1");
					recoList.add(headerInfo);
			
					logger.debug("Location Details keyset "+locationDetails.keySet());
							
					for (String location : locationDetails.keySet()) {
						Service service = new Service();
						service.setMenuIndex(++j);
						service.setMessageText(location);
						service.setNextService(serviceRequestDTO.getKeyword());
						Parameters p = new Parameters();
						p.setId("Area");
						p.setValue(location.replaceAll("&#", "ABC"));
						logger.debug(p.getValue());
						ArrayList<Parameters> respParamList = new ArrayList<Parameters>();
						respParamList.add(p);
						for(Parameters param : paramList){

							Parameters inputParameters = new Parameters();
							inputParameters.setId(param.getId());
							inputParameters.setValue(param.getValue());
							respParamList.add(inputParameters);
						
						}
						//	respParamList.add(param);
						//respParamList = paramList;
						//respParamList.add(p);
						service.setParamList(respParamList);
						recoList.add(service);
					}
				}else{
					/*
					 * In case of unicode , there was an issue faced
					 * when request was coming back from ng , on xstream conversion unicode was
					 * getting converted to arabic . 
					 * To Avoid this we decided to replace #& with ABC when sending to ng and on the request
					 * to convert vice versa
					 */
					headerInfo = new Service();
					headerInfo
							.setMessageText(((HashMap<String, String>) AppCache.util
									.get("USSD_MANAGER")).get("LOCATION_BASED_HEADER_"
									+ ussdRequestDTO
											.getLangId()));
					headerInfo.setDefaultOption(1);
					headerInfo.setNextService("1");
					recoList.add(headerInfo);
					areaInfo = areaInfo.replaceAll("ABC", "&#");

					logger.debug("Response from user "+areaInfo);
					String[] areas = locationDetails.get(areaInfo);
					if(areas==null){
						logger.debug("Areas information we got is empty so checking with areaname "+areaInfo);
						areas= locationDetails.get(areaInfo);
						if(areas==null){
							for(String locationName : locationDetails.keySet()){
								logger.debug("Response "+response+" Compated with "+locationName +"  "+(response.equals(locationName.trim())));
							}
						}
					}
					for(String area : areas){
						Service service = new Service();
						service.setMenuIndex(++j);
						service.setMessageText(area);
						 leafService = ((HashMap<String, String>) AppCache.util
									.get("USSD_MANAGER"))
									.get("REDEMPTION_SERVICE");
						service.setNextService(leafService);
						//service.setNextService(serviceRequestDTO.getKeyword());
						Parameters p = new Parameters();
						p.setId("Location");
						p.setValue(area.replaceAll("&#", "ABC"));
						ArrayList<Parameters> respParamList = new ArrayList<Parameters>();
						respParamList.add(p);
						for(Parameters param : paramList){
							Parameters inputParameters = new Parameters();
							inputParameters.setId(param.getId());
							inputParameters.setValue(param.getValue());
							respParamList.add(inputParameters);
						}
						service.setParamList(respParamList);
						recoList.add(service);
					}
				}
			}
			if(!isNoEntry){

				reccomendation = new Service();
				reccomendation.setMenuIndex(""+(++j));
				reccomendation.setMessageText(ussdMap.get("NO_PACK_AVAIL_"+langId));
			//	reccomendation.setNextService(packTree.getCategoryId());
				
				recoList.add(reccomendation);
				ussdResponseDTO.setEos(true);
			
			}else if(isLeaf){}
			logger.info("IsRoot : "+isRoot+" Pack Level :- "+level+" IsLeaf : "+isLeaf);
			if(level-1==1){
				isRoot=true;
			}
			if(!isRoot){
				
				  Service backOption = new Service();
				  backOption.setMenuIndex(""+(++j));
				  backOption.setMessageText(ussdMap.get("Previous_Menu_String_"+ussdRequestDTO.getLangId()));
				  backOption.setNextService(ussdMap.get("Back2Packs_Service"));
				  ArrayList<Parameters> parameters = new ArrayList<Parameters>();
					Parameters param3 = new Parameters();
					param3.setId("LanguageId");
					param3.setValue(ussdRequestDTO.getLangId()+"");
					parameters.add(param3);
			
					Parameters param4 = new Parameters();
					param4.setId("Pin");
					if(!traverseInfo.trim().equalsIgnoreCase(emptyString)){
						if(pathInfo!=null || !(pathInfo.trim().equalsIgnoreCase(emptyString))){
							traverseInfo=traverseInfo+pathDelimiter+pathInfo;
						}
					}else{
						traverseInfo = pathInfo;
					}
					param4.setValue(traverseInfo);
					//param4.setValue(!(traverseInfo.trim().equalsIgnoreCase(""))?(traverseInfo+"$"+pathInfo):pathInfo);
				  parameters.add(param4);
				  backOption.setParamList(parameters);
				  recoList.add(backOption);
				  
			}
			else {

				  Service backOption = new Service();
				  //backOption.setMenuIndex(ussdMap.get("Previous_Menu_Index"));
				  backOption.setMenuIndex(""+(++j));
				  
				  backOption.setMessageText(ussdMap.get("Previous_Menu_String_"+ussdRequestDTO.getLangId()));
				  backOption.setNextService(ussdMap.get("Back2Lines_Service"));
				  ArrayList<Parameters> parameters = new ArrayList<Parameters>();
					
				  Parameters param5 = new Parameters();
					param5.setId("TYPE");
					
					param5.setValue("B");
					parameters.add(param5);
					backOption.setParamList(parameters);
				  recoList.add(backOption);
			}
			ussdResponseDTO.setTraversalPath(serviceRequestDTO.getKeyWordId()
					+ "");
			reccomendations = new ServiceList();
			reccomendations.setServices(recoList);
			ussdResponseDTO.setStatus("SC0000");
			ussdResponseDTO.setStatusDesc(StatusCodes.SC0000);
			// ussdResponseDTO.setTraversalPath()

			ussdResponseDTO.setServiceList(reccomendations);

		} catch (AxisFault e) {
			
			logger.error("Axis Exception occured for Request ["
					+ ussdRequestDTO.getTransactionId() + "]", e);
			
			e.printStackTrace();
			throw new CommonException("Wsdl Problem ,Severe one please check");
		} catch (RemoteException e) {
			logger.error("Remote Exception occured for Request ["
					+ ussdRequestDTO.getTransactionId() + "]", e);
			e.printStackTrace();
			throw new CommonException("Remote System is not available , Please check");

		} catch (Exception e) {
			logger.error(
					" Exception occured for Request ["
							+ ussdRequestDTO.getTransactionId() + "]", e);
			throw new CommonException("System Error");

		} finally {
//			logger.info(AppCache.util.get("PACKAGE_TREE_1")+ " \n "+AppCache.util.get("PACKAGE_TREE_11"));
			pointManagementStub = null;
		}
		return ussdResponseDTO;
	}
	
	private ArrayList<Object> fetchPackageFromLMS(UssdRequestDTO ussdRequestDTO,ServiceRequestDTO serviceRequestDTO,boolean isLangBasedPacks) throws RemoteException {
		ArrayList<Object> objectList = new ArrayList<Object>();
		GetPackages getPackages = null;
		PackageDTO packageDto = null;
		PointManagementStub pointManagementStub = null;
		PackageInfoDTO packageInfoDTO = null;
		PackageDetailsDTO[] packageDetailsDTOList = null;
		HashMap<Integer, LinkedList<PackageTree>> packageMap = null;
		getPackages = new GetPackages();
		packageDto = new PackageDTO();
		packageDto.setTranscationId(ussdRequestDTO.getTransactionId());
		packageDto.setTimestamp(ussdRequestDTO.getTimeStamp());
		packageDto.setChannel("USSD");
		packageDto.setSubscriberNumber(ussdRequestDTO.getMsisdn());
		getPackages.setPackageDTO(packageDto);

		pointManagementStub = new PointManagementStub(
				serviceRequestDTO.getUrl());
		packageInfoDTO = (PackageInfoDTO) pointManagementStub
				.getPackages(getPackages).get_return();
		packageDetailsDTOList = packageInfoDTO.getPackages();
		GenerateTree gen = new GenerateTree();
		packageMap = (gen.createTree(
				packageDetailsDTOList, true));
		if(packageMap.size()>0){
			logger.info("Puting language based packs PACKAGE_TREE_"+ussdRequestDTO.getLangId());
			if(isLangBasedPacks){
				AppCache.util.put("PACKAGE_TREE_"+ussdRequestDTO.getLangId(), packageMap);
				AppCache.util.put("LOCATION_INFO_"+ussdRequestDTO.getLangId(),gen.getLocationMap() );
				logger.debug("Language Based Location map "+gen.getLocationMap());
			}
			else{
				AppCache.util.put("PACKAGE_TREE",packageMap);
				AppCache.util.put("LOCATION_INFO",gen.getLocationMap());
				logger.debug("Language Based Location map "+gen.getLocationMap());
				
			}
		}
		objectList.add(packageMap);
		objectList.add(gen.getLocationMap());

		return objectList;
	}


	public static void main(String[] args) {
		/*String t = "IB14$12345670_1%20%2$12345670_1%20%2";
		StringTokenizer stk = new StringTokenizer(t,"%");
		System.out.println(t.replace("IB", ""));
		while(stk.hasMoreTokens())
		//	System.out.println("T :- "+stk.nextElement());
		if(t.contains("$"))
			t = t.substring(0,t.indexOf("$"));
	*/	System.out.println("******************");
		String t1 = "14$$$$$$$$92615512_1%32%2$$";
//		System.out.println(//t1);
		System.out.println(t1.substring(t1.length()-1));
	}
}

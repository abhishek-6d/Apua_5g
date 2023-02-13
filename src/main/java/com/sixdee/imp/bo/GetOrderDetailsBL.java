package com.sixdee.imp.bo;

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
 * <td>May 29,2013 12:11:41 PM</td>
 * <td>Nithin Kunjappan</td>
 * </tr>
 * </table>
 * </p>
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.sixdee.fw.bo.BOCommon;
import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.common.util.CommonUtil;
import com.sixdee.imp.dao.GetOrderDetailsDAO;
import com.sixdee.imp.dao.TableDetailsDAO;
import com.sixdee.imp.dao.TableInfoDAO;
import com.sixdee.imp.dto.GetOrderDetailsDTO;
import com.sixdee.imp.dto.LoyaltyProfileTabDTO;
import com.sixdee.imp.dto.ResponseOrderDetailsDTO;
import com.sixdee.imp.dto.VoucherOrderDetailsDTO;

public class GetOrderDetailsBL extends BOCommon {
	
	/**
	 * This method is called from BOCommon. Access the DAO object is to create CampaignDefntion
	 * 
	 * @param genericDTO
	 * @throws CommonException
	 */
	

	
	public GenericDTO buildProcess(GenericDTO genericDTO) throws CommonException {

		logger.info("Class => GetOrderDetailsBL :: Method => buildProcess()");	
		ArrayList<ResponseOrderDetailsDTO> orderDetails = null;
		GetOrderDetailsDTO getOrderDetailsDTO = null;		
		
	
		
		try{
			getOrderDetailsDTO = (GetOrderDetailsDTO) genericDTO.getObj();
			logger.info("getOrderDetailsDTO.getSubscriberNumber()"+getOrderDetailsDTO.getSubscriberNumber());
			if(getOrderDetailsDTO.getSubscriberNumber()==null || getOrderDetailsDTO.getSubscriberNumber().equalsIgnoreCase("null"))
				orderDetails = getOrderLists(getOrderDetailsDTO);
			else
				orderDetails = getOrderDetails(getOrderDetailsDTO);
			getOrderDetailsDTO.setObj(orderDetails);
		  	
			//getOrderDetailsDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_SUCCESS_"+Cache.cacheMap.get("DEFAULT_LANGUAGEID")).getStatusCode());
			//getOrderDetailsDTO.setStatusDesc(Cache.getServiceStatusMap().get("GET_ORDER_SUCCESS_"+Cache.cacheMap.get("DEFAULT_LANGUAGEID")).getStatusDesc());
		//	logger.info("STATUS CODE IN BL"+getOrderDetailsDTO.getStatusCode());

			genericDTO.setObj(getOrderDetailsDTO);
		}catch (CommonException e) {
			logger.error(e.getMessage());
			genericDTO.setStatus(e.getMessage());
			throw e;
		}catch (Exception e) {
			logger.error("Exception occured ",e);
			if(genericDTO.getStatus().equals("SC0000"))
				genericDTO.setStatus("SC0004");
			throw new CommonException();
		}				
			return genericDTO;
		}
	
	private ArrayList<ResponseOrderDetailsDTO> getOrderLists(
			GetOrderDetailsDTO getOrderDetailsDTO) {
		
		ArrayList<ResponseOrderDetailsDTO> orderResplist = null;
		ArrayList<VoucherOrderDetailsDTO> voucherOrderList = null;
		GetOrderDetailsDAO getOrderDetailsDAO = null;		
		TableInfoDAO tableInfoDAO = null;	
		DateFormat df=new SimpleDateFormat("dd-MM-yyyy");		
		String tableName = null;
		String fromDate = null;
		String endDate = null;
		String langId= null;
		try{		
			
			logger.info("inside getOrderLists function");
			langId = getOrderDetailsDTO.getLangId();
			getOrderDetailsDAO = new GetOrderDetailsDAO();
			tableInfoDAO = new TableInfoDAO();
				fromDate = getOrderDetailsDTO.getFromDate();
				endDate = getOrderDetailsDTO.getEndDate();
				
				tableName = tableInfoDAO.getVoucherOrderDBTable(null);
				
				if(fromDate != null && endDate != null && !fromDate.trim().equalsIgnoreCase("") && !endDate.trim().equalsIgnoreCase("")){
					voucherOrderList = getOrderDetailsDAO.getOrderLists(tableName,langId,fromDate,endDate,
							getOrderDetailsDTO.getOffset()-1,getOrderDetailsDTO.getLimit());
				}else if(getOrderDetailsDTO.getNoOfMonths()!=0){					
					Date previousDate = getPreviousMonth(getOrderDetailsDTO.getNoOfMonths());
					voucherOrderList = getOrderDetailsDAO.getOrderLists(tableName,langId,
							previousDate,getOrderDetailsDTO.getOffset()-1, getOrderDetailsDTO.getLimit());
				}else if(getOrderDetailsDTO.getNoOfLastTransactions()!=0){
					voucherOrderList = getOrderDetailsDAO.getLastNOrderLists(tableName,langId,
							getOrderDetailsDTO.getNoOfLastTransactions(),getOrderDetailsDTO.getOffset());
				}
				if(voucherOrderList!=null && (voucherOrderList.size()!=0)){
					orderResplist = new ArrayList<ResponseOrderDetailsDTO>();
					ResponseOrderDetailsDTO responseDTO  = null;
					for(VoucherOrderDetailsDTO loyaltyTransactions : voucherOrderList){
						responseDTO = new ResponseOrderDetailsDTO();
						responseDTO.setOrderId(loyaltyTransactions.getOrderId());
						responseDTO.setOrderDate(df.format(loyaltyTransactions.getOrderDate()));
						responseDTO.setItemName(loyaltyTransactions.getVoucherName());
						responseDTO.setItemNumber(loyaltyTransactions.getVoucherNumber());
						responseDTO.setQuantity(loyaltyTransactions.getQuantity());
						responseDTO.setRedeemPoints(loyaltyTransactions.getRedeemPoints());
						responseDTO.setOrderStatus(Cache.voucherStatusMap.get(loyaltyTransactions.getOrderStatus()+"_"+langId));
						responseDTO.setExpiryDate(df.format(loyaltyTransactions.getExpiryDate()));
						orderResplist.add(responseDTO);						
					}
					getOrderDetailsDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_SUCCESS_"+langId).getStatusCode());
					getOrderDetailsDTO.setStatusDesc(Cache.getServiceStatusMap().get("GET_ORDER_SUCCESS_"+langId).getStatusDesc());
					
					//System.out.println("IN BL SIZE"+orderResplist.size());
				}else{
					getOrderDetailsDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_NO_ORDERS_"+langId).getStatusCode());
					getOrderDetailsDTO.setStatusDesc(Cache.getServiceStatusMap().get("GET_ORDER_NO_ORDERS_"+langId).getStatusDesc());
				//	throw new CommonException(Cache.getServiceStatusMap().get("GET_ORDER_NO_ORDERS_"+langId!=null?langId:Cache.cacheMap.get("DEFAULT_LANGUAGEID")).getStatusDesc());

				}			     
		    	   
			
			if(orderResplist!=null)
			logger.info("No of Orders recieved "+orderResplist.size());
		}
		
		catch (Exception e) {
			logger.info("Error Occured :: "+e.getMessage());
			
			// TODO: handle exception
		}
		finally{		
		}
		return orderResplist;
	}

	private ArrayList<ResponseOrderDetailsDTO> getOrderDetails(
			GetOrderDetailsDTO getOrderDetailsDTO) throws CommonException {
		
		LoyaltyProfileTabDTO loyaltyProfileTabDTO = null;
		ArrayList<ResponseOrderDetailsDTO> orderResplist = null;
		ArrayList<VoucherOrderDetailsDTO> voucherOrderList = null;
		GetOrderDetailsDAO getOrderDetailsDAO = null;		
		TableInfoDAO tableInfoDAO = null;	
		TableDetailsDAO tableDetailsDAO = null;
		DateFormat df=new SimpleDateFormat("dd-MM-yyyy");		
		String loyaltyId = null;
		String subscriber = null;
		String tableName = null;
		String fromDate = null;
		String endDate = null;
		CommonUtil commonUtil=new CommonUtil();
		String langId= null;
		try{			
			langId = getOrderDetailsDTO.getLangId();
			getOrderDetailsDAO = new GetOrderDetailsDAO();
			subscriber = getOrderDetailsDTO.getSubscriberNumber();
			tableInfoDAO = new TableInfoDAO();
			tableDetailsDAO= new TableDetailsDAO();
		//	tableName = tableInfoDAO.getSubscriberNumberDBTable(subscriber);
			if(commonUtil.isItChar(getOrderDetailsDTO.getSubscriberNumber()))
				tableName = tableInfoDAO.getADSLNumberTable(subscriber);
			else
				tableName = tableInfoDAO.getSubscriberNumberTable(subscriber);
			loyaltyId = getOrderDetailsDAO.getLoyaltyId(tableName, subscriber);
			loyaltyId = loyaltyId != null ? loyaltyId:subscriber;
			logger.info("Subscriber Number["+subscriber+"]  Loyalty Id ["+loyaltyId+"]" );
			
		
			if(loyaltyId != null){
				
			loyaltyProfileTabDTO = tableDetailsDAO.getLoyaltyProfile(Long.parseLong(loyaltyId));
			
			
		       if(loyaltyProfileTabDTO!=null)
		       {
		    	   
		    	  logger.info("loyaltyProfileTabDTO.getStatusID()::"+loyaltyProfileTabDTO.getStatusID());
		    	   
		    	   if(loyaltyProfileTabDTO.getStatusID()!=1)
					{
						logger.info("Subscriber Number is not Active Mode : "+getOrderDetailsDTO.getSubscriberNumber()+"  Mode : "+Cache.statusMap.get(loyaltyProfileTabDTO.getStatusID()));
						getOrderDetailsDTO.setStatusCode(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_"+getOrderDetailsDTO.getLangId()).getStatusCode());
						getOrderDetailsDTO.setStatusDesc(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_"+getOrderDetailsDTO.getLangId()).getStatusDesc());
						throw new CommonException(Cache.getServiceStatusMap().get("SUBS_NOT_ACT_"+getOrderDetailsDTO.getLangId()).getStatusDesc());
					}
				fromDate = getOrderDetailsDTO.getFromDate();
				endDate = getOrderDetailsDTO.getEndDate();
				
				tableName = tableInfoDAO.getVoucherOrderDBTable(loyaltyId);
				
				if(fromDate != null && endDate != null && !fromDate.trim().equalsIgnoreCase("") && !endDate.trim().equalsIgnoreCase("")){
					voucherOrderList = getOrderDetailsDAO.getOrderDetails(tableName,loyaltyId,langId,fromDate,endDate,
							getOrderDetailsDTO.getOffset()-1,getOrderDetailsDTO.getLimit());
				}else if(getOrderDetailsDTO.getNoOfMonths()!=0){
					
					Date previousDate = getPreviousMonth(getOrderDetailsDTO.getNoOfMonths());
					voucherOrderList = getOrderDetailsDAO.getOrderDetails(tableName, ""+loyaltyId,langId,
							previousDate,getOrderDetailsDTO.getOffset()-1, getOrderDetailsDTO.getLimit());
				}else if(getOrderDetailsDTO.getNoOfLastTransactions()!=0){
					voucherOrderList = getOrderDetailsDAO.getLastNTransactions(tableName,""+loyaltyId,langId,
							getOrderDetailsDTO.getNoOfLastTransactions(),getOrderDetailsDTO.getOffset());
				}
				else if(getOrderDetailsDTO.isLuluVoucher())
				{
					voucherOrderList = getOrderDetailsDAO.getVoucherDetails(tableName,loyaltyId);
				}
				if(voucherOrderList!=null && (voucherOrderList.size()!=0)){
					orderResplist = new ArrayList<ResponseOrderDetailsDTO>();
					ResponseOrderDetailsDTO responseDTO  = null;
					for(VoucherOrderDetailsDTO loyaltyTransactions : voucherOrderList){
						responseDTO = new ResponseOrderDetailsDTO();
						responseDTO.setOrderId(loyaltyTransactions.getOrderId());
						responseDTO.setOrderDate(df.format(loyaltyTransactions.getOrderDate()));
						responseDTO.setItemName(loyaltyTransactions.getVoucherName());
						responseDTO.setItemNumber(loyaltyTransactions.getVoucherNumber());
						responseDTO.setQuantity(loyaltyTransactions.getQuantity());
						responseDTO.setRedeemPoints(loyaltyTransactions.getRedeemPoints());
						responseDTO.setOrderStatus(Cache.voucherStatusMap.get(loyaltyTransactions.getOrderStatus()+"_"+langId));
						responseDTO.setExpiryDate(df.format(loyaltyTransactions.getExpiryDate()));
						orderResplist.add(responseDTO);						
					}
					getOrderDetailsDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_SUCCESS_"+langId).getStatusCode());
					getOrderDetailsDTO.setStatusDesc(Cache.getServiceStatusMap().get("GET_ORDER_SUCCESS_"+langId).getStatusDesc());
					
					//System.out.println("IN BL SIZE"+orderResplist.size());
				}else{
					getOrderDetailsDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_NO_ORDERS_"+langId).getStatusCode());
					getOrderDetailsDTO.setStatusDesc(Cache.getServiceStatusMap().get("GET_ORDER_NO_ORDERS_"+langId).getStatusDesc());
				//	throw new CommonException(Cache.getServiceStatusMap().get("GET_ORDER_NO_ORDERS_"+langId!=null?langId:Cache.cacheMap.get("DEFAULT_LANGUAGEID")).getStatusDesc());

				}
			}	
		       else
		       {
		    	   getOrderDetailsDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_NO_LOYALTY_ID_"+langId).getStatusCode());
					getOrderDetailsDTO.setStatusDesc(Cache.getServiceStatusMap().get("GET_ORDER_NO_LOYALTY_ID_"+langId).getStatusDesc());
				//	throw new CommonException(Cache.getServiceStatusMap().get("GET_ORDER_NO_LOYALTY_ID_"+Cache.cacheMap.get("DEFAULT_LANGUAGEID")).getStatusDesc());

		       }
		    	   
			}else{
				getOrderDetailsDTO.setStatusCode(Cache.getServiceStatusMap().get("GET_ORDER_SUB_NOT_REG_"+langId).getStatusCode());
				getOrderDetailsDTO.setStatusDesc(Cache.getServiceStatusMap().get("GET_ORDER_SUB_NOT_REG_"+langId).getStatusDesc());
				
			//	throw new CommonException(Cache.getServiceStatusMap().get("GET_ORDER_SUB_NOT_REG_"+Cache.cacheMap.get("DEFAULT_LANGUAGEID")).getStatusDesc());

			}
			if(orderResplist!=null)
			logger.info("No of Orders recieved "+orderResplist.size());
		}/*catch (Exception e) {
			logger.error("Exception occured ",e);
			throw new CommonException("SC0004");
		}*/finally{
			
		}
		return orderResplist;
	}

	private Date getPreviousMonth(int noOfMonths) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -noOfMonths);
		return calendar.getTime();
	}
}

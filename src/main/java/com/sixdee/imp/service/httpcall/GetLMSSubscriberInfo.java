package com.sixdee.imp.service.httpcall;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.fw.exception.CommonException;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.service.httpcall.dto.DataSet;
import com.sixdee.imp.service.httpcall.dto.LMSSubscriberInfoDTO;
import com.sixdee.imp.bo.GetLMSSubscriberInfoBO;
import com.sixdee.imp.service.httpcall.dto.Parameters;
import com.sixdee.imp.service.httpcall.dto.Request;
import com.sixdee.imp.service.httpcall.dto.Response;

/**
 * @author Paramesh
 * @version 1.0.0
 * 
 * <p>
 * <b><u>Development History</u></b><br>
 * <table border="1" width="100%">
 * <tr>
 * <td width="15%">Date</td>
 * <td width="20%">Author</td>
 * <td>Description</td>
 * </tr>
 * <tr>
 * <td>Jan 31, 2014</td>
 * <td>paramesh</td>
 * <td>Created this class</td>
 * </tr>
 * </table>
 * </p>
 */
public class GetLMSSubscriberInfo extends HttpServlet {

	Logger logger = Logger.getLogger(GetLMSSubscriberInfo.class);
	

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
	}

	private void execute(HttpServletRequest req, HttpServletResponse resp) {
	
		String xml = null;
		BufferedInputStream inputStream = null;
		StringBuffer sb = null;
		LMSSubscriberInfoDTO subscriberInfoDTO=null;
		GenericDTO genericDTO = null;
		Request request=null;
		try {
			long t1 = System.currentTimeMillis();

			inputStream = new BufferedInputStream(req.getInputStream(), 32 * 1024);

			sb = new StringBuffer();
				int character = inputStream.read();
				while (character != -1) {

					sb.append((char) character);
					character = inputStream.read();
				}
				xml = sb.toString();
				
			logger.debug("Request recieved in POST Mode [" + xml + "]");

			boolean isSynch = false;
			boolean isRedeem = false;
			// instantRequestDTO.setDataSet(subsReqDTO.get)
			if (xml != null) {
				int i = 0;
				request = (Request) Cache.coomonReqXstream.fromXML(xml);
				logger.info("Request recieved [" + request.getRequestId() + "]");
				
				subscriberInfoDTO=new LMSSubscriberInfoDTO();
				subscriberInfoDTO.setRequestId(request.getRequestId());
				subscriberInfoDTO.setFeatureId(request.getFeatureId());
				subscriberInfoDTO.setKeyWord(request.getKeyWord());
				subscriberInfoDTO.setMsisdn(request.getMsisdn());
				subscriberInfoDTO.setDataSet(request.getDataSet());
				subscriberInfoDTO.setTimeStamp(request.getTimeStamp());
				
				DataSet dataSet = subscriberInfoDTO.getDataSet();
				 
				if (dataSet != null) {
					for (Parameters param : dataSet.getParams()) {
						
						 if (param.getId().equalsIgnoreCase("MSISDN")) {
							
							subscriberInfoDTO.setMsisdn(param.getValue().trim());
							i++;
						}else if(param.getId().equalsIgnoreCase("LANG_ID")){
							
							subscriberInfoDTO.setLanguageID(param.getValue().trim());
							i++;
						}
						 
						 if(i==2)
							 break;
					}
					if (i == 0) {
						throw new CommonException("Mandatory Parameter is missing");
					}
				}
				
				new GetLMSSubscriberInfoBO().getSubscriberDetails(subscriberInfoDTO);
				/*logger.warn(String
				 *				 
						.format("1%s|2%s|3%s|4|5|6%s|7|8|9%s|10%s|11%s|12'%s'|13%s|14%s|15%s|16%s|17%s|18%s|19  |20 |21 |22 |23%s|24%s|",
								1redeemPointsDTO.getTransactionId(),
								2df.format(new Date()),
								3redeemPointsDTO.getSubscriberNumber(),
								6parentLoyaltyId,
								9loyaltyProfileTabDTO.getTierId(),
								10loyaltyProfileTabDTO.getTierId(),
								11genericDTO.getStatusCode(),
								12genericDTO.getStatus(),
								13packageDetails.getRedeemPoints(),
								14packageDetails.getPackageId(),
								15loyaltyRegisteredNumberTabDTO
										.getAccountTypeId() != null ? loyaltyRegisteredNumberTabDTO
										.getAccountTypeId() : "",
								16redeemPointsDTO.getChannel(),
								17isTestNumber,
								18transactionDTO.getVoucherOrderID(),
								23redeemPointsDTO.getArea() != null ? redeemPointsDTO
										.getArea() : "",
								24redeemPointsDTO.getLocation() != null ? redeemPointsDTO
										.getLocation() : ""));*/
				
				SendRespMsg(resp,subscriberInfoDTO);
				
				if (isSynch) {

					/*genericDTO = new GenericDTO();
					genericDTO.setObj(subsReqDTO);

					InstantRewardsBO instantRewardsBO = new InstantRewardsBO();
					genericDTO = instantRewardsBO.buildProcess(genericDTO);*/
					// subsReqDTO = (SubscriberRequest) genericDTO.getObj();

				} else {
				/*	RequestProcessDTO processDTO = new RequestProcessDTO();
					processDTO = new RequestProcessDTO();
					processDTO.setFeatureName("InstantRewards");
					processDTO.setObject(subsReqDTO);
					SendRespMsg(resp, "SC0000", "SUCCESS");
					logger.info("Success Response Send , adding to Threadpool [" + subsReqDTO.getRequestId() + "] @ [" + (System.currentTimeMillis() - t1) + "]");
					ThreadInitiator.instantRewardPool.addTask(processDTO);*/
				}
			} else {
				logger.error("Request is empty");
			}
		} catch (CommonException e) {
			logger.info("Common Exception ",e);
			SendRespMsg(resp,subscriberInfoDTO);

		} catch (Exception e) {
			logger.info("Exception occured ", e);
			SendRespMsg(resp, subscriberInfoDTO);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			request=null;
		}

	}

	public void SendRespMsg(HttpServletResponse res,LMSSubscriberInfoDTO subscriberInfoDTO) {
		BufferedWriter writer = null;
		String respXML=null;
		Response response=null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(res.getOutputStream()));
			if (subscriberInfoDTO == null) {				
				respXML="<Response>  <ClientTxnId><![CDATA[2272236]]></ClientTxnId> <Timestamp><![CDATA["+System.currentTimeMillis()+"]]></Timestamp>  <Msisdn><![CDATA[]]></Msisdn>" +
	  		      "  <RespCode><![CDATA[SC1000]]></RespCode>  <RespDesc><![CDATA[Failure]]></RespDesc></Response>";
			}else{
				System.out.println(subscriberInfoDTO.getStatusDesc());
				response=new Response();
				
				response.setRequestId(subscriberInfoDTO.getRequestId());
				response.setTimeStamp(subscriberInfoDTO.getTimeStamp());
				
				response.setMsisdn(subscriberInfoDTO.getMsisdn());
				response.setStatus(subscriberInfoDTO.getStatus());
				response.setStatusDesc(subscriberInfoDTO.getStatusDesc());
				
				response.setDataSet(subscriberInfoDTO.getDataSet());
				
				respXML=Cache.coomonResXstream.toXML(response);
				logger.info(subscriberInfoDTO.getRequestId()+" : "+respXML);
				
			}
			writer.write(respXML);
			writer.flush();
		} catch (Exception exp) {
			logger.info("Exception Occured while Sending Response:::",exp);
		} finally {
			if (writer != null) {
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			respXML=null;
			response=null;
		}
	}

}

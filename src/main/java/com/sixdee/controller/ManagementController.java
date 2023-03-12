package com.sixdee.controller;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.QueryParam;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.bo.ReserveAndCommitBL;
import com.sixdee.imp.common.config.Cache;
import com.sixdee.imp.dto.LoyaltyResponseDTO;
import com.sixdee.imp.dto.RequestPoint;
import com.sixdee.imp.dto.ReserveAndCommitReqDto;
import com.sixdee.imp.dto.ReserveAndCommitRespDto;
import com.sixdee.imp.dto.ResponseDTO;
import com.sixdee.imp.dto.ResponsesDTO;

import com.sixdee.imp.dto.UserResponseDTO;
import com.sixdee.imp.dto.parser.RERequestHeader;
import com.sixdee.imp.dto.parser.REResponseDataSet;
import com.sixdee.imp.dto.parser.REResponseHeader;


import com.sixdee.imp.utill.Request;
import com.sixdee.lms.util.parser.JsonParser;
import com.sixdee.service.CreateAccountService;
import com.sixdee.service.GeneralManagementService;
import com.sixdee.service.PointEarnService;
import com.sixdee.service.PointExpiryService;
import com.sixdee.service.TransactionManagementService;
import com.sixdee.service.impl.CreateAccountServiceImpl;
import com.sixdee.service.impl.GeneralManagementServiceImpl;
import com.sixdee.service.impl.PointEarnServiceImpl;
import com.sixdee.service.impl.PointExpiryServiceImpl;
import com.sixdee.service.impl.TransactionManagementServiceImpl;

@RestController
@RequestMapping("/loyaltyManagement")
@CrossOrigin(origins = "*")
public class ManagementController {

	private Logger logger = LogManager.getLogger(ManagementController.class);

	private CreateAccountService creatAccountService;
	private GeneralManagementService generalManagementService;
	private PointEarnService pointEarnService;
	private TransactionManagementService transactionService;
	private PointExpiryService pointExpiryService;
	

	@PostMapping({ "/loyaltyProgramMember/{phoneNumber}" })
	public ResponsesDTO creatAccount(@RequestHeader("Authorization") String bearerToken,
			@PathVariable("phoneNumber") String phoneNumber, @RequestHeader Map<String, String> headers,HttpServletResponse servletResponse) {

		ResponsesDTO response = null;
		long t1 = System.currentTimeMillis();
		Map<String, String> lowerCaseMap = null;
		String token = null;

		try {
			creatAccountService = new CreateAccountServiceImpl();
			lowerCaseMap = new TreeMap<>();
			lowerCaseMap.putAll(headers);
			logger.info("...bearerToken:{}  requestId:{}", bearerToken, lowerCaseMap.get("X_CORRELATION_ID"));
			if (bearerToken != null) {
				token = bearerToken.substring(7);
			}
			if (authenticateSecurityToken(token)) {
				if (headers.size() > 0 && phoneNumber != null && !phoneNumber.equalsIgnoreCase("")) {

					if ((lowerCaseMap.get("X_CORRELATION_ID") != null
							&& !lowerCaseMap.get("X_CORRELATION_ID").trim().equalsIgnoreCase(""))
							&& (lowerCaseMap.get("CHANNEL") != null
									&& !lowerCaseMap.get("CHANNEL").trim().equalsIgnoreCase(""))) {

						response = creatAccountService.createAccount(phoneNumber, headers,servletResponse);

					} else {
						response = new ResponsesDTO();
						response.setReason("Required parameter not passing");
						response.setMessage("Please Pass Manditory Parameter");
						response.setCode("400");
						response.setReason("Missing Mandatory Parameters");
						servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
					}

				} else {
					response = new ResponsesDTO();
					response.setReason("Required parameter not passing");
					response.setMessage("Please Pass Manditory Parameter");
					response.setCode("400");
					response.setReason("Invalid Request");
				}

			} else {

				response = new ResponsesDTO();
				response.setReason("Authenticated value not passing");
				response.setMessage("Please pass uthenticated value");
				response.setCode("401");
				response.setReason("Unauthenticated");

			}

		} catch (Exception ex) {
			logger.error("Exception is " + ex);
		}finally {
			
			logger.info("Total Time taken :" + (System.currentTimeMillis() - t1));
		}

		return response;
	}

	@GetMapping({ "/loyaltyProgramMember/{phoneNumber}" })
	public UserResponseDTO getAccount(@RequestHeader("Authorization") String bearerToken,
			@PathVariable("phoneNumber") String phoneNumber, @RequestHeader Map<String, String> headers,HttpServletResponse servletResponse) {
		logger.info("**inside GetUserProfile***");
		UserResponseDTO response = null;
		long t2 = System.currentTimeMillis();
		Map<String, String> lowerCaseMap = null;
		String token = null;
		try {

			creatAccountService = new CreateAccountServiceImpl();
			lowerCaseMap = new TreeMap<>();
			lowerCaseMap.putAll(headers);
			logger.info("bearerToken:{}  requestId:{}", bearerToken, lowerCaseMap.get("X_CORRELATION_ID"));
			if (bearerToken != null) {
				token = bearerToken.substring(7);
			}
			if (authenticateSecurityToken(token)) {
				if (headers.size() > 0 && phoneNumber != null && !phoneNumber.equalsIgnoreCase("")) {

					if ((lowerCaseMap.get("X_CORRELATION_ID") != null
							&& !lowerCaseMap.get("X_CORRELATION_ID").trim().equalsIgnoreCase(""))
							&& (lowerCaseMap.get("CHANNEL") != null
									&& !lowerCaseMap.get("CHANNEL").trim().equalsIgnoreCase(""))) {

						response = creatAccountService.getUserProfile(phoneNumber, headers,servletResponse);

					} else {
						response = new UserResponseDTO();
						response.setReason("Required parameter not passing");

						response.setCode("400");
						response.setReason("Missing Mandatory Parameters");
					}

				} else {
					response = new UserResponseDTO();
					response.setReason("Required parameter not passing");
					response.setCode("400");
					response.setReason("Invalid Request");
				}

			} else {

				response = new UserResponseDTO();
				response.setReason("Authenticated value not passing");

				response.setCode("401");
				response.setReason("Unauthenticated");

			}

		} catch (Exception ex) {
			logger.error("Exception is " + ex);
		}finally {
			
			logger.info("Total Time taken :" + (System.currentTimeMillis() - t2));
		}

		return response;
	}

	@DeleteMapping({ "/loyaltyProgramMember/{phoneNumber}" })
	public ResponsesDTO deleteAccount(@RequestHeader("Authorization") String bearerToken,
			@PathVariable("phoneNumber") String phoneNumber, @RequestHeader Map<String, String> headers,HttpServletResponse servletResponse) {
		logger.info("***inside DeleteAccount***");
		ResponsesDTO response = null;
		long t3 = System.currentTimeMillis();
		Map<String, String> lowerCaseMap = null;
		String token = null;
		try {
			creatAccountService = new CreateAccountServiceImpl();
			lowerCaseMap = new TreeMap<>();
			lowerCaseMap.putAll(headers);
			logger.info("bearerToken:{} requestId:{}", bearerToken, lowerCaseMap.get("X_CORRELATION_ID"));
			if (bearerToken != null) {
				token = bearerToken.substring(7);
			}
			if (authenticateSecurityToken(token)) {
				if (phoneNumber != null) {

					if ((lowerCaseMap.get("X_CORRELATION_ID") != null) && (lowerCaseMap.get("CHANNEL") != null)) {

						response = creatAccountService.deleteLoyaltyAccount(phoneNumber, headers,servletResponse);

					} else {
						response = new ResponsesDTO();
						response.setReason("Required parameter not passing");
						response.setMessage("Please Pass Manditory Parameter");
						response.setCode("400");
						response.setReason("Missing Mandatory Parameters");
					}

				} else {
					response = new ResponsesDTO();
					response.setReason("Required parameter not passing");
					response.setMessage("Please Pass Manditory Parameter");
					response.setCode("400");
					response.setReason("Invalid Request");
				}

			} else {

				response = new ResponsesDTO();
				response.setReason("Authenticated value not passing");
				response.setMessage("Please pass uthenticated value");
				response.setCode("401");
				response.setReason("Unauthenticated");

			}

		} catch (Exception ex) {

		}

		return response;
	}

	@PostMapping({ "/loyaltyProgramMember/blacklist/{phoneNumber}" })
	public LoyaltyResponseDTO summaryofBlacklistSubscriber(@RequestHeader("Authorization") String bearerToken,
			@PathVariable("phoneNumber") String phoneNumber, @RequestHeader Map<String, String> headers,HttpServletResponse servletResponse) {
		logger.info("****inside blacklist****");
		LoyaltyResponseDTO response = null;
		long t3 = System.currentTimeMillis();
		Map<String, String> lowerCaseMap = null;
		String token = null;
		String txnId = null;
		try {

			generalManagementService = new GeneralManagementServiceImpl();
			lowerCaseMap = new TreeMap<>();
			lowerCaseMap.putAll(headers);
			if (bearerToken != null) {
				token = bearerToken.substring(7);
			}
			txnId = lowerCaseMap.get("X_CORRELATION_ID");
			logger.info("bearerToken:{} requestId:{}", bearerToken, lowerCaseMap.get("X_CORRELATION_ID"));
			if (authenticateSecurityToken(token)) {
				if (headers.size() > 0 && phoneNumber != null && !phoneNumber.equalsIgnoreCase("")) {

					if ((lowerCaseMap.get("X_CORRELATION_ID") != null
							&& !lowerCaseMap.get("X_CORRELATION_ID").trim().equalsIgnoreCase(""))
							&& (lowerCaseMap.get("CHANNEL") != null
									&& !lowerCaseMap.get("CHANNEL").trim().equalsIgnoreCase(""))) {

						response = generalManagementService.serviceManagement(phoneNumber, headers,servletResponse);

					} else {
						response = new LoyaltyResponseDTO();

						response.setRequestId(txnId);
						response.setTimestamp(t3);
						response.setRespCode("400");
						response.setRespDesc("Missing Mandatory Parameters");

					}

				} else {
					response = new LoyaltyResponseDTO();
					response.setRequestId(txnId);
					response.setTimestamp(t3);
					response.setRespCode("400");
					response.setRespDesc("Invalid Request");

				}

			} else {

				response = new LoyaltyResponseDTO();
				response.setRequestId(txnId);
				response.setTimestamp(t3);
				response.setRespCode("401");
				response.setRespDesc("Unauthenticated");

			}

		} catch (Exception ex) {

		}finally {
			
			logger.info("Total Time taken :" + (System.currentTimeMillis() - t3));
		}

		return response;
	}

	@PostMapping({ "/loyaltyProgramMember/{phoneNumber}/pointsadjustment" })
	public LoyaltyResponseDTO PointaddService(@RequestHeader("Authorization") String bearerToken,
			@PathVariable("phoneNumber") String phoneNumber, @RequestHeader Map<String, String> headers,
			@RequestBody(required = false) @Valid RequestPoint requestPoint,HttpServletResponse servletResponse) {
		logger.info("**inside Add/Sub Point***");
		logger.info("**inside Add/Sub Point***");
		LoyaltyResponseDTO response = null;
		long t1 = System.currentTimeMillis();
		Map<String, String> lowerCaseMap = null;
		String token = null;
		String txnId = null;
		try {
			pointEarnService = new PointEarnServiceImpl();
			lowerCaseMap = new TreeMap<>();
			lowerCaseMap.putAll(headers);

			if (bearerToken != null) {
				token = bearerToken.substring(7);
			}
			txnId = lowerCaseMap.get("X_CORRELATION_ID");
			if (authenticateSecurityToken(token)) {
				if (headers.size() > 0 && phoneNumber != null && !phoneNumber.equalsIgnoreCase("")) {

					if ((lowerCaseMap.get("X_CORRELATION_ID") != null) && (lowerCaseMap.get("CHANNEL") != null)) {

						response = pointEarnService.adjustPoints(phoneNumber, headers, requestPoint,servletResponse);

					} else {
						response = new LoyaltyResponseDTO();

						response.setRequestId(txnId);
						response.setTimestamp(t1);
						response.setRespCode("400");
						response.setRespDesc("Missing Mandatory Parameters");
						servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
					}

				} else {
					response = new LoyaltyResponseDTO();
					response.setRequestId(txnId);
					response.setTimestamp(t1);
					response.setRespCode("400");
					response.setRespDesc("Invalid Request");
					servletResponse.setStatus(HttpStatus.SC_BAD_REQUEST);
				}

			} else {

				response = new LoyaltyResponseDTO();
				response.setRequestId(txnId);
				response.setTimestamp(t1);
				response.setRespCode("401");
				response.setRespDesc("Unauthenticated");
				servletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);

			}

		} catch (Exception ex) {
			logger.info("**Exception***" + ex);
		}finally {
			
			logger.info("Total Time taken :" + (System.currentTimeMillis() - t1));
		}
		
		return response;
	}

	@GetMapping({ "/loyaltyProgramMember/history/{phoneNumber}" })
	public String transactionService(@RequestHeader("Authorization") String bearerToken,
			@RequestHeader("X_CORRELATION_ID") String requestId,
			@RequestHeader(name = "X_LANGUAGE", required = false) String language,
			@RequestHeader("channel") String channel, @PathVariable("phoneNumber") String phoneNumber,
			HttpServletResponse servletResponse, @QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate, @QueryParam("start") Integer start,
			@QueryParam("limit") Integer limit, @QueryParam("transactionType") String transactionType,
			@QueryParam("loyaltyId") String loyaltyId) {
		logger.info("**inside Transaction***");
		String response = null;
		List<String> responseList = null;
		long t1 = System.currentTimeMillis();
		Map<String, String> lowerCaseMap = null;
		String token = null;
		String txnId = null;
		try {
			transactionService = new TransactionManagementServiceImpl();

			if (bearerToken != null) {
				token = bearerToken.substring(7);
			}
			logger.info("Get TransactionDetails TransactionId :" + requestId + " Number :" + phoneNumber + " Channel :"
					+ channel + " Language :" + language + " startDate :" + startDate + " endDate :" + endDate
					+ " Offset :" + start + " Limit :" + limit + " TransactionType :" + transactionType + " Token :"
					+ bearerToken);

			if (authenticateSecurityToken(token)) {

				responseList = transactionService.getTransactionDetails(requestId, language, channel, phoneNumber,
						servletResponse, startDate, endDate, start, limit, transactionType, loyaltyId);

				if (responseList != null && responseList.size() > 0) {
					response = responseList.get(0);
					servletResponse.setContentType("application/json");
				
				}

			} else {

				servletResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);

			}

		} catch (Exception ex) {

		}finally {
			
			logger.info("Total Time taken :" + (System.currentTimeMillis() - t1));
		}

		return response;
	}
	@PostMapping({"/ReserveAndCommit"})
	public  ReserveAndCommitRespDto reserveAndCommite(HttpServletRequest req) {
	
		Long t1=System.currentTimeMillis();
		InputStream inputsteam=null;
		StringBuilder sb =null;
		String reqJson =null;
		ReserveAndCommitReqDto reserveAndCommitReq=null;
		GenericDTO genericDTO=null;
		ReserveAndCommitBL reserveAndCommitBO=null;
		ReserveAndCommitRespDto reseveCommitResponse=null;
		try {
			inputsteam=new BufferedInputStream(req.getInputStream(),32*1024);
			sb = new StringBuilder();
			int character =inputsteam.read();
			while(character!=-1){
				sb.append((char)character);
				character =inputsteam.read();
			}
			reqJson=sb.toString();
			
			
			reserveAndCommitReq=(ReserveAndCommitReqDto)JsonParser.fromJson(reqJson, ReserveAndCommitReqDto.class);
			 if(reserveAndCommitReq!=null && reserveAndCommitReq.getRequestId()!=null && !reserveAndCommitReq.getRequestId().equalsIgnoreCase("")
						&& reserveAndCommitReq.getChannel()!=null && !reserveAndCommitReq.getChannel().equalsIgnoreCase("")
						&& reserveAndCommitReq.getKeyword()!=null && !reserveAndCommitReq.getKeyword().equalsIgnoreCase("")
			) {
					
				
					genericDTO=new GenericDTO();
					genericDTO.setObj(reserveAndCommitReq);
					reserveAndCommitBO=new ReserveAndCommitBL();
					genericDTO=reserveAndCommitBO.buildProcess(genericDTO);
					
					if(genericDTO!=null && genericDTO.getObj()!=null){
						
						
						
						
						
					reseveCommitResponse=(ReserveAndCommitRespDto)genericDTO.getObj();
					
					
					
					}else {
						reseveCommitResponse=new ReserveAndCommitRespDto();
						
						reseveCommitResponse.setRequestId(reserveAndCommitReq.getRequestId());
						reseveCommitResponse.setTimestamp(reserveAndCommitReq.getTimestamp());
						reseveCommitResponse.setRespCode("SC0001");
						reseveCommitResponse.setRespDesc("FAILURE");
					}
					
					
				}else {
					reseveCommitResponse=new ReserveAndCommitRespDto();
					
					reseveCommitResponse.setRequestId(reserveAndCommitReq.getRequestId());
					reseveCommitResponse.setTimestamp(reserveAndCommitReq.getTimestamp());
					reseveCommitResponse.setRespCode("SC0001");
					reseveCommitResponse.setRespDesc("FAILURE");
						
				}
					
			 
			 
		}catch(Exception ex){
			
		}finally{
			//respMsg = JsonParser.toJson(reseveCommitResponse);
			logger.info("Total Time taken :" + (System.currentTimeMillis() - t1));
			
		}
		
		
		return reseveCommitResponse;
		
	}
	
	@PostMapping({"/PointExpiry"})
	public ResponseDTO callingRuleEngine(HttpServletRequest req) {
		
		
		logger.info("Request recieved to execute referral event " + req.toString());
		RERequestHeader request=null;
		long t1 = System.currentTimeMillis();
		ResponseDTO response = null;
		InputStream inputStream =null;
		StringBuilder sb =null;
		String reqJson =null;
	    
		try {
			
			
			inputStream=new BufferedInputStream(req.getInputStream(),32*1024);
			sb = new StringBuilder();
			int character =inputStream.read();
			while(character!=-1){
				sb.append((char)character);
				character =inputStream.read();
			}
			reqJson=sb.toString();
			 logger.info("REQ Json>>>>"+reqJson);
			
			 request=(RERequestHeader)JsonParser.fromJson(reqJson, RERequestHeader.class);
			
			 
			pointExpiryService=new PointExpiryServiceImpl();
			
			response=pointExpiryService.pointExpiry(request);
			
			
		}catch(Exception ex) {
			
		}finally {
			
			logger.info("Total Time taken :" + (System.currentTimeMillis() - t1));
		}
		
				return response;
		
	}
	
	

	public boolean authenticateSecurityToken(String securityToken) {
		boolean flag = false;
		try {
			if (Cache.getSecurityTokenDetailsMap().containsKey(securityToken))
				flag = true;
			else
				logger.debug(" Invalid Security Token");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return flag;

	}

}

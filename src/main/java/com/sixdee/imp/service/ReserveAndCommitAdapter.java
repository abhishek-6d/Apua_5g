package com.sixdee.imp.service;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.LogManager;

import com.sixdee.fw.dto.GenericDTO;
import com.sixdee.imp.bo.ReserveAndCommitBL;
import com.sixdee.imp.dto.ReserveAndCommitReqDto;
import com.sixdee.imp.dto.ReserveAndCommitRespDto;
import com.sixdee.lms.util.parser.JsonParser;

public class ReserveAndCommitAdapter extends HttpServlet {

	private static final long serialVersionUID=1L;
	private static final Logger logger=LogManager.getLogger(ReserveAndCommitAdapter.class);
	
	public void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException, ServletException {
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException, ServletException {
		execute(req,res);
	}
	private void execute(HttpServletRequest req, HttpServletResponse res) {
		BufferedInputStream inputStream=null;
		StringBuilder sb=null;
		ReserveAndCommitReqDto reserveAndCommitReq=null;
		GenericDTO genericDTO = null;
		ReserveAndCommitRespDto reseveCommitResponse=null;
		String request=null;
		String respMsg = null;
		Long t1=System.currentTimeMillis();
		ReserveAndCommitBL reserveAndCommitBO=null;
		try {
			reseveCommitResponse=new ReserveAndCommitRespDto();
			inputStream=new BufferedInputStream(req.getInputStream(),32*1024);
			
			sb=new StringBuilder();
			int i;
			while((i=inputStream.read())!=-1){
				sb.append((char) i);
			}
		    request=sb.toString();
			logger.info("Reserve and Commit Request {}",request);
			
			
			
			reserveAndCommitReq=(ReserveAndCommitReqDto)JsonParser.fromJson(request, ReserveAndCommitReqDto.class);
			if(reserveAndCommitReq!=null && reserveAndCommitReq.getRequestId()!=null && !reserveAndCommitReq.getRequestId().equalsIgnoreCase("")
					&& reserveAndCommitReq.getChannel()!=null && !reserveAndCommitReq.getChannel().equalsIgnoreCase("")
					&& reserveAndCommitReq.getKeyword()!=null && !reserveAndCommitReq.getKeyword().equalsIgnoreCase("")
		) {
				
				logger.info("reserveAndCommitReq:{}",reserveAndCommitReq.toString());
				ThreadContext.put("requestId", reserveAndCommitReq.getRequestId());
				genericDTO=new GenericDTO();
				genericDTO.setObj(reserveAndCommitReq);
				reserveAndCommitBO=new ReserveAndCommitBL();
				genericDTO=reserveAndCommitBO.buildProcess(genericDTO);
				
				if(genericDTO!=null && genericDTO.getObj()!=null){
					
					
					
					
					
				reseveCommitResponse=(ReserveAndCommitRespDto)genericDTO.getObj();
				
				
				logger.info("responseCode {} response desc {}",reseveCommitResponse.getRespCode(),reseveCommitResponse.getRespDesc());		
				}else {
					reseveCommitResponse=new ReserveAndCommitRespDto();
					logger.info("<<<   Failure From BL class   >>>");
					reseveCommitResponse.setRequestId(reserveAndCommitReq.getRequestId());
					reseveCommitResponse.setTimestamp(reserveAndCommitReq.getTimestamp());
					reseveCommitResponse.setRespCode("SC0001");
					reseveCommitResponse.setRespDesc("FAILURE");
				}
				
				
			}else {
				reseveCommitResponse=new ReserveAndCommitRespDto();
				logger.info(" Missing Mandatory Paramters");
				reseveCommitResponse.setRequestId(reserveAndCommitReq.getRequestId());
				reseveCommitResponse.setTimestamp(reserveAndCommitReq.getTimestamp());
				reseveCommitResponse.setRespCode("SC0001");
				reseveCommitResponse.setRespDesc("FAILURE");
					
			}
				
			
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			respMsg = JsonParser.toJson(reseveCommitResponse);
			res.setContentType("application/json");
			SendRespMsg(res, "SC0001", respMsg);
			reseveCommitResponse = null;
			inputStream = null;
			reserveAndCommitBO = null;
			logger.info("Transaction Id {} Total Time Taken in ReserveAndCommitAdapter {}",reserveAndCommitReq.getRequestId(),(System.currentTimeMillis()-t1));
			reserveAndCommitReq = null;
			ThreadContext.clearAll();
		}
		
	}
	public void SendRespMsg(HttpServletResponse res, String respCode,String respMsg) 

	{
		
		//logger.info(">>>>SendRespMsg>>>"+respMsg);
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter(new OutputStreamWriter(res.getOutputStream()));
		    if (respMsg == null) 
		    {
		    	res.setHeader("4xx", "");
		    	
		    	throw new Exception("cannot send ack message");
		    }
		   // res.setStatus(535);
		    writer.write(respMsg);
		    writer.flush();
		}
		catch (Exception exp) 
		{
			logger.error("Exception Occured while Sending Response:::"+exp);
		}
		finally 
		{
			if(writer != null){
				try {
					writer.close();
					writer = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    respCode = null;
		    respMsg =null;
		}
	}
}

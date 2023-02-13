/*package com.sixdee.lms.util;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.log4j.Logger;




*//**
 * @author Basil Jose
 * @version 1.0.0.0
 * @since 09-Feb-2016 : 4:53:55 pm
 *//*

public class LogMessageHandler implements SOAPHandler<SOAPMessageContext> {
  
  private static Logger logger  = Logger.getLogger(LogMessageHandler.class);
  private String txnId   = "";
  
  
  public LogMessageHandler(String txnId) {
    this.txnId = txnId;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Set<QName> getHeaders() {
    return Collections.EMPTY_SET;
  }

  @Override
  public boolean handleMessage(SOAPMessageContext context) {
    String url = "";
    StringBuilder sb = new StringBuilder();
    SOAPMessage msg  = context.getMessage(); //Line 1
    ByteArrayOutputStream baos = null;
    
    try {
  
      Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
      
      try {
        url = (String) context.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
      } catch (Exception e) {
        url = "";
      }
      
      if (outboundProperty.booleanValue()) {
        sb.append(" :-> URL : " + url + " *** REQUEST XML *** ");
        
      } else {
        sb.append(" *** RESPONSE XML *** ");
      }
      //logger.info("Soap Body "+msg.getSOAPBody());      
      baos = new ByteArrayOutputStream();
      msg.writeTo(baos);
      
      sb.append(baos.toString());
      

    } catch (Exception ex) {
      logger.error("");
    } finally {
      if (logger.isInfoEnabled()) {
        logger.info(txnId + maskPasswords(sb.toString()));
      }
      sb = null;
      msg = null;
      baos = null;
    }
    return true;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    return true;
  }

  @Override
  public void close(MessageContext context) {
  }

  
  *//**
   * Method which used to print the CXF soap xml by handling SOAPMessageContext.
   *//*
  @SuppressWarnings("rawtypes")
  public void printXml(String txnId,Object port) {
    try {
     logger.info("logging request");
      BindingProvider bindingProvider = (BindingProvider)port;
      Binding binding = bindingProvider.getBinding();
      List<Handler> handlerChain = binding.getHandlerChain();
      handlerChain.add(new LogMessageHandler(txnId));
      binding.setHandlerChain(handlerChain);
      logger.info("logging request");
    } catch (Exception e) {
      logger.error(e);
    }
  }
  
  
  protected String formatLoggingMessage(LoggingMessage loggingMessage) {

      String str = loggingMessage.toString();

  //    String output = maskPasswords(str);
      return(str);
  }


  private String maskPasswords(String str) {

              final String[] keys = { "Username","Password","PIN","Name"};
              for (String key : keys) {
                  int beginIndex = 0;
                  int lastIndex = -1;
                  boolean emptyPass = false;
                  while (beginIndex != -1
                          && (beginIndex = StringUtils.indexOfIgnoreCase(str, key,
                                  beginIndex)) > 0) {

                      beginIndex = StringUtils.indexOf(str, ">", beginIndex);
                      if (beginIndex != -1) {
                          char ch = str.charAt(beginIndex - 1);
                          if (ch == '/') {
                              emptyPass = true;
                          }
                          if (!emptyPass) {
                              lastIndex = StringUtils.indexOf(str, "<", beginIndex);
                              if (lastIndex != -1) {
                                  String overlay = "*";
                                  String str2 = StringUtils.substring(str,
                                          beginIndex + 1, lastIndex);
                                  if (str2 != null && str2.length() > 1) {
                                      overlay = StringUtils.rightPad(overlay,
                                              str2.length(), "*");
                                      str = StringUtils.overlay(str, overlay,
                                              beginIndex + 1, lastIndex);
                                  }
                              }
                          }
                          if (emptyPass) {
                              emptyPass = false;
                              lastIndex = beginIndex + 1;
                          } else {
                              if (lastIndex != -1) {
                                  lastIndex = StringUtils
                                          .indexOf(str, ">", lastIndex);
                              }
                          }
                      }
                      beginIndex = lastIndex;
                  }
              }
              return str;

          }

  
  public static void main(String[] args) {
	Pattern pattern = Pattern.compile("<wsse:UsernameToken>\\w*</wsse:UsernameToken>");
	String string = "<wsse:UsernameToken><wsse:Username>**a**</wsse:Username><wsse:Password>*a**</wsse:Password></wsse:UsernameToken>";
	System.out.println(pattern.matcher(string).replaceAll("<wsse:UsernameToken><wsse:Username>*****</wsse:Username><wsse:Password>****</wsse:Password></wsse:UsernameToken>"));
  }
}
*/
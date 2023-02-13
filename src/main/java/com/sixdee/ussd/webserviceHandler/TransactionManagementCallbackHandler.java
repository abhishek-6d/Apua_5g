
/**
 * TransactionManagementCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.sixdee.ussd.webserviceHandler;

    /**
     *  TransactionManagementCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class TransactionManagementCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public TransactionManagementCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public TransactionManagementCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getTransactionDetails method
            * override this method for handling normal response from getTransactionDetails operation
            */
           public void receiveResultgetTransactionDetails(
                    com.sixdee.ussd.webserviceHandler.TransactionManagementStub.GetTransactionDetailsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTransactionDetails operation
           */
            public void receiveErrorgetTransactionDetails(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getOrderDetails method
            * override this method for handling normal response from getOrderDetails operation
            */
           public void receiveResultgetOrderDetails(
                    com.sixdee.ussd.webserviceHandler.TransactionManagementStub.GetOrderDetailsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getOrderDetails operation
           */
            public void receiveErrorgetOrderDetails(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changeOrderStatus method
            * override this method for handling normal response from changeOrderStatus operation
            */
           public void receiveResultchangeOrderStatus(
                    com.sixdee.ussd.webserviceHandler.TransactionManagementStub.ChangeOrderStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changeOrderStatus operation
           */
            public void receiveErrorchangeOrderStatus(java.lang.Exception e) {
            }
                


    }
    
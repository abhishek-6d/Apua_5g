
/**
 * AccountManagementCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.sixdee.ussd.webserviceHandler;

    /**
     *  AccountManagementCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class AccountManagementCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public AccountManagementCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public AccountManagementCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for authenticateSubscriber method
            * override this method for handling normal response from authenticateSubscriber operation
            */
           public void receiveResultauthenticateSubscriber(
                    com.sixdee.ussd.webserviceHandler.AccountManagementStub.AuthenticateSubscriberResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from authenticateSubscriber operation
           */
            public void receiveErrorauthenticateSubscriber(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for accountMerging method
            * override this method for handling normal response from accountMerging operation
            */
           public void receiveResultaccountMerging(
                    com.sixdee.ussd.webserviceHandler.AccountManagementStub.AccountMergingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from accountMerging operation
           */
            public void receiveErroraccountMerging(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for createLoyaltyAccount method
            * override this method for handling normal response from createLoyaltyAccount operation
            */
           public void receiveResultcreateLoyaltyAccount(
                    com.sixdee.ussd.webserviceHandler.AccountManagementStub.CreateLoyaltyAccountResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from createLoyaltyAccount operation
           */
            public void receiveErrorcreateLoyaltyAccount(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for deleteLoyaltyAccount method
            * override this method for handling normal response from deleteLoyaltyAccount operation
            */
           public void receiveResultdeleteLoyaltyAccount(
                    com.sixdee.ussd.webserviceHandler.AccountManagementStub.DeleteLoyaltyAccountResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from deleteLoyaltyAccount operation
           */
            public void receiveErrordeleteLoyaltyAccount(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getTierDetails method
            * override this method for handling normal response from getTierDetails operation
            */
           public void receiveResultgetTierDetails(
                    com.sixdee.ussd.webserviceHandler.AccountManagementStub.GetTierDetailsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getTierDetails operation
           */
            public void receiveErrorgetTierDetails(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for viewAccounts method
            * override this method for handling normal response from viewAccounts operation
            */
           public void receiveResultviewAccounts(
                    com.sixdee.ussd.webserviceHandler.AccountManagementStub.ViewAccountsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from viewAccounts operation
           */
            public void receiveErrorviewAccounts(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changeAccountStatus method
            * override this method for handling normal response from changeAccountStatus operation
            */
           public void receiveResultchangeAccountStatus(
                    com.sixdee.ussd.webserviceHandler.AccountManagementStub.ChangeAccountStatusResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changeAccountStatus operation
           */
            public void receiveErrorchangeAccountStatus(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getRegisteredLines method
            * override this method for handling normal response from getRegisteredLines operation
            */
           public void receiveResultgetRegisteredLines(
                    com.sixdee.ussd.webserviceHandler.AccountManagementStub.GetRegisteredLinesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getRegisteredLines operation
           */
            public void receiveErrorgetRegisteredLines(java.lang.Exception e) {
            }
                


    }
    
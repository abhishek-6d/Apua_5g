
/**
 * UserProfileManagementCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.sixdee.ussd.webserviceHandler;

    /**
     *  UserProfileManagementCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class UserProfileManagementCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public UserProfileManagementCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public UserProfileManagementCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getMerchantDiscountRedeem method
            * override this method for handling normal response from getMerchantDiscountRedeem operation
            */
           public void receiveResultgetMerchantDiscountRedeem(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantDiscountRedeemResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMerchantDiscountRedeem operation
           */
            public void receiveErrorgetMerchantDiscountRedeem(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMerchantRedemption method
            * override this method for handling normal response from getMerchantRedemption operation
            */
           public void receiveResultgetMerchantRedemption(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantRedemptionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMerchantRedemption operation
           */
            public void receiveErrorgetMerchantRedemption(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for startService method
            * override this method for handling normal response from startService operation
            */
           public void receiveResultstartService(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.StartServiceResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startService operation
           */
            public void receiveErrorstartService(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getUserProfile method
            * override this method for handling normal response from getUserProfile operation
            */
           public void receiveResultgetUserProfile(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetUserProfileResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getUserProfile operation
           */
            public void receiveErrorgetUserProfile(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMerchantPackages method
            * override this method for handling normal response from getMerchantPackages operation
            */
           public void receiveResultgetMerchantPackages(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantPackagesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMerchantPackages operation
           */
            public void receiveErrorgetMerchantPackages(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for referralService method
            * override this method for handling normal response from referralService operation
            */
           public void receiveResultreferralService(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ReferralServiceResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from referralService operation
           */
            public void receiveErrorreferralService(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for forgotPassword method
            * override this method for handling normal response from forgotPassword operation
            */
           public void receiveResultforgotPassword(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ForgotPasswordResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from forgotPassword operation
           */
            public void receiveErrorforgotPassword(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changeSubscriberLanguage method
            * override this method for handling normal response from changeSubscriberLanguage operation
            */
           public void receiveResultchangeSubscriberLanguage(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ChangeSubscriberLanguageResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changeSubscriberLanguage operation
           */
            public void receiveErrorchangeSubscriberLanguage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMerchantDiscountDetails method
            * override this method for handling normal response from getMerchantDiscountDetails operation
            */
           public void receiveResultgetMerchantDiscountDetails(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.GetMerchantDiscountDetailsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMerchantDiscountDetails operation
           */
            public void receiveErrorgetMerchantDiscountDetails(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for changePassword method
            * override this method for handling normal response from changePassword operation
            */
           public void receiveResultchangePassword(
                    com.sixdee.ussd.webserviceHandler.UserProfileManagementStub.ChangePasswordResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from changePassword operation
           */
            public void receiveErrorchangePassword(java.lang.Exception e) {
            }
                


    }
    

/**
 * PointManagementCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package com.sixdee.ussd.webserviceHandler;

    /**
     *  PointManagementCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class PointManagementCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public PointManagementCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public PointManagementCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for redeemPoints method
            * override this method for handling normal response from redeemPoints operation
            */
           public void receiveResultredeemPoints(
                    com.sixdee.ussd.webserviceHandler.PointManagementStub.RedeemPointsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from redeemPoints operation
           */
            public void receiveErrorredeemPoints(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for transferPoints method
            * override this method for handling normal response from transferPoints operation
            */
           public void receiveResulttransferPoints(
                    com.sixdee.ussd.webserviceHandler.PointManagementStub.TransferPointsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from transferPoints operation
           */
            public void receiveErrortransferPoints(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for rewardPointsCalculation method
            * override this method for handling normal response from rewardPointsCalculation operation
            */
           public void receiveResultrewardPointsCalculation(
                    com.sixdee.ussd.webserviceHandler.PointManagementStub.RewardPointsCalculationResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from rewardPointsCalculation operation
           */
            public void receiveErrorrewardPointsCalculation(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for transferLine method
            * override this method for handling normal response from transferLine operation
            */
           public void receiveResulttransferLine(
                    com.sixdee.ussd.webserviceHandler.PointManagementStub.TransferLineResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from transferLine operation
           */
            public void receiveErrortransferLine(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPackages method
            * override this method for handling normal response from getPackages operation
            */
           public void receiveResultgetPackages(
                    com.sixdee.ussd.webserviceHandler.PointManagementStub.GetPackagesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPackages operation
           */
            public void receiveErrorgetPackages(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getPointDetails method
            * override this method for handling normal response from getPointDetails operation
            */
           public void receiveResultgetPointDetails(
                    com.sixdee.ussd.webserviceHandler.PointManagementStub.GetPointDetailsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPointDetails operation
           */
            public void receiveErrorgetPointDetails(java.lang.Exception e) {
            }
                


    }
    
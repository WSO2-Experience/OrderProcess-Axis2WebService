
/**
 * OrderServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.2  Built on : May 02, 2016 (05:55:18 BST)
 */

    package orderprocess;

    /**
     *  OrderServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class OrderServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public OrderServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public OrderServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for order method
            * override this method for handling normal response from order operation
            */
           public void receiveResultorder(
                    orderprocess.OrderServiceStub.OrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from order operation
           */
            public void receiveErrororder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for listAllItems method
            * override this method for handling normal response from listAllItems operation
            */
           public void receiveResultlistAllItems(
                    orderprocess.OrderServiceStub.ListAllItemsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listAllItems operation
           */
            public void receiveErrorlistAllItems(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for listOrderedItems method
            * override this method for handling normal response from listOrderedItems operation
            */
           public void receiveResultlistOrderedItems(
                    orderprocess.OrderServiceStub.ListOrderedItemsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from listOrderedItems operation
           */
            public void receiveErrorlistOrderedItems(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for payBill method
            * override this method for handling normal response from payBill operation
            */
           public void receiveResultpayBill(
                    orderprocess.OrderServiceStub.PayBillResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from payBill operation
           */
            public void receiveErrorpayBill(java.lang.Exception e) {
            }
                


    }
    
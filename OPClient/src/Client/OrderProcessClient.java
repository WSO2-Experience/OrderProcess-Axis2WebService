package Client;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

import OrderProcess.beans.MenuItems;

import javax.xml.namespace.QName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class OrderProcessClient {
    private static final String CARD_NAME = "CNo";
    private static final String PIN_NUMBER = "PNo";
    private static final String ITEM_NUMBER = "itemNo";
    private static int total=0;
    private static boolean ordered=false;
    
    
    public static void main(String[] args) throws Exception {
        OrderProcessClient client = new OrderProcessClient();
        client.runClient();
    }

    public void showOptions() {
     System.out.println();
        System.out.println();
        System.out.println(" To list All Available Menu Items                           Press 1");
        System.out.println(" To Order a Item      Press 2 and Type itemNo and Your orderig Item");
        System.out.println(" To list The Bill                                           Press 3");
        if(ordered){
           System.out.println(" To Pay a Bill Press 4 and Type CNo yoursCardNumber and PNo YoursPinNumber");
        }
        else {
         System.out.println(" To exit type -1 ");
  }
   
        System.out.println();
        System.out.println();
       
    }
    public void runClient() throws Exception {
     //System.out.println("Enter service End Point Reference address :          ");
        String epr = "http://localhost:8080/OrderProcessSystem/services/OrderService";
        //String epr =getInput();
        RPCServiceClient rpcClient = new RPCServiceClient();
        Options opts = new Options();
        opts.setTo(new EndpointReference(epr));
        rpcClient.setOptions(opts);
        OrderProcessClient client = new OrderProcessClient();
        
        System.out.println();
        System.out.println();
     System.out.println("###################################################################");
     System.out.println("######### Welcome to Order Process Service Sample testing #########");
     System.out.println("###################################################################");
        System.out.println();
        
        while (true) {
         showOptions();
            System.out.print("Type your request Here : ");
            
            String commandsParms = getInput();
            if (commandsParms != null) {
                String[] args = commandsParms.split(" ");
                String firstarg = args[0];
                int command;
                try {
                 command = Integer.parseInt(firstarg);
                }
                catch (NumberFormatException e) {
                 System.out.println("Illegal argument entered.\n");
                 continue;
    }
                switch (command) {
                    case 1 : {
                        client.listAllItems(rpcClient);
                        break;
                    }
                    case 2 : {
                        String isbn = null;
                        String username = null;
                        if (args.length < 2) {
                         System.out.println("No enough number of arguments");
                         break;
                        }

                        if (ITEM_NUMBER.equals(args[1])) {
                            isbn = args[2];
                        } 
                        
                        else{
                         System.out.println("Type itemNo and your wish Item");
                        }
                        client.orderItem(isbn, rpcClient);
                        ordered=true;
                        client.listOrderedItems(rpcClient);
                        break;
                    }
                    case 3 : {
                        client.listOrderedItems(rpcClient);
                        break;
                    }
                    case 4 : {
                        String cno = null;
                        String pno = null;
                        if (args.length < 5) {
                            System.out.println("No enough number of arguments");
                            break;
                        }
                        if (CARD_NAME.equals(args[1])) {
                            cno = args[2];
                        } else if (CARD_NAME.equals(args[3])) {
                            cno = args[4];
                        }

                        if (PIN_NUMBER.equals(args[1])) {
                            pno = args[2];
                        } else if (PIN_NUMBER.equals(args[3])) {
                            pno = args[4];
                        }
                        client.payBill(cno, pno,String.valueOf(total), rpcClient);
                        break;
                    }

                    case -1 : {
                        System.exit(0);
                    }
                    default: {
                     System.out.println("Wrong argument.\n");
                     break;
                    }
                }
            }
        }

        //  System.in.read()
    }

    //

    private String getInput() {
        try {
            byte b [] = new byte [256];
            int i = System.in.read(b);
            String msg = "";
            if (i != -1) {
                msg = new String(b).substring(0, i - 1).trim();
            }
            return msg;
        } catch (IOException e) {
            System.err.println(" occurred while reading in command : " + e);
            return null;
        }
    }


    public void orderItem(String isbn,
                         RPCServiceClient rpcClient) throws Exception {
        rpcClient.getOptions().setAction("urn:order");
        ArrayList args = new ArrayList();
        args.add(isbn);
       
        try{
        Object obj [] = rpcClient.invokeBlocking(new QName("http://OrderProcess",
                "order"), args.toArray(), new Class[]{MenuItems.class});
        }
        catch (Exception e) {
   // TODO: handle exception
         System.out.println(e.getMessage().toString());
  }
    }


    public void payBill(String userName,
                         String passWord,String total,
                         RPCServiceClient rpcClient) throws Exception {
        rpcClient.getOptions().setAction("urn:payBill");
        ArrayList args = new ArrayList();
        args.add(userName);
        args.add(passWord);
        args.add(total);
        try{
         Object obj [] = rpcClient.invokeBlocking(new QName("http://OrderProcess",
                    "payBill"), args.toArray(), new Class[]{Boolean.class});
            if(((Boolean) obj[0]).booleanValue()){
             System.out.println("Successfully Bill Payed \n WelCome Back Again");
             System.exit(0);
            }
        }
        catch(Exception e){
         System.out.println(e.getMessage().toString());
        }
  
    }

    private void printMenuItems(OMElement element) throws Exception {
        if (element != null) {
        // System.out.println(element);
            Iterator values = element.getChildrenWithName(new QName("http://OrderProcess", "return"));
            System.out.println();
            System.out.println("Item Number \t Item Name \t Item Prize");
            while (values.hasNext()) {
                OMElement omElement = (OMElement) values.next();
                MenuItems menuItems = (MenuItems) BeanUtil.deserialize(MenuItems.class, omElement, new DefaultObjectSupplier(), "item");
                System.out.println(menuItems.getItemNo()+"\t \t"+menuItems.getitemName()+"\t \t"+menuItems.getPrize());
                System.out.println("");
            }

        }
    }
    
    private void printBill(OMElement element) throws Exception {
        if (element != null) {
        // System.out.println(element);
            Iterator values = element.getChildrenWithName(new QName("http://OrderProcess", "return"));
            System.out.println();
            
            System.out.println("Item Number \t Item Name \t Item Prize");
            while (values.hasNext()) {
                OMElement omElement = (OMElement) values.next();
                MenuItems menuItems = (MenuItems) BeanUtil.deserialize(MenuItems.class, omElement, new DefaultObjectSupplier(), "item");
                System.out.println(menuItems.getItemNo()+"\t \t"+menuItems.getitemName()+"\t \t"+menuItems.getPrize());
                total=total+Integer.parseInt(menuItems.getPrize());
                System.out.println("");
            }
            
            System.out.println("Total Amount to pay is :Rs "+total+"/=");

        }
    }

    public void listAllItems(RPCServiceClient rpcClient) throws Exception {
        rpcClient.getOptions().setAction("urn:listAllItems");
        try{
            OMElement elemnt = rpcClient.invokeBlocking(new QName("http://OrderProcess",
                       "listAllItems"), new Object[]{null});
               printMenuItems(elemnt);
        }
        catch(Exception e)
        {
         System.out.println(e.getMessage().toString());
        }
     
    }

    public void listOrderedItems(RPCServiceClient rpcClient) throws Exception {
        rpcClient.getOptions().setAction("urn:listOrderedItems");
        OMElement elemnt = rpcClient.invokeBlocking(new QName("http://OrderProcess",
                "listOrderedItems"), new Object[]{null});
        printBill(elemnt);
    }

}
package OrderProcess;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;

import OrderProcess.OrderServiceConstants;
import OrderProcess.beans.MenuItems;
import OrderProcess.beans.MenuItemList;
import OrderProcess.beans.Card;
import OrderProcess.beans.CardList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OrderService {

 private static final Log log = LogFactory.getLog(OrderService.class);
    //To store all the available Items
    private MenuItemList availableMenuItems;
    //to keep all the Items in the system
    private MenuItemList allMenuItems;
    //to keep all the Ordered Items
    private MenuItemList OrderedItems;
    //to keep the system card Users 
    private CardList cardList;


    public MenuItems[] listAllItems() {
        return allMenuItems.getItemList();
    }

    public MenuItems[] listOrderedItems() {
        return OrderedItems.getItemList();
    }

    public MenuItems[] order(String itemNo) throws AxisFault {
            MenuItems menuItems = availableMenuItems.getItem(itemNo);
            if (menuItems == null) {
                menuItems = OrderedItems.getItem(itemNo);
                if (menuItems != null) {
                    throw new AxisFault("Someone has borrowed the book");
                }
                throw new AxisFault("Your Requested Item Not in Our System");
            }
            OrderedItems.addItem(menuItems);
            return OrderedItems.getItemList();        
    }


    public boolean payBill(String cardNo, String pinNO,String total) throws AxisFault {
        return cardList.billPay(cardNo, pinNO  ,total);
    }

    /**
     * Session related methods
     */
    public void init(ServiceContext serviceContext) {
        AxisService service = serviceContext.getAxisService();
        this.availableMenuItems = (MenuItemList) service.getParameterValue(OrderServiceConstants.AVAILABLE_ITEMS);

        this.availableMenuItems.setListName(OrderServiceConstants.AVAILABLE_ITEMS);
        this.allMenuItems = (MenuItemList) service.getParameterValue(OrderServiceConstants.ALL_ITEMS);
        this.OrderedItems = (MenuItemList) service.getParameterValue(OrderServiceConstants.ORDERED_ITEMS);
        this.cardList = (CardList) service.getParameterValue(OrderServiceConstants.CARD_BALANCE);
    }

    public void destroy(ServiceContext serviceContext) throws AxisFault {
        AxisService service = serviceContext.getAxisService();
        service.addParameter(new Parameter(OrderServiceConstants.AVAILABLE_ITEMS, availableMenuItems));
        service.addParameter(new Parameter(OrderServiceConstants.ALL_ITEMS, allMenuItems));
        service.addParameter(new Parameter(OrderServiceConstants.ORDERED_ITEMS, OrderedItems));
        service.addParameter(new Parameter(OrderServiceConstants.CARD_BALANCE, cardList));
    }
}
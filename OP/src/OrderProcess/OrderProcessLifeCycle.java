package OrderProcess;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.DefaultObjectSupplier;
import org.apache.axis2.engine.ServiceLifeCycle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import OrderProcess.beans.MenuItems;
import OrderProcess.beans.MenuItemList;
import OrderProcess.beans.Card;
import OrderProcess.beans.CardList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import java.io.*;
import java.util.Iterator;

public class OrderProcessLifeCycle implements ServiceLifeCycle {
    private static final Log log = LogFactory.getLog(OrderProcessLifeCycle.class);

    public void startUp(ConfigurationContext configctx,
                        AxisService service) {
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            File tempFile = new File(tempDir);
            File libFile = new File(tempFile, "OrderProcess.xml");
            //creating temporary files for saving the data
            OMElement OrderElement;
            OMElement cardElement;
            boolean noFile = true;
            if (!libFile.exists()) {
                //Service starting at the first time or user has clean the temp.dir
             //if file not already available
                Parameter allItems = service.getParameter(OrderServiceConstants.ALL_ITEMS);
                //read the values with the above parameter name
                OrderElement = allItems.getParameterElement();
                //Library OMElement Successfully created
            } else {
             //if file already created one then start to read the existing file instead of service.xml
                InputStream in = new FileInputStream(libFile);
                XMLStreamReader xmlReader = StAXUtils
                        .createXMLStreamReader(in);
                StAXOMBuilder staxOMBuilder = new StAXOMBuilder(xmlReader);
                OrderElement = staxOMBuilder.getDocumentElement();
              //Library OMElement Successfully created
                noFile = false;
            }
            processOmelemnt(OrderElement, service, noFile);
        } catch (Exception exception) {
            log.info(exception);
        }
    }

    public void shutDown(ConfigurationContext configctx,
                         AxisService service) {
        try {
            MenuItemList availableItemList = (MenuItemList) service.getParameterValue(OrderServiceConstants.AVAILABLE_ITEMS);
            MenuItemList allItemList = (MenuItemList) service.getParameterValue(OrderServiceConstants.ALL_ITEMS);
            MenuItemList orderedItemList = (MenuItemList) service.getParameterValue(OrderServiceConstants.ORDERED_ITEMS);
            CardList cardList = (CardList) service.getParameterValue(OrderServiceConstants.CARD_BALANCE);
            
            OMFactory fac = OMAbstractFactory.getOMFactory();
            OMElement libElement = fac.createOMElement("OrderProcess", null);
            MenuItems[] bookList = allItemList.getItemList();
            
            libElement.addChild(BeanUtil.getOMElement(
                    new QName(OrderServiceConstants.ALL_ITEMS),
                    bookList, new QName("item"), false, null));
            
            libElement.addChild(BeanUtil.getOMElement(
                    new QName(OrderServiceConstants.AVAILABLE_ITEMS),
                    availableItemList.getItemList(), new QName("item"), false, null));
            

            libElement.addChild(BeanUtil.getOMElement(
                    new QName(OrderServiceConstants.CARD_BALANCE),
                    cardList.getUsers(), new QName("card"), false, null));

            String tempDir = System.getProperty("java.io.tmpdir");
            File tempFile = new File(tempDir);
            File libFile = new File(tempFile, "OrderProcess.xml");
            OutputStream out = new FileOutputStream(libFile);
            libElement.serialize(out);
            out.flush();
            out.close();
            
        } catch (Exception e) {
            log.info(e);
        }
    }

    private void processOmelemnt(OMElement element, AxisService service, boolean fileFound) throws AxisFault {
        MenuItemList allBookList = new MenuItemList(OrderServiceConstants.ALL_ITEMS);
       // log.info("created   "+allBookList.toString());
        OMElement bookEle = element.getFirstChildWithName(new QName(OrderServiceConstants.ALL_ITEMS));
        Iterator book_itr = bookEle.getChildren();
        while (book_itr.hasNext()) {
            Object obj = book_itr.next();
            if (obj instanceof OMElement) {
                OMElement omElement = (OMElement) obj;
                allBookList.addItem((MenuItems) BeanUtil.deserialize(MenuItems.class, omElement, new DefaultObjectSupplier(), "book"));
            }
        }
  MenuItemList availableBookList = new MenuItemList(OrderServiceConstants.AVAILABLE_ITEMS);
        OMElement avaliableBooksEle =
                element.getFirstChildWithName(new QName(OrderServiceConstants.AVAILABLE_ITEMS));
        if (avaliableBooksEle != null) {
            Iterator available_book_itr = avaliableBooksEle.getChildren();
            while (available_book_itr.hasNext()) {
                Object obj = available_book_itr.next();
                if (obj instanceof OMElement) {
                    OMElement omElement = (OMElement) obj;
                    availableBookList.addItem((MenuItems) BeanUtil.deserialize(MenuItems.class, omElement, new DefaultObjectSupplier(), "book"));
                }

            }
        }


        MenuItemList lendBookList = new MenuItemList(OrderServiceConstants.ORDERED_ITEMS);
        OMElement lendBooksEle =
                element.getFirstChildWithName(new QName(OrderServiceConstants.ORDERED_ITEMS));
        if (lendBooksEle != null) {
            Iterator lend_book_itr = lendBooksEle.getChildren();
            while (lend_book_itr.hasNext()) {
                Object obj = lend_book_itr.next();
                if (obj instanceof OMElement) {
                    OMElement omElement = (OMElement) obj;
                    lendBookList.addItem((MenuItems) BeanUtil.deserialize(MenuItems.class, omElement, new DefaultObjectSupplier(), "book"));
                }
            }
        }
        
        //Creating a UserList
        CardList users = new CardList();
        OMElement usersEle =
                element.getFirstChildWithName(new QName(OrderServiceConstants.CARD_BALANCE));
        if (usersEle != null) {
            Iterator usre_itr = usersEle.getChildren();
            while (usre_itr.hasNext()) {
                Object obj = usre_itr.next();
                if (obj instanceof OMElement) {
                    OMElement omElement = (OMElement) obj;
                    users.addUser((Card) BeanUtil.deserialize(Card.class, omElement,
                            new DefaultObjectSupplier(), "card"));
                }

            }
        }
        
        if (fileFound) {
         //File if not in there then we need to copy all books into available book
            availableBookList = allBookList.copy();
            service.addParameter(new Parameter(OrderServiceConstants.AVAILABLE_ITEMS, availableBookList));
        } else {
            service.addParameter(new Parameter(OrderServiceConstants.AVAILABLE_ITEMS, availableBookList));
        }

        service.addParameter(new Parameter(OrderServiceConstants.ALL_ITEMS, allBookList));
        service.addParameter(new Parameter(OrderServiceConstants.ORDERED_ITEMS, lendBookList));
        service.addParameter(new Parameter(OrderServiceConstants.CARD_BALANCE, users));
    }
}

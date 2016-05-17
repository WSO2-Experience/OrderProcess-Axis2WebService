package OrderProcess.beans;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;

import java.util.HashMap;
import java.util.Iterator;

public class MenuItemList {

    private HashMap itemTable;
    private String listName;

    public MenuItemList(String listName) {
        this.itemTable = new HashMap();
        this.listName = listName;
    }

    public void addItem(MenuItems menuItems) {
     //Method to add the items in the particular ListName 
        itemTable.put(menuItems.getItemNo().trim(), menuItems);
    }

    public MenuItems getItem(String itemNo) {
     //Method to Return a Item With the Given Item Number
        return (MenuItems) itemTable.get(itemNo.trim());
    }


    public HashMap getItemTable() {
     //to return a Item table
        return itemTable;
    }

    public void setItemTable(HashMap itemtable) {
     //Assign a List of Tables to New Table
        this.itemTable = itemtable;
    }

    public String getListName() {
     //Get the Name of the List
        return listName;
    }

    public void setListName(String listName) {
     //Set the Name to the List
        this.listName = listName;
    }

    public OMElement serialize(OMFactory fac) {
        return null;
    }

    public MenuItems[] getItemList() {
     //Creating New Array From itemTable hashmap 
        MenuItems [] items = new MenuItems[itemTable.size()];
        Iterator items_itr = itemTable.values().iterator();
        int count = 0;
        while (items_itr.hasNext()) {
            items[count] = (MenuItems) items_itr.next();
            //Storing Values into array
            count ++;
        }
        return items;
    }

    public MenuItemList copy() {
     //add values into hashmap in the form of MenuItem Object
        MenuItemList items = new MenuItemList(getListName());
        MenuItems [] allItems = getItemList();
        for (int i = 0; i < allItems.length; i++) {
            MenuItems allItem = allItems[i];
            MenuItems menuItems = new MenuItems();
            menuItems.setPrize(allItem.getPrize());
            menuItems.setItemNo(allItem.getItemNo());
            menuItems.setitemName(allItem.getitemName());
            items.addItem(menuItems);
        }
        return items;
    }
}

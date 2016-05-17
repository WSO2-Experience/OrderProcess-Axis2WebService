package OrderProcess.beans;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;

public class MenuItems {
	private String itemname;
    private String prize;
    private String itemnumber;

    public String getitemName() {
        return itemname;
    }

    public void setitemName(String itemname) {
        this.itemname = itemname;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String itemname) {
        this.prize = itemname;
    }

    public String getItemNo() {
        return itemnumber;
    }

    public void setItemNo(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public OMElement serialize(OMFactory fac) {
        return null;
    }
}

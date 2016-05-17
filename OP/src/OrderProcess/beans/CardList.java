package OrderProcess.beans;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import OrderProcess.OrderProcessLifeCycle;

import java.util.HashMap;
import java.util.Iterator;

public class CardList {
    private HashMap<String, Card> cardList;
    private HashMap<String, Card> cardHolder;

private static final Log log = LogFactory.getLog(CardList.class);

    public CardList() {
        this.cardList = new HashMap<String, Card>();
        cardHolder = new HashMap<String, Card>();
    }

    public void addUser(Card card) throws AxisFault {
        if (cardList.get(card.getcardNo().trim()) != null) {
            throw new AxisFault("User has already registered.");
        }
        cardList.put(card.getcardNo(), card);
    }

    public boolean billPay(String cardNo, String pinNo, String total) throws AxisFault {
        Card card = cardList.get(cardNo.trim());
        if (card == null) {
            throw new AxisFault("user has not registerd");
        }
        if (card.getPinNo().equals(pinNo)) {
            cardHolder.put(cardNo, card);
            if(Integer.parseInt(card.getCardBal())>=Integer.parseInt(total)){
             card.setCardBal(String.valueOf(Integer.parseInt(card.getCardBal())-Integer.parseInt(total)));
             cardList.put(card.getcardNo(), card);
             return true;
            }
            else{
              throw new AxisFault("Check Your Balance");
            }
           
        } else {
            throw new AxisFault("Invalid Card Number , Pin Number incorrect");
        }
    }

    public Card[] getUsers() {
        Card [] users = new Card[cardList.size()];
        Iterator users_itr = cardList.values().iterator();
        int count = 0;
        while (users_itr.hasNext()) {
            users[count] = (Card) users_itr.next();
            count ++;
        }
        return users;
    }
}

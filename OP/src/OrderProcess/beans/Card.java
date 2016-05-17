package OrderProcess.beans;

public class Card {
    private String cardNo;
    private String cardBal;
    private String pinNo;

    public Card() {
    }

    public Card(String cardNo, String pinNo , String cardBal) {
        this.cardNo = cardNo;
        this.cardBal = cardBal;
        this.pinNo = pinNo;
    }

    public String getPinNo() {
        return pinNo;
    }

    public void setPinNo(String PinNo) {
        this.pinNo = PinNo;
    }

    public String getcardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    

    public String getCardBal() {
        return cardBal;
    }

    public void setCardBal(String cardBal) {
        this.cardBal = cardBal;
    }
}	
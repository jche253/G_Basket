package codemeharder.gbasket;

/**
 * Created by Jimmy Chen on 4/4/2016.
 */
public class ReceiptHistItem {
    String date;
    String serial;
    String receiptText;

    public ReceiptHistItem() {

    }

    public ReceiptHistItem(String date, String serial, String receiptText) {
        this.date = date;
        this.serial = serial;
        this.receiptText = receiptText;
    }

    public void setDate (String date) { this.date = date; }
    public String getDate () {return this.date;}
    public void setSerial (String serial) { this.serial = serial; }
    public String getSerial () {return this.serial;}
    public void setReceiptText(String receiptText) {this.receiptText = receiptText; }
    public String getReceiptText () {return this.receiptText;}
}

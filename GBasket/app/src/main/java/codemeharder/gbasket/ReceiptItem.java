package codemeharder.gbasket;

/**
 * Created by Jimmy Chen on 3/17/2016.
 */
public class ReceiptItem {
    private String itemName;
    private double itemPrice;
    private double origPrice;
    private double discount;

    public ReceiptItem(String name, double itemCost, double origCost, double aDiscount) {
        super();
        this.itemName = name;
        this.itemPrice = itemCost;
        this.origPrice = origCost;
        this.discount = aDiscount;

    }
    public String getItemName() {
        return this.itemName;
    }

    public void setName(String name) {
        this.itemName = name;
    }

    public double getItemPrice() {
        return this.itemPrice;
    }

    public void setPrice(Double price) {
        this.itemPrice = price;
    }

    public double getItemOrigPrice() {
        return this.origPrice;
    }

    public void setOrigPrice(Double price) {
        this.origPrice = price;
    }

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(Double discounted) {
        this.discount = discounted;
    }



}

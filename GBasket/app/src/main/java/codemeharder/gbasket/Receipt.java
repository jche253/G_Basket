package codemeharder.gbasket;

import com.stripe.android.model.Card;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jimmy Chen on 3/1/2016.
 */
public class Receipt {
    String date;
    int Serial;
    String accType;
    int transactionID;
    ArrayList<String> item = new ArrayList<String>();
    ArrayList<Double> price = new ArrayList<Double>();
    ArrayList<Double> DiscountOrigPrice = new ArrayList<Double>();
    ArrayList<Double> PriceOff = new ArrayList<Double>();
    double payment_amount = 0;
    double tax;
    double total;

    Receipt(Date CurDate, Card card, ArrayList<String> items, ArrayList<Double> prices) {
        SimpleDateFormat ft =
                new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        this.date = ft.format(CurDate);
        //TODO make an algorithm for generating serial numbers and transaction ID
        String accType = card.getType();
        this.price = prices;
        this.item = items;
        for (int i = 0; i < this.price.size(); i++)
            this.payment_amount += this.price.get(i);
        //TODO find ways to get tax location
        //ToDO calculate total

    }

    //TODO set getters/setters

    public void setDate(Date date) {
        SimpleDateFormat ft =
                new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        this.date = ft.format(date);
    }

    public String getDate() {
        return date;
    }

    public void setSerial(int newSerial) {
        this.Serial = newSerial;
    }

    public int getSerial() {
        return this.Serial;
    }

    public void setAccType(String newAccType) {
        this.accType = newAccType;
    }

    public String getAccType() {
        return this.accType;
    }

    public void setItem(ArrayList<String> newItems) {
        this.item = newItems;
    }

    public ArrayList<String> getItem() {
        return this.item;
    }

    public void setPrice (ArrayList<Double> newPrices) {
        this.price = newPrices;
    }

    public ArrayList<Double> getPrice() {
        return this.price;
    }

    public void setOrigPrice (ArrayList<Double> newDiscount) {
        this.DiscountOrigPrice = newDiscount;
    }

    public ArrayList<Double> getOrigPrice() {
        return this.DiscountOrigPrice;
    }

    public void setPriceOff (ArrayList<Double> newPriceOff) {
        this.PriceOff = newPriceOff;
    }

    public ArrayList<Double> getPriceOff() {
        return this.PriceOff;
    }

    public Double getPaymentAmount() {
        return this.payment_amount;
    }

    public void setTax() {
        //Set tax
    }

    public Double getTax() {
        return this.tax;
    }


    //TODO fill this later once the template is made
    public String fillReceiptTemplate() {
        return null;
    }


}

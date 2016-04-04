package codemeharder.gbasket;

import android.os.Parcel;
import android.os.Parcelable;

import com.stripe.android.model.Card;

import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 3/1/2016.
 */
public class Receipt implements Parcelable {
    String date;
    String Serial;
    String accType;
    ArrayList<EachItem> itemPrice = new ArrayList<EachItem>();
    ArrayList<Double> DiscountOrigPrice = new ArrayList<Double>();
    ArrayList<Double> PriceOff = new ArrayList<Double>();
    double payment_amount = 0;
    double tax = 0;
    double total = 0;

    public Receipt () {

    }

    public Receipt(String CurDate, Card card, ArrayList<EachItem> yourItems, ArrayList<Double> DisOrigPrice,
                   ArrayList<Double> discounts, double tax, String serial) {
        this.date = CurDate;
        //TODO make an algorithm for generating serial numbers and transaction ID
        this.accType = card.getType();
        this.Serial = serial;
        this.itemPrice = yourItems;
        this.DiscountOrigPrice = DisOrigPrice;
        this.PriceOff = discounts;
        for (int i = 0; i < this.itemPrice.size(); i++)
            this.payment_amount += this.itemPrice.get(i).getPrice();
        //TODO Add tax once you get location
        this.tax = tax;
        this.total = this.payment_amount + (this.payment_amount * this.tax/100);
        //TODO find ways to get tax location
    }

    public String getDate() {
        return date;
    }

    public void setDate (String datee) {
        this.date = datee;
    }

    public void setSerial(String newSerial) {
        this.Serial = newSerial;
    }

    public String getSerial() {
        return this.Serial;
    }

    public void setAccType(String newAccType) {
        this.accType = newAccType;
    }

    public String getAccType() {
        return this.accType;
    }

    public void addItemPrice(EachItem newItem) {
        this.itemPrice.add(newItem);
    }

    public ArrayList<EachItem> getItemPrice() {
        return this.itemPrice;
    }

    public void setItemPrice(ArrayList<EachItem> itemPrices) {
        this.itemPrice = itemPrices;
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

    public void setPaymentAmount(Double pam) {
        this.payment_amount = pam;
    }

    public Double getTax() {
        return this.tax;
    }

    public void setTax(Double taxe) {
        this.tax = taxe;
    }

    public Double getTotal() {
        return this.total;
    }

    public void setTotal(Double totaltemp) {
        this.total = totaltemp;
    }

    // Parcelling part
    public Receipt(Parcel in){
        this.date = in.readString();
        this.Serial = in.readString();
        this.accType = in.readString();
        this.itemPrice = (ArrayList<EachItem>) in.readSerializable();
        this.DiscountOrigPrice = (ArrayList<Double>) in.readSerializable();
        this.PriceOff = (ArrayList<Double>) in.readSerializable();
        this.payment_amount = in.readDouble();
        this.tax = in.readDouble();
        this.total = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.Serial);
        dest.writeString(this.accType);
        dest.writeSerializable(this.itemPrice);
        dest.writeSerializable(this.DiscountOrigPrice);
        dest.writeSerializable(this.PriceOff);
        dest.writeDouble(this.payment_amount);
        dest.writeDouble(this.tax);
        dest.writeDouble(this.total);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };
}



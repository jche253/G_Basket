package codemeharder.gbasket;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 4/13/2016.
 */
public class EachItemID implements Parcelable {
    //Each item on the ListView for YourBucket
    int ID;
    String name;
    double price;
    boolean checkbox;

    EachItemID() {
        this.checkbox = false;
    }

    EachItemID(int ident, String name, double price, boolean checkBox) {
        this.ID = ident;
        this.name = name;
        this.price = price;
        this.checkbox = checkBox;
    }

    public int getID() {return this.ID; }
    public String getName(){
        return this.name;
    }
    public double getPrice(){
        return this.price;
    }
    public boolean getCheckBox() {return this.checkbox;}

    public void setID (int ident){this.ID = ident;}
    public void setName(String nname) {this.name = nname;}
    public void setPrice(Double pprice) {this.price = pprice;}
    public void setCheckBOx(boolean tf) {this.checkbox = tf;}


    public EachItemID(Parcel in){
        this.ID = in.readInt();
        this.name = in.readString();
        this.price = in.readDouble();
        this.checkbox = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.name);
        dest.writeDouble(this.price);
        dest.writeByte((byte) (checkbox ? 1 : 0));
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public EachItemID createFromParcel(Parcel in) {
            return new EachItemID(in);
        }

        public EachItemID[] newArray(int size) {
            return new EachItemID[size];
        }
    };
}

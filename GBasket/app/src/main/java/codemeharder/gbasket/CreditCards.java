package codemeharder.gbasket;

import android.os.Parcel;
import android.os.Parcelable;

import com.stripe.android.model.Card;

import java.util.ArrayList;

/**
 * Created by seokyung on 4/6/16.
 */
public class CreditCards implements Parcelable {
        String cardnum;
        String realCardNum;
        boolean selected = false;

        public CreditCards () {

        }

        public CreditCards(String cnum, String real, boolean selected) {
            this.cardnum = cnum;
            this.realCardNum = real;
            this.selected = selected;
        }

        public String getRealCardNum() {return this.realCardNum;}

        public void setRealCardNum(String real) { this.realCardNum = real; }

        public String getCardnum() {
            return cardnum;
        }

        public void setCardnum (String cn) {
            this.cardnum = cn;
        }

        public boolean isSelected() {
        return selected;
    }

        public void setSelected(boolean selected) {
        this.selected = selected;
    }

        // Parcelling part
        public CreditCards(Parcel in){
            this.cardnum = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.cardnum);
        }

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public CreditCards createFromParcel(Parcel in) {
                return new CreditCards(in);
            }

            public CreditCards[] newArray(int size) {
                return new CreditCards[size];
            }
        };
}




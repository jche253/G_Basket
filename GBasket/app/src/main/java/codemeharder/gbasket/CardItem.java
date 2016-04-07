package codemeharder.gbasket;

import java.io.Serializable;

/**
 * Created by seokyung on 4/6/16.
 */
public class CardItem implements Serializable {
    //Each item on the ListView for YourBucket
    String cnum;
    boolean checkbox;

    CardItem(String cnumber, boolean checkBox) {
        this.cnum = cnumber;
        this.checkbox = checkBox;
    }

    public String getCnum() {
        return this.cnum;
    }
}
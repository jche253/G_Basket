package codemeharder.gbasket;

import java.io.Serializable;

/**
 * Created by Jimmy Chen on 2/16/2016.
 */
public class EachItem implements Serializable {
    //Each item on the ListView for YourBucket
    String name;
    double price;
    boolean checkbox;

    EachItem() {

    }

    EachItem(String name, double price, boolean checkBox) {
        this.name = name;
        this.price = price;
        this.checkbox = checkBox;
    }

    public String getName(){
        return this.name;
    }
    public double getPrice(){
        return this.price;
    }
    public boolean getCheckBox() {return this.checkbox;}

    public void setName(String nname) {this.name = nname;}
    public void setPrice(Double pprice) {this.price = pprice;}
    public void setCheckBOx(boolean tf) {this.checkbox = tf;}


}

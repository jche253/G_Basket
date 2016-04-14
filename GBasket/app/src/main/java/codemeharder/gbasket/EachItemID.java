package codemeharder.gbasket;

import java.io.Serializable;

/**
 * Created by Jimmy Chen on 4/13/2016.
 */
public class EachItemID implements Serializable {
    //Each item on the ListView for YourBucket
    int ID;
    String name;
    double price;
    boolean checkbox;

    EachItemID() {

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


}

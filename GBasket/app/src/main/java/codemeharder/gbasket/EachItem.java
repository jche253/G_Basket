package codemeharder.gbasket;

/**
 * Created by Jimmy Chen on 2/16/2016.
 */
public class EachItem {
    //Each item on the ListView for YourBucket
    String name;
    double price;

    EachItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }
    public double getPrice(){
        return this.price;
    }

}
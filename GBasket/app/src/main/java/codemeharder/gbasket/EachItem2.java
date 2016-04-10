package codemeharder.gbasket;

import java.io.Serializable;

/**
 * Created by Alfred Wong on 4/5/2016.
 */

    public class EachItem2 implements Serializable {
        //Each item on the ListView for YourBucket
        String name;
        double price;
        boolean checkbox;
        int ID;

        public EachItem2(int ID, String name, double price, boolean checkBox) {
            this.ID = ID;
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

        public int getID(){return this.ID;}

        public void setName(String name) {this.name = name;}
        public void setID(int ID ) {this.ID = ID;}
        public void setPrice(double price) {this.price = price;}


    }



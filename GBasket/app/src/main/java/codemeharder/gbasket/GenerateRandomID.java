package codemeharder.gbasket;

import java.util.Random;

/**
 * Created by Jimmy Chen on 4/13/2016.
 */
public class GenerateRandomID {
    int ID;

    GenerateRandomID() {
        Random rnd = new Random();
        this.ID = 100000 + rnd.nextInt(900000);
    }

    public int getID() { return this.ID;}
}

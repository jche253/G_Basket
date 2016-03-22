package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Jimmy Chen on 2/14/2016.
 */
public class YourBasketActivity extends Activity {
    ListView lv;
    EachItem[] items;
    Button add, pay, remove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourbucket);

        lv = (ListView) findViewById(R.id.listView);

        //Sample inflation of items
        items = new EachItem[5];
        items[0] = new EachItem("pizza", 3.44, true);
        items[1] = new EachItem("burger", 2.00, true);
        items[2] = new EachItem("olives", 1.00, true);
        items[3] = new EachItem("steak", 12.90, true);
        items[4] = new EachItem("fish", 8.76, true);

        CustomAdapter adapter = new CustomAdapter(this, items);
        lv.setAdapter(adapter);

        add = (Button) findViewById(R.id.buttonAdd);
        remove = (Button) findViewById(R.id.buttonRemove);
        pay = (Button) findViewById(R.id.buttonPay);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getApplicationContext(), ItemActivity.class);
                startActivity(addIntent);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add a way to remove from listview
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Sum up the prices from the receipt

                Intent payIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                //payIntent.putExtra("items")
                startActivity(payIntent);
            }
        });
    }
}

package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 2/14/2016.
 */
public class YourBasketActivity extends Activity {
    ListView lv;
    ArrayList<EachItem> items;
    Button add, pay, remove;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourbucket);

        lv = (ListView) findViewById(R.id.listView);

        //Sample inflation of items
        items = new ArrayList<EachItem>();
        items.add(new EachItem("pizza", 3.44, true));
        items.add(new EachItem("burger", 2.00, true));
        items.add(new EachItem("olives", 1.00, true));
        items.add(new EachItem("steak", 12.90, true));
        items.add(new EachItem("fish", 8.76, true));

        adapter = new CustomAdapter(this, items);
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
            public void onClick(View v)
            {
                SparseBooleanArray checkedItemPositions = lv.getCheckedItemPositions();
                int itemCount = lv.getCount();

                for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.valueAt(i)){
                        adapter.remove(items.get(i));
                    }
                }
                checkedItemPositions.clear();
                adapter.notifyDataSetChanged();

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

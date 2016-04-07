package codemeharder.gbasket;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Jimmy Chen on 2/16/2016.
 */
public class ItemActivity extends Activity {
    Button backBtn, addBtn;
    TextView name, price;
    BasketHelper basketHelper = new BasketHelper(this);

    //TODO get rid of this when we get cloud database going
    int prodID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        backBtn = (Button) findViewById(R.id.backBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        name = (TextView) findViewById(R.id.itemName_TextView);
        price = (TextView) findViewById(R.id.price_TextView);

        Intent i = getIntent();
        String format = i.getStringExtra("format");
        String content = i.getStringExtra("content");

        //TODO just a random ID
        Random rnd = new Random();
        int n = 1000000 + rnd.nextInt(9000000);
        //TODO Fix with database
        if (format != null && content != null) {
            if (format.equals("CODE_128") && content.equals("pizza")) {
                name.setText("pizza");
                price.setText("3.44");
                prodID = n;
            }
        } else {
            name.setText("pizza");
            price.setText("3.44");
            prodID = n;
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(getApplicationContext(), LaunchPadActivity.class);
                startActivity(intentScan);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String format = getIntent().getStringExtra("format");
                String content = getIntent().getStringExtra("content");

                //Translate the format/content through database query for the item

                AddData();


            }
        });

    }

    public void AddData() {
        String price2 = price.getText().toString();
        double price3 = Double.parseDouble(price2);


        boolean isInserted = basketHelper.insertData(prodID, name.getText().toString(), price3);
        if (isInserted == true) {
            Toast.makeText(ItemActivity.this, "Item was successfully added.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), YourBasketActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(ItemActivity.this, "Item was not successfully added.", Toast.LENGTH_LONG).show();
        }


    }


}

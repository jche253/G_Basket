package codemeharder.gbasket;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jimmy Chen on 2/16/2016.
 */
public class ItemActivity extends Activity {
    Button backBtn, addBtn;
    TextView name, price;
    BasketHelper basketHelper = new BasketHelper(this);

    //TODO get rid of this when we get cloud database going
    String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        backBtn = (Button) findViewById(R.id.backBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        name = (TextView) findViewById(R.id.itemName_TextView);
        price = (TextView) findViewById(R.id.price_textView);

        Intent i = getIntent();
        String format = i.getStringExtra("format");
        String content = i.getStringExtra("content");

        //TODO Fix with database
        if (format != null && content != null) {
            if (format.equals("CODE_128") && content.equals("pizza")) {
                name.setText("pizza");
                price.setText("3.44");
                ID = "123456789";

            }
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
        boolean isInserted = basketHelper.insertData(ID, name.getText().toString(), price.getText().toString());
        if (isInserted == true) {
            Toast.makeText(ItemActivity.this, "Account was successfully created.", Toast.LENGTH_LONG).show();
            Intent intentBucket = new Intent(getApplicationContext(), YourBasketActivity.class);
            //intentBucket.putExtra("format", format);
            //intentBucket.putExtra("content", content);
            startActivity(intentBucket);
        }
        else
            Toast.makeText(ItemActivity.this, "Account was not successfully created.", Toast.LENGTH_LONG).show();
    }


}

package codemeharder.gbasket;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Jimmy Chen on 2/16/2016.
 */
public class ItemActivity extends Activity {
    Button backBtn, addBtn;
    TextView name, price;

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

                Intent intentBucket = new Intent(getApplicationContext(), YourBasketActivity.class);
                intentBucket.putExtra("format", format);
                intentBucket.putExtra("content", content);
                startActivity(intentBucket);

            }
        });

    }
}

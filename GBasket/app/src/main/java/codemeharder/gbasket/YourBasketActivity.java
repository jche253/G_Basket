package codemeharder.gbasket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 2/14/2016.
 */
public class YourBasketActivity extends Activity implements View.OnClickListener {
    ListView lv;
    ArrayList<EachItem> items;
    Button add, pay, remove;
    CustomAdapter adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourbucket);
        context = this;

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

        add.setOnClickListener(this);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

               /* new AlertDialog.Builder(context)
                        .setTitle("test")
                        .setMessage(test)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/
                //int itemCount = lv.getCount();

               /* for(int i=itemCount-1; i >= 0; i--){
                    if(checkedItemPositions.valueAt(i)){
                        adapter.remove(items.get(i));
                    }
                }
                checkedItemPositions.clear();
                adapter.notifyDataSetChanged();*/

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAdd) {
            /* TODO
             * If you want to test the application features without adding items/scanning stuff
             * then comment out (1) and leave in (2)
             * If you want to use the application with barcode scanner, please comment out (2)
             * and keep (1) uncommented.
             */

            //TODO (1) Use for actual barcode
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
            scanIntegrator.setOrientationLocked(false);
            scanIntegrator.initiateScan();

            //TODO (2) to sandbox the application without the scanner
            //Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
            //startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            //Result
            //TODO: do stuff with result later!
            String scanContent = scanResult.getContents();
            String scanFormat = scanResult.getFormatName();

            Intent intentItem = new Intent(getApplicationContext(), ItemActivity.class);
            if (scanContent != null && scanFormat != null) {
                intentItem.putExtra("content", scanContent);
                intentItem.putExtra("format", scanFormat);
                startActivity(intentItem);
            }
            else {
                //TODO Need to make a better error message later
                //Display some popup dialog and go back to camera/yourbasket
                Toast toast = Toast.makeText(getApplicationContext(), "No data received", Toast.LENGTH_SHORT);
            }


        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received", Toast.LENGTH_SHORT);
            toast.show();

        }
    }
}

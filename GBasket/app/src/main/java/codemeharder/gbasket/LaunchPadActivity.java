package codemeharder.gbasket;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by jdevillasee on 3/23/16.
 */
public class LaunchPadActivity extends Activity implements View.OnClickListener {
    Button pushscan, account, setting, glist, basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launchpage);

        pushscan = (Button) findViewById(R.id.push_button);
        account = (Button) findViewById(R.id.account);
        setting = (Button) findViewById(R.id.settings);
        glist = (Button) findViewById(R.id.grocerylist);
        basket = (Button) findViewById(R.id.myBasket);

        glist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GroceryListActivity.class);
                startActivity(intent);
            }
        });

        pushscan.setOnClickListener(this);

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), YourBasketActivity.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent UPintent = new Intent(getApplicationContext(), UnitPriceCalc.class);
                startActivity(UPintent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.push_button) {
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
            //Test case from tutorial
            //formatTxt.setText("FORMAT: " + scanFormat);
            //contentTxt.setText("CONTENT: " + scanContent);

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

    @Override
    public void onBackPressed() {
    }
}

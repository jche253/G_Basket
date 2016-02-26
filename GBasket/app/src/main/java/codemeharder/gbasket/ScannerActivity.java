package codemeharder.gbasket;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by Jimmy Chen on 2/13/2016.
 */
public class ScannerActivity extends Activity implements View.OnClickListener {
    Button button2;
    TextView formatTxt, contentTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        button2 = (Button) findViewById(R.id.button_scan);

        //TODO Temporary text
        formatTxt = (TextView) findViewById(R.id.formatTxt);
        contentTxt = (TextView) findViewById(R.id.contentTxt);

        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_scan) {
            /*
             * If you want to test the application features without adding items/scanning stuff
             * then comment out (1) and leave in (2)
             * If you want to use the application with barcode scanner, please comment out (2)
             * and keep (1) uncommented.
             */

            //TODO (1) Use for actual barcode
            //IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            //scanIntegrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
            //scanIntegrator.setOrientationLocked(false);
            //scanIntegrator.initiateScan();

            //TODO (2) to sandbox the application without the scanner
            Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
            startActivity(intent);
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
                intent.putExtra("content", scanContent);
                intent.putExtra("format", scanFormat);
                startActivity(intentItem);
            }
            else {
                //TODO Need to make a better error message later
                //Display some popup dialog and go back to camera/yourbasket
                Toast toast = Toast.makeText(getApplicationContext(), "No data recieved", Toast.LENGTH_SHORT);
            }


        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data recieved", Toast.LENGTH_SHORT);
            toast.show();

        }
    }
}

package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 2/20/2016.
 */
public class HistoryActivity extends Activity {
    ListView historyList;
    ReceiptAdapter adapter;
    ArrayList<ReceiptHistItem> ReceiptArray = new ArrayList<>();
    ReceiptHandler dbHandler = new ReceiptHandler(this, null, null, 3);
    BasketHelper basketHelper = new BasketHelper(this);
    Button exit, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        Bundle b = getIntent().getBundleExtra("bundle");
        final Receipt receipt = b.getParcelable("Receipt");
        final String toWrite = b.getString("ReceiptTxt");
        historyList = (ListView) findViewById(R.id.historyLV);
        System.gc();

        exit = (Button) findViewById(R.id.exit);
        back = (Button) findViewById(R.id.back);
        //populate adapter with sqlite database elements
        ReceiptArray = getAllTuples();

        ReceiptHistItem newItem = new ReceiptHistItem();
        newItem.setDate(receipt.getDate());
        newItem.setSerial(receipt.getSerial());
        newItem.setReceiptText(toWrite);

        adapter = new ReceiptAdapter(this, R.layout.receipt_row,
                ReceiptArray);
        historyList.setAdapter(adapter);

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setFocusable(false);
                // Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
                //TODO Fix this to query instead of pulling a default receipt
                findReceipt(adapter.receipts.get(position).getSerial());
            }
        });

        exit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                basketHelper.deletedb();
                finish();
                System.exit(0);
            }
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LaunchPadActivity.class);
                startActivity(intent);
            }
        });
    }

    public void findReceipt(String Serial) {
        String result = dbHandler.findReceipt(Serial);
        if (result != null) {
            //Open huge alert window temp fix now
           Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "Cannot find serial", Toast.LENGTH_LONG);
        }
    }

    public ArrayList<ReceiptHistItem> getAllTuples() {
        return dbHandler.getAllValues();
    }

    @Override
    public void onBackPressed() {
    }
}

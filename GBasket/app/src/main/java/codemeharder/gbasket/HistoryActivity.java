package codemeharder.gbasket;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    ArrayList<Receipt> ReceiptArray = new ArrayList<Receipt>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Intent intent = getIntent();
        Receipt receipt = (Receipt) intent.getParcelableExtra("Receipt");
        //Toast.makeText(this, receipt.getSerial(), Toast.LENGTH_LONG).show();
        historyList = (ListView) findViewById(R.id.historyLV);
        adapter = new ReceiptAdapter(this, R.layout.receipt_row,
                ReceiptArray);
        ReceiptArray.add(receipt);
        historyList.setAdapter(adapter);

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }
}

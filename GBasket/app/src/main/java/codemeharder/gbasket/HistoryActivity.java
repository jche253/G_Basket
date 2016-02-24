package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Jimmy Chen on 2/20/2016.
 */
public class HistoryActivity extends Activity {
    ListView historyList;
    Button select, back;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button select =  (Button) findViewById(R.id.select);
        Button back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recIntent = new Intent(getApplicationContext(), ReceiptActivity.class);
                startActivity(recIntent);
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO select receipt

            }
        });

    }
}

package codemeharder.gbasket;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jimmychen.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import static java.lang.Math.random;


/**
 * Created by Jimmy Chen on 2/16/2016.
 */
public class ItemActivity extends Activity implements AsyncResponse {
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

        EndpointsAsyncTaskPQuery test = new EndpointsAsyncTaskPQuery();
        test.delegate = this;
        test.execute(new Triplet(getApplicationContext(), format, content));

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
                //Translate the format/content through database query for the item
                String price2 = price.getText().toString();
                double price3 = Double.parseDouble(price2);
                String Pname = name.getText().toString();
                boolean isInserted = basketHelper.insertData(Pname, price3);
                if (isInserted) {
                    Toast.makeText(ItemActivity.this, Pname + " was successfully added.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), YourBasketActivity.class);
                    intent.putExtra("name", Pname);
                    intent.putExtra("price", price3);
                    startActivity(intent);
                } else {
                    Toast.makeText(ItemActivity.this, "Item was not successfully added.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void processFinish(String output) {
        //Toast.makeText(this, output, Toast.LENGTH_LONG).show();
        price.setText(output.substring(0, output.indexOf(' ')));
        name.setText(output.substring(output.indexOf(' ') + 1));
    }

    class EndpointsAsyncTaskPQuery extends AsyncTask<Triplet, Void, String> {
        private MyApi myApiService = null;
        private Context context;
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(Triplet... params) {
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://testing-1261.appspot.com/_ah/api");
                // end options for devappserver

                myApiService = builder.build();
            }

            context = params[0].getContext();
            String format = params[0].getformat();
            String content = params[0].getcontent();


            try {
                return myApiService.productQuery(format, content).execute().getDataPQuery();
            } catch (IOException e) {
                return e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }
    }
}

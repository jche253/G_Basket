package codemeharder.gbasket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 2/14/2016.
 */
public class YourBasketActivity extends Activity implements View.OnClickListener {
    ListView lv;
    ArrayList<EachItemID> items2;
    ArrayList<EachItemID> ids = new ArrayList<EachItemID>();
    Button add, pay, remove;
    CustomAdapter1 adapter;
    Context context;
    BasketHelper basketHelper = new BasketHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourbucket);
        context = this;
        Intent intent = getIntent();
        String name = "";
        Double price = 0.0;
        name = intent.getStringExtra("name");
        price = intent.getDoubleExtra("price", 0);

        lv = (ListView) findViewById(R.id.ToBuyList);

        //Inflation of items with the most recent item (unless it's not from itemactivity)
        items2 = new ArrayList<EachItemID>();
        items2 = basketHelper.queryBasket();
        adapter = new CustomAdapter1(this, R.layout.row, items2);
        lv.setAdapter(adapter);

        add = (Button) findViewById(R.id.buttonAdd);
        remove = (Button) findViewById(R.id.buttonRemove);
        pay = (Button) findViewById(R.id.buttonPay);

        add.setOnClickListener(this);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ids.size() > 0) {
                    for (int i = 0; i < ids.size(); i++) {
                        //Fixed primary key
                        basketHelper.deleteRow(ids.get(i).getID());
                        items2.remove(ids.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (items2.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please scan items first", Toast.LENGTH_LONG).show();
                    return;
                }

                //TODO Move this somewhere else
                basketHelper.deletedb();

                Intent payIntent = new Intent(getApplicationContext(), PaymentActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("items2", items2);
                payIntent.putExtra("bundle", b);
                startActivity(payIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
            } else {
                //TODO Need to make a better error message later
                //Display some popup dialog and go back to camera/yourbasket
                Toast toast = Toast.makeText(getApplicationContext(), "No data received", Toast.LENGTH_SHORT);
            }


        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received", Toast.LENGTH_SHORT);
            toast.show();

        }
    }

    public class CustomAdapter1 extends ArrayAdapter {
        ArrayList<EachItemID> items = null;
        Context context;
        int resourceID;

        public CustomAdapter1(Context context, int layoutresourceID, ArrayList<EachItemID> resource) {
            super(context, R.layout.row, resource);
            this.resourceID = layoutresourceID;
            this.context = context;
            this.items = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = convertView;
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();

                holder = new ViewHolder();
                convertView = inflater.inflate(resourceID, null);
                holder.name = (TextView) convertView.findViewById(R.id.ItemName);
                holder.price = (TextView) convertView.findViewById(R.id.ItemPrice);
                holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(items.get(position).getName());
            if (items.get(position).getPrice() == 0) {
                holder.price.setText("$0.00");
            } else {
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                String priceStr = formatter.format(items.get(position).getPrice());
                holder.price.setText(priceStr);
            }
            if (items.get(position).getCheckBox()) {
                holder.cb.setChecked(false);
            } else {
                holder.cb.setVisibility(View.GONE);
            }

            holder.cb.setOnCheckedChangeListener(null);
            //holder.cb.setChecked(ids.contains(items.get(position)));
            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        ids.add(items.get(position));
                        //Toast.makeText(getApplicationContext(), items.get(position).getName() + " ", Toast.LENGTH_LONG).show();
                    } else {
                        if (ids.contains(items.get(position))) {
                            ids.remove(items.get(position));
                        }
                    }
                }
            });
            return convertView;
        }

        public class ViewHolder {
            CheckBox cb;
            TextView name;
            TextView price;
        }
    }
}




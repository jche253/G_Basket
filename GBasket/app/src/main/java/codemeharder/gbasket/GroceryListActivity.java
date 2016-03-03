package codemeharder.gbasket;

/**
 * Created by jdevillasee on 2/28/16.
 */


import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

    public class GroceryListActivity extends Activity {
        ListView ToBuyList;
        ToBuyCustomAdapter ToBuyAdapter;
        ArrayList<ToBuyItem> ToBuyArray = new ArrayList<ToBuyItem>();
        EditText editText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_grocerylist);

            /**
             * add item in arraylist
             */
            ToBuyArray.add(new ToBuyItem("Bananas"));
            ToBuyArray.add(new ToBuyItem("Dish detergent"));
            ToBuyArray.add(new ToBuyItem("Shrimp"));
            ToBuyArray.add(new ToBuyItem("Cheese"));
            ToBuyArray.add(new ToBuyItem("Pasta"));
            ToBuyArray.add(new ToBuyItem("Macaroni"));
            ToBuyArray.add(new ToBuyItem("Vegetables"));
            /**
             * set item into adapter
             */
            ToBuyAdapter = new ToBuyCustomAdapter(GroceryListActivity.this, R.layout.list_row,
                    ToBuyArray);
            ToBuyList = (ListView) findViewById(R.id.listView);
            ToBuyList.setItemsCanFocus(false);
            ToBuyList.setAdapter(ToBuyAdapter);
            /**
             * get on item click listener
             */
            ToBuyList.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        final int position, long id) {
                    Log.i("List View Clicked", "**********");
                    Toast.makeText(GroceryListActivity.this,
                            "List View Clicked:" + position, Toast.LENGTH_LONG)
                            .show();
                }
            });

            editText = (EditText) findViewById(R.id.txtInput);
            Button btAdd = (Button) findViewById(R.id.btAdd);

            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newItem = editText.getText().toString();
                    // add new item to arraylist
                    GroceryListActivity.this.ToBuyArray.add(new ToBuyItem(newItem));
                    // notify listview of data changed
                    ToBuyAdapter.notifyDataSetChanged();
                }

            });


            Button delete = (Button) findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                    // add new item to arraylist
                    // notify listview of data changed
                    ToBuyAdapter.notifyDataSetChanged();
                }

            });

        }
    }
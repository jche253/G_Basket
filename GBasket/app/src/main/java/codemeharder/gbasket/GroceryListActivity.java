package codemeharder.gbasket;

/**
 * Created by jdevillasee on 2/28/16.
 */


import java.util.ArrayList;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Button;

public class GroceryListActivity extends Activity {
    ListView ToBuyList;
    ToBuyCustomAdapter ToBuyAdapter;
    ArrayList<ToBuyItem> ToBuyArray = new ArrayList<ToBuyItem>();
    EditText editText;
    GroceryListHelper glHelper = new GroceryListHelper(this, null, null, 1);

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_grocerylist);

         ToBuyList = (ListView) findViewById(R.id.ToBuyList);
         ToBuyList.setItemsCanFocus(false);

         ToBuyArray = glHelper.getAllValues();
         ToBuyAdapter = new ToBuyCustomAdapter(GroceryListActivity.this, R.layout.list_row,
                 ToBuyArray);

         ToBuyList.setAdapter(ToBuyAdapter);

         editText = (EditText) findViewById(R.id.txtInput);
         ImageButton btAdd = (ImageButton) findViewById(R.id.btAdd);

         btAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String newItem = editText.getText().toString();
                 // add new item to arraylist
                 GroceryListActivity.this.ToBuyArray.add(new ToBuyItem(newItem));
                 glHelper.addItem(newItem);
                 // notify listview of data changed
                 ToBuyAdapter.notifyDataSetChanged();
                 editText.setText("");
             }

         });
     }
}
package codemeharder.gbasket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * Created by Jimmy Chen on 3/1/2016.
 */

//This will be the page that populates the Shopping history

public class ReceiptAdapter extends ArrayAdapter{
    Receipt[] receipts = null;
    Context context;

    public ReceiptAdapter(Context context, Receipt[] resource) {
        super(context, R.layout.receipt_row, resource);
        this.context = context;
        this.receipts = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.receipt_row, parent, false);
        TextView date = (TextView) convertView.findViewById(R.id.Date);
        TextView serial = (TextView) convertView.findViewById(R.id.Serial);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBoxR);

        //TODO fill in the adapter to actually add stuff to the layout elements
        /*name.setText(items[position].getName());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String priceStr = formatter.format(items[position].getPrice());
        price.setText(priceStr);*/
        cb.setChecked(false);

        return convertView;
    }
}

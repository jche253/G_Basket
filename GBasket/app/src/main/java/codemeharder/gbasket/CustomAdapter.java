package codemeharder.gbasket;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 2/16/2016.
 */
public class CustomAdapter extends ArrayAdapter {
    ArrayList<EachItem> items = null;
    Context context;

    public CustomAdapter(Context context, ArrayList<EachItem> resource) {
        super(context, R.layout.row, resource);
        this.context = context;
        this.items = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.ItemName);
        TextView price = (TextView) convertView.findViewById(R.id.ItemPrice);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        name.setText(items.get(position).getName());
        if (items.get(position).getPrice() == 0) {
            price.setText("$0.00");
        }
        else {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String priceStr = formatter.format(items.get(position).getPrice());
            price.setText(priceStr);
        }
        if (items.get(position).getCheckBox()) {
            cb.setChecked(false);
        }
        else {
            cb.setVisibility(View.GONE);
        }


        return convertView;
    }
}

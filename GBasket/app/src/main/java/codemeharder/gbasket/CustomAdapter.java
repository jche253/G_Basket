package codemeharder.gbasket;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    int resourceID;

    public CustomAdapter(Context context,int layoutresourceID, ArrayList<EachItem> resource) {
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
        }
        else {
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String priceStr = formatter.format(items.get(position).getPrice());
            holder.price.setText(priceStr);
        }
        if (items.get(position).getCheckBox()) {
            holder.cb.setChecked(false);
        }
        else {
            holder.cb.setVisibility(View.GONE);
        }


        return convertView;
    }

    public class ViewHolder {
        CheckBox cb;
        TextView name;
        TextView price;
    }
}
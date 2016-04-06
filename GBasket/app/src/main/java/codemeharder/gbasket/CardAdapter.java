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
import java.util.ArrayList;

/**
 * Created by seokyung on 4/5/16.
 */
public class CardAdapter extends ArrayAdapter {
    ArrayList<CreditCards> items = null;
    Context context;
    int resourceID;

    public CardAdapter(Context context,int layoutresourceID, ArrayList<CreditCards> resource) {
        super(context, R.layout.card_row, resource);
        this.resourceID = layoutresourceID;
        this.context = context;
        this.items = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = convertView;
        ViewHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            holder = new ViewHolder();
            convertView = inflater.inflate(resourceID, null);
            holder.name = (TextView) convertView.findViewById(R.id.Card);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (items.get(position).getCardnum() == null) {
            holder.name.setText("No Cards Added");
        }
        else {
            String cnum = items.get(position).getCardnum();
            holder.name.setText(cnum);
        }

        return convertView;
    }

    public class ViewHolder {
        TextView name;
    }



}

package codemeharder.gbasket;

import android.app.Activity;
import android.content.Context;
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
 * Created by Jimmy Chen on 3/1/2016.
 */

//This will be the page that populates the Shopping history

public class ReceiptAdapter extends ArrayAdapter{
    ArrayList<Receipt> receipts = null;
    Context context;
    int layoutResourceId;

    public ReceiptAdapter(Context context, int layoutResourceId, ArrayList<Receipt> resource) {
        super(context, layoutResourceId, resource);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.receipts = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new RViewHolder();
            holder.date = (TextView) row.findViewById(R.id.Date);
            holder.serial = (TextView) row.findViewById(R.id.Serial);
            row.setTag(holder);
        } else {
            holder = (RViewHolder) row.getTag();
        }

        holder.date.setText(receipts.get(position).getDate());
        holder.serial.setText(receipts.get(position).getSerial());

        return row;
    }

    static class RViewHolder {
        TextView date;
        TextView serial;
    }
}

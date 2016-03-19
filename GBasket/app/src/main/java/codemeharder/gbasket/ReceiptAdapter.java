package codemeharder.gbasket;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
        UserHolder holder = null;
        holder.textDate = (TextView) convertView.findViewById(R.id.Date);
        holder.textSerial = (TextView) convertView.findViewById(R.id.Serial);
        holder.btnOpen = (Button) convertView.findViewById(R.id.open);

        //TODO fill in the adapter to actually add stuff to the layout elements
        holder.btnOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Pull up receipt
            }
        });


        return convertView;
    }

    static class UserHolder {
        TextView textSerial;
        TextView textDate;
        Button btnOpen;
    }
}

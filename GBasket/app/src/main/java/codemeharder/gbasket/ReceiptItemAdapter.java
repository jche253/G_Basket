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
 * Created by Jimmy Chen on 3/17/2016.
 */
public class ReceiptItemAdapter extends ArrayAdapter<ReceiptItem> {
    Context context;

    ArrayList<ReceiptItem> data = new ArrayList<ReceiptItem>();

    public ReceiptItemAdapter(Context context, ArrayList<ReceiptItem> data) {
        super(context, R.layout.receiptitem_row, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.receiptitem_row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.Item);
        TextView price = (TextView) convertView.findViewById(R.id.Price);
        TextView origPrice = (TextView) convertView.findViewById(R.id.OrigPrice);
        TextView discount = (TextView) convertView.findViewById(R.id.DiscountPrice);
        name.setText(data.get(position).getItemName());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String priceStr = formatter.format(data.get(position).getItemPrice());
        price.setText(priceStr);
        if (data.get(position).getDiscount() != 0) {
            String origPriceStr = formatter.format(data.get(position).getItemOrigPrice());
            origPrice.setText("-Original Price: " + origPriceStr);
            String discountStr = formatter.format(data.get(position).getDiscount());
            discount.setText("-Discount: " + discountStr);
        }
        else {
            origPrice.setVisibility(View.GONE);
            discount.setVisibility(View.GONE);
        }

        return convertView;
    }
}

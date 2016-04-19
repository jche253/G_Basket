package codemeharder.gbasket;

/**
 * Created by jdevillasee on 3/1/16.
 */
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ToBuyCustomAdapter extends ArrayAdapter<ToBuyItem> {
    Context context;
    int layoutResourceId;
    ArrayList<ToBuyItem> data = new ArrayList<ToBuyItem>();
    GroceryListHelper glHelper;

    public ToBuyCustomAdapter(Context context, int layoutResourceId,
                              ArrayList<ToBuyItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        glHelper = new GroceryListHelper(context, null, null, 1);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserHolder();
            holder.textName = (TextView) row.findViewById(R.id.itemName);
            holder.btnDelete = (ImageButton) row.findViewById(R.id.delete);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        final ToBuyItem nextItem = data.get(position);
        holder.textName.setText(nextItem.getName());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Item Deleted",
                        Toast.LENGTH_LONG).show();
                glHelper.deleteItem(nextItem);
                data.remove(position);
                notifyDataSetChanged();
            }
        });


        return row;

    }

    static class UserHolder {
        TextView textName;
        ImageButton btnDelete;
    }
}



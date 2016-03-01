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
import android.widget.TextView;
import android.widget.Toast;

public class ToBuyCustomAdapter extends ArrayAdapter<ToBuyItem> {
    Context context;
    int layoutResourceId;
    ArrayList<ToBuyItem> data = new ArrayList<ToBuyItem>();

    public ToBuyCustomAdapter(Context context, int layoutResourceId,
                             ArrayList<ToBuyItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserHolder();
            holder.textName = (TextView) row.findViewById(R.id.itemName);
            holder.btnDelete = (Button) row.findViewById(R.id.delete);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
        ToBuyItem nextItem = data.get(position);
        holder.textName.setText(nextItem.getName());

        //TODO this code needs to be in the activity, not when you populate listview
        //TODO also need to add functionality
        /*holder.btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.i("Edit Button Clicked", "**********");
                Toast.makeText(context, "Edit button Clicked",
                        Toast.LENGTH_LONG).show();
            }
        });*/
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Need to delete the actual item
                Log.i("Delete Button Clicked", "**********");
                Toast.makeText(context, "Delete button Clicked",
                        Toast.LENGTH_LONG).show();
            }
        });

        return row;

    }

    static class UserHolder {
        TextView textName;
        TextView textAddress;
        TextView textLocation;
        Button btnEdit;
        Button btnDelete;
    }
}




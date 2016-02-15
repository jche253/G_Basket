package codemeharder.gbasket;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Jimmy Chen on 2/14/2016.
 */
public class YourBasketActivity extends Activity {
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yourbucket);

        lv = (ListView) findViewById(R.id.listView);


    }
}

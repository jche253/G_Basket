package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by seoyoung kyung on 2/24/16.
 */

public class SignUpActivity extends Activity {

    Button Done;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Done = (Button) findViewById(R.id.done);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                startActivity(intent);
            }
        });
    }

}

package codemeharder.gbasket;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;


/**
 * Created by jdevillasee on 3/22/16.
 */
public class SplashScreenActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splashscreen);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}

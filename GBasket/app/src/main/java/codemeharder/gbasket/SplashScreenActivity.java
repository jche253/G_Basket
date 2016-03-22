package codemeharder.gbasket;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jdevillasee on 3/22/16.
 */
public class SplashScreenActivity extends Activity{
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Your database code here
            }
        }, 2*60*500);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by jdevillasee on 3/22/16.
 */
public class SplashScreenActivity extends Activity{
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    LoginHelper dbHelper = new LoginHelper(this, null, null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (dbHelper.checkAccountCount()) {
                    Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }
                else {
                    Intent signUPIntent = new Intent(SplashScreenActivity.this, SignUpActivity.class);
                    SplashScreenActivity.this.startActivity(signUPIntent);
                    SplashScreenActivity.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
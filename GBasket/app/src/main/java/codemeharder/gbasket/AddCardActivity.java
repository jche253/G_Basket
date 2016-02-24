package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * Created by Jimmy Chen on 2/18/2016.
 * Edited by Seoyoung Kyung on 2/19/2016.
 */
public class AddCardActivity extends Activity {
    // Can be NO_NETWORK for OFFLINE, SANDBOX for TESTING and LIVE for PRODUCTION
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AWtrDxQQ7r2j2oH-XRe-KVEgluiWXLbPwvZFOQpky9vHD23FcwqRJGnXzR4MC1WdlMJW0QlgWa4hkZ_R";
    // when testing in sandbox, this is likely the -facilitator email address.
    private static final String CONFIG_RECEIVER_EMAIL = "jche253-facilitator@emory.edu";

    Button pay, cancel;


    //TODO Process credit card + add it to saved list of cards for the user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcard);

        pay = (Button) findViewById(R.id.btnSubmit);
        cancel = (Button) findViewById(R.id.btnCancel);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReceiptActivity.class);
                //TODO process payment
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(),PaymentActivity.class);
                startActivity(backIntent);
            }
        });
    }
}

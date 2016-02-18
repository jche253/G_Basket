package codemeharder.gbasket;

import android.app.Activity;
import android.os.Bundle;

import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * Created by Jimmy Chen on 2/18/2016.
 */
public class AddCardActivity extends Activity {
    // Can be NO_NETWORK for OFFLINE, SANDBOX for TESTING and LIVE for PRODUCTION
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AWtrDxQQ7r2j2oH-XRe-KVEgluiWXLbPwvZFOQpky9vHD23FcwqRJGnXzR4MC1WdlMJW0QlgWa4hkZ_R";
    // when testing in sandbox, this is likely the -facilitator email address.
    private static final String CONFIG_RECEIVER_EMAIL = "jche253-facilitator@emory.edu";

    //TODO add the card buttons and link (Sprint 2/3)
    //TODO Process credit card + add it to saved list of cards for the user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcard);

    }
}

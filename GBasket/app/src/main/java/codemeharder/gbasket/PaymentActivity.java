package codemeharder.gbasket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import android.widget.Toast;
/*
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
*/
import org.json.JSONException;

import java.math.BigDecimal;

/*
 * Created by Jimmy Chen on 2/18/2016.
 * Edited by Seoyoung Kyung on 2/19/2016.
 * Thanks to: https://trinitytuts.com/paypal-integration-in-android/
 */

public class PaymentActivity extends Activity {

    Button Paywcard, Addcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Paywcard = (Button) findViewById(R.id.ButtonPaywcard);
        Addcard = (Button) findViewById(R.id.ButtonAddcard);

        Paywcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String CreditCardNum = null;
                int expMonth = 0;
                int expYear = 0;
                String CVC = null;
                //TODO select card as item and initialize variables

                //TODO add payment
                //Parameters: string credit card number, int exp month, int exp year, string cvc
                Card card = new Card(CreditCardNum, expMonth, expYear, CVC);
                if (!card.validateCard()) {
                    //Errors
                    new AlertDialog.Builder(PaymentActivity.this)
                            .setTitle("Invalid Card")
                            .setMessage("Please enter a valid card")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO delete card from list
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
                else {
                    Stripe stripe = null;
                    try {
                        stripe = new Stripe("pk_test_2dYE7FzwvBwbxWNCdWtetXTp");
                    } catch (AuthenticationException e) {
                        e.printStackTrace();
                    }
                    assert stripe != null;
                    stripe.createToken(
                            card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    // Send token to your server

                                }

                                public void onError(Exception error) {
                                    // Show localized error message

                                }
                            });
                    Intent receiptIntent = new Intent(getApplicationContext(), ReceiptActivity.class);
                    startActivity(receiptIntent);
                }

            }
        });

        Addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getApplicationContext(), AddCardActivity.class);
                //TODO need to add return to add to listView
                startActivity(addIntent);
            }
        });
    }

}

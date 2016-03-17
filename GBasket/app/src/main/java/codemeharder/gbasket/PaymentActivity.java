package codemeharder.gbasket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
                //TODO select card as item and initialize variables
                String CreditCardNum = "4242424242424242";
                int expMonth = 7;
                int expYear = 2017;
                String CVC = "123";
                //Parameters: string credit card number, int exp month, int exp year, string cvc
                Card card = new Card(CreditCardNum, expMonth, expYear, CVC);
                //TODO Test receipt case
                // Receipt(Date CurDate, Card card, ArrayList<EachItem> yourItems, ArrayList<Double> DisOrigPrice,
                //ArrayList<Double> discounts, String serial)
                Date date = new Date();
                ArrayList<EachItem> items =  new ArrayList<EachItem>();
                ArrayList<Double> orig = new ArrayList<Double>();
                ArrayList<Double> discount = new ArrayList<Double>();
                //Test cases
                items.add(new EachItem("pizza", 3.44));
                items.add(new EachItem("burger", 2.00));
                items.add(new EachItem("olives", 1.00));
                items.add(new EachItem("steak", 12.90));
                items.add(new EachItem("fish", 8.76));


                //Simple serial for now
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date now = new Date();
                String toBarcode = sdf.format(now);

                Receipt todayReceipt = new Receipt(toBarcode, card, items, orig, discount, toBarcode);

                //TODO add payment
                //TODO commented this out for now so I could test receipt
                /*if (!card.validateCard()) {
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

                }*/
                /*else {
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
                }*/

                Intent receiptIntent = new Intent(getApplicationContext(), ReceiptActivity.class);
                receiptIntent.putExtra("Receipt", (Parcelable) todayReceipt);
                startActivity(receiptIntent);
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

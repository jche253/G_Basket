package codemeharder.gbasket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.stripe.android.model.Card;

/**
 * Created by Jimmy Chen on 2/18/2016.\
 */
public class AddCardActivity extends Activity {


    Button addCard, cancel;
    EditText name, card, eMonth, eYear, CV;
    CardHelper cardDB = new CardHelper(this, null, null, 1);

    //TODO Process credit card + add it to saved list of cards for the user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcard);

        addCard = (Button) findViewById(R.id.btnSubmit);
        cancel = (Button) findViewById(R.id.btnCancel);
        name = (EditText) findViewById(R.id.name);
        card = (EditText) findViewById(R.id.ccNum);
        eMonth = (EditText) findViewById(R.id.expMonth);
        eYear = (EditText) findViewById(R.id.expYear);
        CV = (EditText) findViewById(R.id.CVC);

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int ExpireMonth = 0;
                try {
                    ExpireMonth = Integer.parseInt(eMonth.getText().toString());
                } catch (NumberFormatException e) {
                    ExpireMonth = 0;
                }

                int ExpireYear = 0;
                try {
                    ExpireYear = Integer.parseInt(eYear.getText().toString());
                } catch (NumberFormatException e) {
                    ExpireYear = 0;
                }

                String CVV = CV.getText().toString();
                String UserName = name.getText().toString();
                String CreditCardNum = card.getText().toString();

                if (UserName == null || CreditCardNum == null || !(CreditCardNum.matches("^-?\\d+$"))
                        || CreditCardNum.length() < 15 || CVV == null || !(CVV.matches("^-?\\d+$"))
                        || ExpireMonth <= 0 || ExpireYear <= 0) {

                    new AlertDialog.Builder(AddCardActivity.this)
                            .setTitle("Invalid Card")
                            .setMessage("Please enter a valid card")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //Reset form
                                    name.setText("");
                                    card.setText("");
                                    eMonth.setText("");
                                    eYear.setText("");
                                    CV.setText("");
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {
                    //Card is good, card can be added to the database

                    String exist = cardDB.findCard(card.getText().toString());

                    if (exist != null) {
                        Toast.makeText(AddCardActivity.this, "This card is in use. Please use a different card.", Toast.LENGTH_LONG).show();
                    }

                    if (exist == null) {
                        boolean added = cardDB.addCard(card.getText().toString(), eMonth.getText().toString(), eYear.getText().toString(), CV.getText().toString());
                        if (added) {
                            Toast.makeText(AddCardActivity.this, "Card has been added", Toast.LENGTH_LONG).show();
                            //Card is good, can move on to Payment form
                            Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);

                            //Todo add credit card to listView
                            startActivity(intent);
                        } else if (!added) {
                            Toast.makeText(AddCardActivity.this, "Please enter a valid card.", Toast.LENGTH_LONG).show();
                        }
                    }

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(getApplicationContext(),PaymentActivity.class);
                //Just return to payment without doing anything
                startActivity(backIntent);
            }
        });
    }

}

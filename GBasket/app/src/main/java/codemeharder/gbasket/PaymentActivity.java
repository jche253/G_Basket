package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.stripe.android.model.Card;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import android.widget.Toast;
/*
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;
*/

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
 * Created by Jimmy Chen on 2/18/2016.
 * Edited by Seoyoung Kyung on 2/19/2016.
 */

public class PaymentActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button Paywcard, Addcard;
    ListView CardList;

    private static final String TAG = ReceiptActivity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestLocationUpdates = false;

    private LocationRequest mLocationRequest;

    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;

    double loc_Tax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        CardHelper carddb = new CardHelper(this, null, null, 1);
        ArrayList<CreditCards> items = carddb.getAllcards();

        CardList = (ListView) findViewById(R.id.CardListView);
        Paywcard = (Button) findViewById(R.id.ButtonPaywcard);
        Addcard = (Button) findViewById(R.id.ButtonAddcard);

        if(items.size()>0) // check if list contains items.
        {

            CardAdapter adapter = new CardAdapter(this, R.layout.card_row, items);
            CardList.setAdapter(adapter);

        }


        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        Paywcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO select card as item and initialize variables
               // Toast.makeText(getApplicationContext(), loc_Tax + " ", Toast.LENGTH_LONG).show();
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
                orig.add(3.44);
                discount.add(0.00);
                items.add(new EachItem("pizza", setDiscountPrice(orig.get(0), discount.get(0)), false));

                orig.add(2.00);
                discount.add(1.00);
                items.add(new EachItem("burger", setDiscountPrice(orig.get(1), discount.get(1)), false));

                orig.add(1.00);
                discount.add(0.00);
                items.add(new EachItem("olives", setDiscountPrice(orig.get(2), discount.get(2)), false));

                orig.add(12.90);
                discount.add(3.00);
                items.add(new EachItem("steak", setDiscountPrice(orig.get(3), discount.get(3)), false));

                orig.add(8.76);
                discount.add(1.00);
                items.add(new EachItem("fish",  setDiscountPrice(orig.get(4), discount.get(4)), false));

                //Simple serial for now
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date now = new Date();
                String toBarcode = sdf.format(now);
                final Receipt todayReceipt = new Receipt(toBarcode, card, items, orig, discount, loc_Tax, toBarcode);

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
                Intent receiptIntent = new Intent(getApplicationContext(), ReceiptActivity.class);
                Bundle b = new Bundle();
                b.putParcelable("Receipt", todayReceipt);
                receiptIntent.putExtra("bundle", b);
                startActivity(receiptIntent);
            }
        });

        Addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(getApplicationContext(), AddCardActivity.class);
                //TODO need to add return to add to listView inSQLite
                startActivity(addIntent);
            }
        });
    }

    public double setDiscountPrice(Double Orig, Double Disc) {
        return Orig - Disc;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        if (mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private double displayLocation() {
        String state = "";
        double tax = 0;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            //lblLocation.setText(latitude + ", " + longitude);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null) {
                    state = addresses.get(0).getAdminArea();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!state.isEmpty()) {
                switch (state) {
                    case "Georgia":
                        tax = 6.96;
                    case "Alabama":
                        tax = 8.91;
                    case "Florida":
                        tax = 6.65;
                    case "South Carolina":
                        tax = 7.18;
                    case "MA":
                        tax = 6.25;
                    case "Rhode Island":
                        tax = 7;
                    case "Connecticut":
                        tax = 6.35;
                    case "New Jersey":
                        tax = 6.97;
                    case "Delaware":
                        tax = 0;
                    case "Maryland":
                        tax = 6;
                    case "District of Columbia":
                        tax = 5.75;
                    case "Vermont":
                        tax = 6.14;
                    case "New Hampshire":
                        tax = 0;
                    case "Mississippi":
                        tax = 7.07;
                    case "Louisiana":
                        tax = 8.91;
                    case "Texas":
                        tax = 8.05;
                    case "New Mexico":
                        tax = 7.35;
                    case "Arizona":
                        tax = 8.17;
                    case "California":
                        tax = 8.44;
                    case "Nevada":
                        tax = 7.94;
                    case "Utah":
                        tax = 6.68;
                    case "Colorado":
                        tax = 7.44;
                    case "Kansas":
                        tax = 8.2;
                    case "Oklahoma":
                        tax = 8.77;
                    case "Arkansas":
                        tax = 9.26;
                    case "Tennessee":
                        tax = 9.45;
                    case "North Carolina":
                        tax = 6.9;
                    case "Virginia":
                        tax = 5.63;
                    case "West Virginia":
                        tax = 6.07;
                    case "Kentucky":
                        tax = 6;
                    case "Missouri":
                        tax = 7.81;
                    case "Nebraska":
                        tax = 6.8;
                    case "Wyoming":
                        tax = 5.47;
                    case "Idaho":
                        tax = 6.01;
                    case "Oregon":
                        tax = 0;
                    case "Washington":
                        tax = 8.89;
                    case "Montana":
                        tax = 0;
                    case "South Dakota":
                        tax = 5.83;
                    case "Iowa":
                        tax = 6.78;
                    case "Illinois":
                        tax = 8.19;
                    case "Indiana":
                        tax = 7;
                    case "Ohio":
                        tax = 7.1;
                    case "Pennsylvania":
                        tax = 6.34;
                    case "New York":
                        tax = 8.48;
                    case "Maine":
                        tax = 5.5;
                    case "Michigan":
                        tax = 6;
                    case "Wisconsin":
                        tax = 5.43;
                    case "Minnesota":
                        tax = 7.2;
                    case "North Dakota":
                        tax = 6.56;
                }
                loc_Tax = tax;
            }
        }
        //Toast.makeText(this, loc_Tax + " ", Toast.LENGTH_LONG).show();
        return loc_Tax;
    }

    private void togglePeriodLocationUpdates() {
        if (!mRequestLocationUpdates) {
            //btnStartLocationUpdates.setText(getString(R.string.btn_stop_location_updates));
            mRequestLocationUpdates = true;

            startLocationUpdates();
        } else {
            //btnStartLocationUpdates.setText(getString(R.string.btn_start_location_updates));
            mRequestLocationUpdates = false;

            stopLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        loc_Tax = displayLocation();

        if (mRequestLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        displayLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onBackPressed() {
    }
}


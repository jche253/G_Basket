package codemeharder.gbasket;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.stripe.android.model.Token;

import android.widget.TextView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * Created by Jimmy Chen on 2/18/2016.
 * Edited by Seoyoung Kyung on 2/19/2016.
 */

public class PaymentActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    Button Paywcard, Addcard, remove;
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
    private final static String STRIPE_KEY = "pk_test_sYYQzY9tF9dsFoQ9ZgoF3VyW";

    ArrayList<CreditCards> ids = new ArrayList<CreditCards>();
    double loc_Tax = 0;

    CardHelper carddb = new CardHelper(this, null, null, 1);
    BasketHelper basketHelper = new BasketHelper(this);
    ArrayList<CreditCards> items;
    ArrayList<EachItemID> items2;
    CardAdapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent i = getIntent();
        Bundle bundle = i.getBundleExtra("bundle");
        items2 = bundle.getParcelableArrayList("items2");


        CardList = (ListView) findViewById(R.id.CardListView);
        Paywcard = (Button) findViewById(R.id.ButtonPaywcard);
        Addcard = (Button) findViewById(R.id.ButtonAddcard);
        remove = (Button) findViewById(R.id.removeCard);

        items = carddb.getAllcards();
        adapter = new CardAdapter1(this, R.layout.card_row, items);
        CardList.setAdapter(adapter);


        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ids.size() > 0) {
                    for (int i = 0; i < ids.size(); i++) {
                        //Fixed primary key
                        carddb.deleteRow(ids.get(i).getRealCardNum());
                        items.remove(ids.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        Paywcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CardList.getAdapter().getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select a card", Toast.LENGTH_LONG).show();
                    return;
                }

                String CreditCardNum = ids.get(0).realCardNum;
                Card card = carddb.findCardStripe(CreditCardNum);

                //Arrays to add to receipt object
                ArrayList<EachItem> items =  new ArrayList<EachItem>();
                ArrayList<Double> orig = new ArrayList<Double>();
                ArrayList<Double> discount = new ArrayList<Double>();

                for (int i = 0; i < items2.size(); i++) {
                    items.add(new EachItem(items2.get(i).getName(), items2.get(i).getPrice(), false));
                    orig.add(items2.get(i).getPrice());
                    //Discount will be omitted for the purposes of this store, but the database has an option to add this
                    discount.add(0.00);
                }
                //Example of a discount
                /*orig.add(3.44);
                discount.add(1.00);
                items.add(new EachItem("pizza", setDiscountPrice(orig.get(0), discount.get(0)), false));*/

                //Simple serial for now
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String toBarcode = sdf.format(now);
                final Receipt todayReceipt = new Receipt(toBarcode, card, items, orig, discount, loc_Tax, toBarcode);

                Double doublechargeamt = (todayReceipt.getTotal() * 100);
                final int chargeAmt = doublechargeamt.intValue();
                Toast.makeText(getApplicationContext(), "Your card has been charged: $" + todayReceipt.getTotal(), Toast.LENGTH_LONG).show();

                Stripe stripe = new Stripe();

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

                else if (card.validateCard()) {
                    new Stripe().createToken(
                            card,
                            STRIPE_KEY,
                            new TokenCallback() {

                                @Override
                                public void onSuccess(final Token token) {
                                    basketHelper.deletedb();
                                    // getTokenList().addToList(token);
                                    //Log.e("Token Json", "27th March::-" + token);
                                    final Map<String, Object> chargeParams = new HashMap<String, Object>();
                                    chargeParams.put("amount", chargeAmt);
                                    chargeParams.put("currency", "usd");
                                    chargeParams.put("card", token.getId());
                                    // chargeParams.put("captured", false);
                                    com.stripe.Stripe.apiKey = "sk_test_i9V6baInFfFuKOMq8JYjaA2i";
                                    // Charge charge = Charge.create(chargeParams);
                                    // Charge ch = Charge.retrieve(charge.getId());
                                    // // Used it here for demonstration
                                    // ch.capture();
                                    // Charge.create(chargeParams);

                                    new Thread() {
                                        public void run() {
                                            TokenAsyncTask post = new TokenAsyncTask();
                                            post.execute(chargeParams);
                                        }
                                    }.start();

                                }

                                public void onError(Exception error) {
                                    Toast.makeText(getApplicationContext(), error + "", Toast.LENGTH_LONG).show();
                                }
                            });

                }

                //TODO add payment for charging card
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

    class CardAdapter1 extends ArrayAdapter {
        ArrayList<CreditCards> items = null;
        Context context;
        int resourceID;
        int selected_pos=-1;
        int count = 0;

        public CardAdapter1(Context context,int layoutresourceID, ArrayList<CreditCards> resource) {
            super(context, R.layout.card_row, resource);
            this.resourceID = layoutresourceID;
            this.context = context;
            this.items = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View row = convertView;
            ViewHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();

                holder = new ViewHolder();
                convertView = inflater.inflate(resourceID, null);
                holder.name = (TextView) convertView.findViewById(R.id.Card);
                holder.select = (CheckBox) convertView.findViewById(R.id.checkBox);
                convertView.setTag(holder);

                holder.select.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (((CheckBox) view).isChecked())
                        {
                            ids.add(items.get(position));
                            selected_pos= position;
                            count++;
                        }
                        else
                        {
                            if (ids.contains(items.get(position))) {
                                ids.remove(items.get(position));
                            }
                            selected_pos=-1;
                            count--;
                        }

                        notifyDataSetChanged();

                    }
                });

            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(selected_pos==position)
            {
                holder.select.setChecked(true);
            }
            else
            {
                holder.select.setChecked(false);
            }
            String cnum = items.get(position).getCardnum();
            holder.name.setText(cnum);
            holder.select.setTag(items.get(position));

            return convertView;
        }

        public double getCountChecked() {
            return this.count;
        }

        public class ViewHolder {
            TextView name;
            CheckBox select;
        }
    }
}


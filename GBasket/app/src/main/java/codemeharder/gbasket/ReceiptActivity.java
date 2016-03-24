package codemeharder.gbasket;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Jimmy Chen on 2/20/2016.
 */
public class ReceiptActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ImageView genBarcode;
    Bitmap bitmap;
    Button save, history;
    ListView itemList, talist;
    ArrayList<ReceiptItem> items = new ArrayList<ReceiptItem>();
    TextView DateTime, SerialNum, AccountType;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private static final String TAG = MainActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_receipt);
        Intent intent = getIntent();
        Receipt receipt = (Receipt) intent.getParcelableExtra("Receipt");

        history = (Button) findViewById(R.id.history);
        genBarcode = (ImageView) findViewById(R.id.barcode);
        DateTime = (TextView) findViewById(R.id.dateTimeInput);
        SerialNum = (TextView) findViewById(R.id.serialInput);
        AccountType = (TextView) findViewById(R.id.accountName);

        DateTime.setText(receipt.getDate());
        SerialNum.setText(receipt.getSerial());
        AccountType.setText(receipt.accType);

        itemList = (ListView) findViewById(R.id.listView3);
        itemList.setFocusable(false);

        talist = (ListView) findViewById(R.id.listView4);
        talist.setFocusable(false);

        if(checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }


        Toast.makeText(getApplicationContext(), "" + loc_Tax, Toast.LENGTH_SHORT).show();
        receipt.setTax(loc_Tax);

        //Convert receipt into receipt item
        for (int i = 0; i < receipt.getItemPrice().size(); i++) {
            EachItem temp = receipt.getItemPrice().get(i);
            ReceiptItem test = new ReceiptItem(temp.getName(), temp.getPrice(), receipt.getOrigPrice().get(i),
                    receipt.getPriceOff().get(i));
            items.add(test);
        }

        ReceiptItemAdapter adapter = new ReceiptItemAdapter(this, items);
        itemList.setAdapter(adapter);
        setListViewHeightBasedOnItems(itemList);


        //Get totals
        EachItem[] paymentArr = new EachItem[3];
        paymentArr[0] = new EachItem("Total: ", receipt.getPaymentAmount(), false);
        paymentArr[1] = new EachItem("Tax: ", receipt.getTax(), false);
        paymentArr[2] = new EachItem("Balance Due: ", receipt.getTotal(), false);

        CustomAdapter sumAdapter = new CustomAdapter(this, paymentArr);
        talist.setAdapter(sumAdapter);
        setListViewHeightBasedOnItems(talist);


        try {
            bitmap = encodeAsBitmap(receipt.getSerial(), BarcodeFormat.CODE_128, 600, 300);
            genBarcode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(historyIntent);
            }
        });
    }

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        if(mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private double displayLocation() {
        String state="";
        double tax=0;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            new AlertDialog.Builder(this)
                    .setTitle("Data")
                    .setMessage("Latitude: " + latitude + "\nLongitude: " +longitude);

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
                    case "Georgia": tax=6.96;
                    case "Alabama": tax=8.91;
                    case "Florida": tax=6.65;
                    case "South Carolina": tax=7.18;
                    case "MA": tax=6.25;
                    case "Rhode Island": tax=7;
                    case "Connecticut": tax=6.35;
                    case "New Jersey": tax=6.97;
                    case "Delaware": tax=0;
                    case "Maryland": tax=6;
                    case "District of Columbia": tax=5.75;
                    case "Vermont": tax=6.14;
                    case "New Hampshire": tax=0;
                    case "Mississippi": tax=7.07;
                    case "Louisiana": tax=8.91;
                    case "Texas": tax=8.05;
                    case "New Mexico": tax=7.35;
                    case "Arizona": tax=8.17;
                    case "California": tax=8.44;
                    case "Nevada": tax=7.94;
                    case "Utah": tax=6.68;
                    case "Colorado": tax=7.44;
                    case "Kansas": tax=8.2;
                    case "Oklahoma": tax=8.77;
                    case "Arkansas": tax=9.26;
                    case "Tennessee": tax=9.45;
                    case "North Carolina": tax=6.9;
                    case "Virginia": tax=5.63;
                    case "West Virginia": tax=6.07;
                    case "Kentucky": tax=6;
                    case "Missouri": tax=7.81;
                    case "Nebraska": tax=6.8;
                    case "Wyoming": tax=5.47;
                    case "Idaho": tax=6.01;
                    case "Oregon": tax=0;
                    case "Washington": tax=8.89;
                    case "Montana": tax=0;
                    case "South Dakota": tax=5.83;
                    case "Iowa": tax=6.78;
                    case "Illinois": tax=8.19;
                    case "Indiana": tax=7;
                    case "Ohio": tax=7.1;
                    case "Pennsylvania": tax=6.34;
                    case "New York": tax=8.48;
                    case "Maine": tax=5.5;
                    case "Michigan": tax=6;
                    case "Wisconsin": tax=5.43;
                    case "Minnesota": tax=7.2;
                    case "North Dakota": tax=6.56;
                }
                return tax;
            }
        }
        return 0;
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
        if(resultCode != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
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


    @Override
    public void onConnected(Bundle bundle) {
        loc_Tax = displayLocation();

        startLocationUpdates();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        loc_Tax = displayLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }
}

package codemeharder.gbasket;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jimmychen.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;


/**
 * Created by Jimmy Chen on 2/16/2016.
 */
public class ItemActivity extends Activity implements AsyncResponse, AsyncResponse2 {
    Button backBtn, addBtn;
    TextView name, price;
    ImageView img;
    BasketHelper basketHelper = new BasketHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        backBtn = (Button) findViewById(R.id.backBtn);
        addBtn = (Button) findViewById(R.id.addBtn);
        name = (TextView) findViewById(R.id.itemName_TextView);
        price = (TextView) findViewById(R.id.price_TextView);
        img = (ImageView) findViewById(R.id.imageView2);

        Intent i = getIntent();
        String format = i.getStringExtra("format");
        String content = i.getStringExtra("content");

        EndpointsAsyncTaskPQuery test = new EndpointsAsyncTaskPQuery();
        test.delegate = this;
        test.execute(new Triplet(getApplicationContext(), format, content));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(getApplicationContext(), LaunchPadActivity.class);
                startActivity(intentScan);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Translate the format/content through database query for the item
                String price2 = price.getText().toString();
                if (price2.equals("") || price2.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Error: Please rescan item", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    double price3 = Double.parseDouble(price2);
                    String Pname = name.getText().toString();
                    int ID = new GenerateRandomID().getID();
                    boolean isInserted = basketHelper.insertData(ID, Pname, price3);
                    if (isInserted) {
                        Toast.makeText(ItemActivity.this, Pname + " added.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), YourBasketActivity.class);
                        intent.putExtra("name", Pname);
                        intent.putExtra("price", price3);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ItemActivity.this, "Item was not successfully added.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void processFinish(String output) {
        //RULE: String must be price+name (with no other plus signs in name)
        //Toast.makeText(getApplicationContext(),output, Toast.LENGTH_LONG).show();
        String[] tokens = output.split("\\+");
        //Toast.makeText(getApplicationContext(), tokens[0] + " " + tokens[1], Toast.LENGTH_LONG).show();
        price.setText(tokens[0]);
        name.setText(tokens[1]);
        String editName = tokens[1].replace(" ", "%20");
        String urlString = "https://storage.googleapis.com/testing-1261.appspot.com/Sample%20Store/" + editName
                + ".jpg";
        //Toast.makeText(getApplicationContext(), urlString, Toast.LENGTH_LONG).show();
        try {
            URL url = new URL(urlString);
            URLimageAsyncTask urlTask = new URLimageAsyncTask();
            urlTask.del = this;
            urlTask.execute(url);
        } catch (MalformedURLException e) {
            Toast.makeText(this, "could not get image at " + urlString, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void processImage (String result) {
        Bitmap image = StringToBitMap(result);
        img.setImageBitmap(image);
        scaleImage(img);
    }

    class URLimageAsyncTask extends AsyncTask<URL, Void, String> {
        public AsyncResponse2 del = null;
        @Override
        protected String doInBackground(URL... params) {
            URL url1 = params[0];
            Bitmap image = null;
            try {
                image = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                return BitMapToString(image);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            del.processImage(result);
        }
    }

    class EndpointsAsyncTaskPQuery extends AsyncTask<Triplet, Void, String> {
        private MyApi myApiService = null;
        private Context context;
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(Triplet... params) {
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                        .setRootUrl("https://testing-1261.appspot.com/_ah/api");
                // end options for devappserver

                myApiService = builder.build();
            }

            context = params[0].getContext();
            String format = params[0].getformat();
            String content = params[0].getcontent();


            try {
                return myApiService.productQuery(format, content).execute().getDataPQuery();
            } catch (IOException e) {
                return e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }
    }

    private void scaleImage(ImageView view) throws NoSuchElementException {
        // Get bitmap from the the ImageView.
        Bitmap bitmap = null;

        try {
            Drawable drawing = view.getDrawable();
            bitmap = ((BitmapDrawable) drawing).getBitmap();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("No drawable on given view");
        } catch (ClassCastException e) {
            // Check bitmap is Ion drawable
        }

        // Get current dimensions AND the desired bounding box
        int width = 0;

        try {
            width = bitmap.getWidth();
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Can't find bitmap on given view/drawable");
        }

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);


        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);

    }

    private int dpToPx(int dp) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}

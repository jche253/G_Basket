package codemeharder.gbasket;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.example.jimmychen.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by Jimmy Chen on 3/24/2016.
 */
public class EndpointsAsyncTask extends AsyncTask<Triplet, Void, String> {
    private static MyApi myApiService = null;
    private Context context;

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
        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
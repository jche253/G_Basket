package codemeharder.gbasket;

/**
 * Created by seokyung on 4/20/16.
 */
import android.os.AsyncTask;
import com.stripe.model.Charge;
import java.util.Map;

class TokenAsyncTask extends AsyncTask<Map<String, Object>, Void, Void> {

    Charge charge;

    @Override
    protected Void doInBackground(
            Map<String, Object>... params) {
        Map<String, Object> map = params[0];
        try {
            com.stripe.Stripe.apiKey = "sk_test_i9V6baInFfFuKOMq8JYjaA2i";
            charge = Charge
                    .create(map);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // showAlert("Exception while charging the card!",
            // e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
package codemeharder.gbasket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button login;
    TextView forgot;
    EditText inputEmail;
    EditText inputPassword;
    LoginHelper dbHelper = new LoginHelper(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.email_sign_in_button);
        forgot = (TextView) findViewById(R.id.forgotTextView);

        //TODO If you ever forget your test login/pass, just uncomment this toast
        Toast.makeText(MainActivity.this, dbHelper.seeUser(), Toast.LENGTH_LONG).show();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = dbHelper.findAcc(inputEmail.getText().toString(), inputPassword.getText().toString());
                if (name != null) {
                    Toast.makeText(MainActivity.this, "Welcome " + name, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LaunchPadActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "Please enter a valid email and password", Toast.LENGTH_LONG).show();
                    inputEmail.setText("");
                    inputPassword.setText("");
                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}

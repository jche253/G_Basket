package codemeharder.gbasket;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotActivity extends AppCompatActivity {
    EditText forgotfname, forgotlname, forgotemail;
    Button recover;
    LoginHelper dbHelper = new LoginHelper(this, null, null, 1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        forgotfname = (EditText) findViewById(R.id.forgotfnameEdit);
        forgotlname = (EditText) findViewById(R.id.forgotlnameEdit);
        forgotemail = (EditText) findViewById(R.id.forgotemailEdit);
        recover = (Button) findViewById(R.id.Recover);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recover = dbHelper.recoverPass(forgotfname.getText().toString(), forgotlname.getText().toString(),
                        forgotemail.getText().toString());

                if (recover != null)
                    Toast.makeText(ForgotActivity.this, "Your password is: " + recover, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ForgotActivity.this, "Incorrect entry", Toast.LENGTH_LONG).show();
            }
        });
    }

}

package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AccountActivity extends Activity {
    TextView fname, lname, email;
    EditText oldpass, newpass, confirmpass;
    Button change, newpb;
    LoginHelper loginHelper;
    ArrayList<String> userData = new ArrayList<>();
    public static final String SPECIAL_CHARACTERS = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";
    public static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        loginHelper = new LoginHelper(this, null, null, 1);
        userData = loginHelper.getAllData();

        fname = (TextView) findViewById(R.id.FName);
        lname = (TextView) findViewById(R.id.LName);
        email = (TextView) findViewById(R.id.EMail);
        oldpass = (EditText) findViewById(R.id.oldPassEntry);
        newpass = (EditText) findViewById(R.id.newPassEntry);
        confirmpass = (EditText) findViewById(R.id.confirmEntry);
        change = (Button) findViewById(R.id.changepass);

        fname.setText(userData.get(1));
        lname.setText(userData.get(2));
        email.setText(userData.get(0));

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldP = oldpass.getText().toString();
                String newP = newpass.getText().toString();
                String conP = confirmpass.getText().toString();
                if (!oldP.equals(userData.get(3))) {
                    Toast.makeText(AccountActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                } else if (!newP.equals(conP)) {
                    Toast.makeText(AccountActivity.this, "Please enter matching passwords", Toast.LENGTH_LONG).show();
                } else if (oldP.equals(newP)) {
                    Toast.makeText(AccountActivity.this, "Please enter a new password", Toast.LENGTH_LONG).show();
                } else if (newP.length() < 8 || !isAcceptablePassword(newP)) {
                    Toast.makeText(AccountActivity.this, "Please type a password that is at least 8 characters" +
                                    " long and contains at least 1 Upper/Lowercase letter, a symbol, and a number",
                            Toast.LENGTH_LONG).show();
                } else {
                    loginHelper.updatePassword(userData.get(0), userData.get(3), conP);
                    Toast.makeText(AccountActivity.this, "Successfully changed password", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), LaunchPadActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public static boolean isAcceptablePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            System.out.println("empty string.");
            return false;
        }
        password = password.trim();
        int len = password.length();
        if(len < MIN_PASSWORD_LENGTH ) {
            System.out.println("Please make a password 8 characters or longer");
            return false;
        }
        char[] aC = password.toCharArray();
        boolean isUpper = false;
        boolean isLower = false;
        boolean isSpecial = false;
        boolean isDigit = false;
        for(char c : aC) {
            if (Character.isUpperCase(c)) {
                isUpper = true;
            } else
            if (Character.isLowerCase(c)) {
                isLower = true;
            } else
            if (Character.isDigit(c)) {
                isDigit = true;
            } else
            if (SPECIAL_CHARACTERS.indexOf(String.valueOf(c)) >= 0) {
                isSpecial = true;
            }

        }
        if (isUpper == true && isLower == true && isSpecial == true && isDigit == true) {
            return true;
        }
        else {
            return false;
        }
    }
}

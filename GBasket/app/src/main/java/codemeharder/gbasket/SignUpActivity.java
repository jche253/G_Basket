package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by seoyoung kyung on 2/24/16.
 * Edited by Jimmy Chen 4/4/16.
 */

public class SignUpActivity extends Activity {
    LoginHelper Acc_Info;
    Button Done;
    EditText editemail, editfname, editlname, editpass, confirmpass;

    public static final String SPECIAL_CHARACTERS = "!@#$%^&*()~`-=_+[]{}|:\";',./<>?";
    public static final int MIN_PASSWORD_LENGTH = 8;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Acc_Info = new LoginHelper(this, null, null, 1);
        
        editemail = (EditText) findViewById(R.id.User_ID);
        editfname = (EditText) findViewById(R.id.User_FName);
        editlname = (EditText) findViewById(R.id.User_LName);
        editpass = (EditText) findViewById(R.id.User_Pass);
        confirmpass = (EditText) findViewById(R.id.user_cpw);
        Done = (Button) findViewById(R.id.Done);

        Done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = editemail.getText().toString();
                String fname = editfname.getText().toString();
                String lname = editlname.getText().toString();
                String pass1 = editpass.getText().toString();
                String pass2 = confirmpass.getText().toString();
                if (email.matches("") || fname.matches("") || lname.matches("")) {
                    Toast.makeText(SignUpActivity.this, "Please fill in your first and last name and email", Toast.LENGTH_LONG).show();
                }
                else if (pass1.length() < 6 || !isAcceptablePassword(pass1)) {
                    Toast.makeText(SignUpActivity.this, "Please type a password that is at least 8 characters" +
                            " long and contains at least 1 Upper/Lowercase letter, a symbol, and a number",
                            Toast.LENGTH_LONG).show();
                }
                //Not matching passwords
                else if (!pass1.equals(pass2)) {
                    Toast.makeText(SignUpActivity.this, "Please type matching passwords", Toast.LENGTH_LONG).show();

                    //Clear text
                    confirmpass.setText("");
                    editpass.setText("");
                }

                else {
                    //Create account
                    boolean isInserted = Acc_Info.insertData(email, fname, lname, pass1);
                    if (isInserted == true)
                        Toast.makeText(SignUpActivity.this, "Account was successfully created.", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(SignUpActivity.this, "Account was not successfully created.", Toast.LENGTH_LONG).show();
                    if (isInserted == true) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
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


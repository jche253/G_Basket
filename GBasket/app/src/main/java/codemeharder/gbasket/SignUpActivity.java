package codemeharder.gbasket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
<<<<<<< HEAD
import com.facebook.FacebookSdk;

=======
import android.widget.EditText;
import android.widget.Toast;
>>>>>>> master

/**
 * Created by seoyoung kyung on 2/24/16.
 */

public class SignUpActivity extends Activity {
<<<<<<< HEAD


=======
    DatabaseHelper Acc_Info;
>>>>>>> master
    Button Done;
    EditText editemail, editfname, editlname, editpass;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
<<<<<<< HEAD
        FacebookSdk.sdkInitialize(getApplicationContext());
=======
        Acc_Info = new DatabaseHelper(this);
>>>>>>> master

        editemail = (EditText) findViewById(R.id.User_ID);
        editfname = (EditText) findViewById(R.id.User_FName);
        editlname = (EditText) findViewById(R.id.User_LName);
        editpass = (EditText) findViewById(R.id.User_Pass);
        Done = (Button) findViewById(R.id.Done);
        AddData();


        /*Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                startActivity(intent);
            }
        }
        );*/
    }

    public void AddData(){
        Done.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        boolean isInserted = Acc_Info.insertData(editemail.getText().toString(),
                                editfname.getText().toString(),
                                editlname.getText().toString(),
                                editpass.getText().toString());
                        if(isInserted == true)
                            Toast.makeText(SignUpActivity.this, "Account was successfully created.", Toast.LENGTH_LONG).show();
                        else Toast.makeText(SignUpActivity.this, "Account was not successfully created.", Toast.LENGTH_LONG).show();
                        if(isInserted == true){
                            Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                            startActivity(intent);
                        }
                    }
                }
        );


    }

}

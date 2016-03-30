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

import java.text.DecimalFormat;

public class UnitPriceCalc extends AppCompatActivity {

    Button calculate;
    EditText item1, item2, unit1, unit2, cost1, cost2;
    TextView results1, results2;
    int i1    = 0;
    int i2    = 0;
    double u1 = 0;
    double u2 = 0;
    double c1 = 0;
    double c2 = 0;
    double totalUnits1 = 0;
    double totalUnits2 = 0;
    double cpu1        = 0;
    double cpu2        = 0;

    private static DecimalFormat df2 = new DecimalFormat("#.##");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_price_calc);
        calculate = (Button) findViewById(R.id.button);
        item1     = (EditText) findViewById(R.id.item1);
        item2     = (EditText) findViewById(R.id.item2);
        unit1     = (EditText) findViewById(R.id.unit1);
        unit2     = (EditText) findViewById(R.id.unit2);
        cost1     = (EditText) findViewById(R.id.cost1);
        cost2     = (EditText) findViewById(R.id.cost2);
        results1  = (TextView) findViewById(R.id.results1);
        results2  = (TextView) findViewById(R.id.results2);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    i1    = Integer.parseInt(item1.getText().toString());
                    i2    = Integer.parseInt(item2.getText().toString());
                    u1 = Double.parseDouble(unit1.getText().toString());
                    u2 = Double.parseDouble(unit2.getText().toString());
                    c1 = Double.parseDouble(cost1.getText().toString());
                    c2 = Double.parseDouble(cost2.getText().toString());
                    totalUnits1 = i1*u1;
                    totalUnits2 = i2*u2;
                    cpu1        = c1/totalUnits1;
                    cpu2        = c2/totalUnits2;
                    results1.setText("Total Units:"+totalUnits1+"   Cost Per Unit:$"+df2.format(cpu1));
                    results2.setText("Total Units:"+totalUnits2+"   Cost Per Unit:$"+df2.format(cpu2));
                }catch(NumberFormatException numberEx){
                    Toast toast = new Toast(getApplicationContext());
                    toast.makeText(UnitPriceCalc.this, "Please input valid information.", toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

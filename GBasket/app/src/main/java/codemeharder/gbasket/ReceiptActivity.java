package codemeharder.gbasket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Jimmy Chen on 2/20/2016.
 */
public class ReceiptActivity extends Activity  {
    ImageView genBarcode;
    Bitmap bitmap;
    Button save, history;
    ListView itemList, talist;
    ArrayList<ReceiptItem> items = new ArrayList<ReceiptItem>();
    TextView DateTime, SerialNum, AccountType;
    String toWrite;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        final Receipt receipt = bundle.getParcelable("Receipt");

        history = (Button) findViewById(R.id.history);
        save = (Button) findViewById(R.id.save);
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
        ArrayList<EachItem> paymentArr = new ArrayList<EachItem>();
        paymentArr.add(new EachItem("Total: ", receipt.getPaymentAmount(), false));
        paymentArr.add(new EachItem("Tax: ", receipt.getTax()/100 * receipt.getPaymentAmount(), false));
        paymentArr.add(new EachItem("Balance Due: ", receipt.getTotal(), false));

        CustomAdapter sumAdapter = new CustomAdapter(this, R.layout.row, paymentArr);
        talist.setAdapter(sumAdapter);
        setListViewHeightBasedOnItems(talist);


        try {
            System.gc();
            bitmap = encodeAsBitmap(receipt.getSerial(), BarcodeFormat.CODE_128, 600, 300);
            genBarcode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        toWrite = "G BASKET RECEIPT\nStoreName\nAddress\nPhoneNumber\n";
        toWrite += ("Date/time: " + receipt.getDate() + "\n");
        toWrite += ("Serial number: " + receipt.getSerial() + "\n");
        toWrite += ("Account Type: " + receipt.getAccType() + "\n");
        for (int i = 0; i < receipt.getItemPrice().size(); i++) {
            EachItem temp = receipt.getItemPrice().get(i);
            ReceiptItem test = new ReceiptItem(temp.getName(), temp.getPrice(), receipt.getOrigPrice().get(i),
                    receipt.getPriceOff().get(i));
            toWrite += (test.getItemName() + "\t\t$" + test.getItemPrice() + "\n");
            if (test.getDiscount() != 0.00) {
                toWrite += ("    Original Price: $" + test.getItemOrigPrice() + "\n");
                toWrite += ("    Discount: $-" + test.getDiscount() + "\n");
            }
        }

        toWrite += ("Total: " + receipt.getPaymentAmount() + "\n");
        toWrite += ("Tax: " +  receipt.getTax() / 100 * receipt.getPaymentAmount() + "\n");
        toWrite += ("Balance Due: " + receipt.getTotal());

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReceipt(receipt, toWrite);
                Bundle b = new Bundle();
                Intent historyIntent = new Intent(getApplicationContext(), HistoryActivity.class);
                b.putString("ReceiptTxt", toWrite);
                b.putParcelable("Receipt", receipt);
                historyIntent.putExtra("bundle", b);
                startActivity(historyIntent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String MY_FILE_NAME = receipt.getSerial() + "_Receipt";
                    //File file = new File(getApplicationContext().getFilesDir(), MY_FILE_NAME);
                    FileOutputStream fileos = openFileOutput(MY_FILE_NAME, MODE_PRIVATE);
                    OutputStreamWriter out = new OutputStreamWriter(getApplicationContext().openFileOutput(MY_FILE_NAME, Context.MODE_PRIVATE));

                    out.write(toWrite);
                    out.close();
                    Toast.makeText(getApplicationContext(), "Saved receipt to disk!", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    public void addReceipt (Receipt receipt, String toWrite) {
        ReceiptHandler dbHandler = new ReceiptHandler(this, null, null, 3);
        dbHandler.addReceipt(receipt, toWrite);
    }
}
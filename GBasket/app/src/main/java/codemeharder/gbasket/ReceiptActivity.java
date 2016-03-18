package codemeharder.gbasket;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Jimmy Chen on 2/20/2016.
 */
public class ReceiptActivity extends Activity {
    ImageView genBarcode;
    Bitmap bitmap;
    Button save, history;
    ListView itemList;
    ArrayList<ReceiptItem> items = new ArrayList<ReceiptItem>();
    TextView DateTime, SerialNum, AccountType;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Intent intent = getIntent();
        Receipt receipt = (Receipt) intent.getParcelableExtra("Receipt");
        save =  (Button) findViewById(R.id.save);
        history = (Button) findViewById(R.id.history);
        genBarcode = (ImageView) findViewById(R.id.barcode);
        DateTime = (TextView) findViewById(R.id.dateTimeInput);
        SerialNum = (TextView) findViewById(R.id.serialInput);
        AccountType = (TextView) findViewById(R.id.accountName);

        DateTime.setText(receipt.getDate());
        SerialNum.setText(receipt.getSerial());
        AccountType.setText(receipt.accType);

        itemList = (ListView) findViewById(R.id.listView3);
        itemList.setFocusable(false);

        //Convert receipt into receipt item
        for (int i = 0; i < receipt.getItemPrice().size(); i++) {
            EachItem temp = receipt.getItemPrice().get(i);
            ReceiptItem test = new ReceiptItem(temp.getName(), temp.getPrice(), receipt.getOrigPrice().get(i),
                    receipt.getPriceOff().get(i));
            items.add(test);
        }

        ReceiptItemAdapter adapter = new ReceiptItemAdapter(this, items);
        itemList.setAdapter(adapter);

        try {
            bitmap = encodeAsBitmap(receipt.getSerial(), BarcodeFormat.CODE_128, 600, 300);
            genBarcode.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO save the receipt as an image
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(historyIntent);
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

}

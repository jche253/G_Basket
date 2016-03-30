package codemeharder.gbasket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 3/29/2016.
 */
public class ReceiptHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "receiptDB.db";
    private static final String TABLE_RECEIPTS = "receipt";

    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_SERIAL = "serial";
    private static final String COLUMN_TYPE = "accType";
    private static final String COLUMN_ITEMPRICE = "Item cost";
    private static final String COLUMN_ORIGPRICE = "Original Price";
    private static final String COLUMN_PRICEOFF = "discount";
    private static final String COLUMN_PAYMENT = "payment_amount";
    private static final String COLUMN_TAX = "tax";
    private static final String COLUMN_TOTAL = "total";
    public ReceiptHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECEIPT_TABLE = "CREATE TABLE " +
                TABLE_RECEIPTS + "("
                + COLUMN_DATE + " TEXT, " + COLUMN_SERIAL
                + " TEXT PRIMARY KEY," + COLUMN_TYPE + " TEXT, "
                +  COLUMN_ITEMPRICE + " TEXT, " + COLUMN_ORIGPRICE + " TEXT, "
                + COLUMN_PRICEOFF + " TEXT, " + COLUMN_PAYMENT + " FLOAT, "  +
                COLUMN_TAX + " FLOAT, " + COLUMN_TOTAL + " FLOAT)";
        db.execSQL(CREATE_RECEIPT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEIPTS);
        onCreate(db);
    }

    public void addProduct(Receipt receipt) {
        ArrayList<EachItem> itemPrice = new ArrayList<EachItem>();
        ArrayList<Double> DiscountOrigPrice = new ArrayList<Double>();
        ArrayList<Double> PriceOff = new ArrayList<Double>();

        Gson gson= new Gson();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, receipt.getDate());
        values.put(COLUMN_SERIAL, receipt.getSerial());
        values.put(COLUMN_TYPE, receipt.getAccType());
        values.put(COLUMN_ITEMPRICE, gson.toJson(itemPrice).getBytes());
        values.put(COLUMN_ORIGPRICE, gson.toJson(DiscountOrigPrice).getBytes());
        values.put(COLUMN_PRICEOFF, gson.toJson(PriceOff).getBytes());
        values.put(COLUMN_PAYMENT, receipt.getPaymentAmount());
        values.put(COLUMN_TAX, receipt.getTax());
        values.put(COLUMN_TOTAL, receipt.getTotal());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_RECEIPTS, null, values);
        db.close();
    }

    public Receipt findReceipt(String lookSerial) {
        String query = "Select * FROM " + TABLE_RECEIPTS + " WHERE " + COLUMN_SERIAL + " =  \"" + lookSerial + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Receipt receipt = new Receipt();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            receipt.setDate(cursor.getString(0));
            receipt.setSerial(cursor.getString(1));
            receipt.setAccType(cursor.getString(2));

            byte[] blob = cursor.getBlob(cursor.getColumnIndex(COLUMN_ITEMPRICE));
            String json = new String(blob);
            Gson gson = new Gson();
            ArrayList<EachItem> itemPrice = gson.fromJson(json, new TypeToken<ArrayList<EachItem>>()
            {}.getType());
            receipt.setItemPrice(itemPrice);

            blob = cursor.getBlob(cursor.getColumnIndex(COLUMN_ORIGPRICE));
            json = new String(blob);
            gson = new Gson();
            ArrayList<Double> DiscountOrigPrice = gson.fromJson(json, new TypeToken<ArrayList<Double>>() {
            }.getType());
            receipt.setOrigPrice(DiscountOrigPrice);

            blob = cursor.getBlob(cursor.getColumnIndex(COLUMN_PRICEOFF));
            json = new String(blob);
            gson = new Gson();
            ArrayList<Double> PriceOff = gson.fromJson(json, new TypeToken<ArrayList<Double>>()
            {}.getType());
            receipt.setOrigPrice(PriceOff);

            receipt.setPaymentAmount(cursor.getDouble(6));
            receipt.setTax(cursor.getDouble(7));
            receipt.setTotal(cursor.getDouble(8));
            
            cursor.close();
        } else {
            receipt = null;
        }
        db.close();
        return receipt;
    }
}

package codemeharder.gbasket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 3/29/2016.
 */
public class ReceiptHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "receiptDB.db";
    private static final String TABLE_RECEIPTS = "receipt";

    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_SERIAL = "serial";
    private static final String COLUMN_RECSTRING = "RECSTRING";

    public ReceiptHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECEIPT_TABLE = "CREATE TABLE " + TABLE_RECEIPTS
                + "(" + COLUMN_DATE + " TEXT, " + COLUMN_SERIAL
                + " TEXT PRIMARY KEY," + COLUMN_RECSTRING + " TEXT)";
        db.execSQL(CREATE_RECEIPT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEIPTS);
        onCreate(db);
    }

    public void addReceipt (Receipt receipt, String recString) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, receipt.getDate());
        values.put(COLUMN_SERIAL, receipt.getSerial());
        values.put(COLUMN_RECSTRING, recString);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_RECEIPTS, null, values);
        db.close();
    }

    public String findReceipt(String lookSerial) {
        String query = "Select * FROM " + TABLE_RECEIPTS + " WHERE " + COLUMN_SERIAL + " =  \"" + lookSerial + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String result;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(COLUMN_RECSTRING));
        } else {
            result = null;
        }
        db.close();
        return result;
    }

    public ArrayList<ReceiptHistItem> getAllValues() {
        String query = "Select * FROM " + TABLE_RECEIPTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<ReceiptHistItem> resultList = new ArrayList<>();

        try {
            if (cursor .moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    ReceiptHistItem receipt = new ReceiptHistItem();

                    receipt.setDate(cursor.getString(0));
                    receipt.setSerial(cursor.getString(1));
                    receipt.setReceiptText(cursor.getString(2));
                    resultList.add(receipt);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        db.close();
        return resultList;
    }

}

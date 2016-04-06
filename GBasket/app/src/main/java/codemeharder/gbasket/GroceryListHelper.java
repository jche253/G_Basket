package codemeharder.gbasket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Jimmy Chen on 4/5/2016.
 */
public class GroceryListHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wishlistDB.db";
    public static final String TABLE_NAME = "AccInfo_Table";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ITEM = "item";

    public GroceryListHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECEIPT_TABLE = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ITEM + " TEXT)";
        db.execSQL(CREATE_RECEIPT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addItem (String itemName) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM, itemName);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteItem (ToBuyItem item) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM + " =  \"" + item.getName() + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            db.delete(TABLE_NAME, COLUMN_ITEM + " = ?",
                    new String[] { String.valueOf(item.getName()) });
            cursor.close();
        }
        db.close();
    }

    public ArrayList<ToBuyItem> getAllValues() {
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<ToBuyItem> itemList = new ArrayList<>();

        cursor.moveToFirst();
        try {
            while (cursor.moveToNext()) {
                ToBuyItem test = new ToBuyItem(cursor.getString(0));
                itemList.add(test);
            }
        } finally {
            cursor.close();
        }
        db.close();
        return itemList;
    }
}

package codemeharder.gbasket;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Alfred Wong on 3/28/2016.
 */
public class BasketHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Basket.db";
    public static final String TABLE_NAME = "Basket_Table";
    public static final String ID = "ID";
    public static final String ITEMNAME = "Item_Name";
    public static final String ITEMPRICE = "Item_Price";

    public BasketHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, Item_Name TEXT, Item_Price REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deletedb(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public boolean insertData(int ident, String productname, double productprice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, ident);
        contentValues.put(ITEMNAME, productname);
        contentValues.put(ITEMPRICE, productprice);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<EachItemID> queryBasket() {
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<EachItemID> list = new ArrayList<>();

        try {
            if (cursor .moveToFirst()) {
                while (cursor.isAfterLast() == false) {
                    EachItemID eachItem2 = new EachItemID();
                    eachItem2.setID(cursor.getInt(0));
                    eachItem2.setName(cursor.getString(1));
                    eachItem2.setPrice(cursor.getDouble(2));
                    eachItem2.setCheckBOx(true);
                    list.add(eachItem2);
                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        db.close();
        return list;
    }


    public void deleteRow(int ident){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID+" = "+ ident, null);
    }

}
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
    public static final String ID = "Prod_ID";
    public static final String ITEMNAME = "Item_Name";
    public static final String ITEMPRICE = "Item_Price";

    ArrayList<EachItem2> items = new ArrayList<EachItem2>();
    public int currentID;
    public String currentName;
    public double currentPrice;
    EachItem2 eachItem2 = new EachItem2(1, "Nothing", 0.00, true);




    public BasketHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (Prod_ID INTEGER PRIMARY KEY, Item_Name TEXT, Item_Price REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void delete(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }

    public boolean insertData(int productid, String productname, double productprice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, productid);
        contentValues.put(ITEMNAME, productname);
        contentValues.put(ITEMPRICE, productprice);
        long result = db.insert(TABLE_NAME, null, contentValues);
        currentID = productid;
        currentName = productname;
        currentPrice = productprice;
        eachItem2 = addcurrent();

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public ArrayList<EachItem2> queryBasket() {
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        setCurrent();



        cursor.moveToFirst();
        try {
            while (cursor.moveToNext()) {
                eachItem2.setID(cursor.getInt(0));
                eachItem2.setName(cursor.getString(1));
                eachItem2.setPrice(cursor.getDouble(2));

                items.add(eachItem2);
            }
        } finally {
            cursor.close();
        }
        db.close();
        return items;


    }

    public EachItem2 addcurrent(){
        EachItem2 currentItem = new EachItem2(currentID, currentName, currentPrice, true);
        return currentItem;
    }
    public void setCurrent(){
        items.add(eachItem2);
    }

    public void deleteRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID+"="+id, null);
    }

}
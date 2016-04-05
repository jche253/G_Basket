package codemeharder.gbasket;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alfred Wong on 3/28/2016.
 */
public class BasketHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Basket.db";
    public static final String TABLE_NAME = "Basket_Table";
    public static final String ID = "Prod_ID";
    public static final String ITEMNAME = "Item_Name";
    public static final String ITEMPRICE = "Item_Price";

    public BasketHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (Prod_ID TEXT PRIMARY KEY, Item_Name TEXT, Item_Price TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String productid, String productname, String productprice){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, productid);
        contentValues.put(ITEMNAME, productname);
        contentValues.put(ITEMPRICE, productprice);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else return true;
    }




}


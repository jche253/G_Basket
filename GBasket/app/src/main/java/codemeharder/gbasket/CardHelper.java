package codemeharder.gbasket;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by seokyung on 4/2/16.
 */
public class CardHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Payment.db";
    public static final String TABLE_NAME = "Payment_Table";
    public static final String User_Name = "uname";
    public static final String Card = "Card";
    public static final String Exp_Month = "Exp_Month";
    public static final String Exp_Year = "Exp_Year";
    public static final String cvc = "cvc";

    public CardHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (uname TEXT PRIMARY KEY, Card_Num TEXT, Exp_Month TEXT, Exp_Year TEXT, cvc TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String Name, String Card_Num, String EMonth, String EYear, String CVC){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User_Name, Name);
        contentValues.put(Card, Card_Num);
        contentValues.put(Exp_Month, EMonth);
        contentValues.put(Exp_Year, EYear);
        contentValues.put(cvc, CVC);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else return true;
    }


}

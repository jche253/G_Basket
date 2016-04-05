package codemeharder.gbasket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by seokyung on 4/4/16.
 */
public class CardHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cardDB.db";
    private static final String TABLE_CARDS = "cdata";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_card = "card";
    public static final String COLUMN_eMonth = "exp_date";
    public static final String COLUMN_eYear = "exp_year";
    public static final String COLUMN_cvc = "cvc";

    public CardHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CARDS + " (card TEXT PRIMARY KEY, exp_date TEXT, exp_year TEXT, cvc TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }


    public boolean addCard(String crednum, String eMonth, String eYear, String cvv) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_card, crednum);
        values.put(COLUMN_eMonth, eMonth);
        values.put(COLUMN_eYear, eYear);
        values.put(COLUMN_cvc, cvv);

        long res = db.insert(TABLE_CARDS, null, values);
        db.close();

        if (res == -1) return false;
        return true;
    }

    public String findCard (String Cardnum) {
        String query = "Select * FROM " + TABLE_CARDS + " WHERE " + COLUMN_card + " =  \"" + Cardnum + "\" ";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String result;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(COLUMN_card));
            cursor.close();
        } else {
            result = null;
            cursor.close();
        }
        db.close();
        return result;
    }

    public ArrayList<Card> getAllValues() {
        String query = "Select * FROM " + TABLE_CARDS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Card> resList = new ArrayList<>();

        cursor.moveToFirst();

        try {
            while (cursor.moveToNext()) {
                Card card = new Card();

                card.setCard(cursor.getString(0));
                card.seteMonth(cursor.getString(1));
                card.seteYear(cursor.getString(2));
                card.setCVC(cursor.getString(3));
                resList.add(card);
            }
        } finally {
            cursor.close();
        }
        db.close();
        return resList;
    }


}
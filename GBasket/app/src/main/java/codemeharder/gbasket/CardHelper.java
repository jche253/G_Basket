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
    private static final String TABLE_CARDS = "card";

    public static final String COLUMN_card = "card";
    public static final String COLUMN_eMonth = "expiration date";
    public static final String COLUMN_eYear = "expiration year";
    public static final String COLUMN_cvc = "cvc";

    public CardHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_CARDS + "("
                + COLUMN_card + " TEXT PRIMARY KEY," + COLUMN_eMonth
                + " TEXT," + COLUMN_eYear + " TEXT" + COLUMN_cvc + "TEXT" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }

    public boolean regCard(String card, String eMonth, String eYear, String cvc){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_card, card);
        contentValues.put(COLUMN_eMonth, eMonth);
        contentValues.put(COLUMN_eYear, eYear);
        contentValues.put(COLUMN_cvc, cvc);
        long result = db.insert(TABLE_CARDS, null, contentValues);
        if(result == -1)
            return false;
        else return true;
    }



    public void addCard (Card Card) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_card, Card.getCard());
        values.put(COLUMN_eMonth, Card.geteMonth());
        values.put(COLUMN_eYear, Card.geteYear());
        values.put(COLUMN_cvc, Card.getCVC());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_CARDS, null, values);
        db.close();
    }

    public String findCard (String Cardnum) {
        String query = "Select * FROM " + TABLE_CARDS + "WHERE " + COLUMN_card + " =  \"" + Cardnum + "\" ";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        String result;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(COLUMN_card));
        } else {
            result = null;
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
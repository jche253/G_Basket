package codemeharder.gbasket;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alfred Wong on 2/23/2016.
 * Edited by Jimmy Chen on 4/4/2016
 */
public class LoginHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AccInfo.db";
    public static final String TABLE_NAME = "AccInfo_Table";
    public static final String ID = "User_ID";
    public static final String FNAME = "User_FName";
    public static final String LNAME = "User_LName";
    public static final String PASSWORD = "User_Pass";

    public LoginHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (USER_ID TEXT PRIMARY KEY, USER_FName TEXT, USER_LName TEXT, User_Pass TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String email, String fname, String lname, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, email);
        contentValues.put(FNAME, fname);
        contentValues.put(LNAME, lname);
        contentValues.put(PASSWORD, pass);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else return true;
    }




}

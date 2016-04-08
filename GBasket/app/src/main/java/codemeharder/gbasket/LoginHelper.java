package codemeharder.gbasket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Alfred Wong on 2/23/2016.
 * Edited by Jimmy Chen on 4/4/2016
 */
public class LoginHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "AccountInfo.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "AccInfo_Table";
    public static final String ID = "User_ID";
    public static final String FNAME = "User_FName";
    public static final String LNAME = "User_LName";
    public static final String PASSWORD = "User_Pass";

    public LoginHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
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

    public void resetUser(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
        db.close();
        if (result == -1)
            return false;
        else return true;

    }

    public String findAcc(String email, String password) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + ID + " =  \"" + email +
                "\" AND " + PASSWORD + " = \"" + password + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String name;
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            name = cursor.getString(1);
            cursor.close();
            db.close();
            return name;
        } else {
            cursor.close();
            db.close();
            return null;
        }

    }

    public String recoverPass(String fname, String lname, String email) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + ID + " =  \"" + email +
                "\" AND " + FNAME + " = \"" + fname + "\""
                + " AND " + LNAME + " = \"" + lname + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String password;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            password = cursor.getString(3);
            cursor.close();
            db.close();
            return password;
        } else {
            cursor.close();
            db.close();
            return null;
        }

    }

    public ArrayList<String> getAllData () {
        String query = "Select * FROM " + TABLE_NAME;
        ArrayList<String> userData = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            userData.add(cursor.getString(0));
            userData.add(cursor.getString(1));
            userData.add(cursor.getString(2));
            userData.add(cursor.getString(3));

            cursor.close();
            db.close();
            return userData;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public String seeUser() {
        String query = "Select * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            String name = cursor.getString(0) + " " + cursor.getString(3);
            cursor.close();
            db.close();
            return name;
        } else {
            cursor.close();
            db.close();
            return null;
        }

    }

    public void updatePassword(String id, String oldPass, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PASSWORD, newPass);
        db.update(TABLE_NAME, cv, ID + "= \'" + id + "\'", null);
        db.close();
    }

    public boolean checkAccountCount() {
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 1)
            return true;
        else
            return false;
    }

    public boolean deleteAcc(String email) {
        boolean result = false;
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + ID + " =  \"" + email + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            db.delete(TABLE_NAME, ID + " = ?",
                    new String[] { cursor.getString(0) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

}

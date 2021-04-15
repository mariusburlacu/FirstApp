package com.example.firstapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";
    public static final String TABLE_NAME = "Users";
    public static final String Col_ID = "ID";
    public static final String Col_Username = "USERNAME";
    public static final String Col_Email = "EMAIL";
    public static final String Col_Password = "PASSWORD";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT," +
                "EMAIL TEXT, PASSWORD TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String username, String email, String password) {
        if(!isUsernameExist(username)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Col_Username, username);
            contentValues.put(Col_Email, email);
            contentValues.put(Col_Password, password);
            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        }
        else {

            return false;
        }
    }

    public boolean isUsernameExist(String value){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + Col_Username + " = ?";
        String[] whereArgs = {value};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        if(count==0){
            return false; // daca count este 0, metoda o sa returneze fals => putem salva date
        }
        else {
            return true;
        }
    }

    public boolean isPasswordExist(String value){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + Col_Password + " = ?";
        String[] whereArgs = {value};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, whereArgs);

        int count = cursor.getCount();

        cursor.close();

        if(count==0){
            return false; // daca count este 0, metoda o sa returneze fals => putem salva date
        }
        else {
            return true;
        }
    }
}

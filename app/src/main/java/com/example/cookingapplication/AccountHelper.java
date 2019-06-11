package com.example.cookingapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="cooking.db";
    private static final String TABLE_NAME="account";
    private static final int SCHEMA_VERSION=1;
    public AccountHelper(Context context){
        super(context,DATABASE_NAME,null,SCHEMA_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addAccount(Account account){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues c= new ContentValues();
        c.put("id",account.getId());
        c.put("email",account.getEmail());
        c.put("password",account.getPassword());
        db.insert("account",null,c);
        db.close();
    }
    public Account getAccountById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"id",
                        "email", "password"}, "id" + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Account account = new Account(0,"","");
        if (cursor.moveToFirst()){
            account = new Account(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
        }
         cursor.close();
         db.close();
        return account;

    }
    public void deleteAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id" + " = ?",
                new String[] { String.valueOf(account.getId()) });
        db.close();
    }
    public int getCount() {
       // Log.i(TAG, "MyDatabaseHelper.getNotesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        // return count
        return count;
    }

}

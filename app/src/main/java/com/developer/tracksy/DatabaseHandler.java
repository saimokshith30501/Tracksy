package com.developer.tracksy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tracksy";
    private static final String TABLE_RATING = "rating";
    private static final String KEY_CENTER_ID = "center_id";
    private static final String KEY_RATING = "rating";
    private static final String KEY_GIVEN = "given";
    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RATING + "("
                + KEY_CENTER_ID + " INTEGER PRIMARY KEY," + KEY_RATING + " TEXT,"
                + KEY_GIVEN + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RATING);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addRating(Rating rating){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CENTER_ID, rating.center_id);
        values.put(KEY_RATING, rating.rating);
        values.put(KEY_GIVEN, rating.given);
        db.insert(TABLE_RATING, null, values);
        db.close();
    }
    public int updateContact(Rating rating) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RATING, rating.getRating());
        values.put(KEY_GIVEN, rating.isGiven());

        return db.update(TABLE_RATING, values, KEY_CENTER_ID + " = ?",
                new String[] { String.valueOf(rating.getCenter_id()) });
    }
    Rating getRating(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_RATING, new String[] { KEY_CENTER_ID,
                        KEY_RATING, KEY_GIVEN }, KEY_CENTER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            Rating contact = new Rating(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2));
            return contact;
        }
        else
            return new Rating(0,null,null);


    }



}

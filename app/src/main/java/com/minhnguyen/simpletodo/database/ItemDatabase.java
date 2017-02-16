package com.minhnguyen.simpletodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.minhnguyen.simpletodo.models.Item;

import java.util.ArrayList;

/**
 * Created by MINH NPA on 24 Sep 2016.
 */

public class ItemDatabase extends SQLiteOpenHelper {
    // Database Info
    public static final String DATABASE_NAME = "itemsDatabase";
    public static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_ITEMS = "items";

    // Item Table Columns
    public static final String KEY_ITEM_ID = "id";
    public static final String KEY_ITEM_DATE = "date";
    public static final String KEY_ITEM_SUBJECT = "subject";
    public static final String KEY_ITEM_CONTENT = "content";
    public static final String KEY_ITEM_COLOR = "color";
    public static final String KEY_ITEM_STATUS = "status";

    public ItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_DATE + " TEXT, " +
                KEY_ITEM_SUBJECT + " TEXT, " +
                KEY_ITEM_CONTENT + " TEXT NOT NULL, " +
                KEY_ITEM_COLOR + " TEXT NOT NULL, " +
                KEY_ITEM_STATUS + " TEXT NOT NULL" +
                ")";

        db.execSQL(createTable);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> itemList = new ArrayList<>();

        // Select all query
        String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " ORDER BY " + KEY_ITEM_DATE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();

                item.setId(cursor.getInt(0));
                item.setDate(cursor.getString(1));
                item.setSubject(cursor.getString(2));
                item.setContent(cursor.getString(3));
                item.setColor(cursor.getString(4));
                item.setStatus(cursor.getString(5));

                // And adding to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        return itemList;
    }

    public void addItem(Item item, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(KEY_ITEM_ID, item.getId());
            values.put(KEY_ITEM_DATE, item.getDate());
            values.put(KEY_ITEM_SUBJECT, item.getSubject());
            values.put(KEY_ITEM_CONTENT, item.getContent());
            values.put(KEY_ITEM_COLOR, item.getColor());
            values.put(KEY_ITEM_STATUS, item.getStatus());

            // insert
            db.insert(TABLE_ITEMS, null, values);
            db.close(); // close table

            Log.d("mr.log", "Added. Position: " + position + ". Id: " + item.getId());
        } catch (Exception e) {
            Log.d("mr.log", "Add error. Position: " + position + ". Id: " + item.getId());
        }
    }

    public void updateItem(Item item, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put(KEY_ITEM_DATE, item.getDate());
            values.put(KEY_ITEM_SUBJECT, item.getSubject());
            values.put(KEY_ITEM_CONTENT, item.getContent());
            values.put(KEY_ITEM_COLOR, item.getColor());
            values.put(KEY_ITEM_STATUS, item.getStatus());

            // update with id
            db.update(TABLE_ITEMS, values, KEY_ITEM_ID + "=?",
                    new String[]{"" + item.getId()});
            db.close();

            Log.d("mr.log", "Updated. Position: " + position + ". Id: " + item.getId());
        } catch (Exception e) {
            Log.d("mr.log", "Update error. Position: " + position + ". Id: " + item.getId());
        }
    }

    public void deleteItem(int id, int position) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TABLE_ITEMS, KEY_ITEM_ID + "=?",
                    new String[]{"" + id});
            db.setTransactionSuccessful();

            Log.d("mr.log", "Deleted. Position: " + position + ". Id: " + id);
        } catch (Exception e) {
            Log.d("mr.log", "Delete error. Position: " + position + ". Id: " + id);
        }
    }
}

package com.hfad.dbtester;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DBTESTER.db";

    private static final String DB_TABLE_LIST = "LIST";
    private static final String DB_TABLE_ITEMS = "ITEMS";
    private  static final String CATEGORY_ID = "CATEGORY_ID";


    // list category columns
    private static final String _id = "_id";
    private static final String NAME = "NAME";


    // TO DO LIST COLUMNS
    private static final String _ID = "_ID";
    private static final String DESCRIPTION = "description";



    // TO DO LIST TABLE
    private static final String CREATE_TABLE_LIST = "CREATE TABLE " + DB_TABLE_LIST + " (" +
            _id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME + " TEXT " + ")";


    // TO DO ITEM TABLE
    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE " + DB_TABLE_ITEMS + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DESCRIPTION + " TEXT, " +
            CATEGORY_ID + " INTEGER NOT NULL, " +
            " FOREIGN KEY ("+CATEGORY_ID+") REFERENCES "+DB_TABLE_LIST+"("+_id+"))";


    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LIST);
        db.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ITEMS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_LIST);
        onCreate(sqLiteDatabase);
    }

    //insert data to table list
    public boolean insertDataList(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);

        long result = db.insert(DB_TABLE_LIST, null, contentValues);
        return result != -1;
    }


    //insert data to table items
    public boolean insertDataItems(String todoid, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_ID, todoid);
        contentValues.put(DESCRIPTION, description);

        long result = db.insert(DB_TABLE_ITEMS, null, contentValues);
        return result != -1;
    }

    public boolean deleteData(String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete("ITEMS","DESCRIPTION = ?", new String[] {description});
        return result != -1;
    }

    public boolean deleteDataList(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete("LIST","NAME = ?", new String[] {name});
        return result != -1;
    }


    //view data
    public Cursor viewDataList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE_LIST;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor viewData(String todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT *  FROM " + DB_TABLE_ITEMS +
                " WHERE " + CATEGORY_ID + " =  "+todo_id;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public Cursor sortData(String todo_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE_ITEMS +
                " WHERE " + CATEGORY_ID + " =  "+todo_id +
                " ORDER BY DESCRIPTION";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor sortDataList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE_LIST +
                " ORDER BY NAME";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public Cursor searchData(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE_ITEMS +
                " WHERE " + DESCRIPTION + " LIKE  '%"+text+"%'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor searchDataList(String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE_LIST + " WHERE " + NAME + " LIKE  '%"+text+"%'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


}
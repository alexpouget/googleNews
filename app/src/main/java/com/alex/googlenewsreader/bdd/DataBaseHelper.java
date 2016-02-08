package com.alex.googlenewsreader.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alex on 03/02/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_FILENAME="GNews.lite";
    public static final String DB_NAME = "GNews";
    public static final int DB_VERSION=1;
    public static final String DB_TABLE_NAME = "News";
    public static final String[] FIELD = {"TITLE","URL","IMAGE","SNIPPET","TAG","ISACTIVE"};

    public static final String DB_CREATE_TABLE_NEWS = "CREATE TABLE "+DB_TABLE_NAME+" "+ " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT, "+FIELD[0]+" VARCHAR," +
            FIELD[1]+" VARCHAR,"+FIELD[2]+" VARCHAR,"+FIELD[3]+" VARCHAR,"+FIELD[4]+" VARCHAR,"+FIELD[5]+" INTEGER)";


    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.alex.googlenewsreader.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.Html;
import android.widget.Toast;

import com.alex.googlenewsreader.MainActivity;
import com.alex.googlenewsreader.News;

import java.io.Serializable;


/**
 * Created by alex on 11/02/2016.
 */
public class Database implements Serializable {
    private Context context;
    private SQLiteDatabase db;

    public Database(Context c){
        context = c;
        initDB();

    }
    //bdd
    public void initDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context,DataBaseHelper.DB_NAME,null,DataBaseHelper.DB_VERSION);
        try {
            db = dataBaseHelper.getWritableDatabase();
        }catch(SQLiteException e){
            db = context.openOrCreateDatabase(DataBaseHelper.DB_NAME, context.MODE_PRIVATE, null);
        }
    }

    public void insert(String title,String url,String image,String snippet){
        if(db!=null){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseHelper.FIELD[0], title);
            contentValues.put(DataBaseHelper.FIELD[1], url);
            contentValues.put(DataBaseHelper.FIELD[2], image);
            contentValues.put(DataBaseHelper.FIELD[3], snippet);
            contentValues.put(DataBaseHelper.FIELD[4], MainActivity.activeTag);
            contentValues.put(DataBaseHelper.FIELD[5], 1);
            long id = db.insert(DataBaseHelper.DB_TABLE_NAME,null,contentValues);
            Toast.makeText(context, "Inserted in DB = " + id, Toast.LENGTH_SHORT).show();
        }
    }
    public void insertOrUpdate(String title,String url,String image,String snippet){
        if(db!=null){
            String whereClause = DataBaseHelper.FIELD[0]+" = ?";
            String[] whereArgs = {title};
            Cursor cursor =  db.query(DataBaseHelper.DB_TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if(cursor.moveToNext()){
                //cursor.moveToNext();
                //if(cursor != null && cursor.getCount()>0 && cursor.getPosition()>=0){
                System.out.println("update " + cursor.getLong(0));
                update((int)cursor.getLong(0), title, url,image,snippet);
            }else{
                insert(title, url, image, snippet);
            }
        }
    }


    public boolean isPresent(String title){
        if(db!=null){
            String whereClause = DataBaseHelper.FIELD[0]+" = ?";
            String[] whereArgs = {title};
            Cursor cursor =  db.query(DataBaseHelper.DB_TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if(cursor.moveToNext()){
                return true;
            }
            return false;
        }
        return false;
    }


    public void update(String title,String url,String image,String snippet){
        if(db!=null){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseHelper.FIELD[0], title);
            contentValues.put(DataBaseHelper.FIELD[1], url);
            contentValues.put(DataBaseHelper.FIELD[2], image);
            contentValues.put(DataBaseHelper.FIELD[3], snippet);
            contentValues.put(DataBaseHelper.FIELD[4], MainActivity.activeTag);
            long nbRows = db.update(DataBaseHelper.DB_TABLE_NAME, contentValues, " _id = ? ", new String[]{"1"});
            Toast.makeText(context, "update " + nbRows, Toast.LENGTH_SHORT).show();
        }
    }
    public void update(int id,String title,String url,String image,String snippet){
        if(db!=null){
            ContentValues contentValues = new ContentValues();
            String[] _id = {Integer.toString(id)};
            contentValues.put(DataBaseHelper.FIELD[0], title);
            contentValues.put(DataBaseHelper.FIELD[1], url);
            contentValues.put(DataBaseHelper.FIELD[2], image);
            contentValues.put(DataBaseHelper.FIELD[3], snippet);
            contentValues.put(DataBaseHelper.FIELD[4], MainActivity.activeTag);
            long nbRows = db.update(DataBaseHelper.DB_TABLE_NAME, contentValues, " _id = ? ", _id);
            Toast.makeText(context, "update " + nbRows, Toast.LENGTH_SHORT).show();
        }
    }

    public void deactivate(long id){

        if(db!=null){
            ContentValues contentValues = new ContentValues();
            String[] _id = {Long.toString(id)};
            contentValues.put(DataBaseHelper.FIELD[5], 0);
            long nbRows = db.update(DataBaseHelper.DB_TABLE_NAME, contentValues, " _id = ? ", _id);

        }
    }

    public void deleteAll(){
        if (db != null) {
            long nbRows = db.delete(DataBaseHelper.DB_TABLE_NAME, null, null);
            Toast.makeText(context, "delete " + nbRows, Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getAllNews(){
        if(db!=null){
            Cursor cursor = db.query(DataBaseHelper.DB_TABLE_NAME, null, null, null, null, null, null);
            return cursor;
        }
        return null;
    }

    public boolean getNewNews(){
        if(db!=null){
            Cursor cursor = db.query(DataBaseHelper.DB_TABLE_NAME, null, null, null, null, null, null);
            while(cursor.moveToNext()) {
                if (isPresent(cursor.getString(1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getNbNews()
    {
        Cursor cursor = null;
        if(db!=null) {
            cursor = db.query(DataBaseHelper.DB_TABLE_NAME, null, null, null, null, null, null);
            cursor.moveToNext();
            //Toast.makeText(this, cursor.getCount(), Toast.LENGTH_LONG).show();

        }
        return cursor.getCount();
    }

    public int getNbNewsActivated(){

        Cursor cursor = null;
        String whereClause = DataBaseHelper.FIELD[5]+" = 1";
        if(db!=null) {
            cursor = db.query(DataBaseHelper.DB_TABLE_NAME, null, whereClause, null, null, null, null);
            cursor.moveToNext();

        }
        return cursor.getCount();
    }

    public long reactivateAllNews() {
        long nbRows =0l;
        if (db != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseHelper.FIELD[5], 1);
            nbRows = db.update(DataBaseHelper.DB_TABLE_NAME, contentValues, null, null);
            return nbRows;
        }
        return nbRows;
    }
}

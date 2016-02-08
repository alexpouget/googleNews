package com.alex.googlenewsreader;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alex.googlenewsreader.bdd.DataBaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<News> actu = null;
    SQLiteDatabase db;
    String activeTag = "barack%20obama";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDB();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            LoadNews("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q="+activeTag);
        }else{
            System.out.println("non connecter");
        }

        Button search_button = (Button)findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //log();

            }
        });
    }


    @Override
    public void onRefresh(){
        activeTag = "football";
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadNews("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=" + activeTag);
            }
        }, 2000);
    }


    public void LoadNews(String myurl){

        setContentView(R.layout.activity_main);

        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setOnItemClickListener(new MyOnItemClickListener());
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this); //listener sur le pull to refresh (onRefresh)

        try {
            URL url = new URL(myurl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            final StringBuilder out = new StringBuilder();
            final byte[] buffer = new byte[1024];
            try {
                for (int ctr; (ctr = inputStream.read(buffer)) != -1;) {
                    out.append(new String(buffer, 0, ctr));
                }
            } catch (IOException e) {
                throw new RuntimeException("Cannot convert stream to string", e);
            }
            String result = out.toString();
            // On récupère le JSON complet
            JSONObject jsonObject = new JSONObject(result);
            // On récupère le tableau d'objets qui nous concernent
            jsonObject = new JSONObject(jsonObject.getString("responseData"));
            JSONArray array = new JSONArray(jsonObject.getString("results"));

            actu = new ArrayList<News>();
            // Pour tous les objets on récupère les infos
            //deleteAll();
            for (int i = 0; i < array.length(); i++) {
                // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));

                // on instancie la classe news
                //News news = new News();

                // on set le titre
                //news.setTitle(obj.getString("title"));
                //actu[i] = String.valueOf(Html.fromHtml(news.getTitle()+"<br>"+obj.getString("content")+"</br>"));
               // news.setSnippet(String.valueOf(Html.fromHtml(news.getTitle() + "<br>" + obj.getString("content") + "</br>")));
                String urlDecoded = java.net.URLDecoder.decode(obj.getString("url"), "UTF-8");
                //news.setUrl(urlDecoded);
                if(!obj.isNull("image")) {
                    JSONObject obj_image = new JSONObject(obj.getString("image"));
                    insertOrUpdate(obj.getString("title"), urlDecoded, obj_image.getString("url"), obj.getString("content"));
                }else{
                    insertOrUpdate(obj.getString("title"), urlDecoded, null, obj.getString("content"));
                }

            }
            getAllNews();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (JSONException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyAdapter arrayActu = new MyAdapter(this, actu);
        lv.setAdapter(arrayActu);
    }

    //bdd
    private void initDB() {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this,DataBaseHelper.DB_NAME,null,DataBaseHelper.DB_VERSION);
        try {
            db = dataBaseHelper.getWritableDatabase();
        }catch(SQLiteException e){
            db = openOrCreateDatabase(DataBaseHelper.DB_NAME,MODE_PRIVATE,null);
        }
    }

    private void insert(String title,String url,String image,String snippet){
        if(db!=null){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseHelper.FIELD[0], title);
            contentValues.put(DataBaseHelper.FIELD[1], url);
            contentValues.put(DataBaseHelper.FIELD[2], image);
            contentValues.put(DataBaseHelper.FIELD[3], snippet);
            contentValues.put(DataBaseHelper.FIELD[4], activeTag);
            contentValues.put(DataBaseHelper.FIELD[5], 1);
            long id = db.insert(DataBaseHelper.DB_TABLE_NAME,null,contentValues);
            Toast.makeText(MainActivity.this, "Inserted in DB = " + id, Toast.LENGTH_SHORT).show();
        }
    }
    private void insertOrUpdate(String title,String url,String image,String snippet){
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


    private void update(String title,String url,String image,String snippet){
        if(db!=null){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseHelper.FIELD[0], title);
            contentValues.put(DataBaseHelper.FIELD[1], url);
            contentValues.put(DataBaseHelper.FIELD[2], image);
            contentValues.put(DataBaseHelper.FIELD[3], snippet);
            contentValues.put(DataBaseHelper.FIELD[4], activeTag);
            long nbRows = db.update(DataBaseHelper.DB_TABLE_NAME, contentValues, " _id = ? ", new String[]{"1"});
            Toast.makeText(MainActivity.this, "update " + nbRows, Toast.LENGTH_SHORT).show();
        }
    }
    private void update(int id,String title,String url,String image,String snippet){
        if(db!=null){
            ContentValues contentValues = new ContentValues();
            String[] _id = {Integer.toString(id)};
            contentValues.put(DataBaseHelper.FIELD[0], title);
            contentValues.put(DataBaseHelper.FIELD[1], url);
            contentValues.put(DataBaseHelper.FIELD[2], image);
            contentValues.put(DataBaseHelper.FIELD[3], snippet);
            contentValues.put(DataBaseHelper.FIELD[4], activeTag);
            long nbRows = db.update(DataBaseHelper.DB_TABLE_NAME, contentValues, " _id = ? ", _id);
            Toast.makeText(MainActivity.this, "update " + nbRows, Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteAll(){
        if (db != null) {
            long nbRows = db.delete(DataBaseHelper.DB_TABLE_NAME,null,null);
            Toast.makeText(MainActivity.this, "delete "+nbRows, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllNews(){
        if(db!=null){

            Cursor cursor = db.query(DataBaseHelper.DB_TABLE_NAME,null,null,null,null,null,null);
            while(cursor.moveToNext()){
                News news = new News();
                long id = cursor.getLong(0);

                news.setTitle(cursor.getString(1));
                news.setSnippet(String.valueOf(Html.fromHtml(news.getTitle() + "<br>" + cursor.getString(4) + "</br>")));
                news.setUrl(cursor.getString(2));
                news.setImage(cursor.getString(3));
                System.out.println(" ligne : "+cursor.getString(1)+"active : "+cursor.getString(6)+" tag : "+cursor.getString(5));
                if(cursor.getInt(6)==1 && cursor.getString(5).equals(activeTag)) {
                    actu.add(news);
                }
            }

        }
    }

}

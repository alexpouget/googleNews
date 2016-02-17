package com.alex.googlenewsreader;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alex.googlenewsreader.Notifications.MyReceiver;
import com.alex.googlenewsreader.asyncTask.BackTask;
import com.alex.googlenewsreader.bdd.Database;
import com.alex.googlenewsreader.file_manager.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class
        MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<News> actu = null;
    public static Database dbi;
    public static Boolean co = false;
    public static String activeTag;

    MyReceiver myReceiver = new MyReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(activeTag == null || activeTag ==""){
            activeTag = "actu";
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        registerReceiver(myReceiver, new IntentFilter("N3W5"));
        dbi = new Database(this);
        BackTask backTask = new BackTask(this,dbi);
        backTask.execute();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            co=true;
            LoadNews("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q="+activeTag);
        }else{
            ListView lv = (ListView)findViewById(R.id.listView);
            co=false;
            System.out.println("non connecter");
            actu = new ArrayList<News>();
            getAllNews();
            MyAdapter arrayActu = new MyAdapter(this, actu);
            lv.setAdapter(arrayActu);
        }

        Button search_button = (Button)findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText recherche = (EditText)findViewById(R.id.editText);
                recherche.setVisibility(View.VISIBLE);
                recherche.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_ENTER)
                        {
                            activeTag = String.valueOf(recherche.getText());
                            recherche.setVisibility(View.GONE);
                            hideSoftKeyboard(MainActivity.this);
                            LoadNews("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=" + activeTag);
                            FileManager fm =new FileManager(v.getContext());
                            fm.Write(activeTag);
                        }
                        return false;
                    }
                });
            }
        });

        Button settings_button = (Button) findViewById(R.id.settings_button);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ParametersActivity.class);
                //intent.putExtra("db",dbi);
                startActivity(intent);
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onRefresh(){
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadNews("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=" + activeTag);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }


    public void LoadNews(String myurl){


        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setOnItemClickListener(new MyOnItemClickListener());
        lv.setOnItemLongClickListener(new MyOnItemClickListener());
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
                    dbi.insertOrUpdate(obj.getString("title"), urlDecoded, obj_image.getString("url"), obj.getString("content"));
                }else{
                    dbi.insertOrUpdate(obj.getString("title"), urlDecoded, null, obj.getString("content"));
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







    private void getAllNews(){
        Cursor cursor = dbi.getAllNews();
        while(cursor.moveToNext()){
            if(cursor.getInt(6)==1 && cursor.getString(5).equals(activeTag)) {
                News news = new News();
                long id = cursor.getLong(0);
                news.setId(id);
                news.setTitle(String.valueOf(Html.fromHtml(cursor.getString(1))));
                news.setSnippet(String.valueOf(Html.fromHtml(news.getTitle() + "<br>" + cursor.getString(4) + "</br>")));
                news.setUrl(cursor.getString(2));
                if(co==true) {
                    news.setImage(cursor.getString(3));
                }
                System.out.println(" ligne : " + cursor.getString(1) + "active : " + cursor.getString(6) + " tag : " + cursor.getString(5));
                actu.add(news);
            }
        }

    }


}

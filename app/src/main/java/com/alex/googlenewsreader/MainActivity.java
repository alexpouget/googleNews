package com.alex.googlenewsreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView)findViewById(R.id.listView);

        String myurl= "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=barack%20obama";
        String[] actu = null;
        URL url = null;
        try {
            url = new URL(myurl);

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
            
            actu = new String[array.length()];
            // Pour tous les objets on récupère les infos
            for (int i = 0; i < array.length(); i++) {
            // On récupère un objet JSON du tableau
                JSONObject obj = new JSONObject(array.getString(i));
                News news = new News();
                news.setTitle(obj.getString("title"));
                actu[i] = news.getTitle();
            }


        } catch (MalformedURLException e) {
                e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        ArrayAdapter<String> arrayActu = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, actu);
        lv.setAdapter(arrayActu);
    }

    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }*/
}

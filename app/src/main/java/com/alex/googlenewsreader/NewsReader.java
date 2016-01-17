package com.alex.googlenewsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class NewsReader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_reader);

        WebView webview = (WebView)findViewById(R.id.webView);

        Intent intent = getIntent();

        webview.loadUrl(intent.getStringExtra("url"));

        Button retour = (Button)findViewById(R.id.retour);
        
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r_intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(r_intent);
            }
        });


    }
}

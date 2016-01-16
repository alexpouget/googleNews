package com.alex.googlenewsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NewsReader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_reader);

        TextView tv = (TextView)findViewById(R.id.textView2);
        Intent intent = getIntent();
        tv.setText(intent.getStringExtra("url"));
    }
}

package com.alex.googlenewsreader;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by alex on 15/01/2016.
 */
public class MyOnItemClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News n = (News)parent.getItemAtPosition(position);
        Intent intent = new Intent(parent.getContext(), NewsReader.class);
        intent.putExtra("url", n.getUrl());
        parent.getContext().startActivity(intent);
    }
}

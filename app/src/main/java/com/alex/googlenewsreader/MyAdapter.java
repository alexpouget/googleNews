package com.alex.googlenewsreader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alex on 16/01/2016.
 */
public class MyAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<News> data;
    private static LayoutInflater layoutInflater;


    public MyAdapter() {
    }

    public MyAdapter(Activity a, ArrayList<News> d) {
        activity = a;
        data=d;
        layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = layoutInflater.inflate(R.layout.row_list, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView snippet = (TextView)vi.findViewById(R.id.snippet); // artist name
        ImageView image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        // Setting all values in listview
        title.setText(data.get(position).getTitle());
        snippet.setText(data.get(position).getSnippet());

        URL url = null;
        try {
            url = new URL(data.get(position).getImage());
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            Bitmap bit = BitmapFactory.decodeStream(in);
            image.setImageBitmap(bit);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vi;
    }
}

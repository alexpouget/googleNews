package com.alex.googlenewsreader;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alex.googlenewsreader.bdd.DataBaseHelper;
import com.alex.googlenewsreader.bdd.Database;

/**
 * Created by alex on 15/01/2016.
 */
public class MyOnItemClickListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener  {

    private long id_del;
    private Context c;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News n = (News)parent.getItemAtPosition(position);
        Intent intent = new Intent(parent.getContext(), NewsReader.class);
        intent.putExtra("url", n.getUrl());
        parent.getContext().startActivity(intent);
    }
    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        c = view.getContext();
        System.out.println("Parent:" + view.getContext());
        News n = (News)parent.getItemAtPosition(position);
        id_del = n.getId();
        AlertDialog.Builder dialogz = new AlertDialog.Builder(view.getContext());

        dialogz.setMessage("Supprimer la news ?");
        dialogz.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.dbi.deactivate(id_del);
                Intent intent = new Intent(c,MainActivity.class);
                c.startActivity(intent);
            }
        });
        dialogz.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("Annuler");
            }
        });
        dialogz.show();


        return true;
    }




}

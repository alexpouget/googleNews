package com.alex.googlenewsreader;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
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
public class MyOnItemClickListener extends DialogFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener  {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News n = (News)parent.getItemAtPosition(position);
        Intent intent = new Intent(parent.getContext(), NewsReader.class);
        intent.putExtra("url", n.getUrl());
        parent.getContext().startActivity(intent);
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
        // Toast.makeText(null,"Inserted in DB = ", Toast.LENGTH_SHORT).show();
        System.out.println("Parent:" + parent.getContext());

        AlertDialog.Builder dialogz = new AlertDialog.Builder(parent.getContext());
        dialogz.setMessage("Supprimer la news tah zebi ?");
        dialogz.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("chiiiiiiiiiiiiiibre");
                //MainActivity.dbi.deactivate(id);
                //System.out.println("chneeeeeeeeeeeekkk");
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

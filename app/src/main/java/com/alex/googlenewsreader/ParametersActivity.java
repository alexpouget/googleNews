package com.alex.googlenewsreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alex.googlenewsreader.bdd.Database;
import com.alex.googlenewsreader.file_manager.FileManager;

import java.io.File;
import java.util.ArrayList;

public class ParametersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);
        //Intent intent = getIntent();
        //final Database db = (Database) intent.getParcelableExtra("db");
        // recuperation des bouttons
        final Button delete_news = (Button)findViewById(R.id.button_delete_news);
        final Button reactivate_news = (Button)findViewById(R.id.button_reactivate_news);

        delete_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParametersActivity.this, MainActivity.class);
                System.out.println("Nb news avant suppression: " + MainActivity.dbi.getNbNews());
                // suppression de toutes les news de la bdd
                MainActivity.dbi.deleteAll();
                System.out.println("Nb news apres suppression: " + MainActivity.dbi.getNbNews());
                // on relance la main activity
                Toast.makeText(ParametersActivity.this, "News supprimées", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        reactivate_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParametersActivity.this, MainActivity.class);

                // Nb de news activées avant réactivation
                System.out.println("Nb news avant reactivation: " + MainActivity.dbi.getNbNewsActivated());

                // Réaction de toutes les news de la bdd
                MainActivity.dbi.reactivateAllNews();

                // Nb de news activées apres réactivation
                System.out.println("Nb news apres reactivation: " + MainActivity.dbi.getNbNewsActivated());

                Toast.makeText(ParametersActivity.this, "News réactivée(s)", Toast.LENGTH_SHORT).show();
                // on relance la main activity
                startActivity(intent);
            }
        });

        //recuperation du spinner(liste deroulante) et affichage des news dans celui ci
        final Spinner oldTags = (Spinner)findViewById(R.id.spinnerOldTags);
        ArrayList<String> listOldTags = new ArrayList<>();
        FileManager fm = new FileManager(ParametersActivity.this);
        listOldTags = fm.Read();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, listOldTags);
        final MainActivity ma = new MainActivity();
        oldTags.setAdapter(arrayAdapter);

        // au click sur un item de la liste on relance les news avec ce tag
        oldTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            Intent intent = new Intent(ParametersActivity.this, MainActivity.class);
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String item = oldTags.getSelectedItem().toString();
                System.out.println("selection: "+item);

                Button applyTag = (Button)findViewById(R.id.applyTag);
                applyTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.activeTag = item;
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




}

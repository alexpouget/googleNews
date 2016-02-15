package com.alex.googlenewsreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.alex.googlenewsreader.bdd.Database;

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
                System.out.println("Nb news avant suppression: "+MainActivity.dbi.getNbNews());
                // suppression de toutes les news de la bdd
                MainActivity.dbi.deleteAll();
                System.out.println("Nb news apres suppression: " + MainActivity.dbi.getNbNews());
                // on relance la main activity
                startActivity(intent);
            }
        });

        reactivate_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParametersActivity.this, MainActivity.class);

                // Nb de news activées avant réactivation
                System.out.println("Nb news avant reactivation: "+MainActivity.dbi.getNbNewsActivated());

                // Réaction de toutes les news de la bdd
                MainActivity.dbi.reactivateAllNews();

                // Nb de news activées apres réactivation
                System.out.println("Nb news apres reactivation: " + MainActivity.dbi.getNbNewsActivated());

                // on relance la main activity
                startActivity(intent);
            }
        });
    }




}

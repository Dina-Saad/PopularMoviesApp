package com.example.dinasaad.popularmoviesapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by DinaSaad on 02/07/2017.
 */
public class DetailActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.TabletContainer, new  DetailFragment())
                    .commit();

         }
    }


}

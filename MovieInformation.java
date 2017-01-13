package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by john.osorio on 11/01/2017.
 */

public class MovieInformation extends AppCompatActivity {

    private TextView movieInformation;
    private ImageView moviePosterThumbnail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_information);


        movieInformation = (TextView)findViewById(R.id.tv_movie_title);
        moviePosterThumbnail = (ImageView)findViewById(R.id.movie_thumbnail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String movieData = extras.getString("EXTRA_MOVIE_DATA");
        String moviePosterPath = extras.getString("EXTRA_MOVIE_POSTER_PATH");

        movieInformation.setText(movieData);
        Picasso.with(this).load(moviePosterPath).into(moviePosterThumbnail);
    }
}

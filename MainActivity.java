package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.MovieAdapter.MovieAdapterOnClickHandler;
import com.example.android.popularmovies.Utilities.JSONUtils;
import com.example.android.popularmovies.Utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private TextView mMovieCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieCategory = (TextView)findViewById(R.id.tv_movie_category);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_movielist);

        mErrorMessageDisplay = (TextView)findViewById(R.id.tv_error_message_display);



        /*
         * LinearLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */

        GridLayoutManager gridManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gridManager);

         /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        /*
         * The MovieAdapter is responsible for linking our weather data with the Views that
         * will end up displaying our weather data.
         */
        mMovieAdapter = new MovieAdapter(this);

        /* Setting the adapter attaches it to the RecyclerView in our layout. */
        mRecyclerView.setAdapter(mMovieAdapter);

        /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         *
         * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
         * circle. We didn't make the rules (or the names of Views), we just follow them.
         */
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //Once all of our views are setup, we then load the popular movie data by default
        loadPopularMovieData();


    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     *
     * @param currentMovieData The weather for the day that was clicked
     */

    //This also displays the information about the clicked movie
    @Override
    public void onClick(String currentMovieData, String currentMoviePosterURL) {
        Context context = this;

        //Let's do an intent here and pass on the data to the MovieInformation class
        Class destinationActivity = MovieInformation.class;
        Intent intentMovieData = new Intent(context, destinationActivity);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_MOVIE_DATA", currentMovieData);
        extras.putString("EXTRA_MOVIE_POSTER_PATH", currentMoviePosterURL);
        intentMovieData.putExtras(extras);
        startActivity(intentMovieData);
    }

    private void loadPopularMovieData(){
        showMovieDataView();
        mMovieCategory.setText("Popular Movies");
        new fetchPopularMovieDBTask().execute();
    }

    private void loadTopRatedMovieData(){
        showMovieDataView();
        mMovieCategory.setText("Top Rated Movies");
        new fetchTopRatedMovieDBTask().execute();
    }

    /**
     * This method will make the View for the movie data visible and
     * hide the error message.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the movie
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }


//    TODO (3) Create a class that logins to movieDB also extends ASYNC for POPULAR movies
    //This is for showing the popular movies
    public class fetchPopularMovieDBTask extends AsyncTask<String, Void, String[][]>{

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected String[][] doInBackground(String... params) {

            URL movieRequestUrl = NetworkUtils.buildPopularMoviesUrl();

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                String[][] jsonMovieData = JSONUtils.getMovieDBStringInfoFromJson(MainActivity.this, jsonMovieResponse);

                return jsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
    }

    //I think this is where the data is loaded in the recycler view
    @Override
    protected void onPostExecute(String[][] jsonMovieData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(jsonMovieData != null){
            showMovieDataView();
            mMovieAdapter.setMovieData(jsonMovieData);
        }
        else{
            showErrorMessage();
        }
    }
}
    //This is for showing the top rated movies
    public class fetchTopRatedMovieDBTask extends AsyncTask<String, Void, String[][]>{

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[][] doInBackground(String... params) {

            URL movieRequestUrl = NetworkUtils.buildTopRatedMoviesUrl();

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                String[][] jsonMovieData = JSONUtils.getMovieDBStringInfoFromJson(MainActivity.this, jsonMovieResponse);

                return jsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[][] jsonMovieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(jsonMovieData != null){
                showMovieDataView();
                mMovieAdapter.setMovieData(jsonMovieData);
            }
            else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_sort_by_popular){
            loadPopularMovieData();
            return true;
        }

        if(id == R.id.action_sort_by_top_rated){
            loadTopRatedMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

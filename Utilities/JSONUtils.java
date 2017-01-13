package com.example.android.popularmovies.Utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by john.osorio on 9/01/2017.
 */

public class JSONUtils {

    /**
     * This method takes in a JSON format Request Token and returns a string array response
     *
     * Movies: poster_path, overview, release_date, vote_average, original_title
     */

    public static String[][] getMovieDBStringInfoFromJson(Context context, String responseFromWebsiteAsURL)
            throws JSONException {

        //POPULAR movies page number
        final String MOVIE_PAGE = context.getString(R.string.page_number);

        //POPULAR movies information. Store it in a list
        final String MOVIE_LIST = context.getString(R.string.results_page);

        //Movie original title
        final String MOVIE_TITLE = context.getString(R.string.original_title);

        //Movie plot / synopsis
        final String MOVIE_OVERVIEW = context.getString(R.string.overview_string);

        //Movie release date
        final String MOVIE_RELEASE_DATE = context.getString(R.string.release_d);

        //movie rating / score
        final String MOVIE_SCORE = context.getString(R.string.vote_score);

        //movie poster path
        final String MOVIE_POSTER = context.getString(R.string.poster_p);
        //String array to hold each movie's data string
        String[][] parsedTopRatedMovieData = null;

        JSONObject moviePageJson = new JSONObject(responseFromWebsiteAsURL);

        //I have no way to know if there is an error

        JSONArray movieArray = moviePageJson.getJSONArray(MOVIE_LIST);

        parsedTopRatedMovieData = new String[movieArray.length()][2];

        //iterate through the list
        for(int i = 0; i < movieArray.length(); i++){
            String title;
            String overview;
            String releaseDate;
            String score;
            String posterPath;
            String posterSize = context.getString(R.string.poster_size);

            //Get the JSON Object representing the current movie
            JSONObject currentMovie = movieArray.getJSONObject(i);

            title = currentMovie.getString(MOVIE_TITLE);
            overview = currentMovie.getString(MOVIE_OVERVIEW);
            releaseDate = currentMovie.getString(MOVIE_RELEASE_DATE);
            score = currentMovie.getString(MOVIE_SCORE);
            posterPath = context.getString(R.string.base_poster_path) + posterSize + currentMovie.getString(MOVIE_POSTER);

            parsedTopRatedMovieData[i][0] = posterPath;
            parsedTopRatedMovieData[i][1] = title + "\n\n" + context.getString(R.string.movie_plot_s) +  overview + "\n\n" + context.getString(R.string.release_date_s) + releaseDate + "\n\n" + context.getString(R.string.user_rating_s) + score;

        }
        return parsedTopRatedMovieData;
    }
}

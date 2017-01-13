package com.example.android.popularmovies.Utilities;

/**
 * Created by john.osorio on 9/01/2017.
 */

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * These utilities will be used to communicate with moviesDB
 */

public class NetworkUtils {
    //This is the base movie URL
    private static String MOVIEDB_BASE_URL = "https://api.themoviedb.org";

    //This only shows the first page
    private static String POPULAR_MOVIES = "3/movie/popular?page=1&language=en-US&api_key=b97894209e7ccf194bb3753807766c01";

    //This only shows the first page
    private static String TOP_RATED_MOVIES = "3/movie/top_rated?page=1&language=en-US&api_key=b97894209e7ccf194bb3753807766c01";

    //base case: http://image.tmdb.org/t/p/image-size/poster_path
    //example: http://image.tmdb.org/t/p/w185//ndlQ2Cuc3cjTL7lTynw6I4boP4S.jpg

    //Hi again, these are the the sizes that I know: "w92", "w154", "w185", "w342", "w500", "w780", or "original";
    //and I think there isn't any other sizes "original" will give you a very large poster, if you're on mobile "w185"
    //is the best choice

    //Use this to create a URL string for popular movies
    public static URL buildPopularMoviesUrl(){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(POPULAR_MOVIES)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.v(TAG, "Built Popular Movies URI " + url);

        return url;
    }

    //Use this to create a URL string for top rated movies
    public static URL buildTopRatedMoviesUrl(){
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendEncodedPath(TOP_RATED_MOVIES)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.v(TAG, "Built Top Rated Movies URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

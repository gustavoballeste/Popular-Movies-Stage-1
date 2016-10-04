package com.balleste.popularmovies.webservices;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.balleste.popularmovies.BuildConfig;
import com.balleste.popularmovies.adapter.Movie;
import com.balleste.popularmovies.listeners.IAsyncListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class to retrieve the movies list from the themoviedb.org
 */
public class GetMoviesAsync extends AsyncTask {

    private IAsyncListener mListener;
    private final String BASE_URL = "http://api.themoviedb.org/3/movie?";
    private final String SORT_PARAM = "sort_by";
    private final String APIKEY_PARAM = "api_key";

    private ArrayList<Movie> mList;

    public GetMoviesAsync(IAsyncListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("IAsyncListener cannot be null");
        }
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onAsyncStart();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String sortOrder = objects[0].toString();
        HttpURLConnection connection;
        BufferedReader reader;
        try {
            Uri buildURI = Uri.parse(BASE_URL).buildUpon()
                    .appendEncodedPath(sortOrder)
                    .appendQueryParameter(APIKEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY).build();

            URL url = new URL(buildURI.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            Log.e("Parada 1", url.toString());

            InputStream inputStream = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            parseJson(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        mListener.onAsyncStop(mList);
    }

    /**
     * Parse the json data to ArrayList of {@link Movie}
     *
     * @param jsonString
     */
    private void parseJson(String jsonString) {
        final String RESULT = "results";
        final String ADULT = "adult";
        final String BACKDROP_PATH = "backdrop_path";
        final String GENRE = "genre_ids";
        final String ID = "id";
        final String ORIGINAL_LANGUAGE = "original_language";
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String POSTER_PATH = "poster_path";
        final String POPULARITY = "popularity";
        final String TITLE = "title";
        final String VIDEO = "video";
        final String VOTE_AVERAGE = "vote_average";
        final String VOTE_COUNT = "vote_count";

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(RESULT);

            mList = new ArrayList<Movie>(jsonArray.length());
            Movie movie;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieJson = jsonArray.getJSONObject(i);
                movie = new Movie();
                movie.setAdult(getIntFromBoolean(movieJson.getBoolean(ADULT)));
                movie.setBackdropPath(movieJson.getString(BACKDROP_PATH));
                movie.setId(movieJson.getInt(ID));
                movie.setOrigLanguage(movieJson.getString(ORIGINAL_LANGUAGE));
                movie.setOrigTitle(movieJson.getString(ORIGINAL_TITLE));
                movie.setOverview(movieJson.getString(OVERVIEW));
                movie.setReleaseDate(movieJson.getString(RELEASE_DATE));
                movie.setPosterPath(movieJson.getString(POSTER_PATH));
                movie.setPopularity(movieJson.getDouble(POPULARITY));
                movie.setTitle(movieJson.getString(TITLE));
                movie.setVideo(getIntFromBoolean(movieJson.getBoolean(VIDEO)));
                movie.setVoteAverage(movieJson.getDouble(VOTE_AVERAGE));
                movie.setVoteCount(movieJson.getInt(VOTE_COUNT));

                JSONArray ids = movieJson.getJSONArray(GENRE);
                int array[] = new int[ids.length()];
                for(int k=0; k<ids.length();k++){
                    int val = ids.getInt(k);
                    array[k] = val;
                }
                movie.setGenreIds(array);

                mList.add(movie);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getIntFromBoolean(boolean value){
        if(value){
            return 1;
        }else{
            return 0;
        }
    }
}

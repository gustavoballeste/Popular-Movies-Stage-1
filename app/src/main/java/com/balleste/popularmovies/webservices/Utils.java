package com.balleste.popularmovies.webservices;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.balleste.popularmovies.R;

public class Utils {
    public static final String IMAGE_PATH = "http://image.tmdb.org/t/p/w185";
    public static final  String IMAGE_PATH_DETAIL = "http://image.tmdb.org/t/p/w342";

    /**
     * Get the Internet Network status ie connected or not
     * @param context
     * @return boolean
     *
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cManager.getActiveNetworkInfo() != null;
    }

    public static String getSortOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_popular));
    }
}

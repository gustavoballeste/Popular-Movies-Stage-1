package com.balleste.popularmovies.listeners;

import com.balleste.popularmovies.adapter.Movie;

import java.util.ArrayList;

/**
 * Interface for AsyncTask to communicate with the fragment.
 */
public interface IAsyncListener {

    /**
     * Called when AsyncTask starts.
     */
    public void onAsyncStart();

    /**
     * Called on AsyncStart complete.
     *
     * @param movieList
     */
    public void onAsyncStop(ArrayList<Movie> movieList);
}

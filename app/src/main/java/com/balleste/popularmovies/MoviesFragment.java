package com.balleste.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.balleste.popularmovies.adapter.Movie;
import com.balleste.popularmovies.adapter.MovieAdapter;
import com.balleste.popularmovies.listeners.IAsyncListener;
import com.balleste.popularmovies.webservices.GetMoviesAsync;
import com.balleste.popularmovies.webservices.Utils;

import java.util.ArrayList;


/**
 * Fragment displays the grid of movies.
 */
public class MoviesFragment extends Fragment implements IAsyncListener {


    private OnFragmentInteractionListener mListener;
    private GridView mGridView;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> mList;
    private GetMoviesAsync mAsync;
    private final String MOVIES_KEY = "movies";
    private final String ASYNC_RUNNING = "async_running";
    private ProgressDialog mProgress;
    private boolean isProgressRunning = false;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.movies_grid);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie) adapterView.getItemAtPosition(i);
                mListener.onMovieSelected(movie);
            }
        });
        if (savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_KEY)) {
            mList = new ArrayList<Movie>();
            if (Utils.isNetworkConnected(getContext())) {
                mAsync = new GetMoviesAsync(this);
                mAsync.execute(Utils.getSortOrder(getContext()));
            } else {
                Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            }
        } else {
            mList = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            onAsyncStop(mList);
       }


        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_KEY, mList);
        outState.putBoolean("",isProgressRunning);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAsyncStart() {
        if(mProgress==null){
            isProgressRunning = true;
            mProgress = ProgressDialog.show(getContext(),"Loading","Please wait...");
        }
    }

    @Override
    public void onAsyncStop(ArrayList<Movie> movieList) {
        if(mProgress!=null){
            mProgress.cancel();
            isProgressRunning = false;
        }
        mList = movieList;
        mAdapter = new MovieAdapter(getContext(), mList);
        mGridView.setAdapter(mAdapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onMovieSelected(Movie movie);
    }

    public void changeOrder(String order) {
        if (Utils.isNetworkConnected(getContext())) {
            mAsync = new GetMoviesAsync(this);
            mAsync.execute(order);
        } else {
            Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
}

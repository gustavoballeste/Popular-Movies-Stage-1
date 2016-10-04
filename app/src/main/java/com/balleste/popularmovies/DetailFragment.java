package com.balleste.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.balleste.popularmovies.adapter.Movie;
import com.balleste.popularmovies.adapter.TrailerAdapter;
import com.balleste.popularmovies.webservices.Utils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Fragment displays the Movie detail.
 */
public class DetailFragment extends Fragment {
    public static final String MOVIE_OBJECT = "movie";

    private Movie mMovie;



    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(MOVIE_OBJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_main, container, false);
        View headerView = inflater.inflate(R.layout.fragment_detail, null);
        setMovieData(headerView);
        ListView listView = (ListView) rootView.findViewById(R.id.detail_list);
        TrailerAdapter adapter = new TrailerAdapter(getContext(), getResources().getStringArray(R.array.trailer_array));
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);

        return rootView;
    }


    private void setMovieData(View view) {
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView year = (TextView) view.findViewById(R.id.year);
        TextView duration = (TextView) view.findViewById(R.id.duration);
        TextView rating = (TextView) view.findViewById(R.id.rating);
        TextView description = (TextView) view.findViewById(R.id.description);
        ImageView image = (ImageView) view.findViewById(R.id.poster_image);
        LinearLayout parent = (LinearLayout) view.findViewById(R.id.parent);

        if (mMovie != null) {
            title.setText(mMovie.getOrigTitle());
            year.setText(getYear(mMovie.getReleaseDate()));
            rating.setText(String.format(getString(R.string.rating_format),mMovie.getVoteAverage()));
            description.setText(mMovie.getOverview());
            Picasso.with(getContext()).load(Utils.IMAGE_PATH + mMovie.getPosterPath()).into(image);
            parent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String getYear(String date){
        String year ="N/A";
        if(date!=null && !date.equals("")) {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
            Date movieDate = null;
            try {
                movieDate = parser.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(movieDate);
            year = "" + cal.get(Calendar.YEAR);
        }
        return  year;
    }

}

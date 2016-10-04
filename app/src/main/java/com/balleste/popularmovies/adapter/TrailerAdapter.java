package com.balleste.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.balleste.popularmovies.R;

/**
 * Class to create list of trailer
 */
public class TrailerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private String[] mTrailerArray;

    public TrailerAdapter(Context context, String[] objects) {
        super(context, 0, objects);
        mContext = context;
        mTrailerArray = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.trailer_row, parent, false);
        }
        TextView trailerName = (TextView) view.findViewById(R.id.trailer_name);
        trailerName.setText(mTrailerArray[position]);
        return view;
    }

}

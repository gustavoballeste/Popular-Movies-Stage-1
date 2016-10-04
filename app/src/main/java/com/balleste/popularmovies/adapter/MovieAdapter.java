package com.balleste.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.balleste.popularmovies.R;
import com.balleste.popularmovies.webservices.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * MovieAdapter create the gridview for movies
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context mContext;
    private List<Movie> mList;


    public static class ViewHolder {
        private TextView name;
        private ImageView image;
    }

    public void setViewHolder(View view, ViewHolder holder) {
        holder.image = (ImageView) view.findViewById(R.id.movie_image);
    }

    public MovieAdapter(Context context, List<Movie> list) {
        super(context, 0, list);
        mContext = context;
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.movie_grid, parent, false);
            holder = new ViewHolder();
            setViewHolder(view, holder);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(mContext).load(Utils.IMAGE_PATH + mList.get(position).getPosterPath()).into(holder.image);
        return view;
    }
}

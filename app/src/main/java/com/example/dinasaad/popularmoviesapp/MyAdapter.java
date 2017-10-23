package com.example.dinasaad.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DinaSaad on 02/07/2017.
 */
public class MyAdapter extends BaseAdapter {
    ArrayList<Movie> results ;
    Context context;
    private LayoutInflater mInflater;
    @Override
    public int getCount() {
        //return results.size();
        if (results == null) return 0;
        else return results.size();
    }
    public MyAdapter(Context context, ArrayList<Movie> Data) {
        results=Data;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    public MyAdapter(Context context)
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public Movie getItem(int i) {
        return results.get(i);
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.image_view, null);
        String MoviePosterPath;
        ImageView imageview = (ImageView) convertView.findViewById(R.id.imageView);
        //picasso

        // Get the JSON object representing the poster_path
        Movie obj = new Movie();
        obj=results.get(i);
        MoviePosterPath = obj.getPoster_path();
        Picasso.with(context).load("http://image.tmdb.org/t/p/w500/"+MoviePosterPath).into(imageview);


        return convertView;
    }
    public void viewimage(String poster_path)
    {

        View convertView= mInflater.inflate(R.layout.fragment_detail, null);
        ImageView imageview_detail = (ImageView) convertView.findViewById(R.id.detailImage);

        Picasso.with(context).load("http://image.tmdb.org/t/p/w500/"+poster_path).into(imageview_detail);

    }
}

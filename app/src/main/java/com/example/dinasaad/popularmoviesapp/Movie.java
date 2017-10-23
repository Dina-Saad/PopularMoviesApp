package com.example.dinasaad.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DinaSaad on 02/07/2017.
 */
public class Movie implements Parcelable {
    String poster_path;
    String overview;
    String release_date;
    String title;
    String vote_average;
    String id;
    int position;
    String IsFavourite;
    ArrayList<Reviews> MOV_Reviews=new ArrayList<Reviews>();
    ArrayList<Trailers> MOV_Trailers = new ArrayList<Trailers>();

    Movie()
    {

    }
    //constructor
   public Movie(String path, String View, String date, String Title, String Vote, String ID, String isFavourite)
    {
        poster_path=path;
        overview=View;
        release_date=date;
        title=Title;
        vote_average = Vote;
        id=ID;
        IsFavourite=isFavourite;

    }
   public Movie(Parcel in) {
        String[] data = new String[7];
        in.readStringArray(data);

        this.poster_path = data[0];
        this.overview =  data[1];
        this.release_date =  data[2];
        this.title =  data[3];
        this.vote_average =  data[4];
        this.id =  data[5];
this.IsFavourite=data[6];

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.poster_path,
                this.overview,
                this.release_date,
                this.title,
                this.vote_average,
                this.id,
                this.IsFavourite});


    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }
        @Override
        public Movie[] newArray(int size) {
            return new Movie [size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Reviews> getMOV_Reviews() {
        return MOV_Reviews;
    }

    public void setMOV_Reviews(ArrayList<Reviews> MOV_Reviews) {
        this.MOV_Reviews = MOV_Reviews;
    }

    public ArrayList<Trailers> getMOV_Trailers() {
        return MOV_Trailers;
    }

    public void setMOV_Trailers(ArrayList<Trailers> MOV_Trailers) {
        this.MOV_Trailers = MOV_Trailers;
    }

    public String isFavourite() {
        return IsFavourite;
    }

    public void setFavourite(String favourite) {
        IsFavourite = favourite;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

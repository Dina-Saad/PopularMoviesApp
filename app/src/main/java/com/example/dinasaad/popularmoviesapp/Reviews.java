package com.example.dinasaad.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DinaSaad on 30/07/2017.
 */

public class Reviews implements Parcelable {
    String author;
    String content;




    Reviews()
    {}
    Reviews(String Author,String Content)
    { author=Author;
        content=Content;}

    public Reviews(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);

        this.author = data[0];
        this.content = data[1];


    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.author,
                this.content});


    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }
        @Override
        public Reviews[] newArray(int size) {
            return new Reviews [size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}



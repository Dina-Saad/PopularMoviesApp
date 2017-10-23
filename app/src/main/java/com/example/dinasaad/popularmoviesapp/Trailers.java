package com.example.dinasaad.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DinaSaad on 30/07/2017.
 */

public class Trailers implements Parcelable {
    String key;
    String name;



    Trailers()
    {}
    Trailers(String Mov_key,String Mov_name)
    {
        key=Mov_key; name=Mov_name;
    }

    public Trailers(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);

        this.key = data[0];
        this.name = data[1];


    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.key,
                this.name});


    }

    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {

        @Override
        public Trailers createFromParcel(Parcel in) {
            return new Trailers(in);
        }
        @Override
        public Trailers[] newArray(int size) {
            return new Trailers [size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    public String getKey() {
        return key;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


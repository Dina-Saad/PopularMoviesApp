package com.example.dinasaad.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by DinaSaad on 01/08/2017.
 */

public class databaseContract implements BaseColumns {
        // COMPLETED (2) Inside create a static final members for the table name and each of the db columns
        public static final String TABLE_NAME = "FavouriteList2";
        public static final String COLUMN_Movie_NAME = "MovieName";
        public static final String COLUMN_Movie_ID = "MovieID";
        /**
         * This it the content authority for DroidTermsExample provider.
         */
        public static final String CONTENT_AUTHORITY = "com.example.dinasaad.popularmoviesapp";

        /**
         * This is the {@link Uri} on which all other DroidTermsExample Uris are built.
         */
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

        /**
         * The path for terms
         */
        public static final String PATH_TERMS = "FavouriteList2";

        /**
         * This is the {@link Uri} used to get a full list of terms and definitions.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TERMS).build();
    }


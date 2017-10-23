package com.example.dinasaad.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dinasaad.popularmoviesapp.data.databaseContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.dinasaad.popularmoviesapp.R.id.FavButton;

/**
 * Created by DinaSaad on 30/07/2017.
 */

public class DetailFragment extends Fragment {
    public Movie moviebundle = new Movie();
    ArrayList<Reviews> Reviews_list = new ArrayList<Reviews>();
    ArrayList<Trailers> Trailers_list = new ArrayList<Trailers>();

    private ArrayAdapter<String> TrailersAdapter;
    private ArrayAdapter<String> ReviewsAdapter;
    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //  View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        moviebundle = new Movie();
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();

        if (extras == null) {
           return null;
        }
        else if (extras != null) {
            moviebundle = (Movie) extras.getParcelable("movieParcelable");
        }


        TrailersAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item, // The name of the layout ID.
                        R.id.list_item_textview); // The ID of the textview to populate.


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        ListView listView = (ListView) rootView.findViewById(R.id.TrailersList);
        listView.setAdapter(TrailersAdapter);

        //Reviews Adapter and list
        ReviewsAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item, // The name of the layout ID.
                        R.id.list_item_textview); // The ID of the textview to populate.

        // Get a reference to thelist_item_forecast.xml ListView, and attach this adapter to it.
        ListView listView1 = (ListView) rootView.findViewById(R.id.ReviewList);
        listView1.setAdapter(ReviewsAdapter);
        FetchMoviesTask MoviesTask = new FetchMoviesTask();
        MoviesTask.execute();

        TrailersAdapter.notifyDataSetChanged();
        ReviewsAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Verify that the intent will resolve to an activity
                String movielink = Trailers_list.get(position).getKey();
                if (new Intent(Intent.ACTION_VIEW, Uri.parse(movielink)) != null) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movielink)));
                }

            }
        });


        ImageView imageview_detail = (ImageView) rootView.findViewById(R.id.detailImage);

        if (intent != null && extras != null) {

            String poster_path = moviebundle.getPoster_path();
            final String original_title = moviebundle.getTitle();
            String release_date = moviebundle.getRelease_date();
            String rate = moviebundle.getVote_average()+"/10";
            String overview = moviebundle.getOverview();
            final Integer Id = Integer.parseInt( moviebundle.getId());
            ((TextView) rootView.findViewById(R.id.detailTitle))
                    .setText(original_title);
            ((TextView) rootView.findViewById(R.id.detailDate))
                    .setText("Release Date: " + release_date);
            ((TextView) rootView.findViewById(R.id.detailRate))
                    .setText("Rate: " + rate);
            ((TextView) rootView.findViewById(R.id.detailDescription))
                    .setText(overview);


            MyAdapter MoviesAdapter = new MyAdapter(getActivity());
            MoviesAdapter.viewimage(poster_path);
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + poster_path).into(imageview_detail);

            ((TextView) rootView.findViewById(R.id.TraText))
                    .setText("Trailers:");

            ((TextView) rootView.findViewById(R.id.overvText))
                    .setText("Overview:");
            ((TextView) rootView.findViewById(R.id.revtext))
                    .setText("Reviews:");


            final Button button = (Button) rootView.findViewById(FavButton);
            if(moviebundle.isFavourite().equals("1"))
                button.setBackgroundColor(getResources().getColor(R.color.gblue));
            else{
                button.setBackgroundColor(getResources().getColor(R.color.gray));
            }

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(moviebundle.isFavourite().equals("0"))
                    {
                        moviebundle.setFavourite("1");
                        addFavouriteMovie(original_title,Id);
                        button.setBackgroundColor(getResources().getColor(R.color.gblue));
                    }
                    else{
                        moviebundle.setFavourite("0");
                        removeFavouriteMovie(Id);
                        button.setBackgroundColor(getResources().getColor(R.color.gray));

                    }
                }
            });

        }
        return rootView;
    }

    public long addFavouriteMovie(String original_title, int Id) {

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(databaseContract.COLUMN_Movie_NAME, original_title);
        contentValues.put(databaseContract.COLUMN_Movie_ID,Id);
        // Insert the content values via a ContentResolver
        Uri uri = getContext().getContentResolver().insert(databaseContract.CONTENT_URI, contentValues);
        if (uri != null)
            return 1;
        else
            return 0;

    }
    public  boolean removeFavouriteMovie(Integer MovieId)
    {

        int id = MovieId;

        // Build appropriate uri with String row id appended
        String stringId = Integer.toString(id);
        Uri uri = databaseContract.CONTENT_URI;
        uri = uri.buildUpon().build();

        // COMPLETED (2) Delete a single row of data using a ContentResolver
        getContext().getContentResolver().delete(uri,
                databaseContract.COLUMN_Movie_ID+"="+MovieId,null);

        return true;
    }
    public class FetchMoviesTask extends AsyncTask<Object, Object, Void> {


        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        FetchMoviesTask() {

        }
        private ArrayList<Trailers> getTrailersDataFromJson(String MoviesJsonStr)
                throws JSONException {
            final String Mov_results = "results";
            String Mov_key;
            JSONObject MoviesJson = new JSONObject(MoviesJsonStr);
            JSONArray MoviesArray = MoviesJson.getJSONArray(Mov_results);
            for (int i = 0; i < MoviesArray.length(); i++) {
                // Get the JSON object representing the poster_path
                JSONObject MovieObject = MoviesArray.getJSONObject(i);

                Mov_key = MovieObject.getString("key");

                String TrailerURL = "https://www.youtube.com/watch?v=" + Mov_key;
                Trailers obj = new Trailers();
                obj.setKey(TrailerURL);
                Trailers_list.add(obj);
            }
            return Trailers_list;

        }

        private ArrayList<Reviews> getReviewsDataFromJson(String MoviesJsonStr)
                throws JSONException {


            final String Mov_results = "results";
            String Mov_author;
            String Mov_contet;

            JSONObject MoviesJson = new JSONObject(MoviesJsonStr);
            JSONArray MoviesArray = MoviesJson.getJSONArray(Mov_results);


            for (int i = 0; i < MoviesArray.length(); i++) {
                Reviews obj = new Reviews();
                // Get the JSON object representing the poster_path
                JSONObject MovieObject = MoviesArray.getJSONObject(i);

                Mov_author = MovieObject.getString("author");
                Mov_contet = MovieObject.getString("content");
                obj.setContent(Mov_contet);
                obj.setAuthor(Mov_author);

                Reviews_list.add(obj);

            }
            return Reviews_list;

        }

        @Override
        protected Void doInBackground(Object... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String MoviesJsonStr = null;
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            try {
                String baseUrl = "";
                baseUrl = "http://api.themoviedb.org/3/movie/" + moviebundle.getId() + "/videos?";


                String apiKey = "api_key=" + BuildConfig.The_Movie_DB_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    // return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    //return null;
                }
                MoviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                //return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                Trailers_list = getTrailersDataFromJson(MoviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            try {
                String baseUrl = "";
                baseUrl = "http://api.themoviedb.org/3/movie/" + moviebundle.getId() + "/reviews?";


                String apiKey = "api_key=" + BuildConfig.The_Movie_DB_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                MoviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                //return null;
            } finally {
                if (urlConnection != null) {
                }
                if (reader != null) {
                    urlConnection.disconnect();
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                //  moviebundle.setMOV_Reviews(getReviewsDataFromJson(MoviesJsonStr));
                Reviews_list = getReviewsDataFromJson(MoviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {


            TrailersAdapter.clear();
            for (int i = 0; i < Trailers_list.size(); i++) {
                String link = Trailers_list.get(i).getKey();

                TrailersAdapter.add(link);


            }

            ReviewsAdapter.clear();
            for (int i = 0; i < Reviews_list.size(); i++) {
                String cont = Reviews_list.get(i).content;
                ReviewsAdapter.add(cont);


            }


        }
    }
}
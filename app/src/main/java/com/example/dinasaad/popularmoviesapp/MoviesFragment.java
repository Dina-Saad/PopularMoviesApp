package com.example.dinasaad.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.dinasaad.popularmoviesapp.data.FavouriteDBHelper;
import com.example.dinasaad.popularmoviesapp.data.Utilities;
import com.example.dinasaad.popularmoviesapp.data.databaseContract;

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

import static com.example.dinasaad.popularmoviesapp.R.id.gridview_Movies;
import static java.lang.Integer.parseInt;


/**
 * Created by DinaSaad on 02/07/2017.
 */
public class MoviesFragment extends Fragment {
    public static SQLiteDatabase DB;
    GridView gridView;
    MyAdapter MoviesAdapter;
    String  unitType;
    String  changedUnitType;
    public static ArrayList<Movie> MovieList = new ArrayList<Movie>();
    ArrayList<Integer> ListFavourite = new ArrayList<Integer>();
    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FavouriteDBHelper dbHelper = new FavouriteDBHelper(getContext());
        DB = dbHelper.getWritableDatabase();
        ListFavourite.clear();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(gridview_Movies);
        // Get a reference to the ListView, and attach this adapter to it.
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Movie MoviesAdapterItem = MoviesAdapter.getItem(position);
                for(int i=0;i<ListFavourite.size();i++)
                    if(parseInt(MoviesAdapterItem.getId())==ListFavourite.get(i))
                    {
                        MoviesAdapterItem.setFavourite("1");
                    }
                MoviesAdapterItem.setPosition(position);
                MoviesAdapter.notifyDataSetChanged();
                if(Utilities.Istablet == true)
                {
                    FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment mfrag = new DetailFragment();
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    Bundle item = new Bundle();
                    item.putParcelable("movieParcelable", MoviesAdapterItem);
                    //mfrag.setArguments(item); //data
                    intent.putExtras(item);
                   // startActivity(intent);
                    t.replace(R.id.TabletContainer, mfrag);
                    t.commit();


                   }
                else
                {
                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                    Bundle extras = new Bundle();
                    extras.putParcelable("movieParcelable", MoviesAdapterItem);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }
    private Cursor getAllFavouriteMovies() {
        // COMPLETED (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
        // Get the content resolver
       // ContentResolver resolver = getContext().getContentResolver();

        // Call the query method on the resolver with the correct Uri from the contract class
        Cursor cursor = getContext().getContentResolver().query(databaseContract.CONTENT_URI,
                null, null, null,
                databaseContract.COLUMN_Movie_NAME);
        return cursor;
    }

    @Override
    public void onStart() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        unitType = sharedPrefs.getString(
                getString(R.string.pref_movies_key),
                getString(R.string.pref_movies_top_rated));
        super.onStart();
        // sort order has changed or the mog.pref_movies_top_ravies list is empty
        if( MovieList.isEmpty() == true  ||changedUnitType != unitType) {
          // fetch the movie list
          updateMovies();
      }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(isNetworkAvailable(getContext())) {
            int id = item.getItemId();
            if (id == R.id.action_Refresh) {

                MovieList.clear();
                updateMovies();
                return true;
            }
            if (id == R.id.action_Favourite) {
                ArrayList<Movie> MovieListFavourite = new ArrayList<Movie>();

                for(int i =0;i<ListFavourite.size();i++)
                {
                    int postitionList = getMoviePositionById(ListFavourite.get(i));

                    Movie m = MovieList.get(postitionList);
                    m.setFavourite("1");
                    MoviesAdapter.notifyDataSetChanged();
                    MovieListFavourite.add(m);
                }
                MovieList.clear();
                MovieList.addAll(MovieListFavourite);
                MoviesAdapter.notifyDataSetChanged();
            }
        }
        else{
            // Provide feedback to user
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
public static int getMoviePositionById (int MovieId)
{
    for(int i = 0;i<MovieList.size();i++)
    {
        if(parseInt(MovieList.get(i).getId()) == MovieId)
        {
            return i;
        }
    }
    return 0;
}
    /**
     * Returns true if network is available or about to become available
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    private void updateMovies() {
        if(isNetworkAvailable(getContext())) {
            // Run network query
            FetchMoviesTask MoviesTask = new FetchMoviesTask();
            MoviesTask.execute();
        }
        else{
            // Provide feedback to user
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }
    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private ArrayList<Movie> getMoviesDataFromJson(String MoviesJsonStr)
                throws JSONException {
            final String Mov_results = "results";
            String Mov_overview;
            String Mov_release_date;
            String Mov_title;
            String Mov_vote_average;
            String Mov_poster;
            String Mov_ID;
            MovieList = new ArrayList<Movie>();
            JSONObject MoviesJson = new JSONObject(MoviesJsonStr);
            JSONArray MoviesArray = MoviesJson.getJSONArray(Mov_results);

            for (int i = 0; i < MoviesArray.length(); i++) {
                // Get the JSON object representing the poster_path
                JSONObject MovieObject = MoviesArray.getJSONObject(i);

                Mov_poster = MovieObject.getString("poster_path");
                Mov_overview = MovieObject.getString("overview");
                Mov_ID = MovieObject.getString("id");
                Mov_release_date = MovieObject.getString("release_date");
                Mov_title = MovieObject.getString("original_title");
                Mov_vote_average = MovieObject.getString("vote_average");

                Movie obj = new Movie(Mov_poster, Mov_overview, Mov_release_date, Mov_title, Mov_vote_average, Mov_ID,"0");

                MovieList.add(obj);
            }
            return MovieList;
        }
        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            Cursor cursor = getAllFavouriteMovies();
            for(int i = 0;i<cursor.getCount();i++)
            {
                //cursor.moveToPosition(i);
                cursor.moveToNext();
                int x = cursor.getInt(cursor.getColumnIndex(databaseContract.COLUMN_Movie_ID));
                ListFavourite.add(x);

            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String MoviesJsonStr = null;
           SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            unitType = sharedPrefs.getString(
                    getString(R.string.pref_movies_key),
                    getString(R.string.pref_movies_top_rated));
            changedUnitType = unitType;

            try {
                String baseUrl = "";
                baseUrl = "http://api.themoviedb.org/3/movie/" + unitType + "?";
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
               return null;
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
                return getMoviesDataFromJson(MoviesJsonStr);
            } catch (JSONException e) {

                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            MoviesAdapter =
                    new MyAdapter(getActivity(), result);
            gridView.setAdapter(MoviesAdapter);
        }

    }


}


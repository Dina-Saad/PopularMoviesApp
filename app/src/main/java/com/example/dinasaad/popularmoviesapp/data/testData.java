package com.example.dinasaad.popularmoviesapp.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DinaSaad on 01/08/2017.
 */

public class testData {
    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(databaseContract.COLUMN_Movie_NAME, "John");
        cv.put(databaseContract.COLUMN_Movie_ID, 12);
        list.add(cv);

        cv = new ContentValues();
        cv.put(databaseContract.COLUMN_Movie_NAME, "Tim");
        cv.put(databaseContract.COLUMN_Movie_ID, 2);
        list.add(cv);

        cv = new ContentValues();
        cv.put(databaseContract.COLUMN_Movie_NAME, "Jessica");
        cv.put(databaseContract.COLUMN_Movie_ID, 99);
        list.add(cv);



        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (databaseContract.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(databaseContract.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }

}

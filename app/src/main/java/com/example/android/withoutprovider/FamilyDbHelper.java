package com.example.android.withoutprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nobody on 2015-02-05.
 */
public class FamilyDbHelper extends SQLiteOpenHelper {
    private final String TAG = "FamilyDbHelper";

    private static final String DATABASE_NAME = "withoutProvider.db";
    private static final int DATABASE_VERSION = 1;

    public FamilyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "FamilyDbHelper()");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");
        db.execSQL(
                "create table " + FamilyContract.FamilyEntry.TABLE_NAME + " (" +
                        FamilyContract.FamilyEntry._ID + " integer primary key, " +
                        FamilyContract.FamilyEntry.COLUMN_NAME_NAME + " text, " +
                        FamilyContract.FamilyEntry.COLUMN_NAME_GENDER + " text, " +
                        FamilyContract.FamilyEntry.COLUMN_NAME_BIRTHDAY + " integer " +
                        ");"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

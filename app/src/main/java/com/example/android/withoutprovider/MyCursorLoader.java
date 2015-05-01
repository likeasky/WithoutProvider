package com.example.android.withoutprovider;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by nobody on 2015-04-23.
 */
public class MyCursorLoader extends CursorLoader {
    private final String TAG = "MyCursorLoader";
    private FamilyDbHelper dbHelper;

    public MyCursorLoader(Context context) {
        super(context);
        Log.d(TAG, "MyCursorLoader()");

        dbHelper = new FamilyDbHelper(context);
    }

    @Override
    public Cursor loadInBackground() {
        Log.d(TAG, "loadInBackground()");

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String table = FamilyContract.FamilyEntry.TABLE_NAME;
        String[] columns = {
                FamilyContract.FamilyEntry._ID,
                FamilyContract.FamilyEntry.COLUMN_NAME_NAME
        };
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor c = db.query(
                table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
        return c;
    }
}

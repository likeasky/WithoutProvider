package com.example.android.withoutprovider;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by nobody on 2015-04-16.
 */
public class MyDb {
    private final String TAG = "MyDb";

    private Context ctx;
    private FamilyDbHelper sqlHelper;
//    private CursorLoaderWithoutProvider loader;
    private Loader loader;

    public MyDb(Context ctx, Loader loader) {
        Log.d(TAG, "MyDb()");
        this.ctx = ctx;
        sqlHelper = new FamilyDbHelper(ctx);
        this.loader = loader;
    }

    void create(ContentValues values) {
        Log.d(TAG, "create()");
        String table = FamilyContract.FamilyEntry.TABLE_NAME;
        String nullColumnHack = null;
        new InsertTask().execute(sqlHelper, table, nullColumnHack, values);
    }

    Cursor read() {
        Log.d(TAG, "read()");
        new ReadTask.execute(table);
    }

    Cursor readAll() {

    }

    void update() {

    }

    void delete() {

    }

    void

    private class InsertTask extends AsyncTask<Object, Void, Long> {
        @Override
        protected Long doInBackground(Object... params) {
            Log.d(TAG, "InsertTask.doInBackground()");
//            FamilyDbHelper sqlHelper = (FamilyDbHelper) params[0];
            String table = (String) params[1];
            String nullColumnHack = (String) params[2];
            ContentValues values = (ContentValues) params[3];

            SQLiteDatabase db = sqlHelper.getWritableDatabase();
            long rowId = db.insert(table, nullColumnHack, values);
            return rowId;
        }

        @Override
        protected void onPostExecute(Long result) {
            Log.d(TAG, "InsertTask.onPostExecute()");
            loader.onContentChanged();
//            #### loader 를 어디서 생성하고 어떻게 넘기는가?
//            ### loadermanager 는 activity에 의존하니, loader도 activity에.. 액티버티가 달라지면 로더아이디에 따른 구분이 의미있나, 달라질까? int일뿐인데..

        }
    }

    private class ReadTask extends AsyncTask<Object, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Object... params) {
            Log.d(TAG, "ReadTask.doInBackground()");
            SQLiteDatabase db = sqlHelper.getReadableDatabase();
            db.query();
        }
    }
}

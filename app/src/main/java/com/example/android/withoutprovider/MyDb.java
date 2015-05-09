package com.example.android.withoutprovider;

import android.content.ContentValues;
import android.content.Context;
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
    private FamilyDbHelper dbHelper;
//    private CursorLoaderWithoutProvider loader;
    private Loader loader;

    public MyDb(Context ctx, Loader loader) {
        Log.d(TAG, "MyDb()");
        this.ctx = ctx;
        dbHelper = new FamilyDbHelper(ctx);
        this.loader = loader;
    }

    void create(ContentValues values) {
        Log.d(TAG, "create()");
        String table = FamilyContract.FamilyEntry.TABLE_NAME;
        String nullColumnHack = null;
        new InsertTask().execute(dbHelper, table, nullColumnHack, values);
    }

    Cursor read() {
        Log.d(TAG, "read()");
//        new ReadTask.execute(table);
        return null;
    }

    Cursor readAll() {
        return null;
    }

    void update(long id, ContentValues values) {
        Log.d(TAG, "update()");
        String table = FamilyContract.FamilyEntry.TABLE_NAME;
        String whereClause = FamilyContract.FamilyEntry._ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        new UpdateTask().execute(table, values, whereClause, whereArgs);
    }

    void delete(long id) {
        Log.d(TAG, "delete()");
        String table = FamilyContract.FamilyEntry.TABLE_NAME;
        String whereClause = FamilyContract.FamilyEntry._ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        new DeleteTask().execute(table, whereClause, whereArgs);
    }

    void delete(long[] ids) {
        Log.d(TAG, "delete(2)");
        String table = FamilyContract.FamilyEntry.TABLE_NAME;
        String str = "";
        String[] whereArgs = new String[ids.length];
        str += "?";
        whereArgs[0] = String.valueOf(ids[0]);
        for (int i = 1; i < ids.length; i++) {
            str += ", ?";
            whereArgs[i] = String.valueOf(ids[i]);
        }
        Log.d(TAG, "str:" + str);
        String whereClause = FamilyContract.FamilyEntry._ID + " in (" + str + ")";
        new DeleteTask().execute(table, whereClause, whereArgs);
    }



    private class InsertTask extends AsyncTask<Object, Void, Long> {
        @Override
        protected Long doInBackground(Object... params) {
            Log.d(TAG, "InsertTask.doInBackground()");
//            FamilyDbHelper dbHelper = (FamilyDbHelper) params[0];
            String table = (String) params[1];
            String nullColumnHack = (String) params[2];
            ContentValues values = (ContentValues) params[3];

            SQLiteDatabase db = dbHelper.getWritableDatabase();
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
            SQLiteDatabase db = dbHelper.getReadableDatabase();
//            db.query();
            return null;
        }
    }

    private class UpdateTask extends AsyncTask<Object, Void, Integer> {
        @Override
        protected Integer doInBackground(Object... params) {
            Log.d(TAG, "UpdateTask.doInBackground()");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String table = (String) params[0];
            ContentValues values = (ContentValues) params[1];
            String whereClause = (String) params[2];
            String[] whereArgs = (String[]) params[3];
            int numOfRows = db.update(table, values, whereClause, whereArgs);
            return numOfRows;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.d(TAG, "UpdateTask.onPostExecute()");
            loader.onContentChanged();
        }
    }

    private class DeleteTask extends AsyncTask<Object, Void, Integer> {
        @Override
        protected Integer doInBackground(Object... params) {
            Log.d(TAG, "DeleteTask.doInBackground()");
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String table = (String) params[0];
            String whereClause = (String) params[1];
            String[] whereArgs = (String[]) params[2];
            int numOfRows = db.delete(table, whereClause, whereArgs);
            return numOfRows;
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.d(TAG, "DeleteTask.onPostExecute()");
            loader.onContentChanged();
        }
    }
}

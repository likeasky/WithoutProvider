package com.example.android.withoutprovider;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.util.Log;

/**
 * Created by nobody on 2015-01-31.
 */
public class CursorLoaderWithoutProvider extends AsyncTaskLoader<Cursor> {
    private String TAG = "CursorLoaderWithoutProvider";

    final ForceLoadContentObserver mObserver;

    Cursor mCursor;
    CancellationSignal mCancellationSignal;

    @Override
    public Cursor loadInBackground() {
        Log.d(TAG, "loadInBackground()");
        synchronized (this) {
            if (isLoadInBackgroundCanceled()) {
                throw new OperationCanceledException();
            }
            mCancellationSignal = new CancellationSignal();
        }
        try {
            FamilyDbHelper dbHelper = new FamilyDbHelper(getContext());
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

            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            if (cursor != null) {
                try {
                    cursor.getCount();
                    cursor.registerContentObserver(mObserver);
                } catch (RuntimeException ex) {
                    cursor.close();
                    throw ex;
                }
            }
            return cursor;
        } finally {
            synchronized (this) {
                mCancellationSignal = null;
            }
        }
    }

    @Override
    public void cancelLoadInBackground() {
        Log.d(TAG, "cancelLoadInBackground()");
        super.cancelLoadInBackground();

        synchronized (this) {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }
    }

    @Override
    public void deliverResult(Cursor cursor) {
        Log.d(TAG, "deliverResult()");
        if (isReset()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    public CursorLoaderWithoutProvider(Context context) {
        super(context);
        Log.d(TAG, "CursorLoaderWithoutProvider()");
        mObserver = new ForceLoadContentObserver();
    }

    public CursorLoaderWithoutProvider(Context context, String args) {
        super(context);
        Log.d(TAG, "CursorLoaderWithoutProvider(2)");
        mObserver = new ForceLoadContentObserver();

    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading()");
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
//        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        Log.d(TAG, "onStopLoading()");
        cancelLoad();
//        super.onStopLoading();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        Log.d(TAG, "onCanceled()");
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        Log.d(TAG, "onReset()");
        super.onReset();

        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }

    @Override
    protected void onForceLoad() {
        Log.d(TAG, "onForceLoad()");
        super.onForceLoad();
    }
}

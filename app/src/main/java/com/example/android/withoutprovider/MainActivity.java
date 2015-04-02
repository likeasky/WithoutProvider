package com.example.android.withoutprovider;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

/*
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 5; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("title", "title_" + i);
            map.put("note", "note_" + i);
            data.add(map);
        }
        int resource = android.R.layout.simple_list_item_1;
        String[] from = {"title"};
        int[] to = {android.R.id.text1};
        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        setListAdapter(adapter);
*/


        int layout = android.R.layout.simple_list_item_1;
        Cursor c = null;
        String[] from = {"title"};
        int[] to = {android.R.id.text1};
        int flags = 0;

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, layout, c, from, to, flags);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, InputFormActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader()");
        return new CursorLoaderWithoutProvider(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished()");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
    }
}

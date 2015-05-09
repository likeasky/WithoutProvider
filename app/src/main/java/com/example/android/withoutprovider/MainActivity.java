package com.example.android.withoutprovider;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class MainActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>, AbsListView.MultiChoiceModeListener {
    private String TAG = "MainActivity";
    private SimpleCursorAdapter mAdapter;
    private MyDb myDb;

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


        int layout = android.R.layout.simple_list_item_multiple_choice;
        Cursor c = null;
        String[] from = { FamilyContract.FamilyEntry.COLUMN_NAME_NAME };
        int[] to = {android.R.id.text1};
        int flags = 0;

        mAdapter = new SimpleCursorAdapter(this, layout, c, from, to, flags);
        setListAdapter(mAdapter);

        Loader loader = getLoaderManager().initLoader(0, null, this);
        myDb = new MyDb(this, loader);

        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu()");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected()");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (item.getItemId()) {
            case R.id.action_plus:
//                String table = FamilyContract.FamilyEntry.TABLE_NAME;
//                String nullColumnHack = null;
//                ContentValues values = new ContentValues();
//                values.put(FamilyContract.FamilyEntry.COLUMN_NAME_NAME, new Random().nextInt(100));
//                myDb.create(table, nullColumnHack, values);
                return true;
            case R.id.action_minus:
                return true;
            case R.id.action_add:
//                Intent intent = new Intent(this, InputFormActivity.class);
//                startActivity(intent);
//                return true;
                AddDialogFragment dialog = new AddDialogFragment();
                dialog.show(getFragmentManager(), "AddDialogFragment");
                return true;
            case R.id.action_settings:
                FamilyDbHelper dbHelper = new FamilyDbHelper(this);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.rawQuery("select * from " + FamilyContract.FamilyEntry.TABLE_NAME, null);
                while (c.moveToNext()) {
                    Log.d(TAG, "id:" + c.getInt(c.getColumnIndexOrThrow(FamilyContract.FamilyEntry._ID)));
                    Log.d(TAG, FamilyContract.FamilyEntry.COLUMN_NAME_NAME + ":" + c.getString(c.getColumnIndex(FamilyContract.FamilyEntry.COLUMN_NAME_NAME)));
                    Log.d(TAG, FamilyContract.FamilyEntry.COLUMN_NAME_GENDER + ":" + c.getString(c.getColumnIndex(FamilyContract.FamilyEntry.COLUMN_NAME_GENDER)));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader()");
        return new MyCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished()");
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
        mAdapter.swapCursor(null);
    }

    void onAddPositiveBtnClick(String name, String gender) {
        Log.d(TAG, "name:" + name + ", gender:" + gender);
        ContentValues values = new ContentValues();
        values.put(FamilyContract.FamilyEntry.COLUMN_NAME_NAME, name);
        values.put(FamilyContract.FamilyEntry.COLUMN_NAME_GENDER, gender);
        myDb.create(values);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // 여러개를 한번에 지울경우 각 아이템이 줄어들때마다 불려짐. onLoadFinished()다음에 지운아이템수만큼 불림.
        Log.d(TAG, "onItemCheckedStateChanged()");
        MenuItem editItem = (MenuItem) mode.getMenu().findItem(R.id.edit);
        Log.d(TAG, "count:" + getListView().getCheckedItemCount());
        if (getListView().getCheckedItemCount() > 1) {
            editItem.setEnabled(false);
        } else {
            editItem.setEnabled(true);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.d(TAG, "onCreateActionMode()");
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.main_actionmode, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Log.d(TAG, "onPrepareActionMode()");
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Log.d(TAG, "onActionItemClicked()");
        long[] ids = getListView().getCheckedItemIds();
        switch (item.getItemId()) {
            case R.id.edit:
                Log.d(TAG, "edit");
                Log.d(TAG, "id:" + ids[0]);

                break;
            case R.id.delete:
                Log.d(TAG, "delete");
                myDb.delete(ids);
//                for (long id : ids) {
//                    // 지우는게 많을 경우 매번 loader와 actionmode의 메소드가 불필요하게 불려짐.
//                    Log.d(TAG, "id:" + id);
//                    myDb.delete(id);
//                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.d(TAG, "onDestroyActionMode()");
    }
}

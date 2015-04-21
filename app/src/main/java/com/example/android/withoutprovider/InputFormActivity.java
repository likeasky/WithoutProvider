package com.example.android.withoutprovider;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by nobody on 2015-04-02.
 */
public class InputFormActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private String TAG = "InputFormActivity";
    private EditText nameET;
    private Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_form);

        nameET = (EditText) findViewById(R.id.nameET);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        Button addButton = (Button) findViewById(R.id.addButton);

        FamilyDbHelper dbHelper = new FamilyDbHelper(this);
        // getWritableDatabase() --> AsyncTask 이용으로 변경.
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String gender = genderSpinner.getSelectedItem().toString();
//                Toast.makeText(InputFormActivity.this, name + ",," + gender, Toast.LENGTH_LONG).show();

                ContentValues values = new ContentValues();
                values.put(FamilyContract.FamilyEntry.COLUMN_NAME_NAME, name);
                values.put(FamilyContract.FamilyEntry.COLUMN_NAME_GENDER, gender);

                long newRowId = db.insert(
                        FamilyContract.FamilyEntry.TABLE_NAME,
                        null,
                        values
                );
                Toast.makeText(getApplicationContext(), "newRowId:" + newRowId, Toast.LENGTH_LONG).show();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    public void onAddButtonClick(View view) {
        String name = nameET.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();
        Toast.makeText(this, name + "," + gender, Toast.LENGTH_LONG).show();
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

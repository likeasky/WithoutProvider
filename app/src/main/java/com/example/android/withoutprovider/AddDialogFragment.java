package com.example.android.withoutprovider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by nobody on 2015-04-27.
 */
public class AddDialogFragment extends DialogFragment {
    private final String TAG = "AddDialogFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add, null);

        final EditText nameET = (EditText) view.findViewById(R.id.nameET);
        final Spinner genderSpinner = (Spinner) view.findViewById(R.id.genderSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(R.string.dialog_add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        Log.d(TAG, "positive Button click");
                        String name = nameET.getText().toString();
                        String gender = genderSpinner.getSelectedItem().toString();
                        ((MainActivity) getActivity()).onAddPositiveBtnClick(name, gender);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        AddDialogFragment.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }
}

package com.example.android.withoutprovider;

import android.provider.BaseColumns;

/**
 * Created by nobody on 2015-02-05.
 */
public final class FamilyContract {
    private String TAG = "FamilyContract";

    public FamilyContract() {

    }

    public class FamilyEntry implements BaseColumns {
        public static final String TABLE_NAME = "family";
    }


}

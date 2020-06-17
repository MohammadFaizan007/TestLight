package com.example.testlite.app;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static final String PREF_NAME = "com.example.testlite";
    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private static final String UNIQUE_KEY = "unique_key";

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    //for fragment
    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    //for getting instance
    public static synchronized PreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
        return sInstance;
    }


    public String getUniqueKey() {
        return mPref.getString(UNIQUE_KEY, "");
    }

    public void setUniqueKey(String value) {
        mPref.edit().putString(UNIQUE_KEY, value).apply();
    }

    public boolean clear() {
        return mPref.edit().clear().commit();
    }
}

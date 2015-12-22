package com.instantly.app.common;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    private static final String APP_ID = "com.instantly.app";
    private static final String KEY_FIRST_TIME_USE = "key_is_first_time_use";
    private static final String KEY_CURRENT_FRAGMENT_INDEX = "key_current_fragment_index";

    private static AppPreference instance;
    private static SharedPreferences mPreference;

    /**
     * Ensure singleton.
     *
     * @param context
     */
    private AppPreference(Context context) {
        mPreference = context.getSharedPreferences(APP_ID, context.MODE_PRIVATE);
    }

    /**
     * Ensure singleton.
     *
     * @param context
     */
    public static AppPreference getInstance(Context context) {
        if (instance == null) {
            instance = new AppPreference(context);
        }
        return instance;
    }

    /**
     * Is first time use the app. Set it false after called.
     *
     * @return true or false
     */
    public boolean isFirstTimeUse() {

        boolean res = mPreference.getBoolean(KEY_FIRST_TIME_USE, true);
        if (res) {
            mPreference.edit().putBoolean(KEY_FIRST_TIME_USE, false).commit();
        }
        return res;
    }

    /**
     * Get current fragment index.
     *
     * @return index
     */
    public int getCurrentFragmentIndex() {
        return mPreference.getInt(KEY_CURRENT_FRAGMENT_INDEX, 0);
    }

    /**
     * Set current fragment index.
     *
     * @param index
     */
    public void setCurrentFragmentIndex(int index) {
        mPreference.edit().putInt(KEY_CURRENT_FRAGMENT_INDEX, index).commit();
    }

}

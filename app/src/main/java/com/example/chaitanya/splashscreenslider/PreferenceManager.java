package com.example.chaitanya.splashscreenslider;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    //shared preference mode
    int PRIVATE_MODE = 0;

    //shared preferences file name
    private static final String PREFERENCE_NAME = "chaitanya-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}

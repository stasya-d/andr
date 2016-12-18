package ru.startandroid.journalofhealth;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

class Preferences {

    static final String APP_PREFERENCES = "APP_PREFERENCES";
    static final String PREF_SETTING_LOCALE = "locale";

    static Integer getRadioButtonIndex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Preferences.APP_PREFERENCES, context.MODE_PRIVATE);
        int savedRadioIndex = 0;
        String savedRadioS = sharedPreferences.getString(PREF_SETTING_LOCALE, "");
        if (savedRadioS.equals("en")) savedRadioIndex = 1;
        Log.d(MainActivity.LOG_TAG, "Load choosing: lan - " + savedRadioS);
        return savedRadioIndex;
    }

    static void savePreferences(String key, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                Preferences.APP_PREFERENCES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}

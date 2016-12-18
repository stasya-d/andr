package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends Activity {

    public static final String LOG_TAG = "myLogs";
    Button mBtnHistory;
    Button mBtnAdd;
    Button mBtnSetting;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreSettings();

        mBtnSetting = (Button) findViewById(R.id.setting);
        mBtnSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        mBtnHistory = (Button) findViewById(R.id.history);
        mBtnHistory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        mBtnAdd = (Button) findViewById(R.id.add);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    void restoreSettings() {
        Log.d(LOG_TAG, "Load mBtnSetting");
        SharedPreferences sharedPreferences = getSharedPreferences(
                Preferences.APP_PREFERENCES, MODE_PRIVATE);
        String savedRadioS = sharedPreferences.getString(
                Preferences.PREF_SETTING_LOCALE, "");
        Locale locale = new Locale(savedRadioS);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_main);
    }
}

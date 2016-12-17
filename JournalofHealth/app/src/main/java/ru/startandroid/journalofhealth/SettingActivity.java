package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingActivity extends Activity {

    public static final String LOG_TAG = "myLogs";
    public static String APP_PREFERENCES = "APP_P";
    public static String nowLang;
    final String KEY_RADIOBUTTON_INDEX = "locale";
    RadioGroup radioGroup;
    Button save;
    private Button load;
    private Button loadJSON;
    private Button saveJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_leng);
        radioGroup
                .setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

        LoadPreferences();

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SavePreferences(KEY_RADIOBUTTON_INDEX, nowLang);
                Log.d(LOG_TAG, "Saved settings");
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        load = (Button) findViewById(R.id.loadXML);
        load.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Xml.loadXML(SettingActivity.this);
                Toast toast =
                        Toast.makeText(getApplicationContext(), getString(R.string.add) + ".", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        loadJSON = (Button) findViewById(R.id.loadJSON);
        loadJSON.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String stringJSON = WorkFile.readFile(SettingActivity.this);
                Toast toast =
                        Toast.makeText(getApplicationContext(), stringJSON, Toast.LENGTH_SHORT);
                toast.show();
                Json.parse(stringJSON, SettingActivity.this);
            }
        });
        saveJSON = (Button) findViewById(R.id.saveJSON);
        saveJSON.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String json = Json.pack(SettingActivity.this);
                Toast toast = Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT);
                toast.show();
                WorkFile.writeFile(json, SettingActivity.this);
            }
        });
    }


    RadioGroup.OnCheckedChangeListener radioGroupOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton checkedRadioButton = (RadioButton) radioGroup
                    .findViewById(checkedId);
            int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);

            Log.d(LOG_TAG, "RadioGroup choose: " + checkedIndex);

            String leng = new String("");
            switch (checkedIndex) {
                case 0:
                    leng = "ru";
                    break;
                case 1:
                    leng = "en";
                    break;
                default:
                    break;
            }
            nowLang = new String(leng);
        }
    };

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APP_PREFERENCES, MODE_PRIVATE);
        int savedRadioIndex = 0;
        String savedRadioS = sharedPreferences.getString(
                KEY_RADIOBUTTON_INDEX, "");
        if (savedRadioS.equals("en")) {
            savedRadioIndex = 1;
        }
        RadioButton savedCheckedRadioButton = (RadioButton) radioGroup
                .getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);
        Log.d(LOG_TAG, "Load choosing: lan - " + savedRadioS);
    }

}
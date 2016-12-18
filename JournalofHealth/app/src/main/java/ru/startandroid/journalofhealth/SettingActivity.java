package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingActivity extends Activity {

    public static final String LOG_TAG = "myLogs";
    private static String nowLang;
    final String KEY_RADIOBUTTON_INDEX = "locale";

    RadioGroup mRadioGroup;
    Button mSave;
    Button mLoad;
    Button mLoadJSON;
    Button mSaveJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_leng);
        mRadioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
        RadioButton savedCheckedRadioButton = (RadioButton) mRadioGroup
                .getChildAt(Preferences.getRadioButtonIndex(SettingActivity.this));
        savedCheckedRadioButton.setChecked(true);

        mSave = (Button) findViewById(R.id.save);
        mSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Preferences.savePreferences(KEY_RADIOBUTTON_INDEX, nowLang, SettingActivity.this);
                Log.d(LOG_TAG, "Saved settings");
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.saved), Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        mLoad = (Button) findViewById(R.id.loadXML);
        mLoad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Xml.loadXML(SettingActivity.this);
                Toast toast =
                        Toast.makeText(getApplicationContext(), getString(R.string.add) + ".", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        mLoadJSON = (Button) findViewById(R.id.loadJSON);
        mLoadJSON.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String stringJSON = WorkFile.readFile(SettingActivity.this);
                Toast toast =
                        Toast.makeText(getApplicationContext(), stringJSON, Toast.LENGTH_SHORT);
                toast.show();
                Json.parse(stringJSON, SettingActivity.this);
            }
        });
        mSaveJSON = (Button) findViewById(R.id.saveJSON);
        mSaveJSON.setOnClickListener(new View.OnClickListener() {
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

            RadioButton checkedRadioButton = (RadioButton) mRadioGroup
                    .findViewById(checkedId);
            int checkedIndex = mRadioGroup.indexOfChild(checkedRadioButton);

            Log.d(LOG_TAG, "RadioGroup choose: " + checkedIndex);

            String leng = "";
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
            nowLang = leng;
        }
    };

}
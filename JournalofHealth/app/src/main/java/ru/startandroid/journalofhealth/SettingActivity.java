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
        radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
        RadioButton savedCheckedRadioButton = (RadioButton) radioGroup
                .getChildAt(Preferences.getRadioButtonIndex(SettingActivity.this));
        savedCheckedRadioButton.setChecked(true);

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
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

}
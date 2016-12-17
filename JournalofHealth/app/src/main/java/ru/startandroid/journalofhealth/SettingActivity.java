package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import java.io.InputStream;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class SettingActivity extends Activity {

    public static final String LOG_TAG = "myLogs";
    public static String APP_PREFERENCES = "APP_P";
    public static String nowLang;
    public static String filename = "load_results.xml";
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
                loadXML();
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

    private void loadXML() {
        Log.d(LOG_TAG, "load");
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

                boolean data = false;
                boolean high = false;
                boolean low = false;
                boolean pulse = false;
                boolean sugar = false;
                boolean comment = false;
                String tdata = "";
                String thigh = "";
                String tlow = "";
                String tpulse = "";
                String tsugar = "";
                String tcomment = "";

                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes)
                        throws SAXException {
                    if (qName.equalsIgnoreCase("data")) {
                        data = true;
                    }
                    if (qName.equalsIgnoreCase("high")) {
                        high = true;
                    }
                    if (qName.equalsIgnoreCase("low")) {
                        low = true;
                    }
                    if (qName.equalsIgnoreCase("pulse")) {
                        pulse = true;
                    }
                    if (qName.equalsIgnoreCase("sugar")) {
                        sugar = true;
                    }
                    if (qName.equalsIgnoreCase("comment")) {
                        comment = true;
                    }
                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("result")) {
                        DBHelper dbHelper = new DBHelper(SettingActivity.this);
                        dbHelper.add(tdata, thigh, tlow, tpulse, tsugar, tcomment);
                        tdata = "";
                        thigh = "";
                        tlow = "";
                        tpulse = "";
                        tsugar = "";
                        tcomment = "";
                    }
                }

                public void characters(char ch[], int start, int length)
                        throws SAXException {

                    if (data) {
                        tdata = new String(ch, start, length);
                        data = false;
                    }
                    if (high) {
                        thigh = new String(ch, start, length);
                        high = false;
                    }
                    if (low) {
                        tlow = new String(ch, start, length);
                        low = false;
                    }
                    if (pulse) {
                        tpulse = new String(ch, start, length);
                        pulse = false;
                    }
                    if (sugar) {
                        tsugar = new String(ch, start, length);
                        sugar = false;
                    }
                    if (comment) {
                        tcomment = new String(ch, start, length);
                        comment = false;
                    }
                }
            };

            InputStream is = getAssets().open(filename);
            saxParser.parse(is, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
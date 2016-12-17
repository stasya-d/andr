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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

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

    final String LOG_TAG = "myLogs";
    public static String APP_PREFERENCES = "APP_P";
    public static String nowLang;
    public static String filename = "load_results.xml";
    public static String filenameJSON = "json1.json";
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
                String stringJSON = readFile();

                Toast toast =
                        Toast.makeText(getApplicationContext(), stringJSON, Toast.LENGTH_SHORT);
                toast.show();
                parseJson(stringJSON);
            }
        });
        saveJSON = (Button) findViewById(R.id.saveJSON);
        saveJSON.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String json = inJson();
                Toast toast = Toast.makeText(getApplicationContext(), json, Toast.LENGTH_SHORT);
                toast.show();
                writeFile(json);
            }
        });
    }

    public void writeFile(String text) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(filenameJSON, MODE_PRIVATE)));
            // пишем данные
            bw.write(text);
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile() {
        String res = "";
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(filenameJSON)));

            // читаем содержимое
            String str = "";
            while ((str = br.readLine()) != null) {
                Log.d(LOG_TAG, "read - " + str);
                res += str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
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

    private void parseJson(String jsonString) {
        JSONObject mainJsonObject;
        try {
            mainJsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = mainJsonObject.getJSONArray("results");
            String numbers = "";
            DBHelper dbHelper = new DBHelper(SettingActivity.this);
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject res = new JSONObject(resultsArray.getString(i));
                String data = res.getString("data");
                String high = res.getString("high");
                String low = res.getString("low");
                String pulse = res.getString("pulse");
                String sugar = res.getString("sugar");
                String comment = res.getString("comment");
                dbHelper.add(data, high, low, pulse, sugar, comment);
            }

            Log.d("TEST", numbers);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String inJson() {
        String json = "{}";
        JSONArray resultsArray = new JSONArray();
        DBHelper dbHelper = new DBHelper(SettingActivity.this);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- Rows in: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("myTable", null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int dataColIndex = c.getColumnIndex("data");
            int highColIndex = c.getColumnIndex("high");
            int lowColIndex = c.getColumnIndex("low");
            int pulseColIndex = c.getColumnIndex("pulse");
            int sugarColIndex = c.getColumnIndex("sugar");
            int commentColIndex = c.getColumnIndex("comment");
            try {
                do {
                    JSONObject result = new JSONObject();
                    result.put("data", c.getString(dataColIndex));
                    result.put("high", c.getString(highColIndex));
                    result.put("low", c.getString(lowColIndex));
                    result.put("pulse", c.getString(pulseColIndex));
                    result.put("sugar", c.getString(sugarColIndex));
                    result.put("comment", c.getString(commentColIndex));
                    resultsArray.put(result);

                } while (c.moveToNext());
                JSONObject results = new JSONObject();
                results.put("results", resultsArray);
                /*{
                    "results":*/
                json = new String(results.toString());
                Log.d(LOG_TAG, json);
            } catch (JSONException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
        return json;
    }
}
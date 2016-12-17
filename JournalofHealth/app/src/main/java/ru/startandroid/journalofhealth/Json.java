package ru.startandroid.journalofhealth;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stasya on 17.12.2016.
 */

public class Json {
    public static void parse(String jsonString, Context context) {
        JSONObject mainJsonObject;
        try {
            mainJsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = mainJsonObject.getJSONArray("results");
            String numbers = "";
            DBHelper dbHelper = new DBHelper(context);
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

    public static String pack(Context context) {
        String json = "{}";
        JSONArray resultsArray = new JSONArray();
        DBHelper dbHelper = new DBHelper(context);
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(MainActivity.LOG_TAG, "--- Rows in: ---");
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
                json = new String(results.toString());
                Log.d(MainActivity.LOG_TAG, json);
            } catch (JSONException e) {
                //Toast toast = Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT);
                //toast.show();
            }
        }
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
        return json;
    }
}

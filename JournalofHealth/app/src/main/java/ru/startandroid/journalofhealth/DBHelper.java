package ru.startandroid.journalofhealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class DBHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myTable", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "-------- onCreate database");
        // создаем таблицу с полями
        db.execSQL("create table myTable ("
                + "id integer primary key autoincrement,"
                + "data text,"
                + "high text,"
                + "low text,"
                + "pulse text,"
                + "sugar text,"
                + "comment text" + ");");
    }

    public void add(String data,
                    String high,
                    String low,
                    String pulse,
                    String sugar,
                    String comment) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = getWritableDatabase();
        Log.d(LOG_TAG, "--- Insert in: ---");
        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put("data", data);
        cv.put("high", high);
        cv.put("low", low);
        cv.put("pulse", pulse);
        cv.put("sugar", sugar);
        cv.put("comment", comment);
        // вставляем запись и получаем ее ID
        long rowID = db.insert("myTable", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        // закрываем подключение к БД
        close();
    }

    List<Result> initializeData(String pressure, String pulse, String sugar, String comment) {
        List<Result> results;
        results = new ArrayList<>();
        // подключаемся к БД
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(LOG_TAG, "--- Rows in: ---");
        Cursor c = db.query("myTable", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int dateColIndex = c.getColumnIndex("data");
            int highColIndex = c.getColumnIndex("high");
            int lowColIndex = c.getColumnIndex("low");
            int pulseColIndex = c.getColumnIndex("pulse");
            int sugarColIndex = c.getColumnIndex("sugar");
            int commentColIndex = c.getColumnIndex("comment");

            do {
                results.add(new Result(pressure,
                        pulse,
                        sugar,
                        comment,
                        c.getString(dateColIndex),
                        c.getString(highColIndex),
                        c.getString(lowColIndex),
                        c.getString(pulseColIndex),
                        c.getString(sugarColIndex),
                        c.getString(commentColIndex),
                        R.drawable.goodsmal));
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        this.close();
        return results;
    }

    void clear() {
        SQLiteDatabase db = getWritableDatabase();
        // закрываем подключение к БД
        Log.d(LOG_TAG, "--- Clear: ---");
        // удаляем все записи
        int clearCount = db.delete("myTable", null, null);
        Log.d(LOG_TAG, "deleted rows count = " + clearCount);
        close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "myTable");
        onCreate(db);
    }
}
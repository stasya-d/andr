package ru.startandroid.journalofhealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    public DBHelper(Context context) {
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "myTable");
        onCreate(db);
    }
}
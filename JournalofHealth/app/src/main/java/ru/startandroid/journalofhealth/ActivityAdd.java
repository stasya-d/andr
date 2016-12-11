package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

public class ActivityAdd extends Activity {
    Button btnAdd, btnClear;
    EditText enterHigh, enterLow, enterPulse, enterSugar, enterComment;
    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        dbHelper = new DBHelper(this);

        btnAdd = (Button) findViewById(R.id.ok);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // создаем объект для данных
                ContentValues cv = new ContentValues();

                enterHigh = (EditText) findViewById(R.id.high);
                enterLow = (EditText) findViewById(R.id.low);
                enterPulse = (EditText) findViewById(R.id.pulse);
                enterSugar = (EditText) findViewById(R.id.sugarEnter);
                enterComment = (EditText) findViewById(R.id.commentEnter);


                // получаем данные из полей ввода
                String data = (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());
                String high = enterHigh.getText().toString();
                String low = enterLow.getText().toString();
                String pulse = enterPulse.getText().toString();
                String sugar = enterSugar.getText().toString();
                String comment = enterComment.getText().toString();


                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();
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
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.add) + ": " + data + ". " + getString(R.string.pressure) + " " +
                                high + "/" + low + ". " + getString(R.string.pulse) + pulse + ". " +
                                getString(R.string.sugar)  + sugar + ".", Toast.LENGTH_SHORT);
                toast.show();

                // закрываем подключение к БД
                dbHelper.close();
                startService(new Intent(ActivityAdd.this, MyService.class));
            }
        });

        btnClear = (Button) findViewById(R.id.cancel);
        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterHigh = (EditText) findViewById(R.id.high);
                enterHigh.setText("");
                enterLow = (EditText) findViewById(R.id.low);
                enterLow.setText("");
                enterPulse = (EditText) findViewById(R.id.pulse);
                enterPulse.setText("");
                enterSugar = (EditText) findViewById(R.id.sugarEnter);
                enterSugar.setText("");
                enterComment = (EditText) findViewById(R.id.commentEnter);
                enterComment.setText("");
            }
        });
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myTable", null, 4);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
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

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + "myTable");
            onCreate(db);
        }
    }
}

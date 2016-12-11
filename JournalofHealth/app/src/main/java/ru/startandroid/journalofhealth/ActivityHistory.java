package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistory extends Activity {

    private List<BlankOfResult> persons;
    private RecyclerView rv;
    Button btnDeleteAll;
    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    final int DIALOG_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        dbHelper = new DBHelper(this);

        btnDeleteAll = (Button) findViewById(R.id.deleteAll);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_DELETE);
            }
        });

        updateList();
    }

    protected void updateList() {
        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

        TextView textCountResualt = (TextView) findViewById(R.id.text_result);
        String pluralRes = this.getResources().getQuantityString(R.plurals.result, persons.size(), persons.size());
        textCountResualt.setText(getString(R.string.list) + "\n" + "("  + pluralRes + ")");

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DELETE) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(R.string.deleting);
            // сообщение
            adb.setMessage(R.string.questionDelete);
            // иконка
            adb.setIcon(R.mipmap.ic);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.yes, myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.no, myClickListener);
            // кнопка нейтрального ответа
            adb.setNeutralButton(R.string.cancel, myClickListener);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:

                    // подключаемся к БД
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    // закрываем подключение к БД
                    Log.d(LOG_TAG, "--- Clear: ---");
                    // удаляем все записи
                    int clearCount = db.delete("myTable", null, null);
                    Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                    dbHelper.close();
                    updateList();

                    Toast.makeText(ActivityHistory.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
                // нейтральная кнопка
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };

    private void initializeData() {
        persons = new ArrayList<>();
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- Rows in: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("myTable", null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int dateColIndex = c.getColumnIndex("data");
            int highColIndex = c.getColumnIndex("high");
            int lowColIndex = c.getColumnIndex("low");
            int pulseColIndex = c.getColumnIndex("pulse");
            int sugarColIndex = c.getColumnIndex("sugar");
            int commentColIndex = c.getColumnIndex("comment");


            do {
                String allInf = "";
                if (c.getString(highColIndex).length() > 0) {
                    allInf = getString(R.string.pressure) + " " + c.getString(highColIndex) + "/" + c.getString(lowColIndex) + "\n";
                }
                if (c.getString(pulseColIndex).length() > 0) {
                    allInf = allInf + getString(R.string.pulse) + " " + c.getString(pulseColIndex) + "\n";
                }
                if (c.getString(sugarColIndex).length() > 0) {
                    allInf = allInf + getString(R.string.sugar) + " " + c.getString(sugarColIndex) + "\n";
                }
                if (c.getString(commentColIndex).length() > 0) {
                    allInf = allInf + getString(R.string.comment) + " " + c.getString(commentColIndex);
                }
                // получаем значения по номерам столбцов и пишем все в лог
                persons.add(new BlankOfResult(allInf, c.getString(dateColIndex),
                        c.getString(highColIndex),
                        c.getString(lowColIndex),
                        c.getString(pulseColIndex),
                        c.getString(sugarColIndex),
                        c.getString(commentColIndex),
                        R.drawable.goodsmal));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        // закрываем подключение к БД
        dbHelper.close();
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }

    class DBHelper extends SQLiteOpenHelper {

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

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + "myTable");
            onCreate(db);
        }
    }
}

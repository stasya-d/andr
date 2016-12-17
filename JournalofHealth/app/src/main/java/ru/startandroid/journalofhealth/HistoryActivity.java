package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends Activity {

    private List<Result> persons;
    private RecyclerView rv;
    private Button btnDeleteAll;
    private DBHelper dbHelper = new DBHelper(this);
    final String LOG_TAG = "myLogs";
    final int DIALOG_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

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
        updateTextViewCount();
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

                    dbHelper.clear();
                    updateList();
                    Toast.makeText(HistoryActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();

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
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "--- Rows in: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("myTable", null, null, null, null, null, null);
        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            int dateColIndex = c.getColumnIndex("data");
            int highColIndex = c.getColumnIndex("high");
            int lowColIndex = c.getColumnIndex("low");
            int pulseColIndex = c.getColumnIndex("pulse");
            int sugarColIndex = c.getColumnIndex("sugar");
            int commentColIndex = c.getColumnIndex("comment");

            do {
                persons.add(new Result(getString(R.string.pressure),
                        getString(R.string.pulse),
                        getString(R.string.sugar),
                        getString(R.string.comment),
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
        // закрываем подключение к БД
        dbHelper.close();
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }

    private void updateTextViewCount() {
        String pluralRes =
                this.getResources().getQuantityString(R.plurals.result, persons.size(), persons.size());
        TextView textCountResualt = (TextView) findViewById(R.id.text_result);
        textCountResualt.setText(getString(R.string.list) + "\n" + "(" + pluralRes + ")");

    }

}

package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity {

    public DBHelper dbHelper = new DBHelper(this);
    final String LOG_TAG = "myLogs";
    EditText mEnterHigh, mEnterLow, mEnterPulse, mEnterSugar, mEnterComment;
    Button mBtnAdd, mBtnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mBtnAdd = (Button) findViewById(R.id.ok);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mEnterHigh = (EditText) findViewById(R.id.high);
                mEnterLow = (EditText) findViewById(R.id.low);
                mEnterPulse = (EditText) findViewById(R.id.pulse);
                mEnterSugar = (EditText) findViewById(R.id.sugarEnter);
                mEnterComment = (EditText) findViewById(R.id.commentEnter);

                // получаем данные из полей ввода
                String data = DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString();
                String high = mEnterHigh.getText().toString();
                String low = mEnterLow.getText().toString();
                String pulse = mEnterPulse.getText().toString();
                String sugar = mEnterSugar.getText().toString();
                String comment = mEnterComment.getText().toString();

                dbHelper.add(data, high, low, pulse, sugar, comment);
                Result blank = new Result();
                String it = blank.getAll(getString(R.string.pressure),
                        getString(R.string.pulse),
                        getString(R.string.sugar),
                        getString(R.string.comment),
                        high, low, pulse, sugar, comment);

                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.add) + ": " + it, Toast.LENGTH_SHORT);
                toast.show();

                // закрываем подключение к БД
                dbHelper.close();
                startService(new Intent(AddActivity.this, MyService.class));
            }
        });

        mBtnClear = (Button) findViewById(R.id.cancel);
        mBtnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEnterHigh = (EditText) findViewById(R.id.high);
                mEnterHigh.setText("");
                mEnterLow = (EditText) findViewById(R.id.low);
                mEnterLow.setText("");
                mEnterPulse = (EditText) findViewById(R.id.pulse);
                mEnterPulse.setText("");
                mEnterSugar = (EditText) findViewById(R.id.sugarEnter);
                mEnterSugar.setText("");
                mEnterComment = (EditText) findViewById(R.id.commentEnter);
                mEnterComment.setText("");
            }
        });
    }
}

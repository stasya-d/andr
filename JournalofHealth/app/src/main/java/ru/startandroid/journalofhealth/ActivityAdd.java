package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityAdd extends Activity {
    Button btnAdd, btnClear;
    EditText enterHigh, enterLow, enterPulse, enterSugar, enterComment;
    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        dbHelper = new DBHelper(this);

        btnAdd = (Button) findViewById(R.id.ok);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

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

                dbHelper.add(data, high, low, pulse, sugar, comment);
                BlankOfResult blank;
                blank = new BlankOfResult();
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
}

package ru.startandroid.journalofhealth;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class HistoryActivity extends Activity {

    private List<Result> results;
    private RecyclerView rv;
    private DBHelper dbHelper = new DBHelper(this);
    final String LOG_TAG = "myLogs";
    Button mBtnDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mBtnDeleteAll = (Button) findViewById(R.id.deleteAll);
        mBtnDeleteAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DeleteDialogFragment dialog = new DeleteDialogFragment();
                dialog.show(getFragmentManager(), "delete");
            }
        });
        updateList();
    }

    @Override
    public void recreate() {
        updateList();
    }

    private void updateList() {
        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        results = dbHelper.initializeData(getString(R.string.pressure),
                getString(R.string.pulse),
                getString(R.string.sugar),
                getString(R.string.comment));
        initializeAdapter();
        updateTextViewCount();
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(results);
        rv.setAdapter(adapter);
    }

    private void updateTextViewCount() {
        String pluralRes =
                this.getResources().getQuantityString(R.plurals.result, results.size(), results.size());
        TextView textCountResualt = (TextView) findViewById(R.id.text_result);
        textCountResualt.setText(getString(R.string.list) + "\n" + "(" + pluralRes + ")");

    }
}

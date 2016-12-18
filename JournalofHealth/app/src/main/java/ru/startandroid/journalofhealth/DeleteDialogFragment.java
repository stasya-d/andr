package ru.startandroid.journalofhealth;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class DeleteDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.deleting);
        adb.setMessage(R.string.questionDelete);
        adb.setIcon(R.mipmap.ic);
        adb.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DBHelper(getActivity()).clear();
                getActivity().recreate();
                Log.d(MainActivity.LOG_TAG, "yes");
            }
        });
        adb.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(MainActivity.LOG_TAG, "no");
            }
        });
        adb.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(MainActivity.LOG_TAG, "cancel");
            }
        });
        return adb.create();
    }
}

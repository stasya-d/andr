package ru.startandroid.journalofhealth;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class WorkFile {

    private static final String FILE_NAME_JSON = "json1.json";

    public static void writeFile(String text, Context context) {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(FILE_NAME_JSON, Context.MODE_PRIVATE)));
            // пишем данные
            bw.write(text);
            // закрываем поток
            bw.close();
            Log.d(MainActivity.LOG_TAG, "write file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context) {
        String res = "";
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput(FILE_NAME_JSON)));

            // читаем содержимое
            String str = "";
            while ((str = br.readLine()) != null) {
                Log.d(MainActivity.LOG_TAG, "read file - " + str);
                res += str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}

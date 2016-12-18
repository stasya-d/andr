package ru.startandroid.journalofhealth;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    final String LOG_TAG = "myLogs";
    final int notificationID = 0;
    private Boolean boolNotification;

    Thread mThreadNotification;
    NotificationManager mNotificationManager;
    Notification mNtfAddResult;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mThreadNotification != null) {
            Thread dummy = mThreadNotification;
            mThreadNotification = null;
            dummy.interrupt();
        }
        boolNotification = true;
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
            boolNotification = false;
        }
        mThreadNotification = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 20; i++) {
                        TimeUnit.SECONDS.sleep(1);
                        Log.d(LOG_TAG, "" + i);
                    }
                    sendNotif();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.interrupted();
            }
        });
        mThreadNotification.start();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_add));
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNtfAddResult = mBuilder.build();
        mNtfAddResult.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;
        mNtfAddResult.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(notificationID, mNtfAddResult);
        boolNotification = true;
    }

    public IBinder onBind(Intent arg0) {
        Log.d(LOG_TAG, "close");
        return null;
    }

}

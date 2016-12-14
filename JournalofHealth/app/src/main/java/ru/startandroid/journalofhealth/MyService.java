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
    Thread threadNotification;
    NotificationManager mNotificationManager;
    final int notificationID = 0;
    Notification notification;
    Boolean boolNotif;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (threadNotification != null) {
            Thread dummy = threadNotification;
            threadNotification = null;
            dummy.interrupt();
        }
        boolNotif = true;
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
            boolNotif = false;
        }
        threadNotification = new Thread(new Runnable() {
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
            }
        });
        threadNotification.start();
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif() {
     //   Context context = getApplicationContext();
     //   Intent notificationIntent = new Intent(Intent.ACTION_VIEW,
     //           Uri.parse("http://developer.alexanderklimov.ru/android/"));
     //   PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
     //           notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

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
        notification = mBuilder.build();
        notification.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(notificationID, notification);
        boolNotif = true;
    }

    public IBinder onBind(Intent arg0) {
        Log.d(LOG_TAG, "close");
        return null;
    }

}

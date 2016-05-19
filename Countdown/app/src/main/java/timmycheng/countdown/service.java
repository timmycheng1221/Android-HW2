package timmycheng.countdown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;

public class service extends Service{
    private Handler handler = new Handler();
    long timeRemaining;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        CountDownTimer timer = new CountDownTimer(Long.valueOf(Pref.getRemain(this)), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {;
                timeRemaining = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                showNotification();
            }
        }.start();
    }

    protected void showNotification() {
        NotificationManager barManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder barMsg = new Notification.Builder(this)
                .setTicker("時間到囉！")
                .setContentTitle("時間到囉！")
                .setContentText("通知一下")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        barManager.notify(0,barMsg.build());
    }
}

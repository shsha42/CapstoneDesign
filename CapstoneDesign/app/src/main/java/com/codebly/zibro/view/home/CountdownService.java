package com.codebly.zibro.view.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

public class CountdownService extends Service {
    private Handler handler;
    private Runnable countdownRunnable;
    private int remainingTime = 600; // 백그아룬드에서 10분(600초) 동안 타이머가 실행됩니다.
    private static final String CHANNEL_ID = "TimerServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        startForeground(1, createNotificationChannel());  // 백그라운드 서비스 유지
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startCountdown();
        return START_STICKY;
    }

    private void startCountdown() {
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;
                    // 브로드캐스트로 남은 시간 전송
                    Intent countdownIntent = new Intent("COUNTDOWN_UPDATED");
                    countdownIntent.putExtra("remaining_time", remainingTime);
                    sendBroadcast(countdownIntent);
                    handler.postDelayed(this, 1000); // 1초마다 실행
                } else {
                    stopSelf();
                }
            }
        };
        handler.post(countdownRunnable);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(countdownRunnable);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Timer Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
        return null;
    }
/*
    private Notification createNotification() {
        // 알림 생성 코드
    }

 */
}

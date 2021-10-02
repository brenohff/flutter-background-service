package br.com.brenohff.flutter_background;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

    boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;

        System.out.println("MyService.onCreate");

        createNotificationChannel();
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Toast.makeText(this, "Service Destroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Others methods

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Background Service";
            String description = "Executing process in background";

            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("FOREGROUND_DEFAULT", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
package br.com.brenohff.flutter_background;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Date;

import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.view.FlutterCallbackInformation;
import io.flutter.view.FlutterMain;

public class MyService extends Service {
    public static final String SHARED_PREFERENCES = "br.com.brenohff.flutter_background";
    public static final String METHOD_HANDLE = "METHOD_HANDLE";
    public static final String ON_DESTROY = "ON_DESTROY";
    public static final String RECEIVER_BROADCAST = "RECEIVER_BROADCAST";

    @Override
    public void onCreate() {
        FlutterMain.startInitialization(this);
        FlutterMain.ensureInitializationComplete(this, new String[0]);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        setSharedPreferences(ON_DESTROY, new Date().toString());
        sendBroadcast(new Intent(this, MyReceiver.class));

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long callbackHandle = Long.parseLong(getSharedPreferences(METHOD_HANDLE));

        FlutterCallbackInformation callback = FlutterCallbackInformation.lookupCallbackInformation(callbackHandle);

        if (callback == null || callbackHandle == 0) {
            Toast.makeText(this, "Service Failure", Toast.LENGTH_LONG).show();
        } else {
            FlutterEngine backgroundEngine = new FlutterEngine(this);

            DartExecutor.DartCallback dartCallback = new DartExecutor.DartCallback(getAssets(), FlutterInjector.instance().flutterLoader().findAppBundlePath(), callback);
            backgroundEngine.getDartExecutor().executeDartCallback(dartCallback);

            Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        }

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Others methods

    public void setSharedPreferences(String key, String value) {
        SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }

    public String getSharedPreferences(String key) {
        SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        return pref.getString(key, "");
    }

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
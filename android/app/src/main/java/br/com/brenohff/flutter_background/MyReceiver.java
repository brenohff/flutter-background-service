package br.com.brenohff.flutter_background;

import static android.content.Context.MODE_PRIVATE;
import static br.com.brenohff.flutter_background.MyService.RECEIVER_BROADCAST;
import static br.com.brenohff.flutter_background.MyService.SHARED_PREFERENCES;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        setSharedPreferences(context, RECEIVER_BROADCAST, "Started broadcast at " + new Date().toString());

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent i = new Intent(context, MyService.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(i);
        }
    }

    public void setSharedPreferences(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }
}
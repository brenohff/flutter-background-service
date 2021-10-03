package br.com.brenohff.flutter_background;

import static br.com.brenohff.flutter_background.MyService.METHOD_HANDLE;
import static br.com.brenohff.flutter_background.MyService.ON_DESTROY;
import static br.com.brenohff.flutter_background.MyService.RECEIVER_BROADCAST;
import static br.com.brenohff.flutter_background.MyService.SHARED_PREFERENCES;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.JSONMethodCodec;
import io.flutter.plugin.common.MethodChannel;
import me.carda.awesome_notifications.utils.StringUtils;

public class MainActivity extends FlutterActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), "br.com.brenohff.background", JSONMethodCodec.INSTANCE).setMethodCallHandler((call, result) -> {
            Intent intent = new Intent(this, MyService.class);

            if (call.method.equals("startService")) {
                populateSharedPreferences((JSONObject) call.arguments);

                if (!isMyServiceRunning(MyService.class)) {
                    String destroyed = getSharedPreferences(ON_DESTROY);
                    String broadcast = getSharedPreferences(RECEIVER_BROADCAST);

                    if (!StringUtils.isNullOrEmpty(destroyed) && !StringUtils.isNullOrEmpty(broadcast)) {
                        Toast.makeText(this, destroyed + " - " + broadcast, Toast.LENGTH_LONG).show();
                    } else if (!StringUtils.isNullOrEmpty(destroyed)) {
                        Toast.makeText(this, destroyed, Toast.LENGTH_LONG).show();
                    } else if (!StringUtils.isNullOrEmpty(broadcast)) {
                        Toast.makeText(this, broadcast, Toast.LENGTH_LONG).show();
                    }

                    startService(intent);

                    result.success(true);
                } else {
                    Toast.makeText(this, "Service already started", Toast.LENGTH_SHORT).show();
                    result.success(false);
                }
            }

            if (call.method.equals("stopService")) {
                if (isMyServiceRunning(MyService.class)) {
                    stopService(intent);
                    result.success(true);
                } else {
                    Toast.makeText(this, "Service already stopped", Toast.LENGTH_SHORT).show();
                    result.success(false);
                }
            }

            if (call.method.equals("statusService")) {
                boolean isRunning = isMyServiceRunning(MyService.class);
                boolean isBroadcastRunning = isMyServiceRunning(MyReceiver.class);

                Toast.makeText(this, String.format("Service: %b - Broadcast: %b", isRunning, isBroadcastRunning), Toast.LENGTH_LONG).show();

                result.success(isRunning);
            }
        });
    }

    private void populateSharedPreferences(JSONObject args) {
        if (args != null) {
            try {
                setSharedPreferences(METHOD_HANDLE, Long.toString(args.getLong("handle")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSharedPreferences(String key, String value) {
        SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }

    public String getSharedPreferences(String key) {
        SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        return pref.getString(key, "");
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

package com.chungauto.tracking;

import static com.chungauto.tracking.MainFragment.KEY_DEVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class ImeiReceiver extends BroadcastReceiver {
    MainActivity mact;

    public ImeiReceiver(MainActivity mact) {
        this.mact = mact;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("com.android.settings.action.resultforimei")) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey("imei_count")) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                if (bundle.getInt("imei_count") > 0) {
                    Log.e("@@@@@@", "Device Id");
                    sharedPreferences.edit().putString(KEY_DEVICE, bundle.getString("imei_" + 0)).apply();
                    mact.addDeviceId(bundle.getString("imei_" + 0));
                }
            }
        }
    }

}

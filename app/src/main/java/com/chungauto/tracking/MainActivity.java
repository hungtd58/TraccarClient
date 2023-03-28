/*
 * Copyright 2017 - 2020 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chungauto.tracking;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private static final String MESSAGE_EVENT = "message_event";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (isNewDevice()) {
            ImeiReceiver imeiReceiver = new ImeiReceiver(this);
            IntentFilter iFilterAndroid = new IntentFilter();
            iFilterAndroid.addAction("com.android.settings.action.resultforimei");
            registerReceiver(imeiReceiver, iFilterAndroid);
            getImeiDevice();
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                addDeviceId(telephonyManager.getImei(0));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                addDeviceId(telephonyManager.getDeviceId(0));
            } else {
                addDeviceId(telephonyManager.getDeviceId());
            }
        }

        try {
            TraccarWsClient c = new TraccarWsClient(new URI("ws://14.225.23.221:3335"))
                    .setOnUpdateIntervalCallback(this::updateTimeInterval);
            c.connect();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isNewDevice() {
        return true;
    }

    private final Emitter.Listener onNewMessage = args -> {
        runOnUiThread(() -> {
            Log.e("@@@@@", "Size Data: " + args.length);
        });
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getImeiDevice() {
        Intent intent = new Intent();
        intent.setAction("com.android.settings.action.requestforimei");
        intent.setPackage("com.android.settings");
        intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        sendBroadcast(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void updateTimeInterval(String imei, int timeInterval) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment item : fragments) {
            if (item instanceof MainFragment) {
                runOnUiThread(() -> ((MainFragment) item).updateTimeInterval(imei, timeInterval));
                break;
            }
        }
    }

    public void addDeviceId(String deviceId) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment item : fragments) {
            if (item instanceof MainFragment) {
                ((MainFragment) item).updateDeviceId(deviceId);
            }
        }
    }
}

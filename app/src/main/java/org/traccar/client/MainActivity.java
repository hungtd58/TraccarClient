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
package org.traccar.client;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImeiReceiver imeiReceiver = new ImeiReceiver(this);
        IntentFilter iFilterAndroid = new IntentFilter();
        iFilterAndroid.addAction("com.android.settings.action.resultforimei");
        registerReceiver(imeiReceiver, iFilterAndroid);
        getImeiDevice();
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

    public void addDeviceId(String deviceId) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment item : fragments) {
            if (item instanceof MainFragment) {
                ((MainFragment) item).updateDeviceId(deviceId);
            }
        }
    }
}

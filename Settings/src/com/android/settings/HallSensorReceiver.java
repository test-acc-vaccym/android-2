
/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.provider.Settings;

public class HallSensorReceiver extends BroadcastReceiver
{
    private static final String TAG = "HallSensorReceiver";

    public void onReceive(final Context context, final Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.HALL_SENSOR_CHANGED"))
        {
            boolean hall_sensor = intent.getBooleanExtra("hall_sensor_close", false);
            Log.e(TAG, "HallSensorReceiver:hall_sensor = " + hall_sensor);
            if (true == hall_sensor)
            {
                int incallPowerBehavior = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR,
                        Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR_DEFAULT);
                Log.e(TAG, "HallSensorReceiver:incallPowerBehavior = " + incallPowerBehavior);

                if (Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR_HANGUP != incallPowerBehavior)
                {
                    Settings.Secure.putInt(context.getContentResolver(),
                        Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR,
                        Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR_HANGUP);
                }
            }
            else if (false == hall_sensor)
            {
                int incallPowerBehavior = Settings.Secure.getInt(context.getContentResolver(),
                        "incall_power_button_behavior_backup",
                        Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR_DEFAULT);
                Log.e(TAG, "HallSensorReceiver:incallPowerBehavior = " + incallPowerBehavior);

                Settings.Secure.putInt(context.getContentResolver(),
                    Settings.Secure.INCALL_POWER_BUTTON_BEHAVIOR, incallPowerBehavior);
            }
        }
    }
}

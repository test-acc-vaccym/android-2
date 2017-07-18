/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.edroplet.qxs.dropletdailer.compat;

import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract.Directory;

public class DirectorySdkCompat {

    private static final String TAG = "DirectorySdkCompat";
    public static  Uri ENTERPRISE_CONTENT_URI = null;
    public static final long ENTERPRISE_LOCAL_DEFAULT = Directory.ENTERPRISE_DEFAULT;
    public static final long ENTERPRISE_LOCAL_INVISIBLE = Directory.ENTERPRISE_LOCAL_INVISIBLE;
    public DirectorySdkCompat(){
        if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            ENTERPRISE_CONTENT_URI = Directory.ENTERPRISE_CONTENT_URI;
        }
    }
    public static boolean isRemoteDirectoryId(long directoryId) {
        if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Directory.isRemoteDirectoryId(directoryId);
        }
        return Directory.isRemoteDirectoryId(directoryId);
    }

    public static boolean isEnterpriseDirectoryId(long directoryId) {
        if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Directory.isEnterpriseDirectoryId(directoryId);
        }
        return Directory.isEnterpriseDirectoryId(directoryId);
    }
}
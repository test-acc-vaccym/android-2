package com.edroplet.qxx.saneteltabactivity.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by qxs on 2017/9/15.
 */

public class AmplifierInfo implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<AmplifierInfo> CREATOR  = new Creator<AmplifierInfo>() {
        @Override
        public AmplifierInfo createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public AmplifierInfo[] newArray(int size) {
            return new AmplifierInfo[size];
        }
    };
}

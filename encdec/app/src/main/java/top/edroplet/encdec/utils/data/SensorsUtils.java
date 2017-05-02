package top.edroplet.encdec.utils.data;

import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by xw on 2017/5/2.
 */

public class SensorsUtils {
    private static final String TAG="SensorsData";
    // 计算方向
    public String calculateOrientation(float [] accelerometerValues, float []magneticFieldValues) {
        float[] values = new float[3];
        float[] R = new float[9];
        String azimuthAngle = "不是正方向";
        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);

        Log.i(TAG, values[0] + "");
        if (values[0] >= -5 && values[0] < 5) {
            azimuthAngle = "正北";
        } else if (values[0] >= 5 && values[0] < 85) {
            // Log.i(TAG, "东北");
            azimuthAngle = "东北";
        } else if (values[0] >= 85 && values[0] <= 95) {
            // Log.i(TAG, "正东");
            azimuthAngle = "正东";
        } else if (values[0] >= 95 && values[0] < 175) {
            // Log.i(TAG, "东南");
            azimuthAngle = "东南";
        } else if ((values[0] >= 175 && values[0] <= 180)
                || (values[0]) >= -180 && values[0] < -175) {
            // Log.i(TAG, "正南");
            azimuthAngle = "正南";
        } else if (values[0] >= -175 && values[0] < -95) {
            // Log.i(TAG, "西南");
            azimuthAngle = "西南";
        } else if (values[0] >= -95 && values[0] < -85) {
            // Log.i(TAG, "正西");
            azimuthAngle = "正西";
        } else if (values[0] >= -85 && values[0] < -5) {
            // Log.i(TAG, "西北");
            azimuthAngle = "西北";
        }
        return azimuthAngle;
    }
}

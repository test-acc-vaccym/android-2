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
        if (accelerometerValues == null || accelerometerValues.length == 0) {
            azimuthAngle = "无加速度传感器";
            return azimuthAngle;
        }

        if (magneticFieldValues == null || magneticFieldValues.length == 0) {
            azimuthAngle = "无磁场传感器";
            return azimuthAngle;
        }

        SensorManager.getRotationMatrix(R, null, accelerometerValues,
                magneticFieldValues);
        SensorManager.getOrientation(R, values);
        /** values[0]：该值表示方位也就是手机绕着Z轴旋转的角度。0表示北（North）；90表示东（East）；
         * 180表示南（South）；270表示西（West）。如果values[0]的值正好是这4个值，并且手机是水平放置，
         * 表示手机的正前方就是这4个方向。可以利用这个特性来实现电子罗盘，实例76将详细介绍电子罗盘的实现过程。
         */
        values[0] = (float) Math.toDegrees(values[0]);

        /** values[1]：该值表示倾斜度，或手机翘起的程度。当手机绕着X轴倾斜时该值发生变化。
         *  values[1]的取值范围是-180≤values[1]
         *  ≤180。假设将手机屏幕朝上水平放在桌子上，这时如果桌子是完全水平的，
         *  values[1]的值应该是0（由于很少有桌子是绝对水平的，因此，该值很可能不为0，
         *  但一般都是-5和5之间的某个值）。这时从手机顶部开始抬起，直到将手机沿X轴旋转180度（屏幕向下水平放在桌面上）。
         *  在这个旋转过程中，values[1]会在0到-180之间变化，也就是说，从手机顶部抬起时，values[1]的值会逐渐变小，直到等于-180。
         *  如果从手机底部开始抬起，直到将手机沿X轴旋转180度，这时values[1]会在0到180之间变化。
         *  也就是values[1]的值会逐渐增大，直到等于180。可以利用values[1]和下面要介绍的values[2]来测量桌子等物体的倾斜度。
         */
        values[1] = (float) Math.toDegrees(values[1]);

        /** values[2]：表示手机沿着Y轴的滚动角度。取值范围是-90≤values[2]≤90。
         * 假设将手机屏幕朝上水平放在桌面上，这时如果桌面是平的，values[2]的值应为0。
         * 将手机左侧逐渐抬起时，values[2]的值逐渐变小，直到手机垂直于桌面放置，这时values[2]的值是-90。
         * 将手机右侧逐渐抬起时，values[2]的值逐渐增大，直到手机垂直于桌面放置，这时values[2]的值是90。
         * 在垂直位置时继续向右或向左滚动，values[2]的值会继续在-90至90之间变化。
         */
        values[2] = (float) Math.toDegrees(values[2]);

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
        return "方向：" + azimuthAngle + "\t倾斜度：" + String.valueOf(values[1]) + "\t旋转：" + String.valueOf(values[2]);
    }
}

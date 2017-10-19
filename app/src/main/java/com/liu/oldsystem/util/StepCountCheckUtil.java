package com.liu.oldsystem.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;

/**
 * Created by fySpring
 * Date : 2017/3/24
 * To do :检测手机是否支持计步工具类
 */

public class StepCountCheckUtil {

    private Context context;
    //是否有传感器
    private boolean hasSensor;

    public StepCountCheckUtil(Context context) {
        this.context = context;
        hasSensor = isSupportStepCountSensor();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean isSupportStepCountSensor() {
        return context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
    }

    /**
     * 判断该设备是否支持计歩
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isSupportStepCountSensor(Context context) {
        // 获取传感器管理器的实例
        SensorManager sensorManager = (SensorManager) context
                .getSystemService(context.SENSOR_SERVICE);
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        return countSensor != null || detectorSensor != null;
    }
}

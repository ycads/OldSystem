package com.liu.oldsystem.location;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;
import com.liu.oldsystem.help.FallActivity;
import com.liu.oldsystem.help.FallHelpActivity;

public class ShakeActivity extends AppCompatActivity  implements SensorEventListener {
    // 定义系统的Sensor管理器
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        // 获取系统的传感器管理服务
        sensorManager = (SensorManager) getSystemService(
                Context.SENSOR_SERVICE);  // ①
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("摇一摇定位");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent =new Intent(ShakeActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // 为系统的加速度传感器注册监听器
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);  // ②
    }
    @Override
    protected void onStop()
    {
        // 取消注册
        sensorManager.unregisterListener(this);
        super.onStop();
    }
    // 以下是实现SensorEventListener接口必须实现的方法
    // 当传感器的值发生改变时回调该方法
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float[] values = event.values;
        if(values[1] > 8 && values[2] > 5) {
            Intent intent =new Intent(ShakeActivity.this,FixPositionActivity.class);
            startActivity(intent);
            finish();
        }
    }
    // 当传感器精度改变时回调该方法
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

}

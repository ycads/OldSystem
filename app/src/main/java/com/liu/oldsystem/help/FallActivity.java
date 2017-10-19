package com.liu.oldsystem.help;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;
import com.liu.oldsystem.db.Word;

import org.litepal.crud.DataSupport;

import java.util.List;

public class FallActivity extends AppCompatActivity implements SensorEventListener {

    private TextView fall_one;
    private TextView fall_two;
    private TextView fall_three;
    private float size;
    private List<Word> words;

        // 定义系统的Sensor管理器
        SensorManager sensorManager;
        EditText etTxt1;
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fall);
            // 获取程序界面上的文本框组件
            etTxt1 = (EditText) findViewById(R.id.txt1);
            // 获取系统的传感器管理服务
            sensorManager = (SensorManager) getSystemService(
                    Context.SENSOR_SERVICE);  // ①
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("摔倒识别");

            fall_one = (TextView)findViewById(R.id.fall_text_one);
            fall_two = (TextView)findViewById(R.id.fall_text_two);
            fall_three = (TextView)findViewById(R.id.fall_text_three);
            words = DataSupport.where("remark = ?", "font").find(Word.class);
            if(words.size() == 0) {
                size = 25;
//                Toast.makeText(FallActivity.this,"当前字体原来大小："+size,Toast.LENGTH_SHORT).show();
            }
            else {
                size = Float.parseFloat(words.get(0).getWordSize());
//                Toast.makeText(FallActivity.this,"当前字体大小："+size,Toast.LENGTH_SHORT).show();
            }
            fall_one.setTextSize(size);
            fall_two.setTextSize(size);
            fall_three.setTextSize(size);
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent intent =new Intent(FallActivity.this,MainActivity.class);
                    startActivity(intent);
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
            StringBuilder sb = new StringBuilder();
            sb.append("屏幕向右的加速度：");
            sb.append(values[0]);
            sb.append("\n屏幕向上的加速度：");
            sb.append(values[1]);
            sb.append("\n屏幕向外的加速度：");
            sb.append(values[2]);
            if(values[1] > 10 && values[2] > 6) {
                Intent intent =new Intent(FallActivity.this,FallHelpActivity.class);
                String up = "" + values[1];
                String out = "" + values[2];
                intent.putExtra("fall_uprate",up);
                intent.putExtra("fall_outrate",out);
                startActivity(intent);
                finish();
            }
            etTxt1.setText(sb.toString());
        }
        // 当传感器精度改变时回调该方法
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {
        }
    }

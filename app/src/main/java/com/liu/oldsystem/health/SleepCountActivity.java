package com.liu.oldsystem.health;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.don.pieviewlibrary.AnimationPercentPieView;
import com.liu.oldsystem.R;
import com.liu.oldsystem.db.Sleep;

import org.litepal.crud.DataSupport;

import java.util.List;

public class SleepCountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_count);
        int[] data = new int[]{0,0,0,0,0};
        List<Sleep> countDay = DataSupport.findAll(Sleep.class);
        for(int i = 0;i < countDay.size();i++) {
            String morningTime = countDay.get(i).getMorningTime();
            String nightTime = countDay.get(i).getNightTime();
            if(morningTime == null) {

            } else if (morningTime.compareTo("06:00") >= 1 && morningTime.compareTo("08:00") < 1)
                data[0] = data[0] + 1;
            else
                data[1] = data[1] + 1;
            if(nightTime == null) {

            }else if (nightTime.compareTo("19:00") >= 1 && nightTime.compareTo("22:00") < 1)
                data[2] = data[2] + 1;
            else if (nightTime.compareTo("22:00") >= 1 && nightTime.compareTo("24:00") < 1)
                data[3] = data[3] + 1;
            else
                data[4] = data[4] + 1;
        }


        String[] name = new String[]{"早起", "晚起", "早睡", "晚睡", "失眠"};
        int[] color = new int[]{
                getResources().getColor(R.color.blue),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.not_pay)};
        AnimationPercentPieView animationPieView = (AnimationPercentPieView)
                findViewById(R.id.animation_pieView);
        animationPieView.setData(data, name,color);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("睡眠监测统计结果");

        TextView togetherDay = (TextView)findViewById(R.id.together_days);
        TextView morningDay = (TextView)findViewById(R.id.morning_days);
        TextView nightDay = (TextView)findViewById(R.id.night_days);
        togetherDay.setText("打卡总天数："+countDay.size()+"天");
         morningDay.setText("起床打卡：其中早起"+data[0]+"天，"+"晚起"+data[1]+"天");
        nightDay.setText("睡前打卡：其中早睡"+data[2]+"天，晚睡"+data[3]+"天，失眠"+data[4]+"天");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

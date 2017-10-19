package com.liu.oldsystem.health;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;


import com.liu.oldsystem.R;
import com.liu.oldsystem.db.Sleep;
import com.liu.oldsystem.help.ContactActivity;
import com.liu.oldsystem.help.PeopleInfoActivity;

import org.litepal.crud.DataSupport;

public class SleepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("睡眠监测");
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

    public void morning_clock_btn(View view) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String StoreTime = "" + df.format(new Date());
        List<Sleep> countDay = DataSupport.where("storeTime = ?", StoreTime).find(Sleep.class);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        String dayTime = "" + tf.format(new Date());
        if(countDay.size() == 0) {
            if(dayTime.compareTo("06:00") == -1 || dayTime.compareTo("12:00") != -1)
                Toast.makeText(this, "当前时间不在起床打卡范畴，不能进行起床打卡!!!", Toast.LENGTH_LONG).show();
            else {
                    Sleep sleep = new Sleep();
                    sleep.setMorningTime(dayTime);
                    sleep.setStoreTime(StoreTime);
                    sleep.save();
            }
        } else if(countDay.get(0).getMorningTime() == null ) {
            if(dayTime.compareTo("06:00") == -1 || dayTime.compareTo("12:00") >= 1)
                Toast.makeText(this, "当前时间不在起床打卡范畴，不能进行起床打卡!!!", Toast.LENGTH_LONG).show();
            else {
                Sleep sleep = new Sleep();
                sleep.setMorningTime(dayTime);
                sleep.setStoreTime(StoreTime);
                sleep.updateAll("storeTime = ?", StoreTime);
            }
        } else
            Toast.makeText(this, "今天起床已打卡，不需要重新打卡!!!", Toast.LENGTH_LONG).show();
    }

    public void night_clock_btn(View view) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String StoreTime = "" + df.format(new Date());
        List<Sleep> countDay = DataSupport.where("storeTime = ?", StoreTime).find(Sleep.class);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        String dayTime = "" + tf.format(new Date());
        if(countDay.size() == 0) {
            if(dayTime.compareTo("19:00") == -1 || dayTime.compareTo("06:00") != -1)
                Toast.makeText(this, "当前时间不在晚睡打卡范畴，不能进行睡前打卡!!!", Toast.LENGTH_LONG).show();
            else {
                Sleep sleep = new Sleep();
                sleep.setNightTime(dayTime);
                sleep.setStoreTime(StoreTime);
                sleep.save();
            }
        } else if(countDay.get(0).getNightTime() == null) {
            if(dayTime.compareTo("19:00") == -1 || dayTime.compareTo("06:00") != -1)
                Toast.makeText(this, "当前时间不在晚睡打卡范畴，不能进行睡前打卡!!!", Toast.LENGTH_LONG).show();
            else {
                Sleep sleep = new Sleep();
                sleep.setNightTime(dayTime);
                sleep.setStoreTime(StoreTime);
                sleep.updateAll("storeTime = ?", StoreTime);
            }
        } else
            Toast.makeText(this, "今天睡前已打卡，不需要重新打卡!!!", Toast.LENGTH_LONG).show();
    }

    public void sleep_result_query_btn(View view) {
//        List<Sleep> countDay = DataSupport.findAll(Sleep.class);
//        Toast.makeText(this, "数据查询成功，打卡天数："+countDay.size()+", 具体内容："+countDay.get(0).getMorningTime()+","+data[0]+temp, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SleepActivity.this, SleepCountActivity.class);
        startActivity(intent);
    }

}

package com.liu.oldsystem.tool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liu.oldsystem.R;

public class ScreenLightActivity extends AppCompatActivity {

    public Button button_add;
    public Button button_reduce;
    public TextView tv;
    public Screen SC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_light);
        SC = new Screen(this);
        button_add = (Button) findViewById(R.id.button_add);
        button_reduce = (Button) findViewById(R.id.button_reduce);
        tv = (TextView) findViewById(R.id.tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("调节亮度");
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

    public void add(View view){
        SC.add(this);
        tv.setText("当前屏幕亮度为："+SC.getScreenBrightness(this));
    }
    public void dre(View view){
        SC.dre(this);
        tv.setText("当前屏幕亮度为："+SC.getScreenBrightness(this));
    }
}

package com.liu.oldsystem.help;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;

import org.w3c.dom.Text;

public class FallHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("识别求救");
        TextView fallResulView = (TextView)findViewById(R.id.fall_result);
        Intent intent = getIntent();
        String uprate = "屏幕向上的加速度："+intent.getStringExtra("fall_uprate")+"\n";
        String outrate = "屏幕向外的加速度："+intent.getStringExtra("fall_outrate");
        fallResulView.setText("系统监测到您已摔倒，请选择下方按钮进行求救操作。\n"+uprate+outrate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent =new Intent(FallHelpActivity.this,FallActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fall_TelHelp(View v) {
        Intent intent =new Intent(FallHelpActivity.this,TelphoneActivity.class);
        startActivity(intent);
        finish();
    }

    public void fall_SmsHelp(View v) {
        Intent intent =new Intent(FallHelpActivity.this,MessageActivity.class);
        startActivity(intent);
        finish();
    }

    public void notFall(View v) {
        Intent intent =new Intent(FallHelpActivity.this,FallActivity.class);
        startActivity(intent);
        finish();
    }
}

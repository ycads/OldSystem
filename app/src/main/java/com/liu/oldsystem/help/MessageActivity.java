package com.liu.oldsystem.help;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;
import com.liu.oldsystem.db.ContactPerson;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    SmsManager  sManager = SmsManager.getDefault();
    ArrayList<String> telnumber = new ArrayList<String>();
    ArrayList<String> telname =  new ArrayList<String>();
    public LocationClient mLocationClient;
    private  EditText infoLocationView;
    StringBuilder currentPosition = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("短信求救");

        infoLocationView = (EditText) findViewById(R.id.info_location);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MessageActivity.this, permissions, 1);
        } else {
            requestLocation();
        }

        final EditText smsPeopleView = (EditText) findViewById(R.id.sms_people);
        Button getTelPeopleButton = (Button) findViewById(R.id.get_telpeople);
        getTelPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ContactPerson> people =  DataSupport.findAll(ContactPerson.class);
                String temp = "";
                int i = 1;
                for (ContactPerson person: people)
                    if(person.getIsContact().equals("是")) {
                        telname.add(person.getName());
                        telnumber.add(person.getTel());
                        temp += "" + i + "." + person.getName() + ", " + "电话：" + person.getTel() + "\n";
                        ++i;
                    }
                if(temp == "")
                    temp = "当前未保存任何联系人，无紧急联系人信息！！！";
                smsPeopleView.setText(temp);
            }
        });
        Button getLocationButton = (Button) findViewById(R.id.get_location);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoLocationView.setText(currentPosition);
            }
        });
    }

    public void sendSmsAll(View view) {
        // 创建一个PendingIntent对象
        PendingIntent pi = PendingIntent.getActivity(
                MessageActivity.this, 0, new Intent(), 0);
        if(infoLocationView.getText().toString().equals("") || infoLocationView.getText().toString().equals(null))
            Toast.makeText(MessageActivity.this,"发送内容不能为空，请输入发送内容！！！",Toast.LENGTH_SHORT).show();
        else if(telnumber.size() == 0)
            Toast.makeText(MessageActivity.this,"紧急联系人接受电话为空，请点击获取紧急联系人电话！！！",Toast.LENGTH_SHORT).show();
        else {
            for(int i = 0;i < telnumber.size();i++) {
                String message = "" + telname.get(i) +", 您好。我刚刚不慎摔倒，急需您的帮助。摔倒位置:"+infoLocationView.getText().toString();
                //发送短信
                sManager.sendTextMessage(telnumber.get(i).trim(), null, message, pi, null);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent =new Intent(MessageActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            currentPosition.append("纬度:").append(location.getLatitude()).append(",");
//            currentPosition.append("经度:").append(location.getLongitude()).append("。");
            currentPosition.append(location.getCountry());
            currentPosition.append(location.getProvince());
            currentPosition.append(location.getCity());
            currentPosition.append(location.getDistrict());
            currentPosition.append(location.getStreet()).append("。");
        }

    }
}

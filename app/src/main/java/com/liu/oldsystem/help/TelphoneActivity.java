package com.liu.oldsystem.help;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;
import com.liu.oldsystem.db.ContactPerson;

import org.litepal.crud.DataSupport;

import java.util.List;

public class TelphoneActivity extends AppCompatActivity {
    private EditText infoNameText;
    private EditText infoUserTelphoneText;
    private static  boolean judge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telphone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("电话求救");
        final EditText tel = (EditText)findViewById(R.id.phonetel);
        Button tel_help = (Button) findViewById(R.id.tel_help);
        tel_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhoneNumber(tel.getText().toString().trim());
            }
        });
        Button personSearchButton = (Button) findViewById(R.id.person_search);
        personSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge = true;
                List<ContactPerson> people =  DataSupport.findAll(ContactPerson.class);
                String temp = "";
                for (ContactPerson person: people) {
                    if(person.getIsContact().equals("是"))
                         temp += "联系人编号："+person.getId()+"\n姓名："+person.getName()+"\n"+"电话："+person.getTel() +"\n\n\n";
                }
                if(temp == "")
                    temp = "当前未保存任何联系人，无紧急联系人信息！！！";
             //   Toast.makeText(TelphoneActivity.this,"所有联系人查找成功！！！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TelphoneActivity.this, UrgentInfoActivity.class);
                intent.putExtra("search_info", temp);
                startActivity(intent);
                finish();
            }
        });

        this.initView();
        Intent intent = this.getIntent();
        String infoUserName = intent.getStringExtra(UrgentInfoActivity.INFO_USER_NAME);
        String infoPerSign = intent.getStringExtra(UrgentInfoActivity.INFO_USER_TEL);
        if(judge) {
            infoNameText.setText(infoUserName);
            infoUserTelphoneText.setText(infoPerSign);
        }
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent =new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNumber));
        startActivity(intent);
    }

    private void initView() {
        infoNameText = (EditText) this.findViewById(R.id.telnname);
        infoUserTelphoneText = (EditText) this.findViewById(R.id.phonetel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent =new Intent(TelphoneActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

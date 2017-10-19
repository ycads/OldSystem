package com.liu.oldsystem.help;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;
import com.liu.oldsystem.db.ContactPerson;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("紧急联系人设置");
        final EditText name = (EditText)findViewById(R.id.personname);
        final EditText tel = (EditText)findViewById(R.id.persontel);
        final EditText iscontact = (EditText)findViewById(R.id.iscontact);
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connector.getDatabase();
                List<ContactPerson> people = DataSupport.select("name", "tel").find(ContactPerson.class);
                Boolean judge = false;
                for(int i = 0;i < people.size();i++) {
                    if(people.get(i).getName().equals(name.getText().toString()) && people.get(i).getTel().equals(tel.getText().toString()))
                        judge = true;
                }
                if(judge == false) {
                    ContactPerson person = new ContactPerson();
                    person.setName(name.getText().toString());
                    person.setTel(tel.getText().toString());
                    person.setIsContact(iscontact.getText().toString());
                    person.save();
                    Toast.makeText(ContactActivity.this,"联系人添加成功！！！",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContactActivity.this, "联系人已经存在，添加失败！！！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactPerson person = new ContactPerson();
              ; person.setTel(tel.getText().toString());
                person.setIsContact(iscontact.getText().toString());
                person.updateAll("name = ?", name.getText().toString());
                Toast.makeText(ContactActivity.this,"联系人修改成功！！！",Toast.LENGTH_SHORT).show();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(ContactPerson.class, "name = ?", name.getText().toString());
                Toast.makeText(ContactActivity.this,"联系人删除成功！！！",Toast.LENGTH_SHORT).show();
            }
        });

        Button queryButton = (Button) findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ContactPerson> people = DataSupport.where("name = ?", name.getText().toString()).find(ContactPerson.class);
                String temp = "";
                for (ContactPerson person: people) {
                    temp += "联系人编号："+person.getId()+"\n姓名："+person.getName()+"\n"+"电话："+person.getTel()+"\n是否为紧急联系人："
                            +person.getIsContact()+"\n\n\n";
                }
                name.setText(people.get(0).getName());
                tel.setText(people.get(0).getTel());
                iscontact.setText(people.get(0).getIsContact());
                if(temp == "")
                    Toast.makeText(ContactActivity.this,"该联系人不存在！！！",Toast.LENGTH_SHORT).show();
            }
        });

        Button queryAllButton = (Button) findViewById(R.id.query_alldata);
        queryAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ContactPerson> people =  DataSupport.findAll(ContactPerson.class);
                String temp = "";
                for (ContactPerson person: people) {
                    temp += "联系人编号："+person.getId()+"\n姓名："+person.getName()+"\n"+"电话："+person.getTel()+"\n是否为紧急联系人："
                            +person.getIsContact()+"\n\n\n";
                }
               if(temp == "")
                   temp = "当前未保存任何联系人，无紧急联系人信息！！！";
            //    Toast.makeText(ContactActivity.this,"所有联系人查找成功！！！",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ContactActivity.this, PeopleInfoActivity.class);
                intent.putExtra("people_info", temp);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent =new Intent(ContactActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

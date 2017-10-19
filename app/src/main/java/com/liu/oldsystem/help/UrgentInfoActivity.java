package com.liu.oldsystem.help;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liu.oldsystem.R;
import com.liu.oldsystem.db.ContactPerson;
import com.liu.oldsystem.db.Word;

import org.litepal.crud.DataSupport;

import java.util.List;

public class UrgentInfoActivity extends AppCompatActivity {
    private EditText infoUserNameText ;
    private EditText infoUserTelphoneText;
    public static final String INFO_USER_NAME = "INFO_USER_NAME";
    public static final String INFO_USER_TEL = "INFO_USER_TEL";
    private List<Word> words;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_info);
        infoUserNameText = (EditText)findViewById(R.id.urgentname);
        infoUserTelphoneText = (EditText)findViewById(R.id.urgenttel);
        TextView urgentView = (TextView) findViewById(R.id.urgentText);
        Intent intent = getIntent();
        String data = intent.getStringExtra("search_info");
        urgentView.setText(data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("紧急联系人查询");
        words = DataSupport.where("remark = ?", "font").find(Word.class);
        float size;
        if(words.size() == 0)
            size = 25;
        else
            size = Float.parseFloat(words.get(0).getWordSize());
        urgentView.setTextSize(size);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent =new Intent(UrgentInfoActivity.this,TelphoneActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void btn_urgentupdate(View view) {
        Intent intent = new Intent(this,TelphoneActivity.class);
        String infoUserName = infoUserNameText.getText().toString();
        String infoPerSignature = infoUserTelphoneText.getText().toString();
        intent.putExtra(INFO_USER_NAME, infoUserName);
        intent.putExtra(INFO_USER_TEL, infoPerSignature);
        this.startActivity(intent);
    }
}

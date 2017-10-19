package com.liu.oldsystem.help;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;
import com.liu.oldsystem.db.Word;

import org.litepal.crud.DataSupport;

import java.util.List;

public class PeopleInfoActivity extends AppCompatActivity {
    private List<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_info);
        TextView peopleView = (TextView) findViewById(R.id.peopleText);
        Intent intent = getIntent();
        String data = intent.getStringExtra("people_info");
        peopleView.setText(data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("所有联系人信息");
        words = DataSupport.where("remark = ?", "font").find(Word.class);
        float size;
        if(words.size() == 0)
            size = 25;
        else
            size = Float.parseFloat(words.get(0).getWordSize());
        peopleView.setTextSize(size);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent =new Intent(PeopleInfoActivity.this,ContactActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

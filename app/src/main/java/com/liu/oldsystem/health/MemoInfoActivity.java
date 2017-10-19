package com.liu.oldsystem.health;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.liu.oldsystem.R;
import com.liu.oldsystem.db.Word;
import com.liu.oldsystem.help.ContactActivity;
import com.liu.oldsystem.help.PeopleInfoActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MemoInfoActivity extends AppCompatActivity {
    private List<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_info);
        TextView memoInfoView = (TextView) findViewById(R.id.meomoTextInfo);
        Intent intent = getIntent();
        String data = intent.getStringExtra("memo_info");
        memoInfoView.setText(data);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("所有备忘信息");
        words = DataSupport.where("remark = ?", "font").find(Word.class);
        float size;
        if(words.size() == 0)
            size = 25;
        else
            size = Float.parseFloat(words.get(0).getWordSize());
        memoInfoView.setTextSize(size);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent =new Intent(MemoInfoActivity.this,MemoActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

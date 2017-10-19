package com.liu.oldsystem.tool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.oldsystem.R;
import com.liu.oldsystem.db.Word;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MagnifierActivity extends AppCompatActivity {
    private TextView textView_wordshow;
    private  float size;
    private List<Word> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnifier);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("放大镜");
        textView_wordshow = (TextView) findViewById(R.id.textView1);
        words = DataSupport.where("remark = ?", "font").find(Word.class);
        if(words.size() == 0) {
            size = 25;
            Word word = new Word();
            word.setWordSize(""+size);
            word.setRemark("font");
            word.save();
        }
        else
            size = Float.parseFloat(words.get(0).getWordSize());
        textView_wordshow.setTextSize(size);
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

    public void suo(View view){
        if(size>=27.5){
            size = size-5;
            textView_wordshow.setTextSize(size);
            Word word = new Word();
            word.setWordSize(""+size);
            word.updateAll("remark = ?", "font");
        }
        else {
            Toast.makeText(this, "字体已达到最小，不能再缩小啦！", Toast.LENGTH_SHORT).show();
        }


    }
    public void kuo(View view){
        if(size<=47.5)
        {
            size = size+5;
            textView_wordshow.setTextSize(size);
            Word word = new Word();
            word.setWordSize(""+size);
            word.updateAll("remark = ?", "font");
        }
        else {
            Toast.makeText(this, "字体已达到最大，不能再扩大啦！", Toast.LENGTH_SHORT).show();
        }
    }
}

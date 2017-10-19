package com.liu.oldsystem.tool;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.liu.oldsystem.R;

public class FlashLightActivity extends AppCompatActivity {
    private ImageButton start;
    private Camera camera = Camera.open();
    private TextView show_status;
    Boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);
        start = (ImageButton) findViewById(R.id.light_Btn);
        start.setOnClickListener(startlistener);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("手电筒");

        show_status = (TextView)findViewById(R.id.show_status1);
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

    View.OnClickListener startlistener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
//            String contenttext = start.getText().toString();

            if (!status)
            {
                camera.startPreview();
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(param);
                start.setBackgroundResource(R.drawable.hl_on);
                status = true;
                show_status.setText("请点击按钮关闭手电筒");
            }
            else {

                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                start.setBackgroundResource(R.drawable.hl_down);
                status = false;
                show_status.setText("请点击按钮打开手电筒");
            }
        }
    };

    @Override
    protected void onPostResume() {
        // TODO Auto-generated method stub
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        camera.release();
    }
}

package es.source.code.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class SCOSEntry extends AppCompatActivity {

    private static final int MIN_DISTANCE = 100; //左右最小滑动距离
    private static final int MAX_DISTANCE = 10;  //上下最大滑动距离
    float x1 = 0;
    float y1 = 0;
    float x2 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //按下手指时
            x1 = event.getX();
            y1 = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            //抬起手指时
            x2 = event.getX();
            y2 = event.getY();
        }
        if((y1 - y2) < MAX_DISTANCE && (x1 - x2) > MIN_DISTANCE){
            //跳转到Button演示界面
            String data = "FromEntry";
            Intent intent = new Intent(SCOSEntry.this,MainScreen.class);
            intent.putExtra("Data",data);
            startActivity(intent);
        }
        return super.onTouchEvent(event);
    }
}

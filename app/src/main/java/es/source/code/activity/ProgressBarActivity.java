package es.source.code.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ProgressBarActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int mProgress=0;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==0x111){
                    progressBar.setProgress(mProgress);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(ProgressBarActivity.this,MainScreen.class);
                    startActivity(intent);
                }
            }
        };
        new Thread(new Runnable(){
            @Override
            public void run(){
                while(true){
                    mProgress=doWork();
                    Message m = new Message();
                    if(mProgress<100){
                        m.what= 0x111;       //设置消息代码
                        mHandler.sendMessage(m);
                    }else{
                        m.what=0x110;
                        mHandler.sendMessage(m);
                        break;
                    }
                }
            }
            private int doWork(){
                mProgress+=1;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return mProgress;
            }
        }).start();
    }
}

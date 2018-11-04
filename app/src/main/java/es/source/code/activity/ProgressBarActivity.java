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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import es.source.code.model.MessageEvent;

public class ProgressBarActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private int mProgress=0;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent){
        if(messageEvent.getMessage().equals("0x111")){
            progressBar.setProgress(mProgress);
        }else{
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(ProgressBarActivity.this,MainScreen.class);
            intent.putExtra("Data","LoginSuccess");
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        EventBus.getDefault().register(this);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        new Thread(new Runnable(){
            @Override
            public void run(){
                while(true){
                    mProgress=doWork();
                    Message m = new Message();
                    if(mProgress<100){
                        MessageEvent messageEvent = new MessageEvent("0x111");
                        EventBus.getDefault().post(messageEvent);
                    }else{
                        MessageEvent messageEvent = new MessageEvent("0x110");
                        EventBus.getDefault().post(messageEvent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

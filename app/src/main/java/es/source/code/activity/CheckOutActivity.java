package es.source.code.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.source.code.fragment.MyOrderFragment;
import es.source.code.model.Data;
import es.source.code.model.User;

/**
 * Created by caobotao on 15/12/2.
 */
public class CheckOutActivity extends Activity {
    private ProgressBar progressBar;
    private MyAsyncTask myAsyncTask;
    private TextView textView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.textView);
        textView.setText("正在结账，请稍后...");
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }


    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPostExecute(Void i) {
            user = (User)getIntent().getSerializableExtra("User");
            double j = CountPrice(user.getOrderList())*0.7;
            Toast.makeText(getApplicationContext(),"结账总金额为" + String.valueOf(j) + "元,新增积分" + j,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //通过publishProgress方法传过来的值进行进度条的更新.
            progressBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //使用for循环来模拟进度条的进度.
            for (int i = 0; i < 100; i++) {
                //调用publishProgress方法将自动触发onProgressUpdaite方法来进行进度条的更新.
                publishProgress(i);
                try {
                    //通过线程休眠模拟耗时操作
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent(CheckOutActivity.this, MainScreen.class);
            intent.putExtra("Data", "FromEntry");
            startActivity(intent);
            return null;
        }
    }
    private static int CountPrice(List<Data> list) {
        int totalPrice = 0;
        if (list == null){
            return 0;
        }
        //遍历列表
        for (Data data : list) {
            totalPrice = totalPrice + data.getPrice();
        }
        return totalPrice;
    }
}
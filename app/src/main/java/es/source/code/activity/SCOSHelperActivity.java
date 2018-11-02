package es.source.code.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class SCOSHelperActivity extends AppCompatActivity {

    private ImageView call;
    private ImageView mail;
    private ImageView message;
    private ImageView about;
    private ImageView protocol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoshelper);

        call = findViewById(R.id.call);
        message = findViewById(R.id.message);
        mail = findViewById(R.id.mail);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:5554"));
                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager manager = SmsManager.getDefault();

                List<String> divideContents = manager.divideMessage("text scos helper");

                for (String text : divideContents) {

                    manager.sendTextMessage("5554", null, text, null, null);

                }

                Toast.makeText(getApplicationContext(),"求助短信发送成功",Toast.LENGTH_SHORT).show();
            }
        });

        mail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EmailSender sender = new EmailSender();
                            //设置服务器地址和端口，可以查询网络
                            sender.setProperties("smtp.163.com", "25");
                            //分别设置发件人，邮件标题和文本内容
                            sender.setMessage("18788872353@163.com", "点菜",
                                    "test scos text");
                            //设置收件人
                            sender.setReceiver(new String[]{"1083984839@qq.com"});
                            //发送邮件
                            sender.sendEmail("smtp.163.com", "18788872353@163.com", "huangrui586941");
                        } catch (AddressException e) {
                            e.printStackTrace();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Toast.makeText(getApplicationContext(), "求助邮件已发送成功", Toast.LENGTH_SHORT).show();

            }
        });
    }

}

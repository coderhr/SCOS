package es.source.code.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

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
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("tel:5554"));
                startActivity(intent);
            }
        });
        message = findViewById(R.id.message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+ "5554"));
                intent.putExtra("sms_body", "test scos helper");
                startActivity(intent);*/
                SmsManager manager = SmsManager.getDefault();

                List<String> divideContents = manager.divideMessage("text scos helper");

                for (String text : divideContents) {

                    manager.sendTextMessage("5554", null, text, null, null);

                }

                Toast.makeText(getApplicationContext(),"求助短信发送成功",Toast.LENGTH_SHORT).show();
            }
        });

    }
}

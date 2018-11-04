package es.source.code.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import es.source.code.model.User;

public class MainScreen extends AppCompatActivity {
    private Button loginButton;
    private Button orderButton;
    private Button checkButton;
    private Button helpButton;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String Data = "FromEntry";
        user = new User("temp","0");
        Intent intent = getIntent(); // 取得从上一个Activity当中传递过来的Intent对象
        String Str = intent.getStringExtra("Data");
        setContentView(R.layout.activity_navigation);

        orderButton=findViewById(R.id.btn_1);
        checkButton=findViewById(R.id.btn_2);
        loginButton=findViewById(R.id.btn_3);

            if(!(Str.equals("LoginSuccess"))) {
                orderButton.setVisibility(View.GONE);
                checkButton.setVisibility(View.GONE);
            }

        //监听登录按钮，并跳转登录界面
        loginButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到登录界面
                Intent intent = new Intent(MainScreen.this,LoginOrRegisterActivity.class);
                startActivity(intent);
            }
        });
        orderButton=findViewById(R.id.btn_1);
        orderButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到FoodViewActivity界面
                Intent intent = new Intent(MainScreen.this,FoodViewActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        checkButton=findViewById(R.id.btn_2);
        checkButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                //跳转到FoodOrderView界面
                Intent intent = new Intent(MainScreen.this,FoodOrderViewActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
        helpButton=findViewById(R.id.btn_4);
        helpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到SCOSHelperActivity界面
                Intent intent = new Intent(MainScreen.this, SCOSHelperActivity.class);
                startActivity(intent);
            }
        });
    }
}


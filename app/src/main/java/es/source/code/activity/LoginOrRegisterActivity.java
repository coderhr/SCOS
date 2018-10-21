package es.source.code.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.source.code.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginOrRegisterActivity extends AppCompatActivity {

    public EditText Et1;
    public EditText Et2;
    public Button Back;
    boolean flag1, flag2;
    String nameStr;
    String passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        Et1 = (EditText) findViewById(R.id.text2);
        Et2 = (EditText) findViewById(R.id.text1);
        Back = (Button) findViewById(R.id.back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginOrRegisterActivity.this, MainScreen.class);
                startActivity(intent);
            }
        });

        Et1.addTextChangedListener(new TextWatcher() {
            //文字改变前的回调方法
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //文字改变时的回调方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            //文字改变后的回调方法
            @Override
            public void afterTextChanged(Editable s) {
                //得到Editable对象的String
                nameStr = s.toString();
                //判断输入的内容是否规范
                boolean b = isRight(nameStr);
                if (!b) {
                    EditText editText1 = findViewById(R.id.text2);
                    editText1.setError("输入内容不符合规则");
                    return;
                }

                Button loginButton1;
                Button registerButton;
                loginButton1 = findViewById(R.id.login);
                registerButton = findViewById(R.id.register);
                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRight(nameStr)&&isRight(passwordStr)) {
                            login(nameStr, passwordStr, false);
                            Intent intent = new Intent(LoginOrRegisterActivity.this, MainScreen.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "欢迎您成为 SCOS 新用户",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            return;
                        }
                    }
                });
                loginButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRight(nameStr)&&isRight(passwordStr)) {
                            login(nameStr, passwordStr, true);
                            Intent intent = new Intent(LoginOrRegisterActivity.this, ProgressBarActivity.class);
                            startActivity(intent);
                        }else{
                            return;
                        }
                    }
                });
            }
        });
        Et2.addTextChangedListener(new TextWatcher() {
            //文字改变前的回调方法
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            //文字改变时的回调方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            //文字改变后的回调方法
            @Override
            public void afterTextChanged(Editable s) {
                //得到Editable对象的String
                passwordStr = s.toString();
                //判断输入的内容是否规范
                boolean b = isRight(passwordStr);
                if (!b) {
                    EditText editText2 = findViewById(R.id.text1);
                    editText2.setError("输入内容不符合规则");
                    return;
                }
                Button loginButton1;
                Button registerButton;
                loginButton1 = findViewById(R.id.login);
                registerButton = findViewById(R.id.register);
                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRight(nameStr)&&isRight(passwordStr)) {
                            login(nameStr, passwordStr, false);
                            Intent intent = new Intent(LoginOrRegisterActivity.this, MainScreen.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "欢迎您成为 SCOS 新用户",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            return;
                        }
                    }
                });
                loginButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRight(nameStr)&&isRight(passwordStr)) {
                            login(nameStr, passwordStr, true);
                            Intent intent = new Intent(LoginOrRegisterActivity.this, ProgressBarActivity.class);
                            startActivity(intent);
                        }else{
                            return;
                        }
                    }
                });
            }
        });
    }

        public void login (String NameStr, String PasswordStr, boolean OldUser) {
            if (isRight(NameStr) && isRight(PasswordStr)) {
                User loginUser = new User(NameStr, PasswordStr);
                loginUser.setPassword(PasswordStr);
                loginUser.setUserName(NameStr);
                loginUser.setOldUser(OldUser);
            }
        }
        public boolean isRight (String Str){
            if (Str.equals("")) {
                return false;
            }
            //定义规定格式的正则表达式
            String regex = "^[0-9a-zA-Z]{1,20}$";
            //设定查看模式
            Pattern p = Pattern.compile(regex);
            //判断Str是否匹配，返回匹配结果
            Matcher m = p.matcher(Str);
            return m.find();
        }
}


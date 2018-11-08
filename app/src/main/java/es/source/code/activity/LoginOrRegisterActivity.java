package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sf.json.JSONObject;

import org.greenrobot.eventbus.EventBus;

import es.source.code.model.MessageEvent;
import es.source.code.model.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginOrRegisterActivity extends AppCompatActivity {

    public EditText Account;
    public EditText Password;
    public Button Back;
    SharedPreferences sharedPreferences;
    String nameStr;
    String passwordStr;
    Boolean judge;

    String baseUrl = "http://192.168.1.101:8080/LoginValidator";

    private void sendJson(){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nameStr",nameStr);
            jsonObject.put("passwordStr", passwordStr);
            URL url = new URL(baseUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();    // 打开一个HttpURLConnection连接
            urlConn.setConnectTimeout(5 * 1000);      // 设置连接超时时间
            urlConn.setReadTimeout(5 * 1000);     //设置从主机读取数据超时zzzzzzzzzzzzz
            urlConn.setDoOutput(true);           // Post请求必须设置允许输出 默认false
            urlConn.setDoInput(true);             //设置请求允许输入 默认是true
            urlConn.setUseCaches(false);          // Post请求不能使用缓存
            urlConn.setRequestMethod("POST");      // 设置为Post请求
            urlConn.setInstanceFollowRedirects(true);         //设置本次连接是否自动处理重定向
            urlConn.setRequestProperty("Content-Type", "application/json");       // 配置请求Content-Type
            String content = String.valueOf(jsonObject);           // 开始连接
            urlConn.connect();
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());      // 发送请求参数
            dos.writeBytes(content);
            dos.flush();
            dos.close();

            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i("log", "接受返回值");
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader bf = new BufferedReader(in);
                String recData = null;
                String result = "";
                while ((recData = bf.readLine()) != null) {
                    result += recData;
                }
                in.close();
                urlConn.disconnect();
                JSONObject json_res = JSONObject.fromObject(result);
                if (json_res.get("RESULTCODE").equals("1")) {
                    if(judge == true){
                        Intent intent = new Intent(LoginOrRegisterActivity.this, ProgressBarActivity.class);
                        intent.putExtra("Data","LoginSuccess");
                        startActivity(intent);
                    }else{

                    }

                } else {
                    MessageEvent messageEvent = new MessageEvent(String.valueOf(2));
                    EventBus.getDefault().post(messageEvent);
                }
            } else {
                MessageEvent messageEvent = new MessageEvent(String.valueOf(2));
                EventBus.getDefault().post(messageEvent);
            }
            urlConn.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class LoginThread implements Runnable{
        public void run(){
            sendJson();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);

        Account = findViewById(R.id.text1);
        Password = findViewById(R.id.text2);
        Back = findViewById(R.id.back);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                String name = sharedPreferences.getString("name", "");
                if(!nameStr.equals(name)){
                    sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("loginState","0");
                }
                Intent intent = new Intent(LoginOrRegisterActivity.this, MainScreen.class);
                intent.putExtra("Data","FromBack");
                startActivity(intent);
            }
        });

        Account.addTextChangedListener(new TextWatcher() {
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
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                String name = sharedPreferences.getString("name", "");
                if(!nameStr.equals(name)){
                    loginButton1.setVisibility(View.GONE);
                    registerButton.setVisibility(View.VISIBLE);
                }else{
                    registerButton.setVisibility(View.GONE);
                    loginButton1.setVisibility(View.VISIBLE);
                }
                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isRight(nameStr)&&isRight(passwordStr)) {
                            login(nameStr, passwordStr, false);
                            Intent intent = new Intent(LoginOrRegisterActivity.this, MainScreen.class);
                            intent.putExtra("Data", "registerButton");
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

                        }else{
                            return;
                        }
                    }
                });
            }
        });
        Password.addTextChangedListener(new TextWatcher() {
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
                            intent.putExtra("Data", "registersuccess");
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
                        }else{
                            return;
                        }
                    }
                });
            }
        });

    }

        public void login (String NameStr, String PasswordStr, boolean OldUser) {

            judge = OldUser;

            if (isRight(NameStr) && isRight(PasswordStr)) {
                Thread loginThread = new Thread(new LoginThread());
                loginThread.start();
                User loginUser = new User(NameStr, PasswordStr);
                loginUser.setPassword(PasswordStr);
                loginUser.setUserName(NameStr);
                loginUser.setOldUser(OldUser);
                sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", nameStr);
                editor.putString("password", passwordStr);
                editor.putString("loginState","1");
                editor.commit();       //提交修改
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


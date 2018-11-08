package es.source.code.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import es.source.code.model.Data;

import static java.lang.Thread.sleep;

public class ServerObserverService extends Service {
    public Context app_context;
    //保存客户端的messenger
    private Messenger messenger_client;
    private int newFood;
    private boolean stop = false;
    Thread mThread;
    String baseUrl = "http://192.168.1.101:8080/FoodUpdateService";
    //String baseUrl = "http://192.168.43.214:8080/web/FoodUpdateService";
    String foodType[] = {"coldFood","hotFood","seaFood","drinkFood"};
    public ServerObserverService() {
    }

    public void onCreate() {
        super.onCreate();
        app_context = getApplicationContext();
        Log.i("server", "远程server");
        if(!isAppOnForeground(app_context)) {
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    public static boolean isAppOnForeground(Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcess = activityManager.getRunningAppProcesses();
        if (appProcess == null)
        {
            return false;
        }
        if (appProcess.get(0).processName.contains(packageName))
        {
            return true;
        }
        return false;
    }

    //处理客户消息与线程消息
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //msg 客户端传来的消息
                case 0:
                    //关闭线程
                    if (mThread != null && mThread.isAlive()) {
                        stop = true;
                    }
                    break;
                case 1:
                    newFood = 1;
                    if(stop == true) {
                        stop = false;
                    }
                    if(mThread == null) {
                        mThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!stop) {
                                    for(int i=0;i<4;i++) {
                                        sendJson(foodType[i]);
                                    }
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        mThread.start();
                    }else {
                        mThread.start();
                    }
                    messenger_client = msg.replyTo;
                    break;

                case 2:
                    //判断主进程是否在运行
                    List<Data> food_list;
                    String food_type = msg.getData().getString("FoodType");
                    food_list = (ArrayList)msg.getData().getSerializable(food_type);
                    if(isAppOnForeground(app_context)) {
                        if(food_list.size()>0) {
                            Message message = Message.obtain();
                            message.replyTo = messenger_client;
                            message.setData(msg.getData());
                            message.what = 10;
                            try {
                                message.replyTo.send(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Data data = new Data("热干面", 10);
                    Intent intent = new Intent(ServerObserverService.this, UpdateService.class);
                    intent.putExtra("Data", data);
                    intent.putExtra("String", "Yes");
                    startService(intent);
                    /*Intent intent = new Intent();
                    intent.setClass(ServerObserverService.this, UpdateService.class);
                    intent.putExtra("String", "NewFood");
                    intent.putExtra("Food", (ArrayList<Data>) food_list);
                    startService(intent);*/
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Messenger mMessenger = new Messenger(mHandler);

    private void sendJson(String FoodType) {
        try {
            //合成参数
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("online", 1);
            jsonObject.put("newFood", newFood);
            newFood = 0;
            jsonObject.put("FoodType", FoodType);
            System.out.println("=============="+jsonObject.toString());
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            String content = String.valueOf(jsonObject);
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.writeBytes(content);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK)  {
                InputStreamReader inputStreamReader = new InputStreamReader(urlConn.getInputStream());
                System.out.println("**************************************************#######");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String recData = null;
                String result = "";
                while ((recData = bufferedReader.readLine()) != null){
                    result += recData;
                }
                inputStreamReader.close();
                urlConn.disconnect();
                JSONArray json_array = JSONArray.fromObject(result);
                System.out.println(result.toString()+"88888888");
                if(json_array.toString() != "") {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    List<Data> food_list = new ArrayList<>(FoodGetFromJson(json_array));
                    bundle.putSerializable(FoodType, (Serializable)food_list);
                    bundle.putString("FoodType",FoodType);
                    message.setData(bundle);
                    message.what = 2;
                    mHandler.sendMessage(message);
                }
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
        }
    }

    private List<Data> FoodGetFromJson(JSONArray json_array){
        if (json_array == null)
            return new ArrayList<>();
        List<Data> foodList = new ArrayList<>();
        JSONObject jsonObject = null;
        Data info = null;
        for (int i = 0; i < json_array.size(); i++) {
            jsonObject = json_array.getJSONObject(i);
            info = new Data(jsonObject.getString("foodName"),jsonObject.getInt("foodPrice"));
            foodList.add(info);
        }
        return foodList;
    }
}
/*import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.source.code.model.Data;


public class ServerObserverService extends Service {
   public Context context;
   public boolean working;
   private boolean newList = true;
   private Messenger messengerClient;
   Thread thread;
   int newFood;
   String Type[] = {"coldFood", "hotFood", "seaFood", "drinkFood"};
   private static String title;
   String Url = "http://192.168.1.101:8080/FoodUpdateService";

    public static boolean isAppRunning(Context context, String packageName){
        ActivityManager scos = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = scos.getRunningTasks(100);
        if(list.size() <= 0){
            return false;
        }
        for(ActivityManager.RunningTaskInfo info : list){
            if(info.baseActivity.getPackageName().equals(packageName)){
                return true;
            }
        }
        return false;
    }

    public ServerObserverService(){

    }

    private Handler cMessageHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    if(thread != null && thread.isAlive()){
                        working = false;
                    }
                    break;
                case 1:
                    newFood = 1;
                    if(working == true){
                        working = false;
                    }
                    if(thread == null){
                        thread = new Thread(new Runnable(){
                            @Override
                            public void run(){
                                while(working){
                                        for(int i = 0; i < 4;i++){
                                            sendJson(Type[i]);
                                        }

                                        Message message = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("coldFood", (Serializable)getFood("coldFood"));
                                        bundle.putSerializable("hotFood", (Serializable)getFood("hotFood"));
                                        bundle.putSerializable("seaFood", (Serializable)getFood("seaFood"));
                                        bundle.putSerializable("drinkFood", (Serializable)getFood("drinkFood"));
                                        message.setData(bundle);
                                       message.what = 2;
                                        cMessageHandler.sendMessage(message);
                                        newList = false;
                                    try{
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        thread.start();
                    }else{
                        thread.start();
                    }
                    messengerClient = msg.replyTo;
                    break;

                case 2:
                    List<Data> dataList;
                    String type = msg.getData().getString("type");
                    dataList = (ArrayList)msg.getData().getSerializable(type);
                    if(isAppRunning(context, context.getPackageName())){
                        Message message = Message.obtain();
                        message.replyTo = messengerClient;
                        message.setData(msg.getData());
                        message.what = 10;
                        try{
                            message.replyTo.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        Data data = new Data("热干面", 10);
                        Intent intent = new Intent(ServerObserverService.this, UpdateService.class);
                        intent.putExtra("Data", data);
                        intent.putExtra("String", "Yes");
                        startService(intent);
                        Intent intent = new Intent();
                        intent.setClass(ServerObserverService.this, UpdateService.class);
                        intent.putExtra("String", "NewFood");
                        intent.putExtra("Food", (ArrayList<Data>) dataList);
                        startService(intent);
                    }
                    break;
            }
            super.handleMessage(msg);

        }
    };

    private Messenger messenger = new Messenger(cMessageHandler);    //利用cMessageHandler来创建传递消息的桥梁

    private void sendJson(String type) {
        try {
            //合成参数
            JSONObject json = new JSONObject();
            json.put("online", 1);
            json.put("newFood", newFood);
            newFood = 0;
            json.put("type", type);
            System.out.println("=============="+json.toString());
            // 新建一个URL对象
            URL url = new URL(Url);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            String content = String.valueOf(json);
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.writeBytes(content);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if(urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i("log", "接受返回值");
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                BufferedReader bf = new BufferedReader(in);
                String recData = null;
                String result = "";
                while ((recData = bf.readLine()) != null){
                    result += recData;
                }
                in.close();
                urlConn.disconnect();
                JSONArray json_array = JSONArray.fromObject(result);
                if(json_array.toString() != "") {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    List<Data> food_list = new ArrayList<>(FoodGetFromJson(json_array));
                    bundle.putSerializable(type, (Serializable)food_list);
                    bundle.putString("type",type);
                    message.setData(bundle);
                    message.what = 2;
                    cMessageHandler.sendMessage(message);
                }
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
        }
    }

    private List<Data> FoodGetFromJson(JSONArray json_array){
        if (json_array == null)
            return new ArrayList<>();
        List<Data> foodList = new ArrayList<>();
        JSONObject jsonObject = null;
        Data info = null;
        for (int i = 0; i < json_array.size(); i++) {
            jsonObject = json_array.getJSONObject(i);
            info = new Data(jsonObject.getString("foodName"),jsonObject.getInt("foodPrice"));
            foodList.add(info);
        }
        return foodList;
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        if(!isAppRunning(context, context.getPackageName())) {
            Message message = new Message();
            message.what = 1;
            cMessageHandler.sendMessage(message);
        }
    }

    @Override
    public IBinder onBind(Intent intent){      //通过onBind方法返回客户端
        return messenger.getBinder();
    }

    public List<Data> getFood(String type){
        List<Data> coldFood = null;
        List<Data> hotFood = null;
        List<Data> seaFood = null;
        List<Data> drinkFood = null;
        switch(type){
            case "coldFood":
                coldFood = new ArrayList<>();
                title = "coldFood";
                for(int i = 1; i < 11; i++) {
                    coldFood.add(new Data("菜名"+i,8*i, false, 1));
                }
                return coldFood;
            case "hotFood":
                hotFood = new ArrayList<>();
                title = "hotFood";
                for(int i = 11; i < 21; i++) {
                    hotFood.add(new Data("菜名"+i, 3*i, false, 1));
                }
                return hotFood;
            case "seaFood":
                seaFood = new ArrayList<>();
                title = "seaFood";
                for(int i = 21; i < 31; i++ ) {
                    seaFood.add(new Data("菜名"+i, 2*i, false, 1));
                }
                return seaFood;
            case "drinkFood":
                drinkFood = new ArrayList<>();
                title = "drinkFood";
                for(int i = 31; i < 41; i++) {
                    drinkFood.add(new Data("菜名"+i, 1*i, false, 1));
                }
                return drinkFood;
        }
        return null;
    }

    public static String  getTitle() {
        return title;
    }
}*/

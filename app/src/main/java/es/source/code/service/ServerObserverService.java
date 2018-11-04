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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import es.source.code.model.Data;


public class ServerObserverService extends Service {
   public Context context;
   public boolean working;
   private boolean newList = true;
   private Messenger messengerClient;
   Thread thread;
   private static String title;

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
                    working = true;
                    if(thread == null){
                        thread = new Thread(new Runnable(){
                            @Override
                            public void run(){
                                while(working){
                                    if(newList){
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
                                    }
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
                    }
                    break;
            }
            super.handleMessage(msg);

        }
    };

    private Messenger messenger = new Messenger(cMessageHandler);    //利用cMessageHandler来创建传递消息的桥梁

    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
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
}

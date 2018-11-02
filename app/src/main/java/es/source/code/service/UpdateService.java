package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import es.source.code.activity.FoodDetailedActivity;
import es.source.code.activity.R;
import es.source.code.model.Data;

public class UpdateService extends IntentService {

    private static final int PUSH_NOTIFICATION_ID = (0x001);
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";
    public Data data;

    public UpdateService() {
        super("UpdateService");
    }

    public void onCreate(){
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent){

        data = (Data) intent.getSerializableExtra("Data");
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID, PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,PUSH_CHANNEL_ID);
        Intent notificationIntent = new Intent(this, FoodDetailedActivity.class);
        notificationIntent.putExtra("Data",data);
        notificationIntent.putExtra("String","UpdateService");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentTitle("新品上架")//设置通知栏标题
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setContentText(data.getName() + "  " + data.getPrice() + "元")
                .setTicker("通知内容") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                .setChannelId(PUSH_CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify(PUSH_NOTIFICATION_ID, notification);
        }
    }
}

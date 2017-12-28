package idv.dev.jason.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity
                          implements View.OnClickListener {

    private AtomicInteger integer = new AtomicInteger();

    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    cancelAllNotification();
                    showNotification();

                    try
                    {
                        Thread.sleep(2000);
                    }
                    catch (InterruptedException e) { }
                }
            }
        }).start();
    }


    void showNotification() {
        // API 26↑(Android 8.0↑) work
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            String channelId = "CHANNEL_1";
            String channelName = "Channel_1";

            Notification.Builder builder = new Notification.Builder(getApplicationContext(), channelId)
                                                           .setContentTitle("Title")
                                                           .setContentText("Content")
                                                           .setSmallIcon(R.drawable.ic_launcher_foreground)
                                                           .setAutoCancel(true);

            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.BLUE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            manager.notify(integer.getAndIncrement(), builder.build());
        }
        // API 21~25(Android 5.0 ~ 7.0) work
        else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.N_MR1)
        {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            Notification.Builder builder = new Notification.Builder(this)
                                                           .setLargeIcon(bitmap)
                                                           .setSmallIcon(R.mipmap.ic_launcher)
                                                           .setContentTitle("Title")
                                                           .setDefaults(Notification.DEFAULT_ALL)
                                                           .setContentText("Content")
                                                           .setPriority(Notification.PRIORITY_HIGH)
                                                           .setAutoCancel(true);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(integer.getAndIncrement(), builder.build());
        }
    }

    void cancelAllNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

}

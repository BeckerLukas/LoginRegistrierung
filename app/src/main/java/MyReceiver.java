import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import de.hshl.loginregistrierung.R;

public class MyReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "0815";
    public static final CharSequence NOTIFICATION_CHANNEL_NAME = "MyReceiver";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getAction(), Toast.LENGTH_LONG).show();

        if (intent.getAction().equals("de.hshl.CUSTOM_BROADCAST")
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel
                    = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            Notification.Builder builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_stat_textsms);
            builder.setContentTitle(context.getString(R.string.app_name));
            builder.setContentText("Notice from MyReceiver");
            builder.setWhen(System.currentTimeMillis());
            builder.setAutoCancel(true);
            Notification notification = builder.build();

            NotificationManager notificationManager
                    = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify( id: 42, notification);

            Log.i(tag:"MyReceiver", msq:"Received de.hshl.CUSTOM_BROADCAST");

        }

    }
}
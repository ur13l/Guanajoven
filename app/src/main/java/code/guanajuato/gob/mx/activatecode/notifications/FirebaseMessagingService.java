package code.guanajuato.gob.mx.activatecode.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import code.guanajuato.gob.mx.activatecode.R;
import code.guanajuato.gob.mx.activatecode.activities.HomeActivity;

/**
 * Created by code on 7/06/16.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
    }

    public void showNotification(String title, String message){
        Log.d("NOTIFICATION",message);
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.code)
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{200, 1000, 200, 1000, 200})
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            notification = new NotificationCompat.BigTextStyle(builder)
                    .bigText(message).build();
        }
        else{
            notification = builder.build();
        }
        manager.notify(0, notification);
    }
}

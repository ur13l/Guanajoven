package code.guanajuato.gob.mx.activatecode.receivers;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.util.Calendar;

/**
 * Created by Uriel on 20/03/2016.
 */
public class RetrieveVideosBroadcastReceiver extends BroadcastReceiver {

    /**
     * Función registerAlarm(Context paramContext)
     *
     * @param paramContext: Contexto sobre el cual se ejecutará la alarma
     */
    public static void registerAlarm(Context paramContext) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // PendingIntent that will perform a broadcast
        PendingIntent localPendingIntent = PendingIntent
                .getBroadcast(
                        paramContext.getApplicationContext(),
                        0,
                        new Intent(paramContext, RetrieveVideosBroadcastReceiver.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
        // Retrieve an AlarmManager to set a repeating daily alarm
        AlarmManager am = ((AlarmManager) paramContext.getSystemService(Context.ALARM_SERVICE));
        //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), localPendingIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, localPendingIntent);
    }

    /**
     * onReceive(context, intent)
     * Se ejecuta cada que es enviado el broadcast, en este caso
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String url = "http://i.4cdn.org/b/1458512222085.webm";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Video");
        request.setTitle("Video");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "omg.webm");

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}
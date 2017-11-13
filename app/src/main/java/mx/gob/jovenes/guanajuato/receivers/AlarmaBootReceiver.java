package mx.gob.jovenes.guanajuato.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by uriel on 31/07/16.
 */
public class AlarmaBootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        configurarTodasAlarmas(context);
    }

    public static void configurarTodasAlarmas(Context context){

    }
}

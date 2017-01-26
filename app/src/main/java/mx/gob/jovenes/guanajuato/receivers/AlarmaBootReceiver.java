package mx.gob.jovenes.guanajuato.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import mx.gob.jovenes.guanajuato.model.Alarma;
import mx.gob.jovenes.guanajuato.model.Login;
import mx.gob.jovenes.guanajuato.persistencia.AlarmasDBHelper;

/**
 * Created by uriel on 31/07/16.
 */
public class AlarmaBootReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        configurarTodasAlarmas(context);
    }

    public static void configurarTodasAlarmas(Context context){
        Login l = new Login(context.getApplicationContext());
        AlarmasDBHelper dbHelper = new AlarmasDBHelper(context, context.getFilesDir().getAbsolutePath());
        ArrayList<Alarma> alarmas = dbHelper.getAlarmas(l.getId());
        RetrieveVideosBroadcastReceiver.registerAlarm(context);
        for(Alarma a: alarmas){
            if(a.isActivo()) {
                AlarmasBroadcastReceiver.registerAlarm(context, a);
            }
        }
    }
}

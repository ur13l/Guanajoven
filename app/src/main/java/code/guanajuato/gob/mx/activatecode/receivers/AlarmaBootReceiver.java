package code.guanajuato.gob.mx.activatecode.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import code.guanajuato.gob.mx.activatecode.model.Alarma;
import code.guanajuato.gob.mx.activatecode.model.Login;
import code.guanajuato.gob.mx.activatecode.persistencia.AlarmasDBHelper;

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

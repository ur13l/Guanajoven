package code.guanajuato.gob.mx.activatecode.receivers;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import code.guanajuato.gob.mx.activatecode.activities.VideoActivacionActivity;
import code.guanajuato.gob.mx.activatecode.model.Alarma;

/**
 * Created by uriel on 27/03/16.
 */
public class AlarmasBroadcastReceiver extends BroadcastReceiver {
    private final static String DIAS_DE_LA_SEMANA = "alarmas_dias_de_la_semana";
    private final static String INFO_AGUA = "alarmas_info_agua";
    private final static String IMAGEN = "alarmas_imagen";
    private final static String LOGIN_APP_ID = "login_app_id";
    private static final String HORA = "alarmas_hora";
    private static final String ID = "alarmas_id";


    /**
     * Función registerAlarm(Context paramContext)
     * @param paramContext: Contexto sobre el cual se ejecutará la alarma
     */
    public static void registerAlarm(Context paramContext, Alarma aa) {
        ArrayList<Integer> daysOfWeek = new ArrayList();
        if (aa.isLunes())
            daysOfWeek.add(Calendar.MONDAY);
        if(aa.isMartes())
            daysOfWeek.add(Calendar.TUESDAY);
        if(aa.isMiercoles())
            daysOfWeek.add(Calendar.WEDNESDAY);
        if(aa.isJueves())
            daysOfWeek.add(Calendar.THURSDAY);
        if(aa.isViernes())
            daysOfWeek.add(Calendar.FRIDAY);
        if(aa.isSabado())
            daysOfWeek.add(Calendar.SATURDAY);
        if(aa.isDomingo())
            daysOfWeek.add(Calendar.SUNDAY);


        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(aa.getHora().split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(aa.getHora().split(":")[1]));
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(paramContext, AlarmasBroadcastReceiver.class);
        intent.putExtra(DIAS_DE_LA_SEMANA, convertIntegers(daysOfWeek));
        intent.putExtra(LOGIN_APP_ID, aa.getIdLoginApp());
        intent.putExtra(HORA, aa.getHora());
        intent.putExtra(ID, aa.getId());

        setAlarm(paramContext, intent, calendar);

    }




    public static void cancelAlarmIfExists(Context mContext,int requestCode,Intent intent){
        try{
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, requestCode, intent,0);
            AlarmManager am=(AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Función ejecutada que permite la actualización de la lista de nutriólogos, es ejecutada una vez
     * por día, y solo carga cuando hay Internet.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar now = Calendar.getInstance();
        int[] daysOfWeek = intent.getIntArrayExtra(DIAS_DE_LA_SEMANA);
        int day = now.get(Calendar.DAY_OF_WEEK);

        if(containsInteger(daysOfWeek, day)) {
            String hora = intent.getStringExtra(HORA);
            int hh = Integer.parseInt(hora.split(":")[0]);
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE
                    | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyWakeLock");

            Intent i = new Intent(context, VideoActivacionActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
                // only for gingerbread and newer versions
                i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                i.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                i.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                KeyguardManager manager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("abc");
                lock.disableKeyguard();

            } else {

                KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock keyguardLock = km.newKeyguardLock("TAG");

                i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                i.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                i.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

                keyguardLock.disableKeyguard();
                wakeLock.acquire();
            }


            context.startActivity(i);
            wakeLock.release();

        }
        Calendar calendar = Calendar.getInstance();

        now.add(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);

        setAlarm(context, intent, calendar);

    }


    public static void setAlarm(Context paramContext, Intent intent, Calendar calendar){
        int id = intent.getIntExtra(ID, -1);
        PendingIntent localPendingIntent = PendingIntent
                .getBroadcast(
                        paramContext.getApplicationContext(),
                        id,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        cancelAlarmIfExists(paramContext, id, intent);
        // Retrieve an AlarmManager to set a repeating daily alarm
        AlarmManager am = ((AlarmManager) paramContext.getSystemService(Context.ALARM_SERVICE));
        //am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), localPendingIntent);

        Calendar now = Calendar.getInstance();
        while (calendar.before(now)){
            Log.d("CALENDAR", calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), localPendingIntent);
        }
        else{
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), localPendingIntent);
        }
    }

    /**
     *
     * @param horaFija
     * @param horaNueva
     * @param rep
     * @return
     */
    public boolean esHoraCorrecta(int horaFija, int horaNueva, int rep){
        if(rep == 0){
            return false;
        }
        while(horaFija <= horaNueva){
            if(horaNueva == horaFija){
                return true;
            }
            horaNueva -= (rep/60);
        }
        return false;
    }


    /**
     * Función para convertir un ArrayList<Integer> en int[]
     * @param integers: Representa la lista de enteros en forma de ArrayList
     * @return ret
     */
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    /**
     * Método para saber si un arreglo de enteros (int[]) contiene un número.
     */
    public boolean containsInteger(int[] array, int value){
        for(int i = 0 ; i < array.length ; i++){
            if(array[i] == value)
                return true;
        }
        return false;
    }

}

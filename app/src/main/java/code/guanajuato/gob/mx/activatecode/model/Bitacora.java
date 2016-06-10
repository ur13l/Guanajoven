package code.guanajuato.gob.mx.activatecode.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.sql.Date;

/**
 * Created by Uriel on 09/03/2016.
 */
public class Bitacora {
    public final static String ID = "bitacora_id";
    public final static String ID_USER = "bitacora_id_user";
    public final static String FECHA = "bitacora_fecha";
    public final static String MINUTOS_EJERCICIO = "bitacora_minutos_ejercicio";
    public final static String CALORIAS_EJERCICIO = "bitacora_calorias_ejercicio";
    public final static String REGISTRAR_AGUA = "bitacora_registrar_agua";


    private int id;
    private int idUser;
    private Date fecha;
    private float minutosEjercicio;
    private float caloriasEjercicio;
    private float registrAgua;
    private SharedPreferences prefs;

    public Bitacora(Context ctx){
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public int getId() {
        int id = prefs.getInt(ID, 0);
        return id;
    }

    public void setId(int id) {
        prefs.edit().putInt(ID, id).commit();
    }

    public int getIdUser() {
        int idUser = prefs.getInt(ID_USER, 0);
        return idUser;
    }

    public void setIdUser(int idUser) {
        prefs.edit().putInt(ID_USER, idUser).commit();
    }

    public Date getFecha() {
        Date fecha = new Date(prefs.getLong(FECHA, new java.util.Date().getTime()));
        return fecha;
    }

    public void setFecha(Date fecha) {
        prefs.edit().putLong(FECHA, fecha.getTime()).commit();
    }

    public float getMinutosEjercicio() {
        float minutosEjercicio = prefs.getFloat(MINUTOS_EJERCICIO, 0);
        return minutosEjercicio;
    }

    public void setMinutosEjercicio(float minutosEjercicio) {
        prefs.edit().putFloat(MINUTOS_EJERCICIO, minutosEjercicio).commit();
    }

    public float getCaloriasEjercicio() {
        float caloriasEjercicio = prefs.getFloat(CALORIAS_EJERCICIO, 0);
        return caloriasEjercicio;
    }

    public void setCaloriasEjercicio(float caloriasEjercicio) {
        prefs.edit().putFloat(CALORIAS_EJERCICIO, caloriasEjercicio).commit();
    }

    public float getRegistrAgua() {
        float registrAgua = prefs.getFloat(REGISTRAR_AGUA, 0);
        return registrAgua;
    }

    public void setRegistrAgua(float registrAgua) {
        prefs.edit().putFloat(REGISTRAR_AGUA, registrAgua).commit();
    }



}

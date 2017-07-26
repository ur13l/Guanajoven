package mx.gob.jovenes.guanajuato.sesion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import mx.gob.jovenes.guanajuato.model.Usuario;

/**
 * Created by code on 7/03/17.
 */

public class Sesion {

    private static SharedPreferences prefs;

    private static final String SESION = "sesion";


    private static Usuario usuario;
    public Sesion(Context ctx){


    }


    public static void sessionStart(Context ctx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        Gson gson = new Gson();
        String jsonUsuario = prefs.getString(SESION, null);
        if(jsonUsuario != null) {
            usuario = gson.fromJson(jsonUsuario, Usuario.class);
        }
        else {
            usuario = new Usuario();
        }
    }


    public static Usuario getUsuario() {
        return usuario;
    }

    /**
     * Permite cargar una instancia de Usuario como el objeto de sesión en el momento.
     * @param usuario
     */
    public static void cargarSesion(Usuario usuario) {
        Gson gson = new Gson();
        String jsonUsuario = gson.toJson(usuario);
        prefs.edit().putString(SESION, jsonUsuario).apply();



    }

    /**
     * Elimina todos los datos guardados en la sesión
     */
    public static void logout(){
        prefs.edit().remove(SESION).apply();
        usuario = new Usuario();
    }

}

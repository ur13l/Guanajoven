package code.guanajuato.gob.mx.activatecode.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Clase generada con SharedPreferences para mantener el estado de la sesión activo, en este caso,
 * se guardan los datos del login para ser consultados después
 */
public class Login {
    public final static String ID = "login_id";
    public final static String CORREO = "login_correo";
    public final static String FACEBOOK = "login_facebook";
    public final static String GOOGLE = "login_google";
    private int id;
    private String correo;
    private boolean facebook;
    private boolean google;
    private static SharedPreferences prefs;

    public Login(Context ctx){

        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public int getId() {
        int id = prefs.getInt(ID,0);
        return id;
    }

    public void setId(int id) {
        prefs.edit().putInt(ID, id).commit();
        this.id = id;
    }

    public String getCorreo() {
        String correo = prefs.getString(CORREO,"demo");
        return correo;
    }

    public void setCorreo(String correo) {
        prefs.edit().putString(CORREO, correo).commit();
        this.correo = correo;
    }

    public boolean getFacebook() {
        boolean facebook = prefs.getBoolean(FACEBOOK, false);
        return facebook;
    }

    public void setFacebook(boolean facebook) {
        prefs.edit().putBoolean(FACEBOOK, facebook).commit();
        this.facebook = facebook;
    }

    public boolean getGoogle() {
        boolean google = prefs.getBoolean(GOOGLE, false);
        return google;
    }

    public void setGoogle(boolean google) {
        prefs.edit().putBoolean(GOOGLE, google).commit();
        this.google = google;
    }

    public static void borrarLogin(){
        prefs.edit().remove(ID).commit();
        prefs.edit().remove(CORREO).commit();
        prefs.edit().remove(FACEBOOK).commit();
        prefs.edit().remove(GOOGLE).commit();
    }

}

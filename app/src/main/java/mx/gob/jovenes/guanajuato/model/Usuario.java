package mx.gob.jovenes.guanajuato.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

/**
 * Clase generada con SharedPreferences para mantener el estado de la sesión activo, en este caso,
 * se guardan los datos del login para ser consultados después
 */
public class Usuario {

    private int id;
    private String email;
    private int admin;
    private String apiToken;
    private DatosUsuario datosUsuario;
    private CodigoGuanajoven codigoGuanajoven;
    private String idGoogle;
    private String idFacebook;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public DatosUsuario getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(DatosUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }

    public CodigoGuanajoven getCodigoGuanajoven() {
        return codigoGuanajoven;
    }

    public void setCodigoGuanajoven(CodigoGuanajoven codigoGuanajoven) {
        this.codigoGuanajoven = codigoGuanajoven;
    }

    public String getIdGoogle() {
        return idGoogle;
    }

    public void setIdGoogle(String idGoogle) {
        this.idGoogle = idGoogle;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }
}

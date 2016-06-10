package code.guanajuato.gob.mx.activatecode.model;

/**
 * Autor: Uriel Infante
 * Clase POJO (Plain Old Java Object) para el Login, esta representa el verdadero modelo de un
 * objeto tipo Login, es utilizada para generar instancias con Gson.
 * Fecha:27/05/2016
 */
public class LoginPOJO {
    private int id_login_app;
    private String correo;
    private int facebook;
    private int google;
    private int inserted;
    private String contrasena;

    public int getId() {
        return id_login_app;
    }

    public void setId(int id) {
        this.id_login_app = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int isFacebook() {
        return facebook;
    }

    public void setFacebook(int facebook) {
        this.facebook = facebook;
    }

    public int isGoogle() {
        return google;
    }

    public void setGoogle(int google) {
        this.google = google;
    }

    public int getInserted() {
        return inserted;
    }

    public void setInserted(int inserted) {
        this.inserted = inserted;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}

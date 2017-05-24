package mx.gob.jovenes.guanajuato.model;

/**
 * Created by Uriel on 22/05/2017.
 */

public class CodigoGuanajoven {
    private int idCodigoGuanajoven;
    private String token;
    private String fechaExpiracion;
    private String fechaLimite;

    public int getIdCodigoGuanajoven() {
        return idCodigoGuanajoven;
    }

    public void setIdCodigoGuanajoven(int idCodigoGuanajoven) {
        this.idCodigoGuanajoven = idCodigoGuanajoven;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}

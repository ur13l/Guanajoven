package code.guanajuato.gob.mx.activatecode.model;

import java.sql.Date;

/**
 * Created by code on 21/06/16.
 */
public class StatusReporte {
    private Date fecha;
    private boolean ejercicio;
    private boolean agua;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public boolean isAgua() {
        return agua;
    }

    public void setAgua(boolean agua) {
        this.agua = agua;
    }

    public boolean isEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(boolean ejercicio) {
        this.ejercicio = ejercicio;
    }
}

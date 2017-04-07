package mx.gob.jovenes.guanajuato.model;

import java.sql.Date;

/**
 * Created by code on 21/06/16.
 */
public class StatusReporte {
    private Date fecha;
    private boolean ejercicio;
    private boolean agua;
    private float ejercicioMin;
    private float aguaLt;

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

    public float getEjercicioMin() {
        return ejercicioMin;
    }

    public void setEjercicioMin(float ejercicioMin) {
        this.ejercicioMin = ejercicioMin;
    }

    public float getAguaLt() {
        return aguaLt;
    }

    public void setAguaLt(float aguaLt) {
        this.aguaLt = aguaLt;
    }
}

package code.guanajuato.gob.mx.activatecode.model;

/**
 * Created by code on 21/06/16.
 */
public class StatusReporte {
    private String fecha;
    private boolean ejercicio;
    private boolean agua;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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

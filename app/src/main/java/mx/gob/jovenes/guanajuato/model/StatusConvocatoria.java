package mx.gob.jovenes.guanajuato.model;

import java.util.Date;

/**
 * Created by esva on 10/04/17.
 */

public class StatusConvocatoria {
    String titulo;
    String descripcion;
    Date fechaInicio;
    Date fechaFinalizacion;
    boolean status;

    public String getTitulo() { return this.titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return this.descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFechaInicio() { return this.fechaInicio; }

    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFinalizacion() { return this.fechaFinalizacion; }

    public void setFechaFinalizacion(Date fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public boolean getStatus() { return this.status; }

    public void setStatus(boolean status) { this.status = status; }

}

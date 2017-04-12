package mx.gob.jovenes.guanajuato.model;

import java.util.Date;

/**
 * Created by esva on 10/04/17.
 */

public class Convocatoria {
    int id;
    String titulo;
    String descripcion;
    String rutaImagen;
    Date fechaInicio;
    Date fechaFinalizacion;
    int status;
    Date fechaCreacion;
    Date fechaActualizacion;
    Date fechaBorrado;

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public String getTitulo() { return this.titulo; }

    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return this.descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRutaImagen() { return this.rutaImagen; }

    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }

    public Date getFechaInicio() { return this.fechaInicio; }

    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFinalizacion() { return this.fechaFinalizacion; }

    public void setFechaFinalizacion(Date fechaFinalizacion) { this.fechaFinalizacion = fechaFinalizacion; }

    public int getStatus() { return this.status; }

    public void setStatus(int status) { this.status = status; }

    public Date getFechaCreacion() { return this.fechaCreacion; }

    public void setFechaCreacion(Date fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Date getFechaActualizacion() { return this.fechaActualizacion; }

    public void setFechaActualizacion(Date fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public Date getFechaBorrado() { return this.fechaBorrado; }

    public void setFechaBorrado(Date fechaBorrado) { this.fechaBorrado = fechaBorrado; }

}

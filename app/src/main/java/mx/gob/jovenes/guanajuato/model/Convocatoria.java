package mx.gob.jovenes.guanajuato.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by esva on 10/04/17.
 */

public class Convocatoria {
    int idConvocatoria;
    String titulo;
    String descripcion;
    String rutaImagen;
    String fechaInicio;
    String fechaCierre;
    int estatus;
    ArrayList<Documento> documentos;

    public Convocatoria(int idConvocatoria, String titulo, String descripcion, String rutaImagen, String fechaInicio, String fechaCierre, int estatus) {
        this.idConvocatoria = idConvocatoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
        this.estatus = estatus;
    }

    public int getIdConvocatoria() {
        return idConvocatoria;
    }

    public void setIdConvocatoria(int idConvocatoria) {
        this.idConvocatoria = idConvocatoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public ArrayList<Documento> getDocumentos() { return documentos; }

    public void setDocumentos(ArrayList<Documento> documentos) { this.documentos = documentos; }
}

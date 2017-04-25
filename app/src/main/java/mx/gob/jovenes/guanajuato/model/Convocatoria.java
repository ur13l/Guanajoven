package mx.gob.jovenes.guanajuato.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by esva on 10/04/17.
 */

public class Convocatoria extends RealmObject{

    @PrimaryKey
    private int idConvocatoria;
    private String titulo;
    private String descripcion;
    private String rutaImagen;
    private String fechaInicio;
    private String fechaCierre;
    private int estatus;
    private RealmList<Documento> documentos;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;


    public Convocatoria () {}

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
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat fmt2 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = fmt.parse(fechaInicio);
            this.fechaInicio = fmt2.format(date);
        }
        catch(ParseException pe) {
            this.fechaInicio = "Error";
        }
    }

    public String getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(String fechaCierre) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        SimpleDateFormat fmt2 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = fmt.parse(fechaCierre);
            this.fechaCierre = fmt2.format(date);
        }
        catch(ParseException pe) {
            this.fechaCierre = "Error";
        }
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public RealmList<Documento> getDocumentos() { return documentos; }

    public void setDocumentos(RealmList<Documento> documentos) { this.documentos = documentos; }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}

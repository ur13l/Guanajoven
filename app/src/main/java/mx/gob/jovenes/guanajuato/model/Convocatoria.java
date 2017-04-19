package mx.gob.jovenes.guanajuato.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by esva on 10/04/17.
 */

public class Convocatoria implements Parcelable{
    int idConvocatoria;
    String titulo;
    String descripcion;
    String rutaImagen;
    String fechaInicio;
    String fechaCierre;
    int estatus;
    ArrayList<Documento> documentos;


    protected Convocatoria(Parcel in) {
        idConvocatoria = in.readInt();
        titulo = in.readString();
        descripcion = in.readString();
        rutaImagen = in.readString();
        fechaInicio = in.readString();
        fechaCierre = in.readString();
        estatus = in.readInt();
        documentos = in.createTypedArrayList(Documento.CREATOR);
    }

    public static final Creator<Convocatoria> CREATOR = new Creator<Convocatoria>() {
        @Override
        public Convocatoria createFromParcel(Parcel in) {
            return new Convocatoria(in);
        }

        @Override
        public Convocatoria[] newArray(int size) {
            return new Convocatoria[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idConvocatoria);
        dest.writeString(titulo);
        dest.writeString(descripcion);
        dest.writeString(rutaImagen);
        dest.writeString(fechaInicio);
        dest.writeString(fechaCierre);
        dest.writeInt(estatus);
        dest.writeTypedList(documentos);
    }
}

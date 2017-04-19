package mx.gob.jovenes.guanajuato.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by esva on 19/04/17.
 */

public class Documento implements Parcelable{
    int idDocumento;
    String titulo;
    String rutaDocumento;
    Formato formato;


    protected Documento(Parcel in) {
        idDocumento = in.readInt();
        titulo = in.readString();
        rutaDocumento = in.readString();
        formato = in.readParcelable(Formato.class.getClassLoader());
    }

    public static final Creator<Documento> CREATOR = new Creator<Documento>() {
        @Override
        public Documento createFromParcel(Parcel in) {
            return new Documento(in);
        }

        @Override
        public Documento[] newArray(int size) {
            return new Documento[size];
        }
    };

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getRutaDocumento() {
        return rutaDocumento;
    }

    public void setRutaDocumento(String rutaDocumento) {
        this.rutaDocumento = rutaDocumento;
    }

    public Formato getFormato() {
        return formato;
    }

    public void setFormato(Formato formato) {
        this.formato = formato;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idDocumento);
        dest.writeString(titulo);
        dest.writeString(rutaDocumento);
        dest.writeParcelable(formato, flags);
    }
}

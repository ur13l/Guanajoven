package mx.gob.jovenes.guanajuato.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by esva on 19/04/17.
 */

public class Formato implements Parcelable{
    private int idFormato;
    private String nombre;

    protected Formato(Parcel in) {
        idFormato = in.readInt();
        nombre = in.readString();
    }

    public static final Creator<Formato> CREATOR = new Creator<Formato>() {
        @Override
        public Formato createFromParcel(Parcel in) {
            return new Formato(in);
        }

        @Override
        public Formato[] newArray(int size) {
            return new Formato[size];
        }
    };

    public int getIdFormato() {
        return idFormato;
    }

    public void setIdFormato(int idFormato) {
        this.idFormato = idFormato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idFormato);
        dest.writeString(nombre);
    }
}

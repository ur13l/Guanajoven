package mx.gob.jovenes.guanajuato.model;

/**
 * Created by codigus on 30/06/2017.
 */

public class DatosUsuarioIdioma {
    private int idDatosUsuario;
    private int idIdiomaAdicional;
    private int conversacion;
    private int lectura;
    private int escritura;

    public DatosUsuarioIdioma(int idDatosUsuario, int idIdiomaAdicional, int conversacion, int lectura, int escritura) {
        this.idDatosUsuario = idDatosUsuario;
        this.idIdiomaAdicional = idIdiomaAdicional;
        this.conversacion = conversacion;
        this.lectura = lectura;
        this.escritura = escritura;
    }

    public int getIdDatosUsuario() {
        return idDatosUsuario;
    }

    public void setIdDatosUsuario(int idDatosUsuario) {
        this.idDatosUsuario = idDatosUsuario;
    }

    public int getIdIdiomaAdicional() {
        return idIdiomaAdicional;
    }

    public void setIdIdiomaAdicional(int idIdiomaAdicional) {
        this.idIdiomaAdicional = idIdiomaAdicional;
    }

    public int getConversacion() {
        return conversacion;
    }

    public void setConversacion(int conversacion) {
        this.conversacion = conversacion;
    }

    public int getLectura() {
        return lectura;
    }

    public void setLectura(int lectura) {
        this.lectura = lectura;
    }

    public int getEscritura() {
        return escritura;
    }

    public void setEscritura(int escritura) {
        this.escritura = escritura;
    }
}

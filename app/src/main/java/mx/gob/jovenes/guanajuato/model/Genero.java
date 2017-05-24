package mx.gob.jovenes.guanajuato.model;

/**
 * Created by Uriel on 22/05/2017.
 */

public class Genero {

    private int idGenero;
    private String nombre;
    private String abreviatura;

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
}

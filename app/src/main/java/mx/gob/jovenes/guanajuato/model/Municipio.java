package mx.gob.jovenes.guanajuato.model;

/**
 * Created by Uriel on 22/05/2017.
 */

public class Municipio {

    private int idMunicipio;
    private String nombre;
    private int idRegion;

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdRegion() {
        return idRegion;
    }

    public void setIdRegion(int idRegion) {
        this.idRegion = idRegion;
    }
}

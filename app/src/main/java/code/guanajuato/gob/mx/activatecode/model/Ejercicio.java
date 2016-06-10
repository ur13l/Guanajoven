package code.guanajuato.gob.mx.activatecode.model;

/**
 * Created by Uriel on 08/03/2016.
 */
public class Ejercicio {
    private int id;
    private String nombre;
    private float tiempo;
    private float calorías;
    private String estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getTiempo() {
        return tiempo;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public float getCalorías() {
        return calorías;
    }

    public void setCalorías(float calorías) {
        this.calorías = calorías;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

package mx.gob.jovenes.guanajuato.api;

/**
 * Created by Uriel on 16/03/2017.
 */

public class RegistroRequest {

    private String email;
    private String password;
    private String confirmar_password;
    private String apellido_paterno;
    private String apellido_materno;
    private String nombre;
    private String genero;
    private String fecha_nacimiento;
    private String codigo_postal;
    private String estado_nacimiento;
    private String ruta_imagen;

    public RegistroRequest(String email, String password, String confirmar_password, String apellido_paterno, String apellido_materno, String nombre, String genero, String fecha_nacimiento, String codigo_postal, String estado_nacimiento, String ruta_imagen) {
        this.email = email;
        this.password = password;
        this.confirmar_password = confirmar_password;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.nombre = nombre;
        this.genero = genero;
        this.fecha_nacimiento = fecha_nacimiento;
        this.codigo_postal = codigo_postal;
        this.estado_nacimiento = estado_nacimiento;
        this.ruta_imagen = ruta_imagen;
    }
}
